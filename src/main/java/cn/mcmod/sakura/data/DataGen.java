package cn.mcmod.sakura.data;

import cn.mcmod.sakura.SakuraMod;
import cn.mcmod.sakura.data.client.SakuraBlockStateProvider;
import cn.mcmod.sakura.data.client.SakuraItemModelProvider;
import cn.mcmod.sakura.level.WorldGenerationRegistry;
import cn.mcmod.sakura.level.tree.SakuraTreeFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.NewRegistryEvent;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = SakuraMod.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGen {
    @SubscribeEvent
    public static void dataGen(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        PackOutput packOutput = dataGenerator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        dataGenerator.addProvider(event.includeClient(),new SakuraBlockStateProvider(packOutput, SakuraMod.MODID, existingFileHelper));
        dataGenerator.addProvider(event.includeClient(),new SakuraItemModelProvider(packOutput, SakuraMod.MODID, existingFileHelper));
        SakuraBlockTagsProvider block_tag = new SakuraBlockTagsProvider(packOutput, provider, SakuraMod.MODID, existingFileHelper);
        dataGenerator.addProvider(event.includeServer(),block_tag);
        dataGenerator.addProvider(event.includeServer(),new SakuraItemTagsProvider(packOutput, provider, block_tag, SakuraMod.MODID, existingFileHelper));
        dataGenerator.addProvider(event.includeServer(),new SakuraFluidTagsProvider(packOutput, provider, SakuraMod.MODID, existingFileHelper));
        dataGenerator.addProvider(event.includeServer(),new SakuraRecipeProvider(packOutput));
        dataGenerator.addProvider(event.includeServer(),new SakuraLootTableProvider(packOutput));
        dataGenerator.addProvider(event.includeServer(),new SakuraFeatureProvider(packOutput, provider));
//        dataGenerator.addProvider(event.includeServer(),new SakuraLootModifierProvider(packOutput, SakuraMod.MODID));
    }
}
