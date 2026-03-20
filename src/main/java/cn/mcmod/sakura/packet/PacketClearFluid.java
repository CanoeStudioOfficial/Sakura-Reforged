package cn.mcmod.sakura.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketClearFluid implements IMessage {
    private int x;
    private int y;
    private int z;
    private int tankType;
    
    public PacketClearFluid() {
    }
    
    public PacketClearFluid(int x, int y, int z, int tankType) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tankType = tankType;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        tankType = buf.readInt();
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(tankType);
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getZ() {
        return z;
    }
    
    public int getTankType() {
        return tankType;
    }
}
