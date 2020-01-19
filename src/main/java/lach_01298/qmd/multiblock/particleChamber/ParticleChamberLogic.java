package lach_01298.qmd.multiblock.particleChamber;

import lach_01298.qmd.multiblock.accelerator.Accelerator;
import lach_01298.qmd.multiblock.accelerator.AcceleratorLogic;
import lach_01298.qmd.multiblock.accelerator.tile.IAcceleratorComponent;
import lach_01298.qmd.multiblock.accelerator.tile.IAcceleratorController;
import lach_01298.qmd.multiblock.accelerator.tile.TileAcceleratorCooler;
import lach_01298.qmd.multiblock.network.ParticleChamberUpdatePacket;
import lach_01298.qmd.multiblock.particleChamber.tile.IParticleChamberController;
import lach_01298.qmd.multiblock.particleChamber.tile.IParticleChamberPart;
import nc.multiblock.Multiblock;
import nc.multiblock.MultiblockLogic;
import nc.multiblock.TileBeefBase.SyncReason;
import nc.multiblock.container.ContainerMultiblockController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class ParticleChamberLogic extends MultiblockLogic<ParticleChamber,IParticleChamberPart,ParticleChamberUpdatePacket>
{ 

	public static final int maxSize = 7;
	public static final int minSize = 3;
	
	
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

	protected ParticleChamber getChamber() 
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

	public void onChamberFormed()
	{
		for (IParticleChamberController contr : getPartMap(IParticleChamberController.class).values()) 
		{
			 getChamber().controller = contr;
		}
		
		getChamber().energyStorage.setStorageCapacity(Accelerator.BASE_MAX_ENERGY * getCapacityMultiplier());
		getChamber().energyStorage.setMaxTransfer(Accelerator.BASE_MAX_ENERGY * getCapacityMultiplier());
		
		
		if (!getWorld().isRemote) 
		{
			refreshChamber();
			getChamber().updateActivity();	 
		}
	
	}

	@Override
	public void onMachineRestored()
	{
		onChamberFormed();
		
	}

	
	public int getCapacityMultiplier() 
	{
		return getChamber().getExteriorVolume();
	}
	
	
	@Override
	public void onMachinePaused()
	{
		
	}

	@Override
	public void onMachineDisassembled()
	{
		getChamber().resetStats();
		if (getChamber().controller != null)
		{
			getChamber().controller.updateBlockState(false);
		}
		getChamber().isChamberOn = false;	
	}

	@Override
	public boolean isMachineWhole(Multiblock multiblock)
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
	public ParticleChamberUpdatePacket getUpdatePacket()
	{
		return null;
	}

	@Override
	public void onPacket(ParticleChamberUpdatePacket message)
	{
		
	}

	public void onAssimilate(Multiblock assimilated)
	{	
	}

	public void onAssimilated(Multiblock assimilator)
	{	
	}

	public void refreshChamber()
	{
		
		
	}

	public boolean onUpdateServer()
	{
		getChamber().sendUpdateToListeningPlayers();
		return true;
	}

	public void onUpdateClient()
	{
		// TODO Auto-generated method stub
		
	}

	public void onResetStats()
	{
		// TODO Auto-generated method stub
		
	}

	public boolean isChamberOn()
	{
		
		return getChamber().beams.get(0).getParticleStack() != null;
	}

	public ContainerMultiblockController<ParticleChamber, IParticleChamberController> getContainer(EntityPlayer player)
	{
		// TODO Auto-generated method stub
		return null;
	}


}