package cn.mcmod.sakura.api.recipes;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import cn.mcmod_mmf.mmlib.util.RecipesUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class MortarRecipes {
    public final Map<Object[], ItemStack[]> RecipesList = Maps.newHashMap();
	private static final MortarRecipes RECIPE_BASE = new MortarRecipes();
	private MortarRecipes() {
		// TODO Auto-generated constructor stub
	}
    public static MortarRecipes instance() {
        return RECIPE_BASE;
    }

    public void addMortarRecipes(ItemStack[] result, Object[] main) {
        RecipesList.put(main, result);
    }

    public ItemStack[] getResult(List<ItemStack> inputs) {
        for (Entry<Object[], ItemStack[]> entry : RecipesList.entrySet()) {
            Object[] requiredIngredients = entry.getKey();
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
                    if (input.isEmpty()) continue;
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
                return entry.getValue();
            }
        }

        return new ItemStack[0];
    }

    public void ClearRecipe(Object[] inputs) {
    	RecipesList.entrySet().removeIf(entry -> {
    		Object[] key = entry.getKey();
    		if (key.length != inputs.length) return false;
    		for (int i = 0; i < key.length; i++) {
    			Object obj1 = key[i];
    			Object obj2 = inputs[i];
    			if (obj1 instanceof ItemStack && obj2 instanceof ItemStack) {
    				if (!ItemStack.areItemsEqual((ItemStack) obj1, (ItemStack) obj2)) return false;
    			} else if (obj1 instanceof String && obj2 instanceof String) {
    				if (!obj1.equals(obj2)) return false;
    			} else {
    				return false;
    			}
    		}
    		return true;
    	});
	}

    public void ClearRecipeByOutput(ItemStack[] outputs) {
    	RecipesList.entrySet().removeIf(entry -> {
    		ItemStack[] value = entry.getValue();
    		if (value.length != outputs.length) return false;
    		for (int i = 0; i < value.length; i++) {
    			if (!ItemStack.areItemsEqual(value[i], outputs[i])) return false;
    		}
    		return true;
    	});
	}
	
    public void ClearAllRecipe() {
    	RecipesList.clear();
	}
}
