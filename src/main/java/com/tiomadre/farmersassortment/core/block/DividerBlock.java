package com.tiomadre.farmersassortment.core.block;

import com.tiomadre.farmersassortment.core.block.state.DividerState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DividerBlock extends HorizontalDirectionalBlock {
    public static final EnumProperty<DividerState> STATE = EnumProperty.create("state", DividerState.class);

    private static final VoxelShape CLOSED_SHAPE = Shapes.or(
            Block.box(0, 0, 0, 16, 16, 1), Block.box(0, 16, 0, 16, 32, 1));
    private static final VoxelShape HALF_OPEN_SHAPE = Shapes.or(
            Block.box(0, 0, 0, 1, 32, 1), Block.box(15, 0, 1, 16, 32, 16));
    private static final VoxelShape OPEN_SHAPE = Shapes.or(
            Block.box(0, 0, 0, 32, 16, 1), Block.box(0, 16, 0, 32, 32, 1));

    public DividerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(STATE, DividerState.HALF_OPEN));
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                          InteractionHand hand, BlockHitResult hit) {
        if (!player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        DividerState current = state.getValue(STATE);
        DividerState next = switch (current) {
            case CLOSED -> DividerState.HALF_OPEN;
            case HALF_OPEN -> canOpen(level, pos, state) ? DividerState.OPEN : DividerState.CLOSED;
            case OPEN -> DividerState.CLOSED;
        };

        if (!level.isClientSide) {
            level.setBlock(pos, state.setValue(STATE, next), Block.UPDATE_ALL);
            level.playSound(null, pos,
                    next == DividerState.CLOSED ? SoundEvents.FENCE_GATE_CLOSE : SoundEvents.FENCE_GATE_OPEN,
                    SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private boolean canOpen(Level level, BlockPos pos, BlockState state) {
        BlockPos extensionPos = pos.relative(extensionDirection(state));
        return level.getWorldBorder().isWithinBounds(extensionPos)
                && level.getBlockState(extensionPos).canBeReplaced();
    }

    private Direction extensionDirection(BlockState state) {
        return state.getValue(FACING).getCounterClockWise();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(STATE, DividerState.HALF_OPEN);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = switch (state.getValue(STATE)) {
            case CLOSED -> CLOSED_SHAPE;
            case HALF_OPEN -> HALF_OPEN_SHAPE;
            case OPEN -> OPEN_SHAPE;
        };
        return rotateFromNorth(shape, state.getValue(FACING));
    }

    private static VoxelShape rotateFromNorth(VoxelShape shape, Direction direction) {
        VoxelShape rotated = shape;
        for (int turns = 0; turns < direction.get2DDataValue(); turns++) {
            VoxelShape next = Shapes.empty();
            for (var box : rotated.toAabbs()) {
                next = Shapes.or(next, Shapes.create(-box.maxZ + 1, box.minY, box.minX,
                        -box.minZ + 1, box.maxY, box.maxX));
            }
            rotated = next;
        }
        return rotated;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING, STATE);
    }
}