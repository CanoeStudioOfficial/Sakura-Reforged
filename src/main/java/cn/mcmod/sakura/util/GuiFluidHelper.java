package cn.mcmod.sakura.util;

import cn.mcmod_mmf.mmlib.util.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiFluidHelper {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###");
    private static final ResourceLocation WIDGETS_TEXTURE = new ResourceLocation("sakura:textures/gui/widgets.png");

    public static void drawFluidTank(Gui gui, FluidTank tank, int x, int y, int width, int height) {
        if (tank == null) return;
        
        FluidStack fluidStack = tank.getFluid();
        if (fluidStack != null && fluidStack.amount > 0) {
            int fluidHeight = (int) ((float) fluidStack.amount / tank.getCapacity() * height);
            if (fluidHeight > 0) {
                ClientUtils.getInstance().drawRepeatedFluidSprite(fluidStack, x, y + height - fluidHeight, width, fluidHeight);
            }
        }
    }

    public static void drawFluidTankOverlay(Gui gui, int x, int y, int width, int height) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(WIDGETS_TEXTURE);
        
        gui.drawTexturedModalRect(x, y, 0, 0, width, height);
    }

    public static List<String> getFluidTooltip(FluidTank tank) {
        List<String> tooltip = new ArrayList<>();
        
        if (tank == null) {
            tooltip.add(I18n.format("sakura.tooltip.fluid.empty"));
            return tooltip;
        }
        
        FluidStack fluidStack = tank.getFluid();
        if (fluidStack == null || fluidStack.amount <= 0) {
            tooltip.add(I18n.format("sakura.tooltip.fluid.empty"));
            tooltip.add(I18n.format("sakura.tooltip.fluid.capacity", DECIMAL_FORMAT.format(tank.getCapacity())));
        } else {
            String fluidName = fluidStack.getLocalizedName();
            tooltip.add(fluidName);
            tooltip.add(I18n.format("sakura.tooltip.fluid.amount", 
                    DECIMAL_FORMAT.format(fluidStack.amount), 
                    DECIMAL_FORMAT.format(tank.getCapacity())));
            
            int percentage = (int) ((float) fluidStack.amount / tank.getCapacity() * 100);
            tooltip.add(I18n.format("sakura.tooltip.fluid.percentage", percentage));
        }
        
        return tooltip;
    }

    public static boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    public static void drawProgressBar(Gui gui, int x, int y, int progress, int maxProgress, int width, int height, int textureX, int textureY) {
        if (maxProgress <= 0) return;
        
        int progressWidth = (int) ((float) progress / maxProgress * width);
        if (progressWidth > 0) {
            gui.drawTexturedModalRect(x, y, textureX, textureY, progressWidth, height);
        }
    }

    public static void drawCenteredString(Gui gui, String text, int x, int y, int color) {
        Minecraft.getMinecraft().fontRenderer.drawString(text, 
                x - Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2, y, color);
    }
}
