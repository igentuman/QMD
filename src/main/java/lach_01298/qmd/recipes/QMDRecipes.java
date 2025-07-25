package lach_01298.qmd.recipes;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lach_01298.qmd.QMDRadSources;
import lach_01298.qmd.block.QMDBlocks;
import lach_01298.qmd.config.QMDConfig;
import lach_01298.qmd.enums.BlockTypes.*;
import lach_01298.qmd.enums.MaterialTypes.*;
import lach_01298.qmd.item.*;
import lach_01298.qmd.recipe.QMDRecipe;
import lach_01298.qmd.recipe.QMDRecipeHandler;
import lach_01298.qmd.recipe.QMDRecipeHelper;
import nc.config.NCConfig;
import nc.enumm.MetaEnums;
import nc.init.NCItems;
import nc.radiation.RadBlockEffects;
import nc.radiation.RadSources;
import nc.recipe.*;
import nc.recipe.generator.DecayGeneratorRecipes;
import nc.recipe.ingredient.*;
import nc.recipe.multiblock.*;
import nc.recipe.other.CollectorRecipes;
import nc.recipe.processor.*;
import nc.recipe.radiation.RadiationScrubberRecipes;
import nc.util.*;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

import static lach_01298.qmd.config.QMDConfig.*;
import static nc.config.NCConfig.processor_time;
import static nc.config.NCConfig.*;
import static nc.util.FluidStackHelper.BUCKET_VOLUME;

public class QMDRecipes
{
	private static boolean initialized = false;
	private static final Object2ObjectMap<String, QMDRecipeHandler> RECIPE_HANDLER_MAP = new Object2ObjectOpenHashMap();

	public static AcceleratorSourceRecipes accelerator_source;
	public static AcceleratorCoolingRecipes accelerator_cooling;
	public static MassSpectrometerRecipes mass_spectrometer;

	public static TargetChamberRecipes target_chamber;
	public static DecayChamberRecipes decay_chamber;
	public static BeamDumpRecipes beam_dump;
	public static CollisionChamberRecipes collision_chamber;

	public static OreLeacherRecipes ore_leacher;
	public static IrradiatorRecipes irradiator;
	public static IrradiatorFuel irradiator_fuel;

	public static NeutralContainmentRecipes neutral_containment;
	public static CellFillingRecipes cell_filling;
	public static NucleosynthesisChamberRecipes nucleosynthesis_chamber;
	public static VacuumChamberHeaterRecipes vacuum_chamber_heating;

	public static void putHandler(QMDRecipeHandler handler)
	{
		RECIPE_HANDLER_MAP.put(handler.getName(), handler);
	}

	public static <T extends QMDRecipeHandler> T getHandler(String name)
	{
		return (T) RECIPE_HANDLER_MAP.get(name);
	}

	public static Collection<QMDRecipeHandler> getHandlers()
	{
		return RECIPE_HANDLER_MAP.values();
	}

	public static List<QMDRecipe> getRecipeList(String name)
	{
		return getHandler(name).getRecipeList();
	}

	public static List<Set<String>> getValidFluids(String name)
	{
		return getHandler(name).validFluids;
	}

	public static final String[] RECIPE_HANDLER_NAME_ARRAY = new String[]
			{
					"accelerator_source",
					"accelerator_cooling",
					"mass_spectrometer",

					"target_chamber",
					"decay_chamber",
					"beam_dump",
					"collision_chamber",

					"ore_leacher",
					"irradiator",
					"irradiator_fuel",

					"neutral_containment",
					"cell_filling",
					"nucleosynthesis_chamber",
					"vacuum_chamber_heating"
			};

	@SubscribeEvent(priority = EventPriority.LOW)
	public void registerRecipes(RegistryEvent.Register<IRecipe> event)
	{
		if (initialized)
			return;

		putHandler(new AcceleratorSourceRecipes());
		putHandler(new AcceleratorCoolingRecipes());
		putHandler(new MassSpectrometerRecipes());

		putHandler(new TargetChamberRecipes());
		putHandler(new DecayChamberRecipes());
		putHandler(new BeamDumpRecipes());
		putHandler(new CollisionChamberRecipes());

		putHandler(new OreLeacherRecipes());
		putHandler(new IrradiatorRecipes());
		putHandler(new IrradiatorFuel());

		putHandler(new NeutralContainmentRecipes());
		putHandler(new CellFillingRecipes());
		putHandler(new NucleosynthesisChamberRecipes());
		putHandler(new VacuumChamberHeaterRecipes());

		this.registerShortcuts();
		addRecipes();

		initialized = true;
	}

	public void registerShortcuts()
	{
		accelerator_source = (AcceleratorSourceRecipes) getHandler("accelerator_source");
		accelerator_cooling = (AcceleratorCoolingRecipes) getHandler("accelerator_cooling");
		mass_spectrometer = (MassSpectrometerRecipes) getHandler("mass_spectrometer");

		target_chamber = (TargetChamberRecipes) getHandler("target_chamber");
		decay_chamber = (DecayChamberRecipes) getHandler("decay_chamber");
		beam_dump = (BeamDumpRecipes) getHandler("beam_dump");
		collision_chamber = (CollisionChamberRecipes) getHandler("collision_chamber");

		ore_leacher = (OreLeacherRecipes) getHandler("ore_leacher");
		irradiator = (IrradiatorRecipes) getHandler("irradiator");
		irradiator_fuel = (IrradiatorFuel) getHandler("irradiator_fuel");

		neutral_containment = (NeutralContainmentRecipes) getHandler("neutral_containment");
		cell_filling = (CellFillingRecipes) getHandler("cell_filling");
		nucleosynthesis_chamber = (NucleosynthesisChamberRecipes) getHandler("nucleosynthesis_chamber");
		vacuum_chamber_heating = (VacuumChamberHeaterRecipes) getHandler("vacuum_chamber_heating");

	}


	public static void init()
	{
		for (QMDRecipeHandler handler : getHandlers())
		{
			handler.init();
		}
	}

	public static void postInit()
	{
		for (QMDRecipeHandler handler : getHandlers())
		{
			handler.postInit();
		}
	}

	public static void refreshRecipeCaches()
	{
		for (QMDRecipeHandler handler : getHandlers())
		{
			handler.refreshCache();
		}
	}



	public static final List<String> PLASTIC_TYPES = Lists.newArrayList("bioplastic", "sheetPlastic");

	public static void addRecipes()
	{
		// Alloy furnace
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Steel", 5, "Chromium", 1, "StainlessSteel", 6, 1D, 1D);
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Niobium", 3, "Tin", 1, "NiobiumTin", 4, 1D, 1D);
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Niobium", 1, "Titanium", 1, "NiobiumTitanium", 2, 1D, 1D);
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Tungsten", 1, "Graphite", 1, "TungstenCarbide", 2, 2D, 2D);
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Osmium", 1, "Iridium", 1, "Osmiridium", 2, 1D, 1D);
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Nickel", 1, "Chromium", 1, "Nichrome", 2, 1D, 1D);
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Nichrome", 2, "NiobiumTitanium", 1, "SuperAlloy", 3, 1D, 1D);
		NCRecipes.alloy_furnace.addRecipe("dustZinc", "dustSulfur", "dustZincSulfide", 1D, 1D);


		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Uranium238", 9, "Uranium235", 1, "Uranium", 10, 1D, 1D);
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Boron11", 9, "Boron10", 3, "Boron", 12, 1D, 1D);
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Lithium7", 9, "Lithium6", 1, "Lithium", 10, 1D, 1D);
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Magnesium24", 8, "Magnesium26", 1, "Magnesium", 9, 1D, 1D);

		// Fluid Infuser
		NCRecipes.infuser.addRecipe(new ItemStack(QMDItems.part, 1, PartType.DETECTOR_CASING.getID()), fluidStack("liquid_hydrogen", FluidStackHelper.BUCKET_VOLUME), new ItemStack(QMDBlocks.particleChamberDetector, 1, DetectorType.BUBBLE_CHAMBER.getID()), 1D, 1D);
		NCRecipes.infuser.addRecipe(new ItemStack(QMDItems.part, 1, PartType.WIRE_CHAMBER_CASING.getID()), fluidStack("argon", FluidStackHelper.BUCKET_VOLUME), new ItemStack(QMDBlocks.particleChamberDetector, 1, DetectorType.WIRE_CHAMBER.getID()), 1D, 1D);

		NCRecipes.infuser.addRecipe(new ItemStack(QMDItems.part, 1, PartType.EMPTY_COOLER.getID()), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), new ItemStack(QMDBlocks.acceleratorCooler1, 1, CoolerType1.WATER.getID()), 1D, 1D);
		NCRecipes.infuser.addRecipe(new ItemStack(QMDItems.part, 1, PartType.EMPTY_COOLER.getID()), fluidStack("liquid_helium", FluidStackHelper.BUCKET_VOLUME), new ItemStack(QMDBlocks.acceleratorCooler2, 1, CoolerType2.LIQUID_HELIUM.getID()), 1D, 1D);
		NCRecipes.infuser.addRecipe(new ItemStack(QMDItems.part, 1, PartType.EMPTY_COOLER.getID()), fluidStack("liquid_nitrogen", FluidStackHelper.BUCKET_VOLUME), new ItemStack(QMDBlocks.acceleratorCooler2, 1, CoolerType2.LIQUID_NITROGEN.getID()), 1D, 1D);
		NCRecipes.infuser.addRecipe(new ItemStack(QMDItems.part, 1, PartType.EMPTY_COOLER.getID()), fluidStack("cryotheum", FluidStackHelper.BUCKET_VOLUME), new ItemStack(QMDBlocks.acceleratorCooler2, 1, CoolerType2.CRYOTHEUM.getID()), 1D, 1D);
		NCRecipes.infuser.addRecipe(new ItemStack(QMDItems.part, 1, PartType.EMPTY_COOLER.getID()), fluidStack("enderium", FluidStackHelper.INGOT_VOLUME * 4), new ItemStack(QMDBlocks.acceleratorCooler2, 1, CoolerType2.ENDERIUM.getID()), 1D, 1D);

		NCRecipes.infuser.addRecipe(new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.EMPTY.getID()), fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME / 4), new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.HYDROGEN.getID()), 1D, 1D);
		NCRecipes.infuser.addRecipe(new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.EMPTY.getID()), fluidStack("helium", FluidStackHelper.BUCKET_VOLUME / 4), new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.HELIUM.getID()), 1D, 1D);
		NCRecipes.infuser.addRecipe(new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.EMPTY.getID()), fluidStack("nitrogen", FluidStackHelper.BUCKET_VOLUME / 4), new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.NITROGEN.getID()), 1D, 1D);
		NCRecipes.infuser.addRecipe(new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.EMPTY.getID()), fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME / 4), new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.OXYGEN.getID()), 1D, 1D);
		NCRecipes.infuser.addRecipe(new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.EMPTY.getID()), fluidStack("neon", FluidStackHelper.BUCKET_VOLUME / 4), new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.NEON.getID()), 1D, 1D);
		NCRecipes.infuser.addRecipe(new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.EMPTY.getID()), fluidStack("argon", FluidStackHelper.BUCKET_VOLUME / 4), new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.ARGON.getID()), 1D, 1D);
		NCRecipes.infuser.addRecipe(new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.ARGON.getID()), fluidStack("sodium", FluidStackHelper.INGOT_VOLUME), new ItemStack(QMDBlocks.dischargeLamp2, 1, LampType2.SODIUM.getID()), 1D, 1D);
		NCRecipes.infuser.addRecipe(new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.ARGON.getID()), fluidStack("mercury", FluidStackHelper.INGOT_VOLUME), new ItemStack(QMDBlocks.dischargeLamp2, 1, LampType2.MERCURY.getID()), 1D, 1D);

		NCRecipes.infuser.addRecipe("dustStrontium", fluidStack("hydrochloric_acid", FluidStackHelper.BUCKET_VOLUME * 2), "dustStrontiumChloride", 1D, 1D);
		NCRecipes.infuser.addRecipe("dustIron", fluidStack("hydrofluoric_acid", 1500), "dustIronFluoride", 1D, 1D);
		NCRecipes.infuser.addRecipe("ingotIron", fluidStack("hydrofluoric_acid", 1500), "dustIronFluoride", 1D, 1D);

		NCRecipes.infuser.addOxidizingRecipe("dustCopper", FluidStackHelper.BUCKET_VOLUME);
		NCRecipes.infuser.addOxidizingRecipe("dustTungsten", FluidStackHelper.BUCKET_VOLUME);
		NCRecipes.infuser.addOxidizingRecipe("dustHafnium", FluidStackHelper.BUCKET_VOLUME);


		// Fluid Enricher
		NCRecipes.enricher.addRecipe("dustTungstenOxide", fluidStack("sodium_hydroxide_solution", FluidStackHelper.GEM_VOLUME * 2), fluidStack("sodium_tungstate_solution", FluidStackHelper.GEM_VOLUME), 1D, 1D);
		NCRecipes.enricher.addRecipe("dustLead", fluidStack("nitric_acid", FluidStackHelper.GEM_VOLUME * 2), fluidStack("lead_nitrate_solution", FluidStackHelper.GEM_VOLUME), 1D, 1D);
		NCRecipes.enricher.addRecipe("ingotYttrium", fluidStack("alumina", 120), fluidStack("yag", 48), 2D, 2D);
		NCRecipes.enricher.addRecipe("ingotNeodymium", fluidStack("yag", FluidStackHelper.INGOT_BLOCK_VOLUME), fluidStack("nd_yag", FluidStackHelper.INGOT_BLOCK_VOLUME), 2D, 2D);

		NCRecipes.enricher.addRecipe("dustSalt", fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("sodium_chloride_solution", FluidStackHelper.GEM_VOLUME), 1D, 1D);


		// Chemical reactor
		NCRecipes.chemical_reactor.addRecipe(fluidStack("sodium_tungstate_solution", FluidStackHelper.GEM_VOLUME), fluidStack("lead_nitrate_solution", FluidStackHelper.GEM_VOLUME), fluidStack("lead_tungstate_solution", FluidStackHelper.GEM_VOLUME), fluidStack("sodium_nitrate_solution", FluidStackHelper.GEM_VOLUME), 1D, 1D);


		NCRecipes.chemical_reactor.addRecipe(fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME), fluidStack("chlorine", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrochloric_acid", 2 * FluidStackHelper.BUCKET_VOLUME), new EmptyFluidIngredient(), 1D, 1D);
		NCRecipes.chemical_reactor.addRecipe(fluidStack("liquidhydrogenchloride", FluidStackHelper.BUCKET_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrochloric_acid", FluidStackHelper.BUCKET_VOLUME), new EmptyFluidIngredient(), 1D, 1D);
		NCRecipes.chemical_reactor.addRecipe(fluidStack("nitrogen", FluidStackHelper.BUCKET_VOLUME), fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME), fluidStack("nitric_oxide", FluidStackHelper.BUCKET_VOLUME * 2), new EmptyFluidIngredient(), 1D, 1D);
		NCRecipes.chemical_reactor.addRecipe(fluidStack("nitric_oxide", FluidStackHelper.BUCKET_VOLUME * 2), fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME), fluidStack("nitrogen_dioxide", FluidStackHelper.BUCKET_VOLUME * 2), new EmptyFluidIngredient(), 1D, 1D);
		NCRecipes.chemical_reactor.addRecipe(fluidStack("nitrogen_dioxide", FluidStackHelper.BUCKET_VOLUME * 3), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("nitric_acid", FluidStackHelper.BUCKET_VOLUME * 2), fluidStack("nitric_oxide", FluidStackHelper.BUCKET_VOLUME), 1D, 1D);
		NCRecipes.chemical_reactor.addRecipe(fluidStack("sodium_hydroxide_solution", FluidStackHelper.GEM_VOLUME), fluidStack("nitric_acid", FluidStackHelper.BUCKET_VOLUME), fluidStack("sodium_nitrate_solution", FluidStackHelper.GEM_VOLUME), new EmptyFluidIngredient(), 1D, 1D);

		// Separator
		NCRecipes.separator.addRecipe(AbstractRecipeHandler.oreStackList(Lists.newArrayList("ingotMagnesium", "dustMagnesium"), 9), AbstractRecipeHandler.oreStack("ingotMagnesium24", 8), AbstractRecipeHandler.oreStack("ingotMagnesium26", 1), 6D, 1D);
		NCRecipes.separator.addRecipe(AbstractRecipeHandler.oreStackList(Lists.newArrayList("ingotCalcium", "dustCalcium"), 8), AbstractRecipeHandler.oreStack("ingotCalcium48", 1), new EmptyItemIngredient(), 6D, 1D);

		// Centrifuge
		NCRecipes.centrifuge.addRecipe(fluidStack("compressed_air", FluidStackHelper.BUCKET_VOLUME * 10), fluidStack("nitrogen", FluidStackHelper.BUCKET_VOLUME * 7), fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME * 2), fluidStack("argon", 750), fluidStack("neon", 200), fluidStack("helium", 50), new EmptyFluidIngredient(), 0.1D, 1D);
		NCRecipes.centrifuge.addRecipe(fluidStack("redstone", FluidStackHelper.REDSTONE_DUST_VOLUME), fluidStack("mercury", FluidStackHelper.INGOT_VOLUME), fluidStack("sulfur", FluidStackHelper.GEM_VOLUME), new EmptyFluidIngredient(), new EmptyFluidIngredient(), new EmptyFluidIngredient(), new EmptyFluidIngredient(), 1D, 1D);
		NCRecipes.centrifuge.addRecipe(fluidStack("coal", FluidStackHelper.COAL_DUST_VOLUME), fluidStack("carbon", FluidStackHelper.COAL_DUST_VOLUME), fluidStack("sulfur", FluidStackHelper.GEM_VOLUME / 6), new EmptyFluidIngredient(), new EmptyFluidIngredient(), new EmptyFluidIngredient(), new EmptyFluidIngredient(), 1D, 1D);

		// fluid mixer
		NCRecipes.salt_mixer.addRecipe(fluidStack("mercury", FluidStackHelper.INGOT_VOLUME), fluidStack("sulfur", FluidStackHelper.GEM_VOLUME), fluidStack("redstone", FluidStackHelper.REDSTONE_DUST_VOLUME), 1D, 1D);


		//Electrolyzer

		NCRecipes.electrolyzer.addRecipe(fluidStack("sodium_chloride", FluidStackHelper.GEM_VOLUME), fluidStack("sodium", FluidStackHelper.INGOT_VOLUME), fluidStack("chlorine", FluidStackHelper.BUCKET_VOLUME / 2), new EmptyFluidIngredient(), new EmptyFluidIngredient(), 1D, 1D);
		NCRecipes.electrolyzer.addRecipe(fluidStack("sodium_chloride_solution", 2 * FluidStackHelper.GEM_VOLUME), fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME), fluidStack("chlorine", FluidStackHelper.BUCKET_VOLUME), fluidStack("sodium_hydroxide_solution", FluidStackHelper.GEM_VOLUME * 2), new EmptyFluidIngredient(), 0.5D, 1D);


		// Manufactory
		NCRecipes.manufactory.addRecipe("bouleSilicon", AbstractRecipeHandler.oreStack("waferSilicon", 4), 1D, 1D);


		// Melter
		NCRecipes.melter.addRecipe("ingotMercury", fluidStack("mercury", FluidStackHelper.INGOT_VOLUME), 0.5D, 0D);
		NCRecipes.melter.addRecipe("dustIodine", fluidStack("iodine", FluidStackHelper.INGOT_VOLUME));
		NCRecipes.melter.addRecipe("dustSamarium", fluidStack("samarium", FluidStackHelper.INGOT_VOLUME));
		NCRecipes.melter.addRecipe("dustTerbium", fluidStack("terbium", FluidStackHelper.INGOT_VOLUME));
		NCRecipes.melter.addRecipe("dustErbium", fluidStack("erbium", FluidStackHelper.INGOT_VOLUME));
		NCRecipes.melter.addRecipe("dustYtterbium", fluidStack("ytterbium", FluidStackHelper.INGOT_VOLUME));
		NCRecipes.melter.addRecipe("dustMolybdenum", fluidStack("molybdenum", FluidStackHelper.INGOT_VOLUME));
		NCRecipes.melter.addRecipe("dustBismuth", fluidStack("bismuth", FluidStackHelper.INGOT_VOLUME));
		NCRecipes.melter.addRecipe("dustPolonium", fluidStack("polonium", FluidStackHelper.INGOT_VOLUME));
		NCRecipes.melter.addRecipe("dustRadium", fluidStack("radium", FluidStackHelper.INGOT_VOLUME));


		if (FluidRegHelper.fluidExists("brine") && QMDConfig.override_nc_recipes)
		{
			List<IFluidIngredient> fluidIngredients = new ArrayList<IFluidIngredient>();
			List<IItemIngredient> itemIngredients = new ArrayList<IItemIngredient>();
			itemIngredients.add(AbstractRecipeHandler.oreStack("dustSalt", 1));
			NCRecipes.melter.removeRecipe(NCRecipes.melter.getRecipeFromIngredients(itemIngredients, fluidIngredients));
			itemIngredients.add(AbstractRecipeHandler.oreStack("itemSalt", 1));
			NCRecipes.melter.removeRecipe(NCRecipes.melter.getRecipeFromIngredients(itemIngredients, fluidIngredients));

			NCRecipes.melter.addRecipe("dustSalt", fluidStack("sodium_chloride", FluidStackHelper.GEM_VOLUME));
		}
		else if (!FluidRegHelper.fluidExists("brine"))
		{
			NCRecipes.melter.addRecipe("dustSalt", fluidStack("sodium_chloride", FluidStackHelper.GEM_VOLUME));
		}


		if (QMDConfig.override_nc_recipes)
		{
			List<IFluidIngredient> fluidIngredients = new ArrayList<IFluidIngredient>();
			List<IItemIngredient> itemIngredients = new ArrayList<IItemIngredient>();
			itemIngredients.add(AbstractRecipeHandler.oreStack("dustGraphite", 1));

			NCRecipes.melter.removeRecipe(NCRecipes.melter.getRecipeFromIngredients(itemIngredients, fluidIngredients));

			itemIngredients = new ArrayList<IItemIngredient>();
			itemIngredients.add(AbstractRecipeHandler.oreStack("dustGraphite", 1));

			NCRecipes.melter.removeRecipe(NCRecipes.melter.getRecipeFromIngredients(itemIngredients, fluidIngredients));
			NCRecipes.melter.addRecipe(AbstractRecipeHandler.oreStackList(Lists.newArrayList("dustGraphite", "ingotGraphite"), 1), fluidStack("carbon", FluidStackHelper.COAL_DUST_VOLUME));

			itemIngredients = new ArrayList<IItemIngredient>();
			itemIngredients.add(AbstractRecipeHandler.oreStack("blockGraphite", 1));

			NCRecipes.melter.removeRecipe(NCRecipes.melter.getRecipeFromIngredients(itemIngredients, fluidIngredients));
			NCRecipes.melter.addRecipe("blockGraphite", fluidStack("carbon", FluidStackHelper.COAL_BLOCK_VOLUME));

		}
		NCRecipes.melter.addRecipe(AbstractRecipeHandler.oreStackList(Lists.newArrayList("dustCharcoal", "charcoal"), 1), fluidStack("carbon", FluidStackHelper.COAL_DUST_VOLUME));


		//ingot former
		NCRecipes.ingot_former.addRecipe(fluidStack("carbon", FluidStackHelper.COAL_DUST_VOLUME), "ingotGraphite");

		if (QMDConfig.override_nc_recipes)
		{
			List<IFluidIngredient> fluidIngredients = new ArrayList<IFluidIngredient>();
			List<IItemIngredient> itemIngredients = new ArrayList<IItemIngredient>();

			fluidIngredients.add(fluidStack("coal", FluidStackHelper.COAL_DUST_VOLUME));

			NCRecipes.ingot_former.removeRecipe(NCRecipes.ingot_former.getRecipeFromIngredients(itemIngredients, fluidIngredients));
			NCRecipes.ingot_former.addRecipe(fluidStack("coal", FluidStackHelper.COAL_DUST_VOLUME), "coal", 0.5D, 1D);
		}

		// Crystallizer
		NCRecipes.crystallizer.addRecipe(fluidStack("silicon", FluidStackHelper.INGOT_BLOCK_VOLUME), "bouleSilicon", 2D, 2D);
		NCRecipes.crystallizer.addRecipe(fluidStack("lead_tungstate_solution", FluidStackHelper.GEM_VOLUME), new ItemStack(QMDItems.part, 1, PartType.SCINTILLATOR_PWO.getID()), 1D, 1D);
		NCRecipes.crystallizer.addRecipe(fluidStack("sodium_nitrate_solution", FluidStackHelper.GEM_VOLUME), "dustSodiumNitrate", 1D, 1D);
		NCRecipes.crystallizer.addRecipe(fluidStack("sodium_chloride_solution", FluidStackHelper.GEM_VOLUME), "dustSalt", 1D, 1D);
		NCRecipes.crystallizer.addRecipe(fluidStack("nd_yag", FluidStackHelper.INGOT_VOLUME * 3), "rodNdYAG", 2D, 2D);
		NCRecipes.crystallizer.addRecipe(fluidStack("water", FluidStackHelper.BUCKET_VOLUME * 10), "dustSalt", 1D, 4D);    //TODO temporary recipe


		NCRecipes.crystallizer.addRecipe(fluidStack("iodine", FluidStackHelper.INGOT_VOLUME), "dustIodine", 0.25D, 0D);
		NCRecipes.crystallizer.addRecipe(fluidStack("samarium", FluidStackHelper.INGOT_VOLUME), "dustSamarium", 0.25D, 0D);
		NCRecipes.crystallizer.addRecipe(fluidStack("terbium", FluidStackHelper.INGOT_VOLUME), "dustTerbium", 0.25D, 0D);
		NCRecipes.crystallizer.addRecipe(fluidStack("erbium", FluidStackHelper.INGOT_VOLUME), "dustErbium", 0.25D, 0D);
		NCRecipes.crystallizer.addRecipe(fluidStack("ytterbium", FluidStackHelper.INGOT_VOLUME), "dustYtterbium", 0.25D, 0D);
		NCRecipes.crystallizer.addRecipe(fluidStack("molybdenum", FluidStackHelper.INGOT_VOLUME), "dustMolybdenum", 0.25D, 0D);

		NCRecipes.crystallizer.addRecipe(fluidStack("bismuth", FluidStackHelper.INGOT_VOLUME), "dustbismuth", 0.25D, 0D);
		NCRecipes.crystallizer.addRecipe(fluidStack("radium", FluidStackHelper.INGOT_VOLUME), "dustRadium", 0.25D, 0D);
		NCRecipes.crystallizer.addRecipe(fluidStack("polonium", FluidStackHelper.INGOT_VOLUME), "dustPolonium", 0.25D, 0D);
		NCRecipes.crystallizer.addRecipe(fluidStack("europium_155", FluidStackHelper.INGOT_VOLUME), "dustEuropium155", 0.25D, 0D);
		NCRecipes.crystallizer.addRecipe(fluidStack("ruthenium_106", FluidStackHelper.INGOT_VOLUME), "dustRuthenium106", 0.25D, 0D);
		NCRecipes.crystallizer.addRecipe(fluidStack("strontium_90", FluidStackHelper.INGOT_VOLUME), "dustStrontium90", 0.25D, 0D);
		NCRecipes.crystallizer.addRecipe(fluidStack("promethium_147", FluidStackHelper.INGOT_VOLUME), "dustPromethium147", 0.25D, 0D);
		NCRecipes.crystallizer.addRecipe(fluidStack("caesium_137", FluidStackHelper.INGOT_VOLUME), "dustCaesium137", 0.25D, 0D);

		// Pressurizer
		NCRecipes.pressurizer.addRecipe((AbstractRecipeHandler.oreStack("dustStrontium90", 9)), "blockStrontium90", 1D, 2D);


		// SuperCooler
		if (QMDConfig.override_nc_recipes)
		{
			List<IItemIngredient> emptyitems = new ArrayList<IItemIngredient>();
			List<IFluidIngredient> helium = new ArrayList<IFluidIngredient>();
			helium.add(fluidStack("helium", FluidStackHelper.BUCKET_VOLUME * 8));
			List<IFluidIngredient> nitrogen = new ArrayList<IFluidIngredient>();
			nitrogen.add(fluidStack("nitrogen", FluidStackHelper.BUCKET_VOLUME * 8));

			NCRecipes.supercooler.removeRecipe(NCRecipes.supercooler.getRecipeFromIngredients(emptyitems, helium));
			NCRecipes.supercooler.removeRecipe(NCRecipes.supercooler.getRecipeFromIngredients(emptyitems, nitrogen));

			NCRecipes.supercooler.addRecipe(fluidStack("helium", 64), fluidStack("liquid_helium", 1), 1D / 150D, 5D);
			NCRecipes.supercooler.addRecipe(fluidStack("nitrogen", 64), fluidStack("liquid_nitrogen", 1), 1D / 150D, 2.5D);

			NCRecipes.supercooler.addRecipe(fluidStack("hydrogen", 64), fluidStack("liquid_hydrogen", 1), 1D / 150D, 3.75D);
			NCRecipes.supercooler.addRecipe(fluidStack("neon", 64), fluidStack("liquid_neon", 1), 1D / 150D, 3.75D);
			NCRecipes.supercooler.addRecipe(fluidStack("argon", 64), fluidStack("liquid_argon", 1), 1D / 150D, 2.5D);
			NCRecipes.supercooler.addRecipe(fluidStack("oxygen", 64), fluidStack("liquid_oxygen", 1), 1D / 150D, 2.5D);

		}
		else
		{
			NCRecipes.supercooler.addRecipe(fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME * 8), fluidStack("liquid_hydrogen", 25), 1D, 1D);
			NCRecipes.supercooler.addRecipe(fluidStack("neon", FluidStackHelper.BUCKET_VOLUME * 8), fluidStack("liquid_neon", 25), 1D, 1D);
			NCRecipes.supercooler.addRecipe(fluidStack("argon", FluidStackHelper.BUCKET_VOLUME * 8), fluidStack("liquid_argon", 25), 0.5D, 0.5D);
			NCRecipes.supercooler.addRecipe(fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME * 8), fluidStack("liquid_oxygen", 25), 0.5D, 0.5D);
		}


		// Decay Hastener
		if (QMDConfig.override_nc_recipes)
		{
			List<IItemIngredient> itemIngredients = new ArrayList<IItemIngredient>();
			itemIngredients.add(new ItemIngredient(new ItemStack(NCItems.plutonium, 1, MetaEnums.PlutoniumType._238.getID())));
			List<IFluidIngredient> fluidIngredients = new ArrayList<IFluidIngredient>();
			NCRecipes.decay_hastener.removeRecipe(NCRecipes.decay_hastener.getRecipeFromIngredients(itemIngredients, fluidIngredients));

			NCRecipes.decay_hastener.addDecayRecipes("Plutonium238", "Uranium234", RadSources.PLUTONIUM_238);
		}


		NCRecipes.decay_hastener.addDecayRecipes("Beryllium7", "Lithium7", QMDRadSources.BERYLLIUM_7);
		NCRecipes.decay_hastener.addDecayRecipes("Protactinium231", "Lead", QMDRadSources.PROTACTINIUM_231);

		NCRecipes.decay_hastener.addDecayRecipes("Uranium234", "Radium", QMDRadSources.URANIUM_234);
		NCRecipes.decay_hastener.addRecipe("ingotCobalt60", "dustNickel", getDecayHasenerTimeMultipler(QMDRadSources.COBALT_60), 1d, QMDRadSources.COBALT_60);
		NCRecipes.decay_hastener.addRecipe("ingotIridium192", "dustPlatinum", getDecayHasenerTimeMultipler(QMDRadSources.IRIDIUM_192), 1d, QMDRadSources.IRIDIUM_192);


		// Assembler
		NCRecipes.assembler.addRecipe(AbstractRecipeHandler.oreStack("dustBSCCO", 3), AbstractRecipeHandler.oreStack("ingotSilver", 6), new EmptyItemIngredient(), new EmptyItemIngredient(), AbstractRecipeHandler.oreStack("wireBSCCO", 6), 1D, 1D);
		NCRecipes.assembler.addRecipe(AbstractRecipeHandler.oreStack("dustSSFAF", 3), AbstractRecipeHandler.oreStack("ingotSilver", 6), new EmptyItemIngredient(), new EmptyItemIngredient(), AbstractRecipeHandler.oreStack("wireSSFAF", 6), 1D, 1D);
		NCRecipes.assembler.addRecipe(AbstractRecipeHandler.oreStack("dustYBCO", 3), AbstractRecipeHandler.oreStack("ingotSilver", 6), new EmptyItemIngredient(), new EmptyItemIngredient(), AbstractRecipeHandler.oreStack("wireYBCO", 6), 1D, 1D);
		NCRecipes.assembler.addRecipe(AbstractRecipeHandler.oreStack("ingotTungsten", 4), AbstractRecipeHandler.oreStack("ingotGold", 2), new EmptyItemIngredient(), new EmptyItemIngredient(), AbstractRecipeHandler.oreStack("wireGoldTungsten", 6), 1D, 1D);
		NCRecipes.assembler.addRecipe(AbstractRecipeHandler.oreStack("dustBismuth", 2), AbstractRecipeHandler.oreStack("dustStrontium", 2), AbstractRecipeHandler.oreStack("dustCalcium", 2), AbstractRecipeHandler.oreStack("dustCopperOxide", 3), AbstractRecipeHandler.oreStack("dustBSCCO", 3), 1D, 1D);
		NCRecipes.assembler.addRecipe(AbstractRecipeHandler.oreStack("dustSamarium", 1), AbstractRecipeHandler.oreStack("dustStrontium", 1), AbstractRecipeHandler.oreStack("dustIronFluoride", 2), AbstractRecipeHandler.oreStack("dustArsenic", 2), AbstractRecipeHandler.oreStack("dustSSFAF", 6), 1D, 1D);
		NCRecipes.assembler.addRecipe(AbstractRecipeHandler.oreStack("dustYttrium", 1), AbstractRecipeHandler.oreStack("dustBarium", 2), AbstractRecipeHandler.oreStack("dustCopperOxide", 3), new EmptyItemIngredient(), AbstractRecipeHandler.oreStack("dustYBCO", 3), 1D, 1D);

		NCRecipes.assembler.addRecipe(AbstractRecipeHandler.oreStackList(PLASTIC_TYPES, 2), AbstractRecipeHandler.oreStack("dyeBlue", 1), new EmptyItemIngredient(), new EmptyItemIngredient(), new ItemStack(QMDItems.part, 1, PartType.SCINTILLATOR_PLASTIC.getID()), 1D, 1D);
		NCRecipes.assembler.addRecipe("siliconNDoped", AbstractRecipeHandler.oreStack("dustRedstone", 4), "ingotGold", "ingotSilver", "processorBasic", 1D, 1D);
		NCRecipes.assembler.addRecipe("processorBasic", AbstractRecipeHandler.oreStack("dustRedstone", 4), "dustHafniumOxide", "siliconPDoped", "processorAdvanced", 1D, 1D);
		NCRecipes.assembler.addRecipe("processorAdvanced", AbstractRecipeHandler.oreStack("wireBSCCO", 4), "dustHafniumOxide", "ingotPlatinum", "processorElite", 1D, 1D);
		NCRecipes.assembler.addRecipe(AbstractRecipeHandler.oreStack("ingotTungsten", 2), new EmptyItemIngredient(), new EmptyItemIngredient(), new EmptyItemIngredient(), IItemParticleAmount.fullItem(new ItemStack(QMDItems.source, 1, SourceType.TUNGSTEN_FILAMENT.getID())), 1D, 1D);
		NCRecipes.assembler.addRecipe(AbstractRecipeHandler.oreStack("ingotFerroboron", 2), "ingotNeodymium", new EmptyItemIngredient(), new EmptyItemIngredient(), "magnetNeodymium", 1D, 1D);
		NCRecipes.assembler.addRecipe("dustPotassium", "dustIodine", new ItemStack(Items.SUGAR, 4), "bioplastic", new ItemStack(QMDItems.potassiumIodineTablet, 4), 1D, 1D);

		//Fission Irradiator
		NCRecipes.fission_irradiator.addRecipe("waferSilicon", "siliconNDoped", 120000, 0d, 0);
		NCRecipes.fission_irradiator.addRecipe("ingotUranium234", "ingotUranium235", 1920000, 0d, QMDRadSources.URANIUM_234);
		NCRecipes.fission_irradiator.addRecipe("dustProtactinium231", "dustProtactinium233", 3840000, 0d, QMDRadSources.PROTACTINIUM_231);
		NCRecipes.fission_irradiator.addRecipe("ingotCobalt", "ingotCobalt60", 1920000, 0d, 0);
		//NCRecipes.fission_irradiator.addRecipe(FluidUtil.getFilledBucket(fluidStack("deuterium", 1000).getStack()), FluidUtil.getFilledBucket(fluidStack("tritium", 1000).getStack()),60000,0d,0); //1920000


		//fuel reprocessor
		NCRecipes.fuel_reprocessor.addRecipe("wasteFissionLight", AbstractRecipeHandler.chanceOreStack("dustStrontium", 1, 20), AbstractRecipeHandler.chanceOreStack("dustStrontium90", 1, 5), AbstractRecipeHandler.chanceOreStack("dustYttrium", 1, 5), AbstractRecipeHandler.chanceOreStack("dustZirconium", 1, 20), AbstractRecipeHandler.chanceOreStack("dustNiobium", 1, 5), AbstractRecipeHandler.chanceOreStack("dustMolybdenum", 1, 30), AbstractRecipeHandler.chanceOreStack("dustRuthenium106", 1, 5), AbstractRecipeHandler.chanceOreStack("dustSilver", 1, 10));
		NCRecipes.fuel_reprocessor.addRecipe("wasteFissionHeavy", AbstractRecipeHandler.chanceOreStack("dustNiobium", 1, 4), AbstractRecipeHandler.chanceOreStack("dustMolybdenum", 1, 21), AbstractRecipeHandler.chanceOreStack("dustRuthenium106", 1, 4), AbstractRecipeHandler.chanceOreStack("dustSilver", 1, 7), AbstractRecipeHandler.chanceOreStack("dustTin", 1, 35), AbstractRecipeHandler.chanceOreStack("dustIodine", 1, 7), AbstractRecipeHandler.chanceOreStack("dustCaesium137", 1, 4), AbstractRecipeHandler.chanceOreStack("dustNeodymium", 1, 18));
		NCRecipes.fuel_reprocessor.addRecipe("wasteSpallationCalifornium", AbstractRecipeHandler.chanceOreStack("dustThorium", 1, 26), AbstractRecipeHandler.chanceOreStack("dustProtactinium231", 1, 13), AbstractRecipeHandler.chanceOreStack("dustRadium", 1, 12), AbstractRecipeHandler.chanceOreStack("dustPolonium", 1, 9), AbstractRecipeHandler.chanceOreStack("dustBismuth", 1, 20), AbstractRecipeHandler.chanceOreStack("dustLead", 1, 20), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCRecipes.fuel_reprocessor.addRecipe("wasteSpallationBerkelium", AbstractRecipeHandler.chanceOreStack("dustRadium", 1, 9), AbstractRecipeHandler.chanceOreStack("dustPolonium", 1, 15), AbstractRecipeHandler.chanceOreStack("dustBismuth", 1, 40), AbstractRecipeHandler.chanceOreStack("dustLead", 1, 35), AbstractRecipeHandler.chanceOreStack("ingotMercury", 1, 1), new EmptyItemIngredient(), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCRecipes.fuel_reprocessor.addRecipe("wasteSpallationCurium", AbstractRecipeHandler.chanceOreStack("dustRadium", 1, 13), AbstractRecipeHandler.chanceOreStack("dustPolonium", 1, 17), AbstractRecipeHandler.chanceOreStack("dustBismuth", 1, 16), AbstractRecipeHandler.chanceOreStack("dustLead", 1, 50), AbstractRecipeHandler.chanceOreStack("ingotMercury", 1, 4), new EmptyItemIngredient(), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCRecipes.fuel_reprocessor.addRecipe("wasteSpallationAmericium", AbstractRecipeHandler.chanceOreStack("dustPolonium", 1, 22), AbstractRecipeHandler.chanceOreStack("dustBismuth", 1, 15), AbstractRecipeHandler.chanceOreStack("dustLead", 1, 55), AbstractRecipeHandler.chanceOreStack("ingotMercury", 1, 5), AbstractRecipeHandler.chanceOreStack("dustGold", 1, 1), AbstractRecipeHandler.chanceOreStack("dustPlatinum", 1, 2), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCRecipes.fuel_reprocessor.addRecipe("wasteSpallationPlutonium", AbstractRecipeHandler.chanceOreStack("dustPolonium", 1, 22), AbstractRecipeHandler.chanceOreStack("dustBismuth", 1, 14), AbstractRecipeHandler.chanceOreStack("dustLead", 1, 55), AbstractRecipeHandler.chanceOreStack("ingotMercury", 1, 5), AbstractRecipeHandler.chanceOreStack("dustGold", 1, 1), AbstractRecipeHandler.chanceOreStack("dustPlatinum", 1, 3), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCRecipes.fuel_reprocessor.addRecipe("wasteSpallationNeptunium", AbstractRecipeHandler.chanceOreStack("dustPolonium", 1, 36), AbstractRecipeHandler.chanceOreStack("dustBismuth", 1, 17), AbstractRecipeHandler.chanceOreStack("dustLead", 1, 34), AbstractRecipeHandler.chanceOreStack("ingotMercury", 1, 7), AbstractRecipeHandler.chanceOreStack("dustGold", 1, 2), AbstractRecipeHandler.chanceOreStack("dustPlatinum", 1, 4), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCRecipes.fuel_reprocessor.addRecipe("wasteSpallationUranium", AbstractRecipeHandler.chanceOreStack("dustPolonium", 1, 21), AbstractRecipeHandler.chanceOreStack("dustBismuth", 1, 12), AbstractRecipeHandler.chanceOreStack("dustLead", 1, 55), AbstractRecipeHandler.chanceOreStack("ingotMercury", 1, 7), AbstractRecipeHandler.chanceOreStack("dustGold", 1, 1), AbstractRecipeHandler.chanceOreStack("dustPlatinum", 1, 4), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCRecipes.fuel_reprocessor.addRecipe("wasteSpallationThorium", AbstractRecipeHandler.chanceOreStack("dustPolonium", 1, 10), AbstractRecipeHandler.chanceOreStack("dustBismuth", 1, 7), AbstractRecipeHandler.chanceOreStack("dustLead", 1, 62), AbstractRecipeHandler.chanceOreStack("ingotMercury", 1, 11), AbstractRecipeHandler.chanceOreStack("dustGold", 1, 2), AbstractRecipeHandler.chanceOreStack("dustPlatinum", 1, 8), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCRecipes.fuel_reprocessor.addRecipe("wasteSpallationProtactinium", AbstractRecipeHandler.chanceOreStack("dustPolonium", 1, 36), AbstractRecipeHandler.chanceOreStack("dustBismuth", 1, 6), AbstractRecipeHandler.chanceOreStack("dustLead", 1, 39), AbstractRecipeHandler.chanceOreStack("ingotMercury", 1, 10), AbstractRecipeHandler.chanceOreStack("dustGold", 1, 2), AbstractRecipeHandler.chanceOreStack("dustPlatinum", 1, 7), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCRecipes.fuel_reprocessor.addRecipe("wasteSpallationRadium", AbstractRecipeHandler.chanceOreStack("dustLead", 1, 58), AbstractRecipeHandler.chanceOreStack("ingotMercury", 1, 18), AbstractRecipeHandler.chanceOreStack("dustGold", 1, 3), AbstractRecipeHandler.chanceOreStack("dustPlatinum", 1, 10), AbstractRecipeHandler.chanceOreStack("dustIridium", 1, 6), AbstractRecipeHandler.chanceOreStack("dustOsmium", 1, 5), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCRecipes.fuel_reprocessor.addRecipe("wasteSpallationPolonium", AbstractRecipeHandler.chanceOreStack("dustIridium", 1, 52), AbstractRecipeHandler.chanceOreStack("dustOsmium", 1, 21), AbstractRecipeHandler.chanceOreStack("dustTungsten", 1, 12), AbstractRecipeHandler.chanceOreStack("dustHafnium", 1, 10), AbstractRecipeHandler.chanceOreStack("dustYtterbium", 1, 5), new EmptyItemIngredient(), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCRecipes.fuel_reprocessor.addRecipe("wasteSpallationBismuth", AbstractRecipeHandler.chanceOreStack("dustIridium", 1, 42), AbstractRecipeHandler.chanceOreStack("dustOsmium", 1, 27), AbstractRecipeHandler.chanceOreStack("dustTungsten", 1, 14), AbstractRecipeHandler.chanceOreStack("dustHafnium", 1, 11), AbstractRecipeHandler.chanceOreStack("dustYtterbium", 1, 6), new EmptyItemIngredient(), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCRecipes.fuel_reprocessor.addRecipe("wasteSpallationLead", AbstractRecipeHandler.chanceOreStack("dustIridium", 1, 27), AbstractRecipeHandler.chanceOreStack("dustOsmium", 1, 35), AbstractRecipeHandler.chanceOreStack("dustTungsten", 1, 15), AbstractRecipeHandler.chanceOreStack("dustHafnium", 1, 12), AbstractRecipeHandler.chanceOreStack("dustYtterbium", 1, 8), AbstractRecipeHandler.chanceOreStack("dustErbium", 1, 3), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCRecipes.fuel_reprocessor.addRecipe("wasteSpallationMercury", AbstractRecipeHandler.chanceOreStack("dustOsmium", 1, 42), AbstractRecipeHandler.chanceOreStack("dustTungsten", 1, 27), AbstractRecipeHandler.chanceOreStack("dustHafnium", 1, 16), AbstractRecipeHandler.chanceOreStack("dustYtterbium", 1, 10), AbstractRecipeHandler.chanceOreStack("dustErbium", 1, 3), new EmptyItemIngredient(), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCRecipes.fuel_reprocessor.addRecipe("wasteSpallationGold", AbstractRecipeHandler.chanceOreStack("dustHafnium", 1, 72), AbstractRecipeHandler.chanceOreStack("dustYtterbium", 1, 20), AbstractRecipeHandler.chanceOreStack("dustErbium", 1, 8), new EmptyItemIngredient(), new EmptyItemIngredient(), new EmptyItemIngredient(), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCRecipes.fuel_reprocessor.addRecipe("wasteSpallationPlatinum", AbstractRecipeHandler.chanceOreStack("dustTungsten", 1, 30), AbstractRecipeHandler.chanceOreStack("dustHafnium", 1, 44), AbstractRecipeHandler.chanceOreStack("dustYtterbium", 1, 17), AbstractRecipeHandler.chanceOreStack("dustErbium", 1, 7), AbstractRecipeHandler.chanceOreStack("dustTerbium", 1, 2), new EmptyItemIngredient(), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCRecipes.fuel_reprocessor.addRecipe("wasteSpallationIridium", AbstractRecipeHandler.chanceOreStack("dustHafnium", 1, 59), AbstractRecipeHandler.chanceOreStack("dustYtterbium", 1, 29), AbstractRecipeHandler.chanceOreStack("dustErbium", 1, 10), AbstractRecipeHandler.chanceOreStack("dustTerbium", 1, 2), new EmptyItemIngredient(), new EmptyItemIngredient(), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCRecipes.fuel_reprocessor.addRecipe("wasteSpallationOsmium", AbstractRecipeHandler.chanceOreStack("dustHafnium", 1, 48), AbstractRecipeHandler.chanceOreStack("dustYtterbium", 1, 35), AbstractRecipeHandler.chanceOreStack("dustErbium", 1, 11), AbstractRecipeHandler.chanceOreStack("dustTerbium", 1, 3), AbstractRecipeHandler.chanceOreStack("dustEuropium155", 1, 3), new EmptyItemIngredient(), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCRecipes.fuel_reprocessor.addRecipe("wasteSpallationTungsten", AbstractRecipeHandler.chanceOreStack("dustYtterbium", 1, 50), AbstractRecipeHandler.chanceOreStack("dustErbium", 1, 26), AbstractRecipeHandler.chanceOreStack("dustTerbium", 1, 6), AbstractRecipeHandler.chanceOreStack("dustEuropium155", 1, 5), AbstractRecipeHandler.chanceOreStack("dustSamarium", 1, 7), AbstractRecipeHandler.chanceOreStack("dustNeodymium", 1, 6), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCRecipes.fuel_reprocessor.addRecipe("wasteSpallationHafnium", AbstractRecipeHandler.chanceOreStack("dustErbium", 1, 32), AbstractRecipeHandler.chanceOreStack("dustTerbium", 1, 16), AbstractRecipeHandler.chanceOreStack("dustEuropium155", 1, 11), AbstractRecipeHandler.chanceOreStack("dustSamarium", 1, 13), AbstractRecipeHandler.chanceOreStack("dustNeodymium", 1, 23), AbstractRecipeHandler.chanceOreStack("dustPromethium147", 1, 5), new EmptyItemIngredient(), new EmptyItemIngredient());


		//Collectors
		AtmosphereCollectorRecipes.registerRecipes();

		// Fission reflector
		for (int i = 0; i < NeutronReflectorType.values().length; i++)
		{
			NCRecipes.fission_reflector.addRecipe(new ItemStack(QMDBlocks.fissionReflector, 1, i), QMDConfig.fission_reflector_efficiency[i], QMDConfig.fission_reflector_reflectivity[i]);
		}

		//Fission fuel recipes
		addFissionFuelRecipes();


		// Fission Heating
		NCRecipes.fission_heating.addRecipe(fluidStack("mercury", 1),fluidStack("high_pressure_mercury", 2),512);
		NCRecipes.fission_heating.addRecipe(fluidStack("hot_mercury", 1),fluidStack("high_pressure_mercury", 2),256);

		// Turbine
		NCRecipes.turbine.addRecipe(fluidStack("high_pressure_mercury", 1),fluidStack("exhaust_mercury", 3),256D,3.0);

		//Heat Exchanger
		NCRecipes.heat_exchanger.addRecipe(fluidStack("exhaust_mercury", 6),fluidStack("hot_mercury", 1), 128D,700,700);

		// Condenser
		NCRecipes.condenser.addRecipe(fluidStack("exhaust_mercury", 6),fluidStack("hot_mercury", 1), 128D,700,700);
		NCRecipes.condenser.addRecipe(fluidStack("hot_mercury", 1),fluidStack("mercury", 1), 256D,700,300);


		// Crafting
		QMDCraftingRecipeHandler.registerCraftingRecipes();

		// Furnace
		for (int i = 0; i < IngotType.values().length; i++)
		{
			String type = StringHelper.capitalize(IngotType.values()[i].getName());
			if (!ore_dict_raw_material_recipes)
			{
				GameRegistry.addSmelting(new ItemStack(QMDItems.dust, 1, i), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(QMDItems.ingot, 1, i), "ingot" + type), 0F);
			}
			else for (ItemStack dust : OreDictionary.getOres("dust" + type))
			{
				GameRegistry.addSmelting(dust, OreDictHelper.getPrioritisedCraftingStack(new ItemStack(QMDItems.ingot, 1, i), "ingot" + type), 0F);
			}
		}

		for (int i = 0; i < IngotType2.values().length; i++)
		{
			if (i == IngotType2.MERCURY.getID())
			{
				continue;
			}

			String type = StringHelper.capitalize(IngotType2.values()[i].getName());
			if (!ore_dict_raw_material_recipes)
			{
				GameRegistry.addSmelting(new ItemStack(QMDItems.dust2, 1, i), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(QMDItems.ingot2, 1, i), "ingot" + type), 0F);
			}
			else for (ItemStack dust : OreDictionary.getOres("dust" + type))
			{
				GameRegistry.addSmelting(dust, OreDictHelper.getPrioritisedCraftingStack(new ItemStack(QMDItems.ingot2, 1, i), "ingot" + type), 0F);
			}
		}

	}


	private static void addFissionFuelRecipes()
	{
		NCRecipes.solid_fission.addFuelDepleteRecipes(copernicium_fuel_time, copernicium_heat_generation, copernicium_efficiency, copernicium_criticality, copernicium_decay_factor, copernicium_self_priming, copernicium_radiation, "MIX291");
		NCRecipes.pebble_fission.addFuelDepleteRecipes(copernicium_fuel_time, copernicium_heat_generation, copernicium_efficiency, copernicium_criticality, copernicium_decay_factor, copernicium_self_priming, copernicium_radiation, "MIX291");

		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Copernicium291", 1, "Zirconium", 1, "Copernicium291ZA", 1, 1D, 1D);
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Copernicium291", 1, "Graphite", 1, "Copernicium291Carbide", 1, 1D, 1D);

		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("MIX291", 1, "Zirconium", 1, "MIX291ZA", 1, 1D, 1D);
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("MIX291", 1, "Graphite", 1, "MIX291Carbide", 1, 1D, 1D);

		NCRecipes.infuser.addRecipe("ingot" + "Copernicium291", fluidStack("oxygen", BUCKET_VOLUME), "ingotCopernicium291Oxide", 1D, 1D);
		NCRecipes.infuser.addRecipe("ingot" + "Copernicium291", fluidStack("nitrogen", BUCKET_VOLUME), "ingotCopernicium291Nitride", 1D, 1D);
		NCRecipes.infuser.addRecipe("ingot" + "MIX291", fluidStack("oxygen", BUCKET_VOLUME), "ingotMIX291Oxide", 1D, 1D);
		NCRecipes.infuser.addRecipe("ingot" + "MIX291", fluidStack("nitrogen", BUCKET_VOLUME), "ingotMIX291Nitride", 1D, 1D);

		NCRecipes.assembler.addRecipe(AbstractRecipeHandler.oreStack("ingotMIX291Carbide", 9), "dustGraphite", "ingotPyrolyticCarbon", "ingotSiliconCarbide", AbstractRecipeHandler.oreStack("ingotMIX291TRISO", 9), 1D, 1D);

		NCRecipes.fuel_reprocessor.addReprocessingRecipes("MIX291", "Americium243", 4, "Curium243", 2, "Curium245", 1, "Berkelium247", 1, "Ruthenium106", "Europium155", 0.5D, 60);

		NCRecipes.separator.addRecipe("ingotCopernicium291Carbide", "ingotCopernicium291", "dustGraphite");
		NCRecipes.separator.addRecipe("ingotCopernicium291ZA", "ingotCopernicium291", "dustZirconium");

		NCRecipes.separator.addRecipe("ingotMIX291Carbide", "ingotMIX291", "dustGraphite");
		NCRecipes.separator.addRecipe("ingotMIX291ZA", "ingotMIX291", "dustZirconium");

		NCRecipes.separator.addRecipe("ingotMIX291", "ingotCopernicium291", AbstractRecipeHandler.oreStack("ingotUranium238", 8));
		reductionIsotopeRecipes(QMDItems.copernicium, 1);
		reductionFissionFuelRecipes(QMDItems.pellet_copernicium, QMDItems.fuel_copernicium, 1);
	}


	public static void reductionIsotopeRecipes(Item isotope, int noTypes)
	{
		for (int i = 0; i < noTypes; i++)
		{
			GameRegistry.addSmelting(new ItemStack(isotope, 1, 5 * i + 2), new ItemStack(isotope, 1, 5 * i), 0F);
			GameRegistry.addSmelting(new ItemStack(isotope, 1, 5 * i + 3), new ItemStack(isotope, 1, 5 * i), 0F);
		}
	}

	public static void reductionFissionFuelRecipes(Item pellet, Item fuel, int noTypes)
	{
		for (int i = 0; i < noTypes; i++)
		{
			GameRegistry.addSmelting(new ItemStack(fuel, 1, 4 * i + 1), new ItemStack(pellet, 1, 2 * i), 0F);
			GameRegistry.addSmelting(new ItemStack(fuel, 1, 4 * i + 2), new ItemStack(pellet, 1, 2 * i), 0F);
		}
	}

	public static double getDecayHasenerTimeMultipler(double radiation)
	{
		double F = Math.log1p(Math.log(2D)), Z = 0.1674477985420331D;
		return NCMath.roundTo(Z * (radiation >= 1D ? F / Math.log1p(Math.log1p(radiation)) : Math.log1p(Math.log1p(1D / radiation)) / F), 5D / NCConfig.processor_time[2]);
	}


	public static FluidIngredient fluidStack(String fluidName, int stackSize)
	{
		if (!FluidRegHelper.fluidExists(fluidName))
			return null;
		return new FluidIngredient(fluidName, stackSize);
	}

}
