package com.tiomadre.farmersassortment.core.block;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.entity.ButcherBlockCabinetBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TridentItem;
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
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;

public class ButcherBlockCabinetBlock extends CabinetBlock implements EntityBlock {

    public ButcherBlockCabinetBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, BlockHitResult hit) {
        Direction face = hit.getDirection();
        if (face == Direction.UP) {
            if (!isOnCuttingBoard(state, hit)) {
                return InteractionResult.PASS;
            }
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ButcherBlockCabinetBlockEntity cabinet) {
                boolean hasBlockAbove = !level.isEmptyBlock(pos.above());
                ItemStack heldStack = player.getItemInHand(hand);
                ItemStack offhandStack = player.getOffhandItem();

                if (cabinet.isBoardEmpty()) {
                    if (hasBlockAbove) {
                        return InteractionResult.PASS;
                    }
                    if (player.isSecondaryUseActive() && !heldStack.isEmpty()) {
                        Item item = heldStack.getItem();
                        if (item instanceof TieredItem || item instanceof TridentItem || item instanceof ShearsItem) {
                            if (cabinet.carveToolOnBoard(player.getAbilities().instabuild ? heldStack.copy() : heldStack)) {
                                level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 0.8F);
                                return InteractionResult.sidedSuccess(level.isClientSide);
                            }
                        }
                    }
                    if (!offhandStack.isEmpty()) {
                        if (hand == InteractionHand.MAIN_HAND && !offhandStack.is(ModTags.OFFHAND_EQUIPMENT) && !(heldStack.getItem() instanceof BlockItem)) {
                            if (offhandStack.getItem() instanceof BlockItem) {
                                ItemStack offhandStackForBoard = player.getAbilities().instabuild ? offhandStack.copy() : offhandStack;
                                if (cabinet.addBoardItem(offhandStackForBoard)) {
                                    return InteractionResult.sidedSuccess(level.isClientSide);
                                }
                            } else {
                                return InteractionResult.PASS;
                            }
                        }
                        if (hand == InteractionHand.OFF_HAND && offhandStack.is(ModTags.OFFHAND_EQUIPMENT)) {
                            return InteractionResult.PASS;
                        }
                    }
                    if (heldStack.isEmpty()) {
                        return InteractionResult.SUCCESS;
                    }

                    if (cabinet.addBoardItem(player.getAbilities().instabuild ? heldStack.copy() : heldStack)) {
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                } else if (!heldStack.isEmpty()) {
                    ItemStack boardStack = cabinet.getBoardItem().copy();
                    if (cabinet.processBoardItemUsingTool(heldStack, player)) {
                        ButcherBlockCabinetBlockEntity.spawnCuttingParticles(level, pos, boardStack, 5);
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                    return InteractionResult.CONSUME;
                } else if (hand == InteractionHand.MAIN_HAND) {
                    ItemStack removed = cabinet.removeBoardItem();
                    if (!removed.isEmpty()) {
                        if (!player.isCreative()) {
                            if (!player.getInventory().add(removed)) {
                                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), removed);
                            }
                        }
                        level.playSound(null, pos, SoundEvents.WOOD_HIT, SoundSource.BLOCKS, 0.25F, 0.5F);
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
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

    private boolean isOnCuttingBoard(BlockState state, BlockHitResult hit) {
        Vec3 localHit = hit.getLocation().subtract(hit.getBlockPos().getX(), hit.getBlockPos().getY(), hit.getBlockPos().getZ());
        Direction facing = state.getValue(FACING);
        double depth;
        switch (facing) {
            case NORTH -> depth = localHit.z;
            case SOUTH -> depth = 1.0D - localHit.z;
            case WEST -> depth = localHit.x;
            case EAST -> depth = 1.0D - localHit.x;
            default -> depth = 0.0D;
        }
        return depth <= 1.0D;
    }

    @Mod.EventBusSubscriber(modid = FarmersAssortment.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ToolCarvingEvent {
        @SubscribeEvent
        @SuppressWarnings("unused")
        public static void onSneakPlaceTool(PlayerInteractEvent.RightClickBlock event) {
            Level level = event.getLevel();
            BlockPos pos = event.getPos();
            Player player = event.getEntity();
            ItemStack heldStack = player.getMainHandItem();
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (player.isSecondaryUseActive() && !heldStack.isEmpty()) {
                if (tileEntity instanceof ButcherBlockCabinetBlockEntity cabinet) {
                    if (heldStack.getItem() instanceof TieredItem ||
                            heldStack.getItem() instanceof TridentItem ||
                            heldStack.getItem() instanceof ShearsItem) {

                        boolean success = cabinet.carveToolOnBoard(
                                player.getAbilities().instabuild ? heldStack.copy() : heldStack);

                        if (success) {
                            level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 0.8F);

                            // Cancel + return SUCCESS
                            event.setCanceled(true);
                            event.setCancellationResult(InteractionResult.SUCCESS);
                        }
                    }
                }
            }
        }
    }
}