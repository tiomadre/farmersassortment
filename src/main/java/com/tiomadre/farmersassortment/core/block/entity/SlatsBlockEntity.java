package com.tiomadre.farmersassortment.core.block.entity;

import com.tiomadre.farmersassortment.core.block.SlatsBlock;
import com.tiomadre.farmersassortment.core.registry.FADynamicFurniture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public class SlatsBlockEntity extends BlockEntity {
    public static final ModelProperty<BlockState> SOIL = new ModelProperty<>();
    private BlockState soil = Blocks.DIRT.defaultBlockState();

    public SlatsBlockEntity(BlockPos pos, BlockState state) {
        super(FADynamicFurniture.SLATS_ENTITY.get(), pos, state);
    }

    public BlockState getSoil() {
        return soil;
    }

    public void setSoil(BlockState state) {
        if (state.isAir() || state.hasBlockEntity() || state.getBlock() instanceof SlatsBlock || state == soil) {
            return;
        }
        soil = state;
        setChanged();
        requestModelDataUpdate();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            if (!level.isClientSide) {
                getBlockState().updateNeighbourShapes(level, worldPosition, 3);
                level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Soil", NbtUtils.writeBlockState(soil));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        BlockState stored = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), tag.getCompound("Soil"));
        soil = stored.isAir() || stored.hasBlockEntity() || stored.getBlock() instanceof SlatsBlock
                ? Blocks.DIRT.defaultBlockState() : stored;
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
        requestModelDataUpdate();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
        if (packet.getTag() != null) {
            handleUpdateTag(packet.getTag());
        }
    }

    @Override
    public ModelData getModelData() {
        return ModelData.builder().with(SOIL, soil).build();
    }
}
