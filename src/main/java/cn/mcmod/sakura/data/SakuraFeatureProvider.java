package cn.mcmod.sakura.data;

import cn.mcmod.sakura.SakuraMod;
import cn.mcmod.sakura.level.WorldGenerationRegistry;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
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

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SakuraFeatureProvider extends DatapackBuiltinEntriesProvider {

    public static final ResourceKey<BiomeModifier> ADD_FEATURES = ResourceKey.create(
            ForgeRegistries.Keys.BIOME_MODIFIERS, // The registry this key is for
            new ResourceLocation(SakuraMod.MODID, "add_features") // The registry name
    );

    public SakuraFeatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries,new RegistrySetBuilder().add(ForgeRegistries.Keys.BIOME_MODIFIERS, bootstrap -> {
            // Lookup any necessary registries.
            // Static registries only need to be looked up if you need to grab the tag data.
            HolderGetter<Biome> biomes = bootstrap.lookup(Registries.BIOME);

            HolderGetter<PlacedFeature> placedFeatures = bootstrap.lookup(Registries.PLACED_FEATURE);


            ForgeRegistries.BIOMES.getEntries().stream().filter(
                    e-> {
                        float temperature = e.getValue().getModifiedClimateSettings().temperature();
                        return temperature >0.4 && temperature<1.0;
                    }
            ).forEach( e->
                    bootstrap.register(ADD_FEATURES,
                            new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                                    // The biome(s) to generate within
                                    HolderSet.direct(biomes.getOrThrow(e.getKey())),
                                    // The feature(s) to generate within the biomes
                                    HolderSet.direct(placedFeatures.getOrThrow(WorldGenerationRegistry.PATCH_BAMBOOSHOOT.getKey())),
                                    // The generation step
                                    GenerationStep.Decoration.LOCAL_MODIFICATIONS
                            )
                    )
            );
        })


                , Set.of(SakuraMod.MODID));
    }
}
