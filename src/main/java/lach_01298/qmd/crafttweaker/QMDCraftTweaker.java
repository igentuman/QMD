package lach_01298.qmd.crafttweaker;

import com.google.common.collect.Lists;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import lach_01298.qmd.item.IItemParticleAmount;
import lach_01298.qmd.recipes.QMDRecipes;
import nc.integration.crafttweaker.CTAddRecipe;
import nc.integration.crafttweaker.CTRemoveAllRecipes;
import nc.integration.crafttweaker.CTRemoveRecipe;
import nc.recipe.IngredientSorption;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

public class QMDCraftTweaker
{
	@ZenClass("mods.qmd.ore_leacher")
	@ZenRegister
	public static class OreLeacherHandler
	{
		
		@ZenMethod
		public static void addRecipe(IIngredient input1,IIngredient input2,IIngredient input3,IIngredient input4, IIngredient output1,IIngredient output2,IIngredient output3, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation)
		{
			CraftTweakerAPI.apply(new CTAddQMDRecipe(QMDRecipes.ore_leacher, Lists.newArrayList(input1, input2, input3, input4, output1, output2, output3, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1,IIngredient input2,IIngredient input3,IIngredient input4)
		{
			CraftTweakerAPI.apply(new CTRemoveQMDRecipe(QMDRecipes.ore_leacher, IngredientSorption.INPUT, Lists.newArrayList(input1,input2,input3,input4)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1,IIngredient output2,IIngredient output3)
		{
			CraftTweakerAPI.apply(new CTRemoveQMDRecipe(QMDRecipes.ore_leacher, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2, output3)));
		}
		
		@ZenMethod
		public static void removeAllRecipes()
		{
			CraftTweakerAPI.apply(new CTRemoveAllQMDRecipes(QMDRecipes.ore_leacher));
		}
	}
	
	@ZenClass("mods.qmd.irradiator")
	@ZenRegister
	public static class IrradiatorHandler
	{
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation)
		{
			CraftTweakerAPI.apply(new CTAddQMDRecipe(QMDRecipes.irradiator, Lists.newArrayList(input, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input)
		{
			CraftTweakerAPI.apply(new CTRemoveQMDRecipe(QMDRecipes.irradiator, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output)
		{
			CraftTweakerAPI.apply(new CTRemoveQMDRecipe(QMDRecipes.irradiator, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes()
		{
			CraftTweakerAPI.apply(new CTRemoveAllQMDRecipes(QMDRecipes.irradiator));
		}
	}
	
	@ZenClass("mods.qmd.irradiator_fuel")
	@ZenRegister
	public static class IrradiatorFuelHandler
	{
		
		@ZenMethod
		public static void addFuel(IIngredient input,  double speedMultiplier)
		{
			CraftTweakerAPI.apply(new CTAddQMDRecipe(QMDRecipes.irradiator_fuel, Lists.newArrayList(input,speedMultiplier)));
		}
		
		@ZenMethod
		public static void removeFuel(IIngredient input)
		{
			CraftTweakerAPI.apply(new CTRemoveQMDRecipe(QMDRecipes.irradiator_fuel, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeAllFuels()
		{
			CraftTweakerAPI.apply(new CTRemoveAllQMDRecipes(QMDRecipes.irradiator_fuel));
		}
	}
	
	@ZenClass("mods.qmd.accelerator_cooling")
	@ZenRegister
	public static class AcceleratorCoolingHandler
	{
		
		@ZenMethod
		public static void addRecipe(IIngredient fluidInput,IIngredient fluidOutput, int heatRemoved, int temperature)
		{
			CraftTweakerAPI.apply(new CTAddQMDRecipe(QMDRecipes.accelerator_cooling, Lists.newArrayList(fluidInput, fluidOutput, heatRemoved, temperature)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient fluidInput)
		{
			CraftTweakerAPI.apply(new CTRemoveQMDRecipe(QMDRecipes.accelerator_cooling, IngredientSorption.INPUT, Lists.newArrayList(fluidInput)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient fluidOutput)
		{
			CraftTweakerAPI.apply(new CTRemoveQMDRecipe(QMDRecipes.accelerator_cooling, IngredientSorption.OUTPUT, Lists.newArrayList(fluidOutput)));
		}
		
		@ZenMethod
		public static void removeAllRecipes()
		{
			CraftTweakerAPI.apply(new CTRemoveAllQMDRecipes(QMDRecipes.accelerator_cooling));
		}
	}
	
	@ZenClass("mods.qmd.target_chamber")
	@ZenRegister
	public static class TargetChamberHandler
	{
		
		
		@ZenMethod
		public static void addRecipe(IIngredient inputItem, IIngredient inputFluid, IIngredient inputParticle, IIngredient outputItem, IIngredient outputFluid, IIngredient outputParticle1, IIngredient outputParticle2, IIngredient outputParticle3, long maxEnergy, double crossSection, @Optional(valueLong = 0) long energyReleased)
		{
			CraftTweakerAPI.apply(new CTAddQMDRecipe(QMDRecipes.target_chamber, Lists.newArrayList(inputItem, inputFluid, inputParticle, outputItem, outputFluid, outputParticle1, outputParticle2, outputParticle3, maxEnergy, crossSection, energyReleased)));
		}
		
		
		@ZenMethod
		public static void addRecipe(IIngredient inputItem, IIngredient inputParticle, IIngredient outputItem, IIngredient outputParticle1, IIngredient outputParticle2, IIngredient outputParticle3, long maxEnergy, double crossSection, @Optional(valueLong = 0) long energyReleased)
		{
			
			CraftTweakerAPI.apply(new CTAddQMDRecipe(QMDRecipes.target_chamber, Lists.newArrayList(inputItem, null, inputParticle, outputItem, null, outputParticle1, outputParticle2, outputParticle3, maxEnergy, crossSection, energyReleased)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient inputItem, IIngredient inputParticle)
		{
			CraftTweakerAPI.apply(new CTRemoveQMDRecipe(QMDRecipes.target_chamber, IngredientSorption.INPUT, Lists.newArrayList(inputItem, null, inputParticle)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient inputItem, IIngredient inputFluid, IIngredient inputParticle)
		{
			CraftTweakerAPI.apply(new CTRemoveQMDRecipe(QMDRecipes.target_chamber, IngredientSorption.INPUT, Lists.newArrayList(inputItem, inputFluid, inputParticle)));
		}
		
		@ZenMethod
		public static void removeAllRecipes()
		{
			CraftTweakerAPI.apply(new CTRemoveAllQMDRecipes(QMDRecipes.target_chamber));
		}
	}
	
	@ZenClass("mods.qmd.decay_chamber")
	@ZenRegister
	public static class DecayChamberHandler
	{
		
		@ZenMethod
		public static void addRecipe(IIngredient inputParticle, IIngredient outputParticle1, IIngredient outputParticle2, IIngredient outputParticle3 , double crossSection, @Optional(valueLong = 0) long energyReleased, @Optional(valueLong = Long.MAX_VALUE) long maxEnergy)
		{
			CraftTweakerAPI.apply(new CTAddQMDRecipe(QMDRecipes.decay_chamber, Lists.newArrayList(inputParticle, outputParticle1, outputParticle2, outputParticle3, maxEnergy, crossSection, energyReleased)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient inputParticle)
		{
			CraftTweakerAPI.apply(new CTRemoveQMDRecipe(QMDRecipes.decay_chamber, IngredientSorption.INPUT, Lists.newArrayList(inputParticle)));
		}
		
		@ZenMethod
		public static void removeAllRecipes()
		{
			CraftTweakerAPI.apply(new CTRemoveAllQMDRecipes(QMDRecipes.decay_chamber));
		}
	}
	
	@ZenClass("mods.qmd.collision_chamber")
	@ZenRegister
	public static class CollisionChamberHandler
	{
		
		@ZenMethod
		public static void addRecipe(IIngredient inputParticle1,IIngredient inputParticle2, IIngredient outputParticle1, IIngredient outputParticle2, IIngredient outputParticle3, IIngredient outputParticle4 , long maxEnergy, double crossSection, @Optional(valueLong = 0) long energyReleased)
		{
			CraftTweakerAPI.apply(new CTAddQMDRecipe(QMDRecipes.collision_chamber, Lists.newArrayList(inputParticle1, inputParticle2, outputParticle1, outputParticle2, outputParticle3, outputParticle4, maxEnergy, crossSection, energyReleased)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient inputParticle1, IIngredient inputParticle2)
		{
			CraftTweakerAPI.apply(new CTRemoveQMDRecipe(QMDRecipes.collision_chamber, IngredientSorption.INPUT, Lists.newArrayList(inputParticle1, inputParticle2)));
		}
		
		@ZenMethod
		public static void removeAllRecipes()
		{
			CraftTweakerAPI.apply(new CTRemoveAllQMDRecipes(QMDRecipes.collision_chamber));
		}
	}
	
	@ZenClass("mods.qmd.beam_dump")
	@ZenRegister
	public static class BeamDumpHandler
	{
		
		@ZenMethod
		public static void addRecipe(IIngredient inputParticle, IIngredient outputFluid, @Optional(valueLong = Long.MAX_VALUE) long maxEnergy)
		{
			CraftTweakerAPI.apply(new CTAddQMDRecipe(QMDRecipes.beam_dump, Lists.newArrayList( inputParticle, outputFluid, maxEnergy)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient inputParticle)
		{
			CraftTweakerAPI.apply(new CTRemoveQMDRecipe(QMDRecipes.beam_dump, IngredientSorption.INPUT, Lists.newArrayList(inputParticle)));
		}
		
		@ZenMethod
		public static void removeAllRecipes()
		{
			CraftTweakerAPI.apply(new CTRemoveAllQMDRecipes(QMDRecipes.beam_dump));
		}
	}
	

	
	@ZenClass("mods.qmd.particle")
	@ZenRegister
	public static class ParticleHandler
	{
		@ZenMethod
		public static void addParticle(String name, String textureLocation, double mass, double charge, double spin, @Optional(valueBoolean = false) boolean weakCharged, @Optional(valueBoolean = false) boolean coloured)
		{
			CraftTweakerAPI.apply(new CTAddParticle(name, textureLocation, mass, charge, spin,weakCharged,coloured));
		}
		@ZenMethod
		public static void addAntiParticle(IIngredient particle, IIngredient antiParticle)
		{
			CraftTweakerAPI.apply(new CTAddAntiParticle(particle, antiParticle));
		}
		
		@ZenMethod
		public static void addComponentParticle(IIngredient parentParticle, IIngredient particle)
		{
			CraftTweakerAPI.apply(new CTAddComponentParticle(parentParticle, particle));
		}
		
	}
	
	
	
	@ZenClass("mods.qmd.nucleosynthesis_chamber")
	@ZenRegister
	public static class NucleosynthesisChamberHandler
	{
		
		@ZenMethod
		public static void addRecipe(IIngredient inputFluid1, IIngredient inputFluid2, IIngredient inputParticle, IIngredient outputFluid1, IIngredient outputFluid2, @Optional(valueLong = Long.MAX_VALUE) long maxEnergy, @Optional(valueLong = 0L)long heatRelased)
		{
			CraftTweakerAPI.apply(new CTAddQMDRecipe(QMDRecipes.nucleosynthesis_chamber, Lists.newArrayList(inputFluid1, inputFluid2, inputParticle, outputFluid1, outputFluid2, maxEnergy,heatRelased)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient inputFluid1, IIngredient inputFluid2, IIngredient inputParticle)
		{
			CraftTweakerAPI.apply(new CTRemoveQMDRecipe(QMDRecipes.nucleosynthesis_chamber, IngredientSorption.INPUT, Lists.newArrayList(inputFluid1,inputFluid2,inputParticle)));
		}
		
		@ZenMethod
		public static void removeAllRecipes()
		{
			CraftTweakerAPI.apply(new CTRemoveAllQMDRecipes(QMDRecipes.nucleosynthesis_chamber));
		}
	}
	
	@ZenClass("mods.qmd.nucleosynthesis_chamber_heater")
	@ZenRegister
	public static class NucleosynthesisChamberHeaterHandler
	{
		
		@ZenMethod
		public static void addRecipe(IIngredient fluidInput,IIngredient fluidOutput, int heatRemoved)
		{
			CraftTweakerAPI.apply(new CTAddQMDRecipe(QMDRecipes.vacuum_chamber_heating, Lists.newArrayList(fluidInput, fluidOutput, heatRemoved)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient fluidInput)
		{
			CraftTweakerAPI.apply(new CTRemoveQMDRecipe(QMDRecipes.vacuum_chamber_heating, IngredientSorption.INPUT, Lists.newArrayList(fluidInput)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient fluidOutput)
		{
			CraftTweakerAPI.apply(new CTRemoveQMDRecipe(QMDRecipes.vacuum_chamber_heating, IngredientSorption.OUTPUT, Lists.newArrayList(fluidOutput)));
		}
		
		@ZenMethod
		public static void removeAllRecipes()
		{
			CraftTweakerAPI.apply(new CTRemoveAllQMDRecipes(QMDRecipes.vacuum_chamber_heating));
		}
	}
	
	@ZenClass("mods.qmd.item_source")
	@ZenRegister
	public static class ItemSourceHandler
	{
		
		@ZenMethod
		public static void setEmptyItem(IIngredient itemSource,IIngredient itemEmpty)
		{
			CraftTweakerAPI.apply(new CTSetEmptyItemSource(itemSource,itemEmpty));
		}

	}
	
	@ZenClass("mods.qmd.accelerator_source")
	@ZenRegister
	public static class AcceleratorSourceHandler
	{
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient outputParticle)
		{
			if(input instanceof IItemStack)
			{
				if(((IItemStack) input).getInternal() instanceof ItemStack)
				{
					ItemStack stack = (ItemStack) input.getInternal();
					
					if(stack.getItem() instanceof IItemParticleAmount)
					{
						CraftTweakerAPI.apply(new CTAddQMDRecipe(QMDRecipes.accelerator_source, Lists.newArrayList(input, null, outputParticle)));
						
					}
					else
					{
						CraftTweakerAPI.logError(input.toString() + " is not an instance of IItemParticleAmount. Can not add accelerator_source recipe.");
					}
				}
			}
			else if(input instanceof ILiquidStack)
			{
				CraftTweakerAPI.apply(new CTAddQMDRecipe(QMDRecipes.accelerator_source, Lists.newArrayList(null, input, outputParticle)));
			}
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input)
		{
			if(input instanceof IItemStack)
			{
				CraftTweakerAPI.apply(new CTRemoveQMDRecipe(QMDRecipes.accelerator_source, IngredientSorption.INPUT, Lists.newArrayList(input, null)));
			}
			else if(input instanceof ILiquidStack)
			{
				CraftTweakerAPI.apply(new CTRemoveQMDRecipe(QMDRecipes.accelerator_source, IngredientSorption.INPUT, Lists.newArrayList(null, input)));
			}
		}
		
		@ZenMethod
		public static void removeAllRecipes()
		{
			CraftTweakerAPI.apply(new CTRemoveAllQMDRecipes(QMDRecipes.accelerator_source));
		}
	}
	
	@ZenClass("mods.qmd.containment_chamber")
	@ZenRegister
	public static class ContainmentChamberHandler
	{
		
		@ZenMethod
		public static void addRecipe(IIngredient inputParticle1, IIngredient inputParticle2, IIngredient outputFluid, @Optional(valueLong = Long.MAX_VALUE) long maxEnergy)
		{
			CraftTweakerAPI.apply(new CTAddQMDRecipe(QMDRecipes.neutral_containment, Lists.newArrayList(inputParticle1, inputParticle2, outputFluid, maxEnergy)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient inputParticle1, IIngredient inputParticle2)
		{
			CraftTweakerAPI.apply(new CTRemoveQMDRecipe(QMDRecipes.neutral_containment, IngredientSorption.INPUT, Lists.newArrayList(inputParticle1,inputParticle2)));
		}
		
		@ZenMethod
		public static void removeAllRecipes()
		{
			CraftTweakerAPI.apply(new CTRemoveAllQMDRecipes(QMDRecipes.neutral_containment));
		}
	}
	
	@ZenClass("mods.qmd.containment_chamber_cell_filling")
	@ZenRegister
	public static class ContainmentChamberCellFillingHandler
	{
		
		@ZenMethod
		public static void addRecipe(IIngredient inputFluid, IIngredient inputFullCell)
		{
			if(inputFullCell instanceof IItemStack)
			{
				if(((IItemStack) inputFullCell).getInternal() instanceof ItemStack)
				{
					ItemStack fullCellStack = (ItemStack) inputFullCell.getInternal();
					
					if(fullCellStack.getItem() instanceof IItemParticleAmount)
					{
						IItemParticleAmount fullCell = (IItemParticleAmount) fullCellStack.getItem();
						if (!fullCell.getEmptyItem().equals(ItemStack.EMPTY))
						{

							IItemStack emptyCell = CraftTweakerAPI.itemUtils.getItem(fullCell.getEmptyItem().getItem().getRegistryName().toString(),fullCell.getEmptyItem().getMetadata());
							CraftTweakerAPI.apply(new CTAddQMDRecipe(QMDRecipes.cell_filling, Lists.newArrayList(emptyCell, inputFluid, inputFullCell,null)));
							CraftTweakerAPI.apply(new CTAddQMDRecipe(QMDRecipes.cell_filling, Lists.newArrayList(inputFullCell, null,emptyCell, inputFluid)));
							
							
							
							return;
						}
						else
						{
							CraftTweakerAPI.logError(inputFullCell.toString() + " does not have a registed empty item. Can not add cell_filling recipe.");
							return;
						}

					}
				}
			}
			CraftTweakerAPI.logError(inputFullCell.toString() + " is not an instance of IItemParticleAmount. Can not add cell_filling recipe.");
		}
		
	}
	
	
	
	
	@ZenClass("mods.qmd.mass_spectrometer")
	@ZenRegister
	public static class MassSpectrometerHandler
	{
		
		@ZenMethod
		public static void addRecipe(IIngredient inputItem, IIngredient inputFluid, IIngredient outputItem1, IIngredient outputItem2, IIngredient outputItem3, IIngredient outputItem4, IIngredient outputFluid1, IIngredient outputFluid2, IIngredient outputFluid3, IIngredient outputFluid4, @Optional(valueDouble = 1D) double timeMultiplier)
		{
			CraftTweakerAPI.apply(new CTAddQMDRecipe(QMDRecipes.mass_spectrometer, Lists.newArrayList(inputItem, inputFluid, outputItem1, outputItem2,outputItem3,outputItem4,outputFluid1,outputFluid2,outputFluid3,outputFluid4,timeMultiplier)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient inputItem, IIngredient inputFluid)
		{
			CraftTweakerAPI.apply(new CTRemoveQMDRecipe(QMDRecipes.mass_spectrometer, IngredientSorption.INPUT, Lists.newArrayList(inputItem,inputFluid)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient outputItem1, IIngredient outputItem2, IIngredient outputItem3, IIngredient outputItem4, IIngredient outputFluid1, IIngredient outputFluid2, IIngredient outputFluid3, IIngredient outputFluid4)
		{
			CraftTweakerAPI.apply(new CTRemoveQMDRecipe(QMDRecipes.mass_spectrometer, IngredientSorption.OUTPUT, Lists.newArrayList(outputItem1,outputItem2,outputItem3,outputItem4,outputFluid1,outputFluid2,outputFluid3,outputFluid4)));
		}
		
		@ZenMethod
		public static void removeAllRecipes()
		{
			CraftTweakerAPI.apply(new CTRemoveAllQMDRecipes(QMDRecipes.mass_spectrometer));
		}
	}
	
}
