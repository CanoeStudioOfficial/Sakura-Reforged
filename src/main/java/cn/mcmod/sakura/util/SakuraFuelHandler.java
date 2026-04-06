package cn.mcmod.sakura.util;

import cn.mcmod.sakura.block.BlockLoader;
import cn.mcmod.sakura.item.ItemLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SakuraFuelHandler {
    public static final int BURN_TIME_PLANK = 300;
    public static final int BURN_TIME_LOG = 300;
    public static final int BURN_TIME_STRAW = 100;
    public static final int BURN_TIME_BAMBOO = 150;
    public static final int BURN_TIME_CHARCOAL = 1600;
    public static final int BURN_TIME_STICK = 100;
    public static final int BURN_TIME_SAPLING = 100;
    public static final int BURN_TIME_WOOL = 100;
    
    public static void registerFuels() {
    }
    
    @SubscribeEvent
    public void onFurnaceFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;
        
        Item item = stack.getItem();
        int meta = stack.getMetadata();
        
        if (item == Item.getItemFromBlock(BlockLoader.STRAW_BLOCK) || 
            item == Item.getItemFromBlock(BlockLoader.STRAW_BLOCK_STAIR) ||
            item == Item.getItemFromBlock(BlockLoader.STRAW_BLOCK_SLAB)) {
            event.setBurnTime(BURN_TIME_STRAW);
            return;
        }
        
        if (item == Item.getItemFromBlock(BlockLoader.BAMBOO_BLOCK) || 
            item == Item.getItemFromBlock(BlockLoader.BAMBOO_BLOCK_SUNBURNT) ||
            item == Item.getItemFromBlock(BlockLoader.BAMBOO_PLANK) ||
            item == Item.getItemFromBlock(BlockLoader.BAMBOO_PLANK_STAIR) ||
            item == Item.getItemFromBlock(BlockLoader.BAMBOO_PLANK_SLAB) ||
            item == Item.getItemFromBlock(BlockLoader.BAMBOO_STAIR) ||
            item == Item.getItemFromBlock(BlockLoader.BAMBOO_SUNBURNT_STAIR) ||
            item == Item.getItemFromBlock(BlockLoader.BAMBOO_SLAB) ||
            item == Item.getItemFromBlock(BlockLoader.BAMBOO_SLAB_SUNBURNT) ||
            item == Item.getItemFromBlock(BlockLoader.BAMBOO_FENCE) ||
            item == Item.getItemFromBlock(BlockLoader.BAMBOO_FENCE_SUNBURNT)) {
            event.setBurnTime(BURN_TIME_BAMBOO);
            return;
        }
        
        if (item == Item.getItemFromBlock(BlockLoader.MAPLE_PLANK) || 
            item == Item.getItemFromBlock(BlockLoader.MAPLE_PLANK_STAIR) ||
            item == Item.getItemFromBlock(BlockLoader.MAPLE_PLANK_SLAB) ||
            item == Item.getItemFromBlock(BlockLoader.MAPLE_LOG) ||
            item == Item.getItemFromBlock(BlockLoader.MAPLE_LOG_SAP)) {
            event.setBurnTime(BURN_TIME_PLANK);
            return;
        }
        
        if (item == Item.getItemFromBlock(BlockLoader.SAKURA_PLANK) || 
            item == Item.getItemFromBlock(BlockLoader.SAKURA_PLANK_STAIR) ||
            item == Item.getItemFromBlock(BlockLoader.SAKURA_PLANK_SLAB) ||
            item == Item.getItemFromBlock(BlockLoader.SAKURA_LOG)) {
            event.setBurnTime(BURN_TIME_PLANK);
            return;
        }
        
        if (item == Item.getItemFromBlock(BlockLoader.UME_LOG)) {
            event.setBurnTime(BURN_TIME_LOG);
            return;
        }
        
        if (item == ItemLoader.MATERIAL) {
            if (meta == 0) {
                event.setBurnTime(BURN_TIME_STRAW);
                return;
            }
            if (meta == 24 || meta == 25 || meta == 26) {
                event.setBurnTime(BURN_TIME_PLANK);
                return;
            }
            if (meta == 48) {
                event.setBurnTime(BURN_TIME_BAMBOO);
                return;
            }
            if (meta == 51) {
                event.setBurnTime(BURN_TIME_CHARCOAL);
                return;
            }
            if (meta == 63 || meta == 64) {
                event.setBurnTime(BURN_TIME_STRAW);
                return;
            }
        }
        
        if (item == Item.getItemFromBlock(BlockLoader.BAMBOO)) {
            event.setBurnTime(BURN_TIME_STICK);
            return;
        }
        
        if (item == Item.getItemFromBlock(BlockLoader.MAPLE_SAPLING_RED) ||
            item == Item.getItemFromBlock(BlockLoader.MAPLE_SAPLING_YELLOW) ||
            item == Item.getItemFromBlock(BlockLoader.MAPLE_SAPLING_ORANGE) ||
            item == Item.getItemFromBlock(BlockLoader.MAPLE_SAPLING_GREEN) ||
            item == Item.getItemFromBlock(BlockLoader.SAKURA_SAPLING) ||
            item == Item.getItemFromBlock(BlockLoader.UME_SAPLING)) {
            event.setBurnTime(BURN_TIME_SAPLING);
            return;
        }
        
        if (item == Item.getItemFromBlock(BlockLoader.STRAW_WEB)) {
            event.setBurnTime(BURN_TIME_STRAW);
            return;
        }
    }
}
