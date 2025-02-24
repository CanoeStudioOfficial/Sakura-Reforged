package cn.mcmod.sakura.loot_modifier;

import cn.mcmod.sakura.item.FoodRegistry;
import cn.mcmod.sakura.item.enums.SakuraFoodSet;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

public class FishingModifiter extends LootModifier{

    protected FishingModifiter(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.add(new ItemStack(FoodRegistry.FOODSET.get(SakuraFoodSet.SHRIMP).get()));
        return generatedLoot;
    }


    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return FishingModifiter.DIRECT_CODEC;
    }
}
