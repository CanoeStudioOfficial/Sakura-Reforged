package cn.mcmod.sakura.gui;

import cn.mcmod.sakura.CommonProxy;
import cn.mcmod.sakura.inventory.ContainerCampfirePot;
import cn.mcmod.sakura.packet.PacketClearFluid;
import cn.mcmod.sakura.tileentity.TileEntityCampfirePot;
import cn.mcmod.sakura.util.GuiFluidHelper;
import cn.mcmod_mmf.mmlib.util.ClientUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiCampfirePot extends GuiContainer {

    private static final ResourceLocation mortarGuiTextures = new ResourceLocation("sakura:textures/gui/pot.png");
    private static final int BUTTON_CLEAR = 0;

    private TileEntityCampfirePot tilePot;
    private final IInventory playerInventory;
    private int guiLeft;
    private int guiTop;

    public GuiCampfirePot(InventoryPlayer inventory, TileEntityCampfirePot tile) {

        super(new ContainerCampfirePot(inventory, tile));

        this.tilePot = tile;
        this.playerInventory = inventory;

    }

    @Override
    public void initGui() {
        super.initGui();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        
        this.buttonList.add(new GuiButton(BUTTON_CLEAR, guiLeft + 155, guiTop + 8, 10, 10, "X"));
        
        updateButtonStates();
    }

    private void updateButtonStates() {
        boolean hasFluid = tilePot.getTank() != null && tilePot.getTank().getFluidAmount() > 0;
        this.buttonList.get(BUTTON_CLEAR).enabled = hasFluid;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == BUTTON_CLEAR) {
            CommonProxy.getNetwork().sendToServer(new PacketClearFluid(tilePot.getPos(), 0));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(mortarGuiTextures);

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        int var7;

        if (this.tilePot.isBurning()) {
            var7 = this.tilePot.getBurnTimeRemainingScaled(12);

            this.drawTexturedModalRect(k + 100, l + 67 - var7, 176, 12 - var7, 14, var7 + 2);
        }

        int l2 = this.getCookProgressScaled(24);
        this.drawTexturedModalRect(k + 96, l + 37, 176, 14, l2 + 1, 16);
        
        if (this.tilePot.getTank().getFluid() != null) {
            FluidTank fluidTank = this.tilePot.getTank();
            int heightInd = (int) (72 * ((float) fluidTank.getFluidAmount() / (float) fluidTank.getCapacity()));
            if (heightInd > 0) {
                ClientUtils.getInstance().drawRepeatedFluidSprite(fluidTank.getFluid(), k + 167- heightInd, l + 11 , heightInd, 16f);
            }

        }
        
        updateButtonStates();
    }

    private int getCookProgressScaled(int pixels) {
        int i = this.tilePot.getField(1);
        int j = this.tilePot.getField(2);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        drawFluidTooltips(mouseX, mouseY);
    }
    
    private void drawFluidTooltips(int mouseX, int mouseY) {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;

        if (GuiFluidHelper.isMouseOver(mouseX, mouseY, k + 152, l + 10, 16, 72)) {
            List<String> tooltip = GuiFluidHelper.getFluidTooltip(this.tilePot.getTank());
            this.drawHoveringText(tooltip, mouseX, mouseY);
        }
    }

}
