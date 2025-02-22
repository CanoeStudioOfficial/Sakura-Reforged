package cn.mcmod.sakura.data;

import cn.mcmod.sakura.SakuraMod;
import cn.mcmod.sakura.data.client.SakuraBlockStateProvider;
import cn.mcmod.sakura.data.client.SakuraItemModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGen {
    @SubscribeEvent
    public static void dataGen(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        dataGenerator.addProvider(event.includeClient(),new SakuraBlockStateProvider(dataGenerator, SakuraMod.MODID, existingFileHelper));
        dataGenerator.addProvider(event.includeClient(),new SakuraItemModelProvider(dataGenerator, SakuraMod.MODID, existingFileHelper));
        SakuraBlockTagsProvider block_tag = new SakuraBlockTagsProvider(dataGenerator,event.getLookupProvider(), SakuraMod.MODID, existingFileHelper);
        dataGenerator.addProvider(event.includeServer(),block_tag);
        dataGenerator.addProvider(event.includeServer(),new SakuraItemTagsProvider(dataGenerator,event.getLookupProvider(), block_tag, SakuraMod.MODID, existingFileHelper));
        dataGenerator.addProvider(event.includeServer(),new SakuraFluidTagsProvider(dataGenerator,event.getLookupProvider(), SakuraMod.MODID, existingFileHelper));
        dataGenerator.addProvider(event.includeServer(),new SakuraRecipeProvider(dataGenerator));
        dataGenerator.addProvider(event.includeServer(),new SakuraLootTableProvider(dataGenerator));
    }
}
