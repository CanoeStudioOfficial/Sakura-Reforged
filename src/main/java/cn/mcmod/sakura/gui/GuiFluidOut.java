package cn.mcmod.sakura.gui;

import cn.mcmod.sakura.CommonProxy;
import cn.mcmod.sakura.inventory.ContainerFluidOut;
import cn.mcmod.sakura.packet.PacketClearFluid;
import cn.mcmod.sakura.tileentity.TileEntityFluidOut;
import cn.mcmod_mmf.mmlib.util.ClientUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiFluidOut extends GuiContainer {
    private static final ResourceLocation mortarGuiTextures = new ResourceLocation("sakura:textures/gui/barrel_out.png");

    private TileEntityFluidOut tilePot;
    private static final int BUTTON_CLEAR = 0;

    public GuiFluidOut(InventoryPlayer inventory, TileEntityFluidOut tile) {
        super(new ContainerFluidOut(inventory, tile));
        this.tilePot = tile;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.buttonList.add(new GuiButton(BUTTON_CLEAR, k + 5, l + 5, 16, 20, "X"));
    }
    
    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == BUTTON_CLEAR) {
            CommonProxy.getNetwork().sendToServer(new PacketClearFluid(tilePot.getPos().getX(), tilePot.getPos().getY(), tilePot.getPos().getZ(), 0));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(mortarGuiTextures);

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        if (this.tilePot.getTank().getFluid() != null) {
            FluidTank fluidTank = this.tilePot.getTank();
            int heightInd = (int) (162 * ((float) fluidTank.getFluidAmount() / (float) fluidTank.getCapacity()));
            if (heightInd > 0) {
            	ClientUtils.getInstance().drawRepeatedFluidSprite(fluidTank.getFluid(), k + 168 - heightInd, l + 60, heightInd, 16F);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        
        if (this.tilePot.getTank().getFluid() != null) {
            String fluidName = this.tilePot.getTank().getFluid().getLocalizedName();
            int amount = this.tilePot.getTank().getFluidAmount();
            int capacity = this.tilePot.getTank().getCapacity();
            int percentage = (amount * 100) / capacity;
            String fluidInfo = String.format("%s: %d/%d mb (%d%%)", fluidName, amount, capacity, percentage);
            this.fontRenderer.drawString(fluidInfo, k + 140, l + 10, 0xFFFFFF);
        }
    }

}