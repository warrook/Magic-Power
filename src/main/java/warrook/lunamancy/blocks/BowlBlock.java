package warrook.lunamancy.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import warrook.lunamancy.blocks.entities.BowlBlockEntity;

public class BowlBlock extends BaseBlock implements BlockEntityProvider {

    protected static final VoxelShape BOUNDS = Block.createCuboidShape(2.0d, 0.0d, 2.0d, 14.0d, 5.0d, 14.0d);

    public BowlBlock() {
        super("bowl", FabricBlockSettings.of(Material.STONE).nonOpaque());
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new BowlBlockEntity();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return BOUNDS;
    }
}
