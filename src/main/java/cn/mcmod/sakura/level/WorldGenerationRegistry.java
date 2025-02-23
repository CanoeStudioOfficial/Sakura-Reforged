package cn.mcmod.sakura.level;

import java.util.function.Supplier;

import com.google.common.collect.Lists;

import cn.mcmod.sakura.SakuraConfig;
import cn.mcmod.sakura.SakuraMod;
import cn.mcmod.sakura.block.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public class WorldGenerationRegistry {

    public static final ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_BAMBOOSHOOT_KEY = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(SakuraMod.MODID, "patch_bambooshoot"));
    public static final ConfiguredFeature<?, ?> FEATURE_PATCH_BAMBOOSHOOT = wildPlantFeature(BlockRegistry.BAMBOOSHOOT, BlockTags.DIRT);

    public static final ResourceKey<PlacedFeature> PATCH_BAMBOOSHOOT_KEY = ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(SakuraMod.MODID, "patch_bambooshoot"));
    public static final PlacedFeature PATCH_BAMBOOSHOOT = wildPlantPatch(FEATURE_PATCH_BAMBOOSHOOT, RarityFilter.onAverageOnceEvery(30),
                    InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

    public static final BlockPos BLOCK_BELOW = new BlockPos(0, -1, 0);

    private static ConfiguredFeature<?, ?> wildPlantFeature(Supplier<Block> wildCrop, TagKey<Block> blockTag) {
        return new ConfiguredFeature<>(Feature.RANDOM_PATCH, getWildCropConfiguration(wildCrop.get(),
                64, 1, BlockPredicate.matchesTag(BLOCK_BELOW,blockTag)));
    }
    private static PlacedFeature wildPlantPatch(ConfiguredFeature<?, ?> feature,
            PlacementModifier... modifiers) {
        return new PlacedFeature(Holder.direct(feature), Lists.newArrayList(modifiers));
    }
    
    private static RandomPatchConfiguration getWildCropConfiguration(Block block, int tries, int xzSpread, BlockPredicate plantedOn) {
        return new RandomPatchConfiguration(tries, xzSpread, 3, PlacementUtils.filtered(
                Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block)),
                        BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, plantedOn)));
    }
}
