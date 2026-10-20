package com.tiomadre.farmersassortment.core.block.entity;

import com.tiomadre.farmersassortment.core.registry.FADyeing;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public class RopeColorBlockEntity extends BlockEntity {
    public static final ModelProperty<Boolean> DYED = new ModelProperty<>();
    private int color = -1;

    public RopeColorBlockEntity(BlockPos pos, BlockState state) {
        super(FADyeing.ROPE_COLOR.get(), pos, state);
    }

    public int getColor() { return color; }

    public void setColor(int color) {
        this.color = color < 0 ? -1 : color & 0xFFFFFF;
        setChanged();
        requestModelDataUpdate();
        if (level != null) {
            BlockState state = getBlockState();
            level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Color", color);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        color = tag.contains("Color", Tag.TAG_ANY_NUMERIC) ? tag.getInt("Color") : -1;
        requestModelDataUpdate();
        if (level != null && level.isClientSide) {
            BlockState state = getBlockState();
            level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public ModelData getModelData() {
        return ModelData.builder().with(DYED, color >= 0).build();
    }
}
