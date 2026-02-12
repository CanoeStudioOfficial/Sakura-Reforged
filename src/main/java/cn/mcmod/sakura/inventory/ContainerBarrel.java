package cn.mcmod.sakura.inventory;

import cn.mcmod.sakura.tileentity.TileEntityBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBarrel extends Container {
    private TileEntityBarrel tileBarrel;
    private int processTime;

    public ContainerBarrel(InventoryPlayer inventory, TileEntityBarrel tile) {
        tileBarrel = tile;
        int i, j, k;
        for (k = 0; k < 3; ++k)
            addSlotToContainer(new Slot(tile, k, 42, 36 + (k - 1) * 18));
        addSlotToContainer(new Slot(tile, 3, 131, 12));
        addSlotToContainer(new Slot(tile, 4, 130, 56) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });


        for (i = 0; i < 3; ++i)
            for (j = 0; j < 9; ++j)
                addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

        for (i = 0; i < 9; ++i)
            addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
    }


    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileBarrel);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i) {
            IContainerListener icontainerlistener = this.listeners.get(i);

            if (this.processTime != this.tileBarrel.getField(0)) {
                icontainerlistener.sendWindowProperty(this, 0, this.tileBarrel.getField(0));
            }
        }

        this.processTime = this.tileBarrel.getField(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value) {
        this.tileBarrel.setField(id, value);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tileBarrel.isUsableByPlayer(player);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 5) {
                if (!this.mergeItemStack(itemstack1, 5, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack1, itemstack);
            } else {
                if (!this.mergeItemStack(itemstack1, 0, 5, false)) {
                    if (index >= 5 && index < 32) {
                        if (!this.mergeItemStack(itemstack1, 32, this.inventorySlots.size(), false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (index >= 32 && index < this.inventorySlots.size()) {
                        if (!this.mergeItemStack(itemstack1, 5, 32, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }

            if (itemstack1.getCount() == 0)
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}
