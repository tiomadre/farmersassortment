package com.tiomadre.farmersassortment.core.block;

import com.tiomadre.farmersassortment.core.block.state.DividerState;
import com.tiomadre.farmersassortment.core.block.entity.RopeColorBlockEntity;
import com.tiomadre.farmersassortment.core.util.DyeColors;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
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
import java.util.ArrayList;
import vectorwing.farmersdelight.common.registry.ModItems;

public class DividerBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public static final EnumProperty<DividerState> STATE =
            EnumProperty.create("state", DividerState.class);

    public static final IntegerProperty PART = IntegerProperty.create("part", 0, 5);
    public static final IntegerProperty CANVAS = IntegerProperty.create("canvas", 0, 3);

    private static final VoxelShape CELL = Block.box(0, 0, 0, 16, 16, 16);

    public DividerBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(STATE, DividerState.HALF_OPEN)
                .setValue(PART, 0)
                .setValue(CANVAS, 0));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RopeColorBlockEntity(pos, state);
    }

    private int getDividerColor(BlockGetter level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof RopeColorBlockEntity divider
                ? divider.getColor() : -1;
    }

    private void setDividerColor(Level level, BlockPos pos, BlockState base, int color) {
        for (int part = 0; part <= 5; part++) {
            BlockPos target = pos.offset(partOffset(base, part));
            if (isPart(level.getBlockState(target), base, part)
                    && level.getBlockEntity(target) instanceof RopeColorBlockEntity divider
                    && divider.getColor() != color) {
                divider.setColor(color);
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = new ItemStack(this);
        DyeColors.setColor(stack, getDividerColor(level, basePosition(state, pos)));
        return stack;
    }

    private static Direction extensionDirection(BlockState state) {
        return state.getValue(FACING).getCounterClockWise();
    }

    private static BlockPos partOffset(BlockState state, int part) {
        return switch (part) {
            case 1 -> BlockPos.ZERO.above();
            case 2 -> BlockPos.ZERO.relative(extensionDirection(state));
            case 3 -> BlockPos.ZERO.relative(extensionDirection(state)).above();
            case 4 -> BlockPos.ZERO.above(2);
            case 5 -> BlockPos.ZERO.relative(extensionDirection(state)).above(2);
            default -> BlockPos.ZERO;
        };
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
        return canFit(level, pos, state.setValue(STATE, DividerState.OPEN));
    }

    private static boolean requiresPart(BlockState state, int part) {
        int height = (4 + state.getValue(CANVAS)) / 2;
        boolean extension = part == 2 || part == 3 || part == 5;
        return partOffset(state, part).getY() < height
                && (!extension || state.getValue(STATE) == DividerState.OPEN);
    }

    private boolean canFit(LevelAccessor level, BlockPos pos, BlockState state) {
        for (int part = 1; part <= 5; part++) {
            if (!requiresPart(state, part)) {
                continue;
            }
            BlockPos target = pos.offset(partOffset(state, part));
            if (level.isOutsideBuildHeight(target)
                    || !level.getWorldBorder().isWithinBounds(target)) {
                return false;
            }
            BlockState existing = level.getBlockState(target);
            if (!isPart(existing, state, part) && !existing.canBeReplaced()) {
                return false;
            }
        }
        return true;
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
            if (level.getBlockEntity(pos) instanceof RopeColorBlockEntity divider) {
                divider.setColor(DyeColors.getColor(stack));
            }
            synchronizeParts(level, pos, state);
        }
    }

    private void synchronizeParts(Level level, BlockPos pos, BlockState base) {
        int flags = Block.UPDATE_ALL | Block.UPDATE_KNOWN_SHAPE;
        int color = getDividerColor(level, pos);

        for (int part = 1; part <= 5; part++) {
            BlockPos target = pos.offset(partOffset(base, part));

            if (requiresPart(base, part)) {
                level.setBlock(target, base.setValue(PART, part), flags);
            } else if (isPart(level.getBlockState(target), base, part)) {
                level.setBlock(target, Blocks.AIR.defaultBlockState(), flags);
            }
        }

        setDividerColor(level, pos, base, color);

        for (int part = 0; part <= 5; part++) {
            BlockPos target = pos.offset(partOffset(base, part));
            level.getBlockState(target).updateNeighbourShapes(
                    level, target, Block.UPDATE_ALL);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand,
                                 BlockHitResult hit) {
        BlockPos basePos = basePosition(state, pos);
        BlockState base = level.getBlockState(basePos);

        if (!isBase(base, state.getValue(FACING))) {
            return InteractionResult.PASS;
        }

        ItemStack held = player.getItemInHand(hand);
        boolean washing = held.is(Items.WATER_BUCKET);
        if (washing || held.getItem() instanceof DyeItem) {
            if (!player.mayBuild() || !level.mayInteract(player, pos)
                    || !level.mayInteract(player, basePos)
                    || !player.mayUseItemAt(pos, hit.getDirection(), held)) {
                return InteractionResult.PASS;
            }

            int previousColor = getDividerColor(level, basePos);
            if (washing && previousColor < 0) {
                return InteractionResult.PASS;
            }

            int color = -1;
            if (!washing) {
                ItemStack previous = new ItemStack(this);
                DyeColors.setColor(previous, previousColor);
                color = DyeColors.blend(List.of(previous), (DyeItem) held.getItem());
            }

            if (!level.isClientSide && color != previousColor) {
                setDividerColor(level, basePos, base, color);
                level.playSound(null, basePos,
                        washing ? SoundEvents.BUCKET_EMPTY : SoundEvents.DYE_USE,
                        SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!washing && !player.getAbilities().instabuild) {
                    held.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (held.is(ModItems.CANVAS.get())) {
            if (base.getValue(CANVAS) == 3) {
                return InteractionResult.FAIL;
            }
            BlockState updated = base.setValue(CANVAS, base.getValue(CANVAS) + 1);
            if (!canFit(level, basePos, updated)) {
                return InteractionResult.FAIL;
            }
            if (!level.isClientSide) {
                level.setBlock(basePos, updated, Block.UPDATE_ALL | Block.UPDATE_KNOWN_SHAPE);
                synchronizeParts(level, basePos, updated);
                if (!player.getAbilities().instabuild) {
                    held.shrink(1);
                }
                level.playSound(null, basePos, SoundEvents.WOOL_PLACE,
                        SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (!player.isShiftKeyDown()) {
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
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level,
                               BlockPos pos, CollisionContext context) {
        double height = 24 + state.getValue(CANVAS) * 8;
        VoxelShape fullShape = switch (state.getValue(STATE)) {
            case CLOSED -> Block.box(0, 0, 0, 16, height, 2);
            case HALF_OPEN -> Shapes.or(
                    Block.box(0, 0, 0, 16, height, 1),
                    Block.box(15, 0, 1, 16, height, 16));
            case OPEN -> Block.box(0, 0, 0, 32, height, 1);
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
                    || !requiresPart(base, part)) {
                return Blocks.AIR.defaultBlockState();
            }
            return base.setValue(PART, part);
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
                for (int part = 1; part <= 5; part++) {
                    BlockPos target = pos.offset(partOffset(state, part));
                    if (isPart(level.getBlockState(target), state, part)) {
                        level.removeBlock(target, moving);
                    }
                }
            } else {
                BlockPos basePos = basePosition(state, pos);
                BlockState base = level.getBlockState(basePos);
                if (isBase(base, state.getValue(FACING))
                        && base.getValue(STATE) == state.getValue(STATE)
                        && base.getValue(CANVAS).equals(state.getValue(CANVAS))
                        && requiresPart(base, state.getValue(PART))) {
                    level.destroyBlock(basePos, true);
                }
            }
        }

        super.onRemove(state, level, pos, replacement, moving);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        if (state.getValue(PART) != 0) {
            return List.of();
        }
        List<ItemStack> drops = new ArrayList<>(super.getDrops(state, params));
        if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY)
                instanceof RopeColorBlockEntity divider) {
            for (ItemStack stack : drops) {
                if (stack.is(asItem())) {
                    DyeColors.setColor(stack, divider.getColor());
                }
            }
        }
        int canvas = state.getValue(CANVAS);
        if (canvas > 0) {
            drops.add(new ItemStack(ModItems.CANVAS.get(), canvas));
        }
        return drops;
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STATE, PART, CANVAS);
    }
}
