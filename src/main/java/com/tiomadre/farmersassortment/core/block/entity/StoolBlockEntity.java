package com.tiomadre.farmersassortment.core.block.entity;

import com.tiomadre.farmersassortment.core.block.state.FurnitureCoverType;
import com.tiomadre.farmersassortment.core.registry.FABlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public class StoolBlockEntity extends BlockEntity {
    public static final ModelProperty<ItemStack> COVER = new ModelProperty<>();
    private ItemStack covering = ItemStack.EMPTY;

    public StoolBlockEntity(BlockPos pos, BlockState state) {
        super(FABlockEntityTypes.STOOL.get(), pos, state);
    }

    public ItemStack getCovering() {
        return covering.copy();
    }

    public void setCovering(ItemStack stack) {
        covering = FurnitureCoverType.isCarpet(stack.getItem()) ? stack.copyWithCount(1) : ItemStack.EMPTY;
        setChanged();
        requestModelDataUpdate();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!covering.isEmpty()) {
            tag.put("Covering", covering.save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ItemStack stored = ItemStack.of(tag.getCompound("Covering"));
        covering = FurnitureCoverType.isCarpet(stored.getItem()) ? stored.copyWithCount(1) : ItemStack.EMPTY;
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
        return ModelData.builder().with(COVER, covering.copy()).build();
    }
}
