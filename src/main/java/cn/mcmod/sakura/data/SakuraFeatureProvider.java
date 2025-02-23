package cn.mcmod.sakura.data;

import cn.mcmod.sakura.SakuraMod;
import cn.mcmod.sakura.level.WorldGenerationRegistry;
import cn.mcmod.sakura.level.tree.SakuraTreeFeatures;
import cn.mcmod.sakura.tags.SakuraBiomeTags;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SakuraFeatureProvider extends DatapackBuiltinEntriesProvider {

    public static final ResourceKey<BiomeModifier> ADD_FEATURES = ResourceKey.create(
            ForgeRegistries.Keys.BIOME_MODIFIERS, // The registry this key is for
            new ResourceLocation(SakuraMod.MODID, "add_features") // The registry name
    );

    public SakuraFeatureProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries, new RegistrySetBuilder()
                        .add(Registries.CONFIGURED_FEATURE, bootstrap -> {
                            SakuraTreeFeatures.ENTRY.forEach(
                                    e -> bootstrap.register(e.getA(), e.getB())
                            );
                            bootstrap.register(WorldGenerationRegistry.FEATURE_PATCH_BAMBOOSHOOT_KEY, WorldGenerationRegistry.FEATURE_PATCH_BAMBOOSHOOT);
                        })
                        .add(Registries.PLACED_FEATURE, bootstrap -> {
                            bootstrap.register(WorldGenerationRegistry.PATCH_BAMBOOSHOOT_KEY, WorldGenerationRegistry.PATCH_BAMBOOSHOOT);
                        })
                , Set.of(SakuraMod.MODID));
    }

}
