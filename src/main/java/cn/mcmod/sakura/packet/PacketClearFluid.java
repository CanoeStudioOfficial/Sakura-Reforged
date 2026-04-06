package cn.mcmod.sakura.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketClearFluid implements IMessage {
    private BlockPos pos;
    private int tankId;
    
    public PacketClearFluid() {
    }
    
    public PacketClearFluid(BlockPos pos, int tankId) {
        this.pos = pos;
        this.tankId = tankId;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        tankId = buf.readInt();
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(tankId);
    }
    
    public BlockPos getPos() {
        return pos;
    }
    
    public int getTankId() {
        return tankId;
    }
}
