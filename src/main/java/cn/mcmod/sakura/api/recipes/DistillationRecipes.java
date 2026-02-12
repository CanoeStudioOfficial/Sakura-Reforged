package cn.mcmod.sakura.api.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.mcmod_mmf.mmlib.util.RecipesUtil;

public class DistillationRecipes {
	private static final DistillationRecipes RECIPE_BASE = new DistillationRecipes();
    private DistillationRecipes() {
	}
	public static DistillationRecipes getInstance() {
		return RECIPE_BASE;
	}
	public final Map<Pair<FluidStack, Object[]>,List<FluidStack>> RecipesList = Maps.newHashMap();

    public void register(FluidStack input, FluidStack output) {
    	register(input,output, new Object[]{ItemStack.EMPTY,ItemStack.EMPTY,ItemStack.EMPTY});
    }
    public void register(FluidStack input, FluidStack output, Object[] additives) {
    	Pair<FluidStack, Object[]> items = Pair.of(output, additives);
		RecipesList.put(items,Lists.newArrayList(input));
    }
	
    public void register(List<FluidStack> input, FluidStack output) {
    	register(input,output, new Object[]{ItemStack.EMPTY,ItemStack.EMPTY,ItemStack.EMPTY});
    }
    public void register(List<FluidStack> input, FluidStack output, Object[] additives) {
    	Pair<FluidStack, Object[]> items = Pair.of(output, additives);
		RecipesList.put(items,input);
    }

	public FluidStack getFluidStack(FluidStack fluid) {
		for (List<FluidStack> entry : RecipesList.values()) {
			for(FluidStack fluid_entry:entry)
				if(fluid_entry.isFluidEqual(fluid))
				return fluid_entry;
		}
		return null;
	}

	public FluidStack getResultFluidStack(FluidStack fluid, ItemStack[] inputs) {
		NonNullList<ItemStack> inputList = NonNullList.create();
		for (ItemStack stack : inputs) {
			if (!stack.isEmpty()) {
				inputList.add(stack);
			}
		}
		for (Entry<Pair<FluidStack, Object[]>,List<FluidStack>> entry : RecipesList.entrySet()) {
			for(FluidStack fluid_entry:entry.getValue())
			if(fluid_entry.isFluidEqual(fluid)) {
				Object[] additives = entry.getKey().getRight();
				NonNullList<Object> requiredIngredients = NonNullList.create();
				for (Object obj : additives) {
					if (obj instanceof ItemStack) {
						if (!((ItemStack) obj).isEmpty()) {
							requiredIngredients.add(obj);
						}
					} else if (obj != null) {
						requiredIngredients.add(obj);
					}
				}

				if (inputList.size() != requiredIngredients.size()) {
					continue;
				}

				NonNullList<ItemStack> remainingInputs = NonNullList.create();
				for (ItemStack stack : inputList) {
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
					return entry.getKey().getKey();
				}
			}
		}

		return null;
	}

	public boolean isIngredient(ItemStack stack) {
		for (Pair<FluidStack, Object[]> recipe : RecipesList.keySet()) {
			for (Object ingredient : recipe.getRight()) {
				if (ingredient instanceof ItemStack) {
					if (ItemStack.areItemsEqual((ItemStack) ingredient, stack)) {
						return true;
					}
				} else if (ingredient instanceof String) {
					NonNullList<ItemStack> ores = OreDictionary.getOres((String) ingredient);
					if (!ores.isEmpty() && RecipesUtil.getInstance().containsMatch(false, ores, stack)) {
						return true;
					}
				}
			}
		}
		return false;
	}
  
    public void ClearRecipe(FluidStack input) {
    	for (Entry<Pair<FluidStack, Object[]>,List<FluidStack>> entry : RecipesList.entrySet()) {
    		Pair<FluidStack, Object[]> recipe = entry.getKey();
    			if(recipe.getLeft().isFluidEqual(input)) {
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
