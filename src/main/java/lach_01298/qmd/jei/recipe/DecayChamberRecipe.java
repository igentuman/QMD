package lach_01298.qmd.jei.recipe;

import lach_01298.qmd.recipe.QMDRecipe;
import lach_01298.qmd.recipes.QMDRecipes;
import lach_01298.qmd.util.Units;
import mezz.jei.api.IGuiHelper;
import nc.util.Lang;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;
import java.text.DecimalFormat;

public class DecayChamberRecipe extends JEIRecipeWrapper
{

	public DecayChamberRecipe(IGuiHelper guiHelper, QMDRecipe recipe)
	{
		super(guiHelper, QMDRecipes.decay_chamber , recipe);
	}
	

	
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
	{
		FontRenderer fontRenderer = minecraft.fontRenderer;

		
		DecimalFormat df = new DecimalFormat("#.##");
		String maxEnergyString = Lang.localize("gui.qmd.jei.reaction.max_energy", Units.getSIFormat(recipe.getMaxEnergy(),3,"eV"));
		String crossSectionString = Lang.localize("gui.qmd.jei.reaction.cross_section", df.format(recipe.getCrossSection()*100));
		String energyReleasedString = Lang.localize("gui.qmd.jei.reaction.energy_released", Units.getParticleEnergy(recipe.getEnergyReleased()));
		
		
		fontRenderer.drawString(crossSectionString, 0, 72, Color.gray.getRGB());
		fontRenderer.drawString(energyReleasedString, 0, 82, Color.gray.getRGB());
		
		if(recipe.getMaxEnergy() != Long.MAX_VALUE)
		{
			fontRenderer.drawString(maxEnergyString, 0, 92, Color.gray.getRGB());
		}
	}

}
