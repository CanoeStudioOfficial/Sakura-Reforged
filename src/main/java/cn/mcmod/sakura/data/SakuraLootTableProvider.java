package cn.mcmod.sakura.data;

import cn.mcmod.sakura.data.loot.SakuraBlockLoot;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class SakuraLootTableProvider extends LootTableProvider {

    public SakuraLootTableProvider(PackOutput packOutput) {
        super(packOutput, Set.of()
                ,List.of(new SubProviderEntry(
                SakuraBlockLoot::new,
                LootContextParamSets.BLOCK))
        );
    }
    
//    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> tables = ImmutableList.of(Pair.of(SakuraBlockLoot::new, LootContextParamSets.BLOCK));

//    @Override
//    public String getName() {
//        return "Sakura's Loot Tables";
//    }
//
//    @Override
//    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootContextParamSet>> getTables() {
//        return tables;
//    }

}
