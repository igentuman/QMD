package lach_01298.qmd.particleChamber.tile;

import com.google.common.collect.Lists;
import lach_01298.qmd.particleChamber.ParticleChamber;
import lach_01298.qmd.particleChamber.block.BlockParticleChamberFluidPort;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.*;
import nc.tile.passive.ITilePassive;
import nc.util.Lang;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.*;

import javax.annotation.*;
import java.util.*;

import static nc.block.property.BlockProperties.FACING_ALL;

public class TileParticleChamberFluidPort extends TileParticleChamberPart implements ITileFluid, ITickable
{

	private final @Nonnull List<Tank> backupTanks = Lists.newArrayList(new Tank(1, new HashSet<>()),
			new Tank(1, new HashSet<>()));

	private @Nonnull FluidConnection[] fluidConnections = ITileFluid
			.fluidConnectionAll(Lists.newArrayList(TankSorption.IN, TankSorption.NON));

	private @Nonnull FluidTileWrapper[] fluidSides;

	

	public TileParticleChamberFluidPort()
	{
		super(CuboidalPartPositionType.WALL);

		fluidSides = ITileFluid.getDefaultFluidSides(this);
		

	}

	@Override
	public void onMachineAssembled(ParticleChamber controller)
	{
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		if (!getWorld().isRemote && getPartPosition().getFacing() != null)
		{
			getWorld().setBlockState(getPos(),
					getWorld().getBlockState(getPos()).withProperty(FACING_ALL, getPartPosition().getFacing()), 2);
		}

	}

	@Override
	public void onMachineBroken()
	{
		super.onMachineBroken();

	}

	public void updateBlockState(boolean isActive)
	{
		if (getBlockType() instanceof BlockParticleChamberFluidPort)
		{
			((BlockParticleChamberFluidPort) getBlockType()).setState(isActive, this);
		}
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public void update()
	{
		EnumFacing facing = getPartPosition().getFacing();
		if (!world.isRemote && !getTanks().get(1).isEmpty() && facing != null && getTankSorption(facing, 1).canDrain())
		{
			pushFluidToSide(facing);
		}
	}

	// Fluids

	@Override
	public @Nonnull List<Tank> getTanks()
	{
		return getMultiblock() != null ? getLogic().getTanks(backupTanks) : backupTanks;
	}

	@Override
	@Nonnull
	public FluidConnection[] getFluidConnections()
	{
		return fluidConnections;
	}

	@Override
	public void setFluidConnections(@Nonnull FluidConnection[] connections)
	{
		fluidConnections = connections;
	}

	@Override
	@Nonnull
	public FluidTileWrapper[] getFluidSides()
	{
		return fluidSides;
	}

	@Override
	public GasTileWrapper getGasWrapper()
	{
		return null;
	}

	@Override
	public void pushFluidToSide(@Nonnull EnumFacing side)
	{
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		if (tile == null || tile instanceof TileParticleChamberPort)
			return;

		if (tile instanceof ITilePassive)
			if (!((ITilePassive) tile).canPushFluidsTo())
				return;

		IFluidHandler adjStorage = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
				side.getOpposite());
		if (adjStorage == null)
			return;

		for (int i = 0; i < getTanks().size(); i++)
		{
			if (getTanks().get(i).getFluid() == null || !getTankSorption(side, i).canDrain())
				continue;

			getTanks().get(i).drain(
					adjStorage.fill(getTanks().get(i).drain(getTanks().get(i).getCapacity(), false), true), true);
		}
	}

	@Override
	public boolean getInputTanksSeparated()
	{
		return false;
	}

	@Override
	public void setInputTanksSeparated(boolean separated)
	{
	}

	@Override
	public boolean getVoidUnusableFluidInput(int tankNumber)
	{
		return false;
	}

	@Override
	public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput)
	{
	}

	@Override
	public TankOutputSetting getTankOutputSetting(int tankNumber)
	{
		return TankOutputSetting.DEFAULT;
	}

	@Override
	public void setTankOutputSetting(int tankNumber, TankOutputSetting setting)
	{
	}

	@Override
	public boolean hasConfigurableFluidConnections()
	{
		return true;
	}

	// IMultitoolLogic

	@Override
	public boolean onUseMultitool(ItemStack multitoolStack, EntityPlayerMP player, World world, EnumFacing facing,
	                              float hitX, float hitY, float hitZ)
	{
		if (player.isSneaking())
		{

		}
		else
		{
			if (getMultiblock() != null)
			{
				if (getTankSorption(facing, 0) != TankSorption.IN)
				{
					for (EnumFacing side : EnumFacing.VALUES)
					{
						setTankSorption(side, 0, TankSorption.IN);
						setTankSorption(side, 1, TankSorption.NON);
					}
					updateBlockState(false);
					getMultiblock().checkIfMachineIsWhole();
					player.sendMessage(new TextComponentString(Lang.localize("nc.block.port_toggle") + " "
							+ TextFormatting.DARK_AQUA + Lang.localize("nc.block.port_mode.input") + " "
							+ TextFormatting.WHITE + Lang.localize("nc.block.port_toggle.mode")));
				}
				else
				{
					for (EnumFacing side : EnumFacing.VALUES)
					{
						setTankSorption(side, 0, TankSorption.NON);
						setTankSorption(side, 1, TankSorption.OUT);
					}
					updateBlockState(true);
					getMultiblock().checkIfMachineIsWhole();
					player.sendMessage(new TextComponentString(Lang.localize("nc.block.port_toggle") + " "
							+ TextFormatting.RED + Lang.localize("nc.block.port_mode.output") + " "
							+ TextFormatting.WHITE + Lang.localize("nc.block.port_toggle.mode")));
				}
				markDirtyAndNotify();
				return true;
			}
		}
		return super.onUseMultitool(multitoolStack, player, world, facing, hitX, hitY, hitZ);
	}

	// NBT

	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt)
	{
		super.writeAll(nbt);
		writeFluidConnections(nbt);

		return nbt;
	}

	@Override
	public void readAll(NBTTagCompound nbt)
	{
		super.readAll(nbt);
		readFluidConnections(nbt);
	}

	// Capability

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side)
	{
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			return !getTanks().isEmpty() && hasFluidSideCapability(side);
		}
		return super.hasCapability(capability, side);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side)
	{
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			if (!getTanks().isEmpty() && hasFluidSideCapability(side))
			{
				return (T) getFluidSide(nonNullSide(side));
			}
			return null;
		}
		return super.getCapability(capability, side);
	}



}
