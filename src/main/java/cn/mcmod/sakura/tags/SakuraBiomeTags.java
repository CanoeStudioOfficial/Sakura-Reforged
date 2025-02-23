package cn.mcmod.sakura.tags;

import cn.mcmod.sakura.SakuraMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class SakuraBiomeTags {
    public static final TagKey<Biome> CAN_SPAWN_BAMBOO = TagKey.create(Registries.BIOME, new ResourceLocation(SakuraMod.MODID,"can_spawn_bamboo"));
}
