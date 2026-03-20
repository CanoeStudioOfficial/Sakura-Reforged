package cn.mcmod.sakura.packet;

import cn.mcmod.sakura.tileentity.TileEntityBarrel;
import cn.mcmod.sakura.tileentity.TileEntityDistillation;
import cn.mcmod.sakura.tileentity.TileEntityFluidOut;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketClearFluidHandler implements IMessageHandler<PacketClearFluid, IMessage> {

    @Override
    public IMessage onMessage(PacketClearFluid message, MessageContext ctx) {
        World world = ctx.getServerHandler().player.world;
        BlockPos pos = new BlockPos(message.getX(), message.getY(), message.getZ());
        TileEntity te = world.getTileEntity(pos);
        
        if (te != null) {
            if (te instanceof TileEntityBarrel) {
                TileEntityBarrel barrel = (TileEntityBarrel) te;
                if (message.getTankType() == 0) {
                    barrel.getTank().drain(barrel.getTank().getFluidAmount(), true);
                } else if (message.getTankType() == 1) {
                    barrel.getResultTank().drain(barrel.getResultTank().getFluidAmount(), true);
                }
                barrel.markDirty();
            } else if (te instanceof TileEntityDistillation) {
                TileEntityDistillation distillation = (TileEntityDistillation) te;
                if (message.getTankType() == 0) {
                    distillation.getTank().drain(distillation.getTank().getFluidAmount(), true);
                } else if (message.getTankType() == 1) {
                    distillation.getResultTank().drain(distillation.getResultTank().getFluidAmount(), true);
                }
                distillation.markDirty();
            } else if (te instanceof TileEntityFluidOut) {
                TileEntityFluidOut fluidOut = (TileEntityFluidOut) te;
                fluidOut.getTank().drain(fluidOut.getTank().getFluidAmount(), true);
                fluidOut.markDirty();
            }
        }
        
        return null;
    }
}
