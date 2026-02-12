package cn.mcmod.sakura.api.recipes;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.mcmod.ppot.PotmanRegistry;
import cn.mcmod.ppot.recipe.BasicPotRecipe;
import cn.mcmod.sakura.SakuraMain;
import cn.mcmod_mmf.mmlib.util.RecipesUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.oredict.OreDictionary;

public class PotRecipes {
	public final Map<Pair<Object[], ItemStack>, List<FluidStack>> RecipesList = Maps.newHashMap();
	
	private static final PotRecipes RECIPE_BASE = new PotRecipes();
    
	public static PotRecipes getInstance() {
		return RECIPE_BASE;
	}

	private int recipe_count = 0;
	
    public void addRecipes(ItemStack result, Object[] list, FluidStack fluidStack) {
		addRecipes(result, list, Lists.newArrayList(fluidStack));
    }
    public void addRecipes(ItemStack result, Object[] list) {
		addRecipes(result, list, RecipesUtil.getInstance().EMPTY_FLUID);
    }
    public void addRecipes(ItemStack result, Object[] list, List<FluidStack> listfluid) {
    	if(listfluid.isEmpty()) {
    		SakuraMain.logger.warn("Some one using an empty fluid list!!! When Craft"+result.getDisplayName());
    		return ;
    	}
    	Pair<Object[], ItemStack> items = Pair.of(list, result);
		RecipesList.put(items, listfluid);
		if(Loader.isModLoaded("proj_pot"))
			registerPotmanRecipe(result, list, listfluid);
		recipe_count ++;
    }
    @Method(modid = "proj_pot")
    private void registerPotmanRecipe(ItemStack result, Object[] list, List<FluidStack> listfluid) {
		PotmanRegistry.POT_RECIPE.register(new BasicPotRecipe(list, listfluid, result, 200, 8000, 18000).setRegistryName("sakura", String.format("sakura_pot_recipe_%d",recipe_count)));
	}

	public FluidStack getResultFluid(FluidStack fluid, List<ItemStack> inputs) {
		return getResultFluid(fluid, checkItems(inputs));
	}
	public FluidStack getResultFluid(FluidStack fluid, Pair<Object[], ItemStack> recipe) {
		if(recipe!=null)
			for(FluidStack k : RecipesList.get(recipe)) {
				if(k.isFluidEqual(fluid))
					return k;
			}
		return null;
	}

	private Pair<Object[], ItemStack> checkItems(List<ItemStack> inputs) {
		for (Entry<Pair<Object[], ItemStack>, List<FluidStack>> entry : RecipesList.entrySet()) {
			Object[] requiredIngredients = entry.getKey().getLeft();
			if (inputs.size() != requiredIngredients.length) {
				continue;
			}

			NonNullList<ItemStack> remainingInputs = NonNullList.create();
			for (ItemStack stack : inputs) {
				remainingInputs.add(stack.copy());
			}

			boolean allMatched = true;
			for (Object ingredient : requiredIngredients) {
				boolean found = false;
				for (int i = 0; i < remainingInputs.size(); i++) {
					ItemStack input = remainingInputs.get(i);
					if (ingredient instanceof ItemStack) {
						if (ItemStack.areItemsEqual((ItemStack) ingredient, input)) {
							remainingInputs.remove(i);
							found = true;
							break;
						}
					} else if (ingredient instanceof String) {
						NonNullList<ItemStack> ores = OreDictionary.getOres((String) ingredient);
						if (!ores.isEmpty() && RecipesUtil.getInstance().containsMatch(false, ores, input)) {
							remainingInputs.remove(i);
							found = true;
							break;
						}
					}
				}
				if (!found) {
					allMatched = false;
					break;
				}
			}

			if (allMatched && remainingInputs.isEmpty()) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	public ItemStack getResultItemStack(FluidStack fluid, List<ItemStack> inputs) {
		Pair<Object[], ItemStack> recipe = checkItems(inputs);
		if(recipe!=null)
			if(getResultFluid(fluid, recipe)!=null ||RecipesList.get(recipe).isEmpty())
				return recipe.getRight();
		return ItemStack.EMPTY;
	}

	public void ClearRecipe(ItemStack itemOutput) {
		for (Entry<Pair<Object[], ItemStack>, List<FluidStack>> entry : RecipesList.entrySet()) {
			Pair<Object[], ItemStack> recipe = entry.getKey();
			if(RecipesUtil.getInstance().compareItems(itemOutput, recipe.getRight())) {
				RecipesList.remove(entry.getKey());
				return ;
			}
		}
		throw new NullPointerException("NO RECIPE HERE");
	}
	
    public void ClearAllRecipe() {
    	RecipesList.clear();
	}

}