package com.tiomadre.farmersassortment.core.block.entity;

import com.tiomadre.farmersassortment.core.block.FloatingDrawerBlock;
import com.tiomadre.farmersassortment.core.menu.FloatingDrawerMenu;
import com.tiomadre.farmersassortment.core.registry.FABlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import vectorwing.farmersdelight.common.registry.ModSounds;

public class FloatingDrawerBlockEntity extends RandomizableContainerBlockEntity {

    private NonNullList<ItemStack> contents = NonNullList.withSize(8, ItemStack.EMPTY);
    private BlockState slabState = Blocks.AIR.defaultBlockState();

    private final ContainerOpenersCounter openers = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            playCabinetSound(state, ModSounds.BLOCK_CABINET_OPEN.get());
            setOpen(true);
        }

        @Override
        protected void onClose(Level level, BlockPos pos, BlockState state) {
            playCabinetSound(state, ModSounds.BLOCK_CABINET_CLOSE.get());
            setOpen(false);
        }
        private void playCabinetSound(BlockState state, SoundEvent sound) {
            if (level == null || level.isClientSide) {
                return;
            }

            Vec3 position = Vec3.atCenterOf(worldPosition)
                    .add(Vec3.atLowerCornerOf(
                            state.getValue(FloatingDrawerBlock.FACING).getNormal()
                    ).scale(0.5D));

            level.playSound(
                    null,
                    position.x,
                    position.y,
                    position.z,
                    sound,
                    SoundSource.BLOCKS,
                    0.5F,
                    level.random.nextFloat() * 0.1F + 0.9F
            );
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos,
                                          BlockState state, int previous, int current) {
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            return player.containerMenu instanceof FloatingDrawerMenu menu
                    && menu.getContainer() == FloatingDrawerBlockEntity.this;
        }
    };

    public FloatingDrawerBlockEntity(BlockPos pos, BlockState state) {
        super(FABlockEntityTypes.FLOATING_COUNTER.get(), pos, state);
    }

    public BlockState getSlabState() {
        return slabState;
    }

    public void setSlabState(BlockState state) {
        if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
            state = state.setValue(BlockStateProperties.WATERLOGGED, false);
        }

        slabState = state;
        setChanged();

        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    private void readSlabState(CompoundTag tag) {
        slabState = tag.contains("SlabState", Tag.TAG_COMPOUND)
                ? NbtUtils.readBlockState(
                        BuiltInRegistries.BLOCK.asLookup(), tag.getCompound("SlabState"))
                : Blocks.AIR.defaultBlockState();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();

        if (!slabState.isAir()) {
            tag.put("SlabState", NbtUtils.writeBlockState(slabState));
        }

        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        readSlabState(tag);
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
        CompoundTag tag = packet.getTag();

        if (tag != null) {
            readSlabState(tag);
        }
    }

    @Override
    public int getContainerSize() {
        return getBlockState().getValue(FloatingDrawerBlock.DOUBLE) ? 8 : 4;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return contents;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        contents = NonNullList.withSize(8, ItemStack.EMPTY);
        for (int index = 0; index < Math.min(items.size(), contents.size()); index++) {
            contents.set(index, items.get(index));
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(getBlockState().getValue(FloatingDrawerBlock.DOUBLE)
                ? "container.farmersassortment.double_counter"
                : "container.farmersassortment.floating_counter");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new FloatingDrawerMenu(id, inventory, this,
                getBlockState().getValue(FloatingDrawerBlock.DOUBLE));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        readSlabState(tag);
        contents = NonNullList.withSize(8, ItemStack.EMPTY);
        if (!tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, contents);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!slabState.isAir()) {
            tag.put("SlabState", NbtUtils.writeBlockState(slabState));
        }
        if (!trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, contents);
        }
    }

    @Override
    public void startOpen(Player player) {
        if (!isRemoved() && !player.isSpectator() && level != null && !level.isClientSide) {
            openers.incrementOpeners(player, level, worldPosition, getBlockState());
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (!isRemoved() && !player.isSpectator() && level != null && !level.isClientSide) {
            openers.decrementOpeners(player, level, worldPosition, getBlockState());
        }
    }

    public void recheckOpen() {
        if (!isRemoved() && level != null && !level.isClientSide) {
            openers.recheckOpeners(level, worldPosition, getBlockState());

            setOpen(openers.getOpenerCount() > 0);
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null && !level.isClientSide) {
            level.scheduleTick(worldPosition, getBlockState().getBlock(), 1);
        }
    }

    private void setOpen(boolean open) {
        if (level != null && !level.isClientSide) {
            BlockState state = getBlockState();
            if (state.getValue(FloatingDrawerBlock.OPEN) != open) {
                level.setBlock(worldPosition,
                        state.setValue(FloatingDrawerBlock.OPEN, open), 3);
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return level != null
                && level.getBlockEntity(worldPosition) == this
                && player.distanceToSqr(
                worldPosition.getX() + 0.5,
                worldPosition.getY() + 0.5,
                worldPosition.getZ() + 0.5) <= 64;
    }
}
