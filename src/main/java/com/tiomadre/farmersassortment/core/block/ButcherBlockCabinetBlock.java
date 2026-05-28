package com.tiomadre.farmersassortment.core.block;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.entity.ButcherBlockCabinetBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.block.CabinetBlock;
import vectorwing.farmersdelight.common.registry.ModSounds;

import javax.annotation.Nullable;

public class ButcherBlockCabinetBlock extends CabinetBlock implements EntityBlock {

    public ButcherBlockCabinetBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, BlockHitResult hit) {
        Direction face = hit.getDirection();
        if (face == Direction.UP) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (!(blockEntity instanceof ButcherBlockCabinetBlockEntity cabinet)) {
                return InteractionResult.PASS;
            }

            ItemStack mainHandStack = player.getMainHandItem();
            if (mainHandStack.isEmpty()) {
                if (cabinet.isBoardEmpty() || level.isClientSide) {
                    return InteractionResult.CONSUME;
                }

                ItemStack removed = cabinet.removeBoardItem();
                if (!player.isCreative()) {
                    if (!player.getInventory().add(removed)) {
                        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), removed);
                    }
                }
                Vec3 centerPos = pos.getCenter();
                level.playSound(null, centerPos.x(), centerPos.y(), centerPos.z(), ModSounds.BLOCK_CUTTING_BOARD_REMOVE.get(), SoundSource.BLOCKS, 0.25F, 0.5F);
                return InteractionResult.SUCCESS;
            }

            if (cabinet.canAddBoardItem(mainHandStack)) {
                if (level.isClientSide) {
                    return InteractionResult.CONSUME;
                }

                ItemStack remainder = cabinet.addBoardItem(player.getAbilities().instabuild ? mainHandStack.copy() : mainHandStack);
                if (!player.isCreative()) {
                    player.setItemSlot(EquipmentSlot.MAINHAND, remainder);
                }
                Vec3 centerPos = pos.getCenter();
                level.playSound(null, centerPos.x(), centerPos.y(), centerPos.z(), ModSounds.BLOCK_CUTTING_BOARD_PLACE.get(), SoundSource.BLOCKS, 1.0F, 0.8F);
                return InteractionResult.SUCCESS;
            } else if (cabinet.processBoardItemUsingTool(mainHandStack, player)) {
                return InteractionResult.SUCCESS;
            }

            return InteractionResult.CONSUME;
        }

        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ButcherBlockCabinetBlockEntity cabinet) {
                player.openMenu(cabinet);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ButcherBlockCabinetBlockEntity cabinet) {
                ItemStack storedItem = cabinet.removeBoardItem();
                if (!storedItem.isEmpty()) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), storedItem);
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void tick(@NotNull BlockState state, ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ButcherBlockCabinetBlockEntity cabinet) {
            cabinet.recheckOpen();
        }
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (stack.hasCustomHoverName()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ButcherBlockCabinetBlockEntity cabinet) {
                cabinet.setCustomName(stack.getHoverName());
            }
        }
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState state, Level level, @NotNull BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ButcherBlockCabinetBlockEntity cabinet) {
            return AbstractContainerMenu.getRedstoneSignalFromContainer(cabinet);
        }
        return super.getAnalogOutputSignal(state, level, pos);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ButcherBlockCabinetBlockEntity(pos, state);
    }


    @Mod.EventBusSubscriber(modid = FarmersAssortment.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ToolCarvingEvent {
        @SubscribeEvent
        @SuppressWarnings("unused")
        public static void onSneakPlaceTool(PlayerInteractEvent.RightClickBlock event) {
            if (event.getFace() != Direction.UP) {
                return;
            }

            Level level = event.getLevel();
            BlockPos pos = event.getPos();
            if (!(level.getBlockEntity(pos) instanceof ButcherBlockCabinetBlockEntity cabinet)) {
                return;
            }

            Player player = event.getEntity();
            ItemStack heldStack = player.getMainHandItem();
            if (!player.isSecondaryUseActive() || heldStack.isEmpty()) {
                return;
            }

            if (cabinet.carveToolOnBoard(player.getAbilities().instabuild ? heldStack.copy() : heldStack)) {
                if (!player.isCreative()) {
                    player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                }
                Vec3 centerPos = pos.getCenter();
                level.playSound(null, centerPos.x(), centerPos.y(), centerPos.z(), ModSounds.BLOCK_CUTTING_BOARD_CARVE.get(), SoundSource.BLOCKS, 1.0F, 0.8F);
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }
}