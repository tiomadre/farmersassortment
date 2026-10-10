package com.tiomadre.farmersassortment.core.block;

import com.tiomadre.farmersassortment.core.block.state.DividerState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class DividerBlock extends HorizontalDirectionalBlock {
    public static final EnumProperty<DividerState> STATE =
            EnumProperty.create("state", DividerState.class);

    public static final IntegerProperty PART = IntegerProperty.create("part", 0, 3);

    private static final VoxelShape CELL = Block.box(0, 0, 0, 16, 16, 16);

    private static final VoxelShape CLOSED_SHAPE =
            Block.box(0, 0, 0, 16, 24, 2);

    private static final VoxelShape HALF_OPEN_SHAPE = Shapes.or(
            Block.box(0, 0, 0, 16, 24, 1),
            Block.box(15, 0, 1, 16, 24, 16));

    private static final VoxelShape OPEN_SHAPE =
            Block.box(0, 0, 0, 32, 24, 1);

    public DividerBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(STATE, DividerState.HALF_OPEN)
                .setValue(PART, 0));
    }

    private static Direction extensionDirection(BlockState state) {
        return state.getValue(FACING).getCounterClockWise();
    }

    private static BlockPos partOffset(BlockState state, int part) {
        BlockPos offset = BlockPos.ZERO;
        if (part >= 2) {
            offset = offset.relative(extensionDirection(state));
        }
        if ((part & 1) != 0) {
            offset = offset.above();
        }
        return offset;
    }

    private static BlockPos basePosition(BlockState state, BlockPos pos) {
        BlockPos offset = partOffset(state, state.getValue(PART));
        return pos.offset(-offset.getX(), -offset.getY(), -offset.getZ());
    }

    private boolean isBase(BlockState state, Direction facing) {
        return state.is(this)
                && state.getValue(PART) == 0
                && state.getValue(FACING) == facing;
    }

    private boolean isPart(BlockState state, BlockState base, int part) {
        return state.is(this)
                && state.getValue(PART) == part
                && state.getValue(FACING) == base.getValue(FACING);
    }

    private boolean canOccupy(LevelAccessor level, BlockPos pos) {
        return !level.isOutsideBuildHeight(pos)
                && level.getWorldBorder().isWithinBounds(pos)
                && level.getBlockState(pos).canBeReplaced();
    }

    private boolean canOpen(LevelAccessor level, BlockPos pos, BlockState state) {
        BlockPos extension = pos.relative(extensionDirection(state));
        return canOccupy(level, extension)
                && canOccupy(level, extension.above());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());

        DividerState adjacent = getAdjacentDividerState(context);
        state = state.setValue(STATE,
                adjacent == null ? DividerState.HALF_OPEN : adjacent);

        if (!canOccupy(context.getLevel(), context.getClickedPos().above())) {
            return null;
        }

        if (state.getValue(STATE) == DividerState.OPEN
                && !canOpen(context.getLevel(), context.getClickedPos(), state)) {
            return null;
        }
        return state;
    }

    private DividerState getAdjacentDividerState(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Direction face = context.getClickedFace();

        if (face.getAxis().isHorizontal()) {
            BlockState clicked =
                    context.getLevel().getBlockState(pos.relative(face.getOpposite()));
            if (clicked.is(this)) {
                return clicked.getValue(STATE);
            }
        }

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState neighbor =
                    context.getLevel().getBlockState(pos.relative(direction));
            if (neighbor.is(this)) {
                return neighbor.getValue(STATE);
            }
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                            LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide && state.getValue(PART) == 0) {
            synchronizeParts(level, pos, state);
        }
    }

    private void synchronizeParts(Level level, BlockPos pos, BlockState base) {
        int flags = Block.UPDATE_ALL | Block.UPDATE_KNOWN_SHAPE;

        for (int part = 1; part <= 3; part++) {
            BlockPos target = pos.offset(partOffset(base, part));

            if (part == 1 || base.getValue(STATE) == DividerState.OPEN) {
                level.setBlock(target, base.setValue(PART, part), flags);
            } else if (isPart(level.getBlockState(target), base, part)) {
                level.setBlock(target, Blocks.AIR.defaultBlockState(), flags);
            }
        }

        for (int part = 0; part <= 3; part++) {
            BlockPos target = pos.offset(partOffset(base, part));
            level.getBlockState(target).updateNeighbourShapes(
                    level, target, Block.UPDATE_ALL);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand,
                                 BlockHitResult hit) {
        if (!player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        BlockPos basePos = basePosition(state, pos);
        BlockState base = level.getBlockState(basePos);

        if (!isBase(base, state.getValue(FACING))) {
            return InteractionResult.PASS;
        }

        DividerState next = switch (base.getValue(STATE)) {
            case CLOSED -> DividerState.HALF_OPEN;
            case HALF_OPEN -> canOpen(level, basePos, base)
                    ? DividerState.OPEN : DividerState.CLOSED;
            case OPEN -> DividerState.CLOSED;
        };

        if (!level.isClientSide) {
            BlockState updated = base.setValue(STATE, next);
            level.setBlock(basePos, updated, Block.UPDATE_ALL | Block.UPDATE_KNOWN_SHAPE);
            synchronizeParts(level, basePos, updated);

            level.playSound(null, basePos,
                    next == DividerState.CLOSED
                            ? SoundEvents.FENCE_GATE_CLOSE
                            : SoundEvents.FENCE_GATE_OPEN,
                    SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return state.getValue(PART) == 0
                ? RenderShape.MODEL : RenderShape.INVISIBLE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level,
                               BlockPos pos, CollisionContext context) {
        VoxelShape fullShape = switch (state.getValue(STATE)) {
            case CLOSED -> CLOSED_SHAPE;
            case HALF_OPEN -> HALF_OPEN_SHAPE;
            case OPEN -> OPEN_SHAPE;
        };

        fullShape = rotateModelShape(fullShape, state.getValue(FACING));
        BlockPos offset = partOffset(state, state.getValue(PART));

        return Shapes.join(
                fullShape.move(-offset.getX(), -offset.getY(), -offset.getZ()),
                CELL,
                net.minecraft.world.phys.shapes.BooleanOp.AND);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level,
                                        BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    private static VoxelShape rotateModelShape(VoxelShape shape, Direction facing) {
        VoxelShape rotated = shape;

        for (int turn = 0; turn < facing.get2DDataValue(); turn++) {
            VoxelShape next = Shapes.empty();
            for (var box : rotated.toAabbs()) {
                next = Shapes.or(next, Shapes.create(
                        1 - box.maxZ, box.minY, box.minX,
                        1 - box.minZ, box.maxY, box.maxX));
            }
            rotated = next;
        }
        return rotated;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction,
                                  BlockState neighbor, LevelAccessor level,
                                  BlockPos pos, BlockPos neighborPos) {
        int part = state.getValue(PART);

        if (part != 0) {
            BlockState base = level.getBlockState(basePosition(state, pos));
            if (!isBase(base, state.getValue(FACING))
                    || (part >= 2 && base.getValue(STATE) != DividerState.OPEN)) {
                return Blocks.AIR.defaultBlockState();
            }
        }

        return super.updateShape(state, direction, neighbor, level, pos, neighborPos);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos,
                                  BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()
                && state.getValue(PART) != 0) {
            BlockPos basePos = basePosition(state, pos);
            BlockState base = level.getBlockState(basePos);
            if (isBase(base, state.getValue(FACING))) {
                level.destroyBlock(basePos, false, player);
            }
        }

        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos,
                         BlockState replacement, boolean moving) {
        if (!state.is(replacement.getBlock()) && !level.isClientSide) {
            if (state.getValue(PART) == 0) {
                for (int part = 1; part <= 3; part++) {
                    BlockPos target = pos.offset(partOffset(state, part));
                    if (isPart(level.getBlockState(target), state, part)) {
                        level.removeBlock(target, moving);
                    }
                }
            } else {
                BlockPos basePos = basePosition(state, pos);
                BlockState base = level.getBlockState(basePos);
                if (isBase(base, state.getValue(FACING))
                        && base.getValue(STATE) == state.getValue(STATE)) {
                    level.destroyBlock(basePos, true);
                }
            }
        }

        super.onRemove(state, level, pos, replacement, moving);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return state.getValue(PART) == 0
                ? super.getDrops(state, params) : List.of();
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STATE, PART);
    }
}
