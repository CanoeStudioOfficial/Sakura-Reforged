package cn.mcmod.sakura.data;

import cn.mcmod.sakura.loot_modifier.SeedsDrop;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

import java.util.List;

public class SakuraLootModifierProvider extends GlobalLootModifierProvider {
    public SakuraLootModifierProvider(PackOutput output, String modid) {
        super(output, modid);
    }

    @Override
    protected void start() {
//        add(
//                "grass_drops",
//                new SeedsDrop.SeedDropModifier(new LootItemCondition[]{
//                        LootItemRandomChanceCondition.randomChance(0.0625F).build(),
//                        MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS)).build(),
//                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.FERN).build()
//                })
//        );
    }
}
