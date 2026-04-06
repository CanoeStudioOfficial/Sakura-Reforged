package cn.mcmod.sakura.packet;

import cn.mcmod.sakura.tileentity.TileEntityBarrel;
import cn.mcmod.sakura.tileentity.TileEntityCampfirePot;
import cn.mcmod.sakura.tileentity.TileEntityDistillation;
import cn.mcmod.sakura.tileentity.TileEntityFluidOut;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketClearFluidHandler implements IMessageHandler<PacketClearFluid, IMessage> {
    
    @Override
    public IMessage onMessage(PacketClearFluid message, MessageContext ctx) {
        ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
            World world = ctx.getServerHandler().player.world;
            TileEntity tile = world.getTileEntity(message.getPos());
            
            if (tile != null) {
                FluidTank tank = null;
                
                if (tile instanceof TileEntityBarrel) {
                    TileEntityBarrel barrel = (TileEntityBarrel) tile;
                    if (message.getTankId() == 0) {
                        tank = barrel.getTank();
                    } else if (message.getTankId() == 1) {
                        tank = barrel.getResultTank();
                    }
                } else if (tile instanceof TileEntityDistillation) {
                    TileEntityDistillation distillation = (TileEntityDistillation) tile;
                    if (message.getTankId() == 0) {
                        tank = distillation.getTank();
                    } else if (message.getTankId() == 1) {
                        tank = distillation.getResultTank();
                    }
                } else if (tile instanceof TileEntityCampfirePot) {
                    TileEntityCampfirePot pot = (TileEntityCampfirePot) tile;
                    tank = pot.getTank();
                } else if (tile instanceof TileEntityFluidOut) {
                    TileEntityFluidOut fluidOut = (TileEntityFluidOut) tile;
                    tank = fluidOut.getTank();
                }
                
                if (tank != null && tank.getFluidAmount() > 0) {
                    tank.drain(tank.getFluidAmount(), true);
                    tile.markDirty();
                }
            }
        });
        
        return null;
    }
}
