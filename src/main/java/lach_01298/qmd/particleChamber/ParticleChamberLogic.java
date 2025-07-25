package lach_01298.qmd.particleChamber;

import lach_01298.qmd.capabilities.CapabilityParticleStackHandler;
import lach_01298.qmd.config.QMDConfig;
import lach_01298.qmd.enums.EnumTypes.IOType;
import lach_01298.qmd.multiblock.network.ParticleChamberUpdatePacket;
import lach_01298.qmd.particle.IParticleStackHandler;
import lach_01298.qmd.particleChamber.tile.*;
import nc.multiblock.*;
import nc.tile.internal.fluid.Tank;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.*;

public class ParticleChamberLogic extends MultiblockLogic<ParticleChamber, ParticleChamberLogic, IParticleChamberPart>
		implements IPacketMultiblockLogic<ParticleChamber, ParticleChamberLogic, IParticleChamberPart, ParticleChamberUpdatePacket>
{

	public static final int maxSize = 7;
	public static final int minSize = 1;
	
	
	public ParticleChamberLogic(ParticleChamber multiblock)
	{
		super(multiblock);
	
	}
	
	public ParticleChamberLogic(ParticleChamberLogic oldLogic)
	{
		super(oldLogic);
	}

	@Override
	public String getID()
	{
		return "";
	}

	protected ParticleChamber getMultiblock()
	{
		return multiblock;
	}
	
	@Override
	public int getMinimumInteriorLength()
	{
		return minSize;
	}

	@Override
	public int getMaximumInteriorLength()
	{
		return maxSize;
	}

	@Override
	public void onMachineAssembled()
	{
		onChamberFormed();
	}
	
	public int getBeamLength()
	{
		return getMultiblock().getExteriorLengthX();
	}

	public void onChamberFormed()
	{
		for (IParticleChamberController contr : getPartMap(IParticleChamberController.class).values())
		{
			 getMultiblock().controller = contr;
		}
		
		getMultiblock().energyStorage.setStorageCapacity(QMDConfig.particle_chamber_base_energy_capacity * getCapacityMultiplier());
		getMultiblock().energyStorage.setMaxTransfer(QMDConfig.particle_chamber_base_energy_capacity * getCapacityMultiplier());
		
		if (!getWorld().isRemote)
		{
			refreshChamber();
			getMultiblock().updateActivity();
		}
	
	}

	@Override
	public void onMachineRestored()
	{
		onChamberFormed();
		
	}

	
	public int getCapacityMultiplier()
	{
		return getMultiblock().getExteriorVolume();
	}
	
	
	@Override
	public void onMachinePaused()
	{
		onChamberBroken();
	}

	@Override
	public void onMachineDisassembled()
	{
		onChamberBroken();
	}
	
	public void onChamberBroken()
	{
		if (!getWorld().isRemote)
		{
			getMultiblock().updateActivity();
		}
	}
	
	@Override
	public boolean isMachineWhole()
	{
		multiblock.setLastError("zerocore.api.nc.multiblock.validation.invalid_logic", null);
		return false;
	}

	@Override
	public void writeToLogicTag(NBTTagCompound data, SyncReason syncReason)
	{
	
	}

	@Override
	public void readFromLogicTag(NBTTagCompound data, SyncReason syncReason)
	{
	
	}

	@Override
	public ParticleChamberUpdatePacket getMultiblockUpdatePacket()
	{
		return null;
	}

	@Override
	public void onMultiblockUpdatePacket(ParticleChamberUpdatePacket message)
	{
	
	}

	public void onAssimilate(ParticleChamber assimilated)
	{
		if (assimilated instanceof ParticleChamber)
		{
			ParticleChamber assimilatedAccelerator = (ParticleChamber) assimilated;
			getMultiblock().energyStorage.mergeEnergyStorage(assimilatedAccelerator.energyStorage);
		}
		
		if (getMultiblock().isAssembled())
		{
			onChamberFormed();
		}
		else
		{
			onChamberBroken();
		}
	}

	public void onAssimilated(ParticleChamber assimilator)
	{
	}

	public void refreshChamber()
	{
	
	}

	public boolean onUpdateServer()
	{
		return true;
	}

	public void onUpdateClient()
	{
		// TODO Auto-generated method stub
		
	}

	public void refreshChamberStats()
	{
		getMultiblock().resetStats();
	}

	public boolean isChamberOn()
	{
		
		return getMultiblock().beams.get(0).getParticleStack() != null;
	}

	/*public ContainerMultiblockController<ParticleChamber, IParticleChamberController> getContainer(EntityPlayer player)
	{
		return null;
	}*/

	
	protected void pull()
	{
		for(TileParticleChamberBeamPort port : getPartMap(TileParticleChamberBeamPort.class).values())
		{
		
			if(port.getIOType() == IOType.INPUT)
			{
				if (port.getOutwardFacing() != null)
				{
					EnumFacing face = port.getOutwardFacing();
					TileEntity tile = port.getWorld().getTileEntity(port.getPos().offset(face));
					if(tile != null)
					{
						if (tile.hasCapability(CapabilityParticleStackHandler.PARTICLE_HANDLER_CAPABILITY, face.getOpposite()))
						{
							IParticleStackHandler otherStorage = tile.getCapability(CapabilityParticleStackHandler.PARTICLE_HANDLER_CAPABILITY,face.getOpposite());
							getMultiblock().beams.get(port.getIONumber()).setParticleStack(otherStorage.extractParticle(face.getOpposite()));
						}
					}
				}
			}
		}
		
	}
	
	protected void push()
	{
		for(TileParticleChamberBeamPort port : getPartMap(TileParticleChamberBeamPort.class).values())
		{
		
			if(port.getIOType() == IOType.OUTPUT)
			{
				if (port.getOutwardFacing() != null)
				{
					EnumFacing face = port.getOutwardFacing();
					TileEntity tile = port.getWorld().getTileEntity(port.getPos().offset(face));
					if(tile != null)
					{
						if (tile.hasCapability(CapabilityParticleStackHandler.PARTICLE_HANDLER_CAPABILITY, face.getOpposite()))
						{
							IParticleStackHandler otherStorage = tile.getCapability(CapabilityParticleStackHandler.PARTICLE_HANDLER_CAPABILITY,face.getOpposite());
							otherStorage.reciveParticle(face.getOpposite(), getMultiblock().beams.get(port.getIONumber()).getParticleStack());
						}
					}
				}
			}
		}
	}

	public boolean toggleSetting(BlockPos pos,int ioNumber)
	{
		return false;
	}

	@Override
	public List<Pair<Class<? extends IParticleChamberPart>, String>> getPartBlacklist()
	{
		return new ArrayList<>();
	}
	
	public void clearAllMaterial()
	{
		for (Tank tank : getMultiblock().tanks)
		{
			tank.setFluidStored(null);
		}
	}
	public @Nonnull List<Tank> getTanks(List<Tank> backupTanks) {
		return getMultiblock().isAssembled() ? getMultiblock().tanks : backupTanks;
	}
	
	
	

}
