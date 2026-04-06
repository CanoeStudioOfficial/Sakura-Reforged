package cn.mcmod.sakura.gui;

import cn.mcmod.sakura.CommonProxy;
import cn.mcmod.sakura.inventory.ContainerBarrel;
import cn.mcmod.sakura.packet.PacketClearFluid;
import cn.mcmod.sakura.tileentity.TileEntityBarrel;
import cn.mcmod.sakura.util.GuiFluidHelper;
import cn.mcmod_mmf.mmlib.util.ClientUtils;
import net.minecraft.client.Minecraft;
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
public class GuiBarrel extends GuiContainer {
    private static final ResourceLocation barrelGuiTextures = new ResourceLocation("sakura:textures/gui/barrel.png");
    private static final int BUTTON_CLEAR_INPUT = 0;
    private static final int BUTTON_CLEAR_OUTPUT = 1;

    private TileEntityBarrel tileBarrel;
    private int guiLeft;
    private int guiTop;

    public GuiBarrel(InventoryPlayer inventory, TileEntityBarrel tile) {
        super(new ContainerBarrel(inventory, tile));
        this.tileBarrel = tile;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        
        this.buttonList.add(new GuiButton(BUTTON_CLEAR_INPUT, guiLeft + 8, guiTop + 10, 10, 10, "X"));
        this.buttonList.add(new GuiButton(BUTTON_CLEAR_OUTPUT, guiLeft + 79, guiTop + 10, 10, 10, "X"));
        
        updateButtonStates();
    }

    private void updateButtonStates() {
        boolean hasInputFluid = tileBarrel.getTank() != null && tileBarrel.getTank().getFluidAmount() > 0;
        boolean hasOutputFluid = tileBarrel.getResultTank() != null && tileBarrel.getResultTank().getFluidAmount() > 0;
        
        this.buttonList.get(BUTTON_CLEAR_INPUT).enabled = hasInputFluid;
        this.buttonList.get(BUTTON_CLEAR_OUTPUT).enabled = hasOutputFluid;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == BUTTON_CLEAR_INPUT) {
            CommonProxy.getNetwork().sendToServer(new PacketClearFluid(tileBarrel.getPos(), 0));
        } else if (button.id == BUTTON_CLEAR_OUTPUT) {
            CommonProxy.getNetwork().sendToServer(new PacketClearFluid(tileBarrel.getPos(), 1));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(barrelGuiTextures);

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        int l2 = this.getCookProgressScaled(24);
        this.drawTexturedModalRect(k + 63, l + 35, 176, 0, l2 + 1, 16);

        if (this.tileBarrel.getTank().getFluid() != null) {
            FluidTank fluidTank = this.tileBarrel.getTank();
            int heightInd = (int) (68 * ((float) fluidTank.getFluidAmount() / (float) fluidTank.getCapacity()));

            if (heightInd > 0) {
                ClientUtils.getInstance().drawRepeatedFluidSprite(fluidTank.getFluid(), k + 18, l + 78 - heightInd, 16f, heightInd);
            }
        }

        if (this.tileBarrel.getResultTank().getFluid() != null) {
            FluidTank fluidTank = this.tileBarrel.getResultTank();
            int heightInd = (int) (68 * ((float) fluidTank.getFluidAmount() / (float) fluidTank.getCapacity()));

            if (heightInd > 0) {
                ClientUtils.getInstance().drawRepeatedFluidSprite(fluidTank.getFluid(), k + 89, l + 78 - heightInd, 16f, heightInd);
            }
        }
        
        updateButtonStates();
    }

    private int getCookProgressScaled(int pixels) {
        int i = this.tileBarrel.getField(0);
        return i != 0 ? i * pixels / 1000 : 0;
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

        if (GuiFluidHelper.isMouseOver(mouseX, mouseY, k + 18, l + 10, 16, 68)) {
            List<String> tooltip = GuiFluidHelper.getFluidTooltip(this.tileBarrel.getTank());
            this.drawHoveringText(tooltip, mouseX, mouseY);
        }

        if (GuiFluidHelper.isMouseOver(mouseX, mouseY, k + 89, l + 10, 16, 68)) {
            List<String> tooltip = GuiFluidHelper.getFluidTooltip(this.tileBarrel.getResultTank());
            this.drawHoveringText(tooltip, mouseX, mouseY);
        }
    }
}
