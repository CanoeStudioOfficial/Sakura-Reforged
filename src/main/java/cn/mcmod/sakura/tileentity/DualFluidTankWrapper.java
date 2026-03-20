package cn.mcmod.sakura.tileentity;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class DualFluidTankWrapper implements IFluidHandler {
    private final FluidTank inputTank;
    private final FluidTank outputTank;
    private final boolean isOutputSide;
    
    public DualFluidTankWrapper(FluidTank inputTank, FluidTank outputTank, boolean isOutputSide) {
        this.inputTank = inputTank;
        this.outputTank = outputTank;
        this.isOutputSide = isOutputSide;
    }
    
    @Override
    public IFluidTankProperties[] getTankProperties() {
        if (isOutputSide) {
            return new IFluidTankProperties[] { outputTank.getTankProperties()[0] };
        }
        return new IFluidTankProperties[] { inputTank.getTankProperties()[0] };
    }
    
    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (isOutputSide) {
            return 0;
        }
        return inputTank.fill(resource, doFill);
    }
    
    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (isOutputSide) {
            return outputTank.drain(resource, doDrain);
        }
        return inputTank.drain(resource, doDrain);
    }
    
    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (isOutputSide) {
            return outputTank.drain(maxDrain, doDrain);
        }
        return inputTank.drain(maxDrain, doDrain);
    }
}
