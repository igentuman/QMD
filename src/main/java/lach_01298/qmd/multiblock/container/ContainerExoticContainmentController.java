package lach_01298.qmd.multiblock.container;

import lach_01298.qmd.container.slot.SlotProcessorInputIgnoreNBT;
import lach_01298.qmd.item.IItemParticleAmount;
import lach_01298.qmd.multiblock.network.VacuumChamberUpdatePacket;
import lach_01298.qmd.recipe.QMDRecipeHandler;
import lach_01298.qmd.recipes.QMDRecipes;
import lach_01298.qmd.vacuumChamber.VacuumChamber;
import lach_01298.qmd.vacuumChamber.tile.*;
import nc.container.multiblock.controller.ContainerMultiblockController;
import nc.container.slot.SlotFurnace;
import nc.recipe.BasicRecipeHandler;
import nc.tile.TileContainerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerExoticContainmentController
		extends ContainerMultiblockController<VacuumChamber, IVacuumChamberPart, VacuumChamberUpdatePacket, TileExoticContainmentController, TileContainerInfo<TileExoticContainmentController>>
{
	protected final QMDRecipeHandler recipeHandler;

	protected int inputSlotsSize;
	protected int outputSlotsSize;
	protected int otherSlotsSize;

	public final TileExoticContainmentController tile;

	public ContainerExoticContainmentController(EntityPlayer player, TileExoticContainmentController tile)
	{
		super(player, tile);
		this.tile = tile;
		this.recipeHandler = QMDRecipes.cell_filling;

		this.inputSlotsSize = 1;
		this.outputSlotsSize = 1;
		this.otherSlotsSize = 0;

		addSlotToContainer(new SlotProcessorInputIgnoreNBT(tile, recipeHandler, 0, 59, 60));
		addSlotToContainer(new SlotFurnace(player, tile, 1, 101, 60));

		addPlayerInventory(player, 8, 95);

	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return tile.isUsableByPlayer(playerIn);
	}

	public void putStackInSlot(int slotID, ItemStack stack)
	{
		this.getSlot(slotID).putStack(stack);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		int invPlayerStart = inputSlotsSize + outputSlotsSize + otherSlotsSize;
		int invPlayerEnd = invPlayerStart + 36;

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < invPlayerStart)
			{
				if (!mergeItemStack(itemstack1, invPlayerStart, invPlayerEnd, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else
			{
				if (recipeHandler.isValidItemInput(IItemParticleAmount.cleanNBT(itemstack1)))
				{
					if (!mergeItemStack(itemstack1, 0, inputSlotsSize, false))
					{
						return ItemStack.EMPTY;
					}
					else
					{
						return ItemStack.EMPTY;
					}
				}

			}
			if (itemstack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}
			if (itemstack1.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}
			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

	protected void addPlayerInventory(EntityPlayer player, int xOffset, int yOffset)
	{
		int slotWidth = 18;
		// add player inventory
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(
						new Slot(player.inventory, j + 9 * i + 9, xOffset + slotWidth * j, yOffset + slotWidth * i));
			}
		}

		for (int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(player.inventory, i, xOffset + slotWidth * i, yOffset + slotWidth * 3 + 4));
		}

	}
}
