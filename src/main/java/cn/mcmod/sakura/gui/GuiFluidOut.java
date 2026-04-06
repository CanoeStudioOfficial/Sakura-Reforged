package cn.mcmod.sakura.gui;

import cn.mcmod.sakura.CommonProxy;
import cn.mcmod.sakura.inventory.ContainerFluidOut;
import cn.mcmod.sakura.packet.PacketClearFluid;
import cn.mcmod.sakura.tileentity.TileEntityFluidOut;
import cn.mcmod.sakura.util.GuiFluidHelper;
import cn.mcmod_mmf.mmlib.util.ClientUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiFluidOut extends GuiContainer {
    private static final ResourceLocation mortarGuiTextures = new ResourceLocation("sakura:textures/gui/barrel_out.png");
    private static final int BUTTON_CLEAR = 0;

    private TileEntityFluidOut tilePot;
    private int guiLeft;
    private int guiTop;

    public GuiFluidOut(InventoryPlayer inventory, TileEntityFluidOut tile) {
        super(new ContainerFluidOut(inventory, tile));
        this.tilePot = tile;
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
        
        updateButtonStates();
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

        if (GuiFluidHelper.isMouseOver(mouseX, mouseY, k + 6, l + 10, 162, 16)) {
            List<String> tooltip = GuiFluidHelper.getFluidTooltip(this.tilePot.getTank());
            this.drawHoveringText(tooltip, mouseX, mouseY);
        }
    }
}
