package cn.mcmod.sakura.level.tree;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class SakuraTreeGrower extends AbstractTreeGrower {

	@Override
	protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean pHasFlowers) {
        if (random.nextInt(10) == 0) {
            return SakuraTreeFeatures.FANCY_SAKURA_KEY;
        } else {
            return SakuraTreeFeatures.SAKURA_KEY;
        }
	}

}
