package warrook.lunamancy.blocks.entities;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import warrook.lunamancy.ModManifest;

public class PylonBlockEntity extends BlockEntity {
    public PylonBlockEntity() {
        this(ModManifest.ModBlocks.PYLON_BLOCK_ENTITY);
    }

    public PylonBlockEntity(BlockEntityType<?> type) {
        super(type);
    }
}
