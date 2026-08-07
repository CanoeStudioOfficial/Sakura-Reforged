package cn.mcmod.sakura.block.slab;

import java.util.Random;

import cn.mcmod.sakura.block.BlockLoader;
import cn.mcmod_mmf.mmlib.block.slab.BlockSlabBase;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A tatami slab with the normal top/bottom/full slab states and a facing used
 * to keep the orientation of the tatami weave.
 */
public class BlockTatamiSlab extends BlockSlabBase {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public final boolean isNS;
    private final boolean isTan;

    public BlockTatamiSlab(Material material, boolean ns, boolean tan) {
        super(material);
        this.isNS = ns;
        this.isTan = tan;
        this.setTickRandomly(!tan);
        this.setSoundType(SoundType.PLANT);
        this.setHardness(0.25F).setResistance(0.5F);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
            float hitZ, int meta, EntityLivingBase placer) {
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer)
                .withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
            ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        /*
         * Metadata 2..5 was used by the old BlockHalfFacing implementation
         * for north/south/west/east bottom slabs. Keep those values intact so
         * existing worlds retain their tatami orientation after upgrading.
         */
        int halfMeta;
        EnumFacing facing;
        switch (meta) {
            case 0:
                halfMeta = 0;
                facing = EnumFacing.NORTH;
                break;
            case 1:
                halfMeta = 0;
                facing = EnumFacing.EAST;
                break;
            case 2:
                halfMeta = 1;
                facing = EnumFacing.NORTH;
                break;
            case 3:
                halfMeta = 1;
                facing = EnumFacing.SOUTH;
                break;
            case 4:
                halfMeta = 1;
                facing = EnumFacing.WEST;
                break;
            case 5:
                halfMeta = 1;
                facing = EnumFacing.EAST;
                break;
            case 6:
                halfMeta = 0;
                facing = EnumFacing.SOUTH;
                break;
            case 7:
                halfMeta = 0;
                facing = EnumFacing.WEST;
                break;
            case 8:
                halfMeta = 2;
                facing = EnumFacing.NORTH;
                break;
            case 9:
                halfMeta = 2;
                facing = EnumFacing.EAST;
                break;
            case 10:
                halfMeta = 2;
                facing = EnumFacing.SOUTH;
                break;
            case 11:
                halfMeta = 2;
                facing = EnumFacing.WEST;
                break;
            default:
                halfMeta = 0;
                facing = EnumFacing.NORTH;
                break;
        }
        return super.getStateFromMeta(halfMeta).withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int facingMeta = state.getValue(FACING).getHorizontalIndex();
        switch (state.getValue(HALF).ordinal()) {
            case 1:
                // Keep the old BlockHalfFacing metadata for bottom slabs.
                return state.getValue(FACING).getIndex();
            case 2:
                return 8 + facingMeta;
            default:
                switch (facingMeta) {
                    case 1:
                        return 1;
                    case 2:
                        return 6;
                    case 3:
                        return 7;
                    default:
                        return 0;
                }
        }
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {HALF, FACING});
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (worldIn.canBlockSeeSky(pos) && worldIn.isDaytime()) {
            BlockTatamiSlab tanSlab = isNS ? BlockLoader.TATAMI_TAN_NS_HALF : BlockLoader.TATAMI_TAN_HALF;
            worldIn.setBlockState(pos, tanSlab.getDefaultState()
                    .withProperty(HALF, state.getValue(HALF))
                    .withProperty(FACING, state.getValue(FACING)));
        }
        super.updateTick(worldIn, pos, state, rand);
    }
}
