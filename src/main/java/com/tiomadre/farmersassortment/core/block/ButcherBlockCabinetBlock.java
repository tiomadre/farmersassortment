package com.tiomadre.farmersassortment.core.block;

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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import vectorwing.farmersdelight.common.block.CabinetBlock;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;

/**
 * A hybrid block that combines the storage of a {@link CabinetBlock} with the
 * cutting interactions from Farmer's Delight's {@code CuttingBoardBlock}.
 */
@SuppressWarnings("deprecation")
public class ButcherBlockCabinetBlock extends CabinetBlock implements EntityBlock {

    public ButcherBlockCabinetBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Direction face = hit.getDirection();
        if (face == Direction.UP) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ButcherBlockCabinetBlockEntity cabinet) {
                ItemStack heldStack = player.getItemInHand(hand);
                ItemStack offhandStack = player.getOffhandItem();

                if (cabinet.isBoardEmpty()) {
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
                            return InteractionResult.PASS;
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
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
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
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ButcherBlockCabinetBlockEntity cabinet) {
            cabinet.recheckOpen();
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (stack.hasCustomHoverName()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ButcherBlockCabinetBlockEntity cabinet) {
                cabinet.setCustomName(stack.getHoverName());
            }
        }
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ButcherBlockCabinetBlockEntity cabinet) {
            return AbstractContainerMenu.getRedstoneSignalFromContainer(cabinet);
        }
        return super.getAnalogOutputSignal(state, level, pos);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ButcherBlockCabinetBlockEntity(pos, state);
    }
}
