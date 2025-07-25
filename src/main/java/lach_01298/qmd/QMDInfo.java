package lach_01298.qmd;

import lach_01298.qmd.config.QMDConfig;
import lach_01298.qmd.enums.BlockTypes.*;
import lach_01298.qmd.enums.ICoolerEnum;
import nc.Global;
import nc.util.*;
import net.minecraft.util.IStringSerializable;

public class QMDInfo
{

	// RF Cavity info
	public static String[][] RFCavityFixedInfo()
	{
		RFCavityType[] values = RFCavityType.values();
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; i++) {
			info[i] = new String[] {
					Lang.localize("info." + QMD.MOD_ID + ".rf_cavity.voltage", values[i].getVoltage()),
					Lang.localize("info." + QMD.MOD_ID + ".item.efficiency", Math.round(100D*values[i].getEfficiency()) + "%"),
					Lang.localize("info." + QMD.MOD_ID + ".item.heat", values[i].getHeatGenerated()),
					Lang.localize("info." + QMD.MOD_ID + ".item.power", values[i].getBasePower()),
					Lang.localize("info." + QMD.MOD_ID + ".item.max_temp", values[i].getMaxOperatingTemp())
					};
		}
		return info;
	}
	
	
	
	public static String[][] RFCavityInfo()
	{
		RFCavityType[] values = RFCavityType.values();
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; i++) {
			info[i] = InfoHelper.formattedInfo(Lang.localize("tile." + QMD.MOD_ID + ".rf_cavity.desc"));
		}
		return info;
	}




	// Magnet info
	public static String[][] magnetFixedInfo()
	{
		MagnetType[] values = MagnetType.values();
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; i++) {
			info[i] = new String[] {
					Lang.localize("info." + QMD.MOD_ID + ".accelerator_magnet.strength", values[i].getStrength()),
					Lang.localize("info." + QMD.MOD_ID + ".item.efficiency", Math.round(100D*values[i].getEfficiency()) + "%"),
					Lang.localize("info." + QMD.MOD_ID + ".item.heat", values[i].getHeatGenerated()),
					Lang.localize("info." + QMD.MOD_ID + ".item.power", values[i].getBasePower()),
					Lang.localize("info." + QMD.MOD_ID + ".item.max_temp", values[i].getMaxOperatingTemp())
					};
		}
		return info;
	}

	public static String[][] magnetInfo()
	{
		MagnetType[] values = MagnetType.values();
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; i++) {
			info[i] = InfoHelper.formattedInfo(Lang.localize("tile." + QMD.MOD_ID + ".accelerator_magnet.desc"));
		}
		return info;
	}
	
	
	// Cooler info
	public static String[][] cooler1FixedInfo()
	{
		return coolerFixedInfo(CoolerType1.values());
	}

	public static String[][] cooler2FixedInfo()
	{
		return coolerFixedInfo(CoolerType2.values());
	}

	private static <T extends Enum<T> & IStringSerializable & ICoolerEnum> String[][] coolerFixedInfo(T[] values) {
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; i++) {
			info[i] = new String[] {coolerCoolingRateString(values[i])};
		}
		return info;
	}

	
	private static <T extends Enum<T> & ICoolerEnum> String coolerCoolingRateString(T type)
	{
		return Lang.localize("tile." + QMD.MOD_ID + ".accelerator.cooler.cooling_rate") + " " + UnitHelper.prefix(type.getHeatRemoved(), 3, "H/t");
	}
	
	

	public static String[][] cooler1Info() {
		CoolerType1[] values = CoolerType1.values();
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; i++) {
			info[i] = InfoHelper.formattedInfo(cooler1InfoString(values[i]));
		}
		return info;
	}
	
	private static String cooler1InfoString(CoolerType1 type)
	{
		return Lang.localize("tile." + QMD.MOD_ID + ".accelerator.cooler." + type.getName() + ".desc");
	}
	
	public static String[][] cooler2Info() {
		CoolerType2[] values = CoolerType2.values();
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; i++) {
			info[i] = InfoHelper.formattedInfo(cooler2InfoString(values[i]));
		}
		return info;
	}
	
	private static String cooler2InfoString(CoolerType2 type)
	{
		return Lang.localize("tile." + QMD.MOD_ID + ".accelerator.cooler." + type.getName() + ".desc");
	}



	// Detector info
	public static String[][] detectorFixedInfo()
	{
		DetectorType[] values = DetectorType.values();
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; i++) {
			info[i] = new String[] {
					Lang.localize("info." + QMD.MOD_ID + ".particle_chamber.detector.efficiency", Math.round(1000D*values[i].getEfficiency())/10d + "%"),
					Lang.localize("info." + QMD.MOD_ID + ".particle_chamber.detector.power", values[i].getBasePower())
					};
		}
		return info;
	}


	public static String[][] detectorInfo()
	{
		DetectorType[] values = DetectorType.values();
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; i++) {
			info[i] = InfoHelper.formattedInfo(detectorInfoString(values[i]));
		}
		return info;
	}


	public static String[] ionSourceFixedInfo(int id)
	{
		String[] info = new String[] {
				Lang.localize("info." + QMD.MOD_ID + ".item.power", QMDConfig.ion_source_power[id]),
				Lang.localize("info." + QMD.MOD_ID + ".ion_source.output_multiplier",QMDConfig.ion_source_output_multiplier[id]),
				Lang.localize("info." + QMD.MOD_ID + ".ion_source.focus",QMDConfig.ion_source_focus[id])
		};
		return info;
	}

	public static String ionSourceInfo()
	{
		return Lang.localize("tile." + QMD.MOD_ID + ".ion_source.desc");
	}

	private static String detectorInfoString(DetectorType type)
	{
		return Lang.localize("tile." + QMD.MOD_ID + ".particle_chamber.detector." + type.getName() + ".desc");
	}


	public static String beamlineInfo()
	{
		return Lang.localize("tile." + QMD.MOD_ID + ".beamline.desc");
	}
	public static String beamlineFixedlineInfo()
	{
		return Lang.localize("info." + QMD.MOD_ID + ".beamline.attenuation",QMDConfig.beamAttenuationRate);
	}

	// Fission Neutron Shields

	public static String[][] neutronShieldFixedInfo()
	{
		NeutronShieldType[] values = NeutronShieldType.values();
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; i++)
		{
			info[i] = new String[] {
					Lang.localize("info." + Global.MOD_ID + ".fission_shield.heat_per_flux.fixd",
							UnitHelper.prefix(values[i].getHeatPerFlux(), 5, "H/t/N")),
					Lang.localize("info." + Global.MOD_ID + ".fission_shield.efficiency.fixd",
							Math.round(100D * values[i].getEfficiency()) + "%"), };
		}
		return info;
	}

	public static String[][] neutronShieldInfo()
	{
		NeutronShieldType[] values = NeutronShieldType.values();
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; i++)
		{
			info[i] = InfoHelper.formattedInfo(Lang.localize("tile." + Global.MOD_ID + ".fission_shield.desc"));
		}
		return info;
	}
	
	
	
	// Heater info
	public static String[][] heaterFixedInfo()
	{
		return heaterFixedInfo(HeaterType.values());
	}
	
	private static <T extends Enum<T> & IStringSerializable & ICoolerEnum> String[][] heaterFixedInfo(T[] values) {
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; i++) {
			info[i] = new String[] {coolerCoolingRateString(values[i])};
		}
		return info;
	}
	
	
	public static String drillInfo(int id)
	{
		int size = 2*QMDConfig.drill_radius[id]+1;
		return Lang.localize("info."+QMD.MOD_ID+".item.drill.desc",size,size);
	}
	
	
	
}
