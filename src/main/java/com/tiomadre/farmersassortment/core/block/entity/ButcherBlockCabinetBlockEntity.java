package com.tiomadre.farmersassortment.core.block.entity;

import com.tiomadre.farmersassortment.core.block.ButcherBlockCabinetBlock;
import com.tiomadre.farmersassortment.core.registry.FABlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.mixin.accessor.RecipeManagerAccessor;
import vectorwing.farmersdelight.common.registry.ModAdvancements;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("ALL")
public class ButcherBlockCabinetBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> contents = NonNullList.withSize(27, ItemStack.EMPTY);
    private final ItemStackHandler boardInventory = createHandler();
    private final LazyOptional<IItemHandler> boardHandler = LazyOptional.of(() -> boardInventory);
    private ResourceLocation lastRecipeId;
    private boolean isItemCarvingBoard;

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            ButcherBlockCabinetBlockEntity.this.playCabinetSound(state, ModSounds.BLOCK_CABINET_OPEN.get());
            ButcherBlockCabinetBlockEntity.this.updateBlockState(state, true);
        }

        @Override
        protected void onClose(Level level, BlockPos pos, BlockState state) {
            ButcherBlockCabinetBlockEntity.this.playCabinetSound(state, ModSounds.BLOCK_CABINET_CLOSE.get());
            ButcherBlockCabinetBlockEntity.this.updateBlockState(state, false);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int i, int j) {
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            if (player.containerMenu instanceof ChestMenu chestMenu) {
                Container container = chestMenu.getContainer();
                return container == ButcherBlockCabinetBlockEntity.this;
            }
            return false;
        }
    };

    public ButcherBlockCabinetBlockEntity(BlockPos pos, BlockState state) {
        super(FABlockEntityTypes.BUTCHER_BLOCK_CABINET.get(), pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        contents = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        if (!tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, contents);
        }
        isItemCarvingBoard = tag.getBoolean("IsItemCarved");
        boardInventory.deserializeNBT(tag.getCompound("Board"));
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, contents);
        }
        tag.put("Board", boardInventory.serializeNBT());
        tag.putBoolean("IsItemCarved", isItemCarvingBoard);
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return contents;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        contents = items;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.farmersassortment.butcher_block_cabinet");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return ChestMenu.threeRows(id, player, this);
    }

    @Override
    public void startOpen(Player player) {
        if (level != null && !remove && !player.isSpectator()) {
            openersCounter.incrementOpeners(player, level, getBlockPos(), getBlockState());
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (level != null && !remove && !player.isSpectator()) {
            openersCounter.decrementOpeners(player, level, getBlockPos(), getBlockState());
        }
    }

    public void recheckOpen() {
        if (level != null && !remove) {
            openersCounter.recheckOpeners(level, getBlockPos(), getBlockState());
        }
    }

    private void updateBlockState(BlockState state, boolean open) {
        if (level != null) {
            level.setBlock(worldPosition, state.setValue(ButcherBlockCabinetBlock.OPEN, open), Block.UPDATE_ALL);
        }
    }

    private void playCabinetSound(BlockState state, SoundEvent sound) {
        if (level == null) {
            return;
        }
        Direction facing = state.getValue(ButcherBlockCabinetBlock.FACING);
        Vec3 offset = Vec3.atCenterOf(worldPosition).add(Vec3.atLowerCornerOf(facing.getNormal()).scale(0.5D));
        level.playSound(null, offset.x, offset.y, offset.z, sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    public boolean addBoardItem(ItemStack stack) {
        if (isBoardEmpty() && !stack.isEmpty()) {
            boardInventory.setStackInSlot(0, stack.split(1));
            isItemCarvingBoard = false;
            inventoryChanged();
            return true;
        }
        return false;
    }

    public boolean carveToolOnBoard(ItemStack tool) {
        if (addBoardItem(tool)) {
            isItemCarvingBoard = true;
            return true;
        }
        return false;
    }

    public ItemStack removeBoardItem() {
        if (!isBoardEmpty()) {
            isItemCarvingBoard = false;
            ItemStack stack = boardInventory.getStackInSlot(0).split(1);
            inventoryChanged();
            return stack;
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getBoardItem() {
        return boardInventory.getStackInSlot(0);
    }

    public boolean isBoardEmpty() {
        return boardInventory.getStackInSlot(0).isEmpty();
    }

    public boolean isItemCarvingBoard() {
        return isItemCarvingBoard;
    }

    public boolean processBoardItemUsingTool(ItemStack toolStack, @Nullable Player player) {
        if (level == null || isItemCarvingBoard) {
            return false;
        }

        Optional<CuttingBoardRecipe> recipe = getMatchingRecipe(new RecipeWrapper(boardInventory), toolStack, player);
        recipe.ifPresent(result -> {
            List<ItemStack> outputs = result.rollResults(level.random, EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, toolStack));
            for (ItemStack stack : outputs) {
                Direction direction = getBlockState().getValue(ButcherBlockCabinetBlock.FACING).getCounterClockWise();
                ItemUtils.spawnItemEntity(level, stack.copy(),
                        worldPosition.getX() + 0.5 + direction.getStepX() * 0.2,
                        worldPosition.getY() + 0.2,
                        worldPosition.getZ() + 0.5 + direction.getStepZ() * 0.2,
                        direction.getStepX() * 0.2F, 0.0F, direction.getStepZ() * 0.2F);
            }
            if (player != null) {
                toolStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            } else if (toolStack.hurt(1, level.random, null)) {
                toolStack.setCount(0);
            }
            playProcessingSound(result.getSoundEventID(), toolStack, getBoardItem());
            removeBoardItem();
            if (player instanceof ServerPlayer serverPlayer) {
                ModAdvancements.CUTTING_BOARD.trigger(serverPlayer);
            }
        });

        return recipe.isPresent();
    }

    private Optional<CuttingBoardRecipe> getMatchingRecipe(RecipeWrapper wrapper, ItemStack toolStack, @Nullable Player player) {
        if (level == null) {
            return Optional.empty();
        }

        if (lastRecipeId != null) {
            Recipe<RecipeWrapper> recipe = ((RecipeManagerAccessor) level.getRecipeManager())
                    .getRecipeMap(ModRecipeTypes.CUTTING.get())
                    .get(lastRecipeId);
            if (recipe instanceof CuttingBoardRecipe cuttingBoardRecipe && cuttingBoardRecipe.matches(wrapper, level) && cuttingBoardRecipe.getTool().test(toolStack)) {
                return Optional.of(cuttingBoardRecipe);
            }
        }

        List<CuttingBoardRecipe> recipes = level.getRecipeManager().getRecipesFor(ModRecipeTypes.CUTTING.get(), wrapper, level);
        if (recipes.isEmpty()) {
            if (player != null) {
                player.displayClientMessage(TextUtils.getTranslation("block.cutting_board.invalid_item"), true);
            }
            return Optional.empty();
        }
        Optional<CuttingBoardRecipe> recipe = recipes.stream().filter(r -> r.getTool().test(toolStack)).findFirst();
        if (recipe.isEmpty()) {
            if (player != null) {
                player.displayClientMessage(TextUtils.getTranslation("block.cutting_board.invalid_tool"), true);
            }
            return Optional.empty();
        }
        lastRecipeId = recipe.get().getId();
        return recipe;
    }

    public void playProcessingSound(String soundEventId, ItemStack tool, ItemStack boardItem) {
        SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(soundEventId));
        if (sound != null) {
            playSound(sound, 1.0F, 1.0F);
        } else if (tool.is(Tags.Items.SHEARS)) {
            playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
        } else if (tool.is(ForgeTags.TOOLS_KNIVES)) {
            playSound(ModSounds.BLOCK_CUTTING_BOARD_KNIFE.get(), 0.8F, 1.0F);
        } else if (boardItem.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            SoundType soundType = block.defaultBlockState().getSoundType();
            playSound(soundType.getBreakSound(), 1.0F, 0.8F);
        } else {
            playSound(SoundEvents.WOOD_BREAK, 1.0F, 0.8F);
        }
    }

    public void playSound(SoundEvent sound, float volume, float pitch) {
        if (level != null) {
            level.playSound(null, worldPosition.getX() + 0.5F, worldPosition.getY() + 0.5F, worldPosition.getZ() + 0.5F, sound, SoundSource.BLOCKS, volume, pitch);
        }
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler() {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }
        };
    }

    private void inventoryChanged() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public static void spawnCuttingParticles(Level level, BlockPos pos, ItemStack stack, int count) {
        for (int i = 0; i < count; ++i) {
            Vec3 vec3 = new Vec3((level.random.nextDouble() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, (level.random.nextDouble() - 0.5D) * 0.1D);
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack),
                        pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D,
                        1, vec3.x, vec3.y + 0.05D, vec3.z, 0.0D);
            } else {
                level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack),
                        pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D,
                        vec3.x, vec3.y, vec3.z);
            }
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        if (capability == ForgeCapabilities.ITEM_HANDLER && side == Direction.UP) {
            return boardHandler.cast();
        }
        return super.getCapability(capability, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        boardHandler.invalidate();
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        load(packet.getTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }
}
