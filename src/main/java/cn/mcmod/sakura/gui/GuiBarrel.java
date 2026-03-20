package cn.mcmod.sakura.gui;


import cn.mcmod.sakura.CommonProxy;
import cn.mcmod.sakura.SakuraMain;
import cn.mcmod.sakura.inventory.ContainerBarrel;
import cn.mcmod.sakura.packet.PacketClearFluid;
import cn.mcmod.sakura.tileentity.TileEntityBarrel;
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
public class GuiBarrel extends GuiContainer {
    private static final ResourceLocation mortarGuiTextures = new ResourceLocation("sakura:textures/gui/barrel.png");

    private TileEntityBarrel tilePot;
    private static final int BUTTON_CLEAR_INPUT = 0;
    private static final int BUTTON_CLEAR_OUTPUT = 1;

    public GuiBarrel(InventoryPlayer inventory, TileEntityBarrel tile) {
        super(new ContainerBarrel(inventory, tile));
        this.tilePot = tile;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.buttonList.add(new GuiButton(BUTTON_CLEAR_INPUT, k + 5, l + 5, 16, 20, "X"));
        this.buttonList.add(new GuiButton(BUTTON_CLEAR_OUTPUT, k + 76, l + 5, 16, 20, "X"));
    }
    
    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == BUTTON_CLEAR_INPUT) {
            CommonProxy.getNetwork().sendToServer(new PacketClearFluid(tilePot.getPos().getX(), tilePot.getPos().getY(), tilePot.getPos().getZ(), 0));
        } else if (button.id == BUTTON_CLEAR_OUTPUT) {
            CommonProxy.getNetwork().sendToServer(new PacketClearFluid(tilePot.getPos().getX(), tilePot.getPos().getY(), tilePot.getPos().getZ(), 1));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(mortarGuiTextures);

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        int l2 = this.getCookProgressScaled(24);
        this.drawTexturedModalRect(k + 63, l + 35, 176, 0, l2 + 1, 16);

        if (this.tilePot.getTank().getFluid() != null) {
            FluidTank fluidTank = this.tilePot.getTank();
            int heightInd = (int) (68 * ((float) fluidTank.getFluidAmount() / (float) fluidTank.getCapacity()));

            if (heightInd > 0) {
                ClientUtils.getInstance().drawRepeatedFluidSprite(fluidTank.getFluid(), k + 18, l + 78 - heightInd, 16f, heightInd);
            }

        }

        if (this.tilePot.getResultTank().getFluid() != null) {
            FluidTank fluidTank = this.tilePot.getResultTank();
            int heightInd = (int) (68 * ((float) fluidTank.getFluidAmount() / (float) fluidTank.getCapacity()));
           /* if (heightInd > 0) {
                ClientUtils.drawRepeatedFluidSprite(fluidTank.getFluid(), k + 167 - heightInd, l + 11, heightInd, 16f);
            }*/

            if (heightInd > 0) {
                ClientUtils.getInstance().drawRepeatedFluidSprite(fluidTank.getFluid(), k + 89, l + 78 - heightInd, 16f, heightInd);
            }

        }
    }

    private int getCookProgressScaled(int pixels) {
        int i = this.tilePot.getField(0);
        return i != 0 ? i * pixels / 1000 : 0;
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
            this.fontRenderer.drawString(fluidInfo, k + 18, l + 10, 0xFFFFFF);
        }
        
        if (this.tilePot.getResultTank().getFluid() != null) {
            String fluidName = this.tilePot.getResultTank().getFluid().getLocalizedName();
            int amount = this.tilePot.getResultTank().getFluidAmount();
            int capacity = this.tilePot.getResultTank().getCapacity();
            int percentage = (amount * 100) / capacity;
            String fluidInfo = String.format("%s: %d/%d mb (%d%%)", fluidName, amount, capacity, percentage);
            this.fontRenderer.drawString(fluidInfo, k + 89, l + 10, 0xFFFFFF);
        }
    }

}