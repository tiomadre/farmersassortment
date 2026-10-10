package com.tiomadre.farmersassortment.core.block;

import com.tiomadre.farmersassortment.core.block.entity.FloatingDrawerBlockEntity;
import com.tiomadre.farmersassortment.core.menu.FloatingDrawerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.ArrayList;
import java.util.List;

public class FloatingDrawerBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public static final BooleanProperty DOUBLE = BooleanProperty.create("double");
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty HAS_SLAB = BooleanProperty.create("has_slab");

    private static final VoxelShape SINGLE_SHAPE =
            Block.box(0, 8, 0, 16, 16, 16);

    public FloatingDrawerBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(DOUBLE, false)
                .setValue(OPEN, false)
                .setValue(HAS_SLAB, false));
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Player player = context.getPlayer();
        boolean hasSlab = isBottomSlab(
                context.getLevel().getBlockState(context.getClickedPos()));

        boolean doubled = !hasSlab
                && player != null
                && player.isShiftKeyDown()
                && context.getItemInHand().getCount() >= 2;

        BlockState state = defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(DOUBLE, doubled)
                .setValue(HAS_SLAB, hasSlab);

        return state.canSurvive(context.getLevel(), context.getClickedPos())
                ? state
                : null;
    }
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                            LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (state.getValue(DOUBLE)
                && placer instanceof Player player
                && player.isShiftKeyDown()
                && stack.getCount() >= 2
                && !player.getAbilities().instabuild) {
            stack.shrink(1);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(DOUBLE) || state.getValue(HAS_SLAB)) {
            return true;
        }

        BlockPos abovePos = pos.above();
        if (level.getBlockState(abovePos)
                .isFaceSturdy(level, abovePos, Direction.DOWN)) {
            return true;
        }

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos supportPos = pos.relative(direction);
            BlockState support = level.getBlockState(supportPos);

            if (support.getBlock() instanceof FloatingDrawerBlock) {
                if (support.getValue(DOUBLE)) {
                    return true;
                }
                continue;
            }

            if (support.is(BlockTags.WALLS)
                    || support.is(BlockTags.FENCES)
                    || support.isFaceSturdy(
                    level, supportPos, direction.getOpposite())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction,
                                  BlockState neighborState, LevelAccessor level,
                                  BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            level.scheduleTick(pos, this, 1);
        }

        return super.updateShape(
                state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state,
                                           BlockGetter level, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, DOUBLE, OPEN, HAS_SLAB);
    }
    public static boolean isBottomSlab(BlockState state) {
        return (state.getBlock() instanceof SlabBlock || state.is(BlockTags.SLABS))
                && state.hasProperty(BlockStateProperties.SLAB_TYPE)
                && state.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.BOTTOM
                && !state.hasBlockEntity();
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FloatingDrawerBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);

        if (!state.getValue(DOUBLE) && !state.getValue(HAS_SLAB) && held.is(asItem())) {
            if (!level.isClientSide) {
                if (level.getBlockEntity(pos) instanceof FloatingDrawerBlockEntity counter) {
                    for (ServerPlayer viewer : ((ServerLevel) level).players()) {
                        if (viewer.containerMenu instanceof
                                FloatingDrawerMenu menu
                                && menu.getContainer() == counter) {
                            viewer.closeContainer();
                        }
                    }
                }

                BlockState stackedState = level.getBlockState(pos)
                        .setValue(DOUBLE, true)
                        .setValue(OPEN, false);

                if (!level.setBlock(pos, stackedState, 3)) {
                    return InteractionResult.FAIL;
                }

                if (!player.getAbilities().instabuild) {
                    held.shrink(1);
                }

                SoundType sound = stackedState.getSoundType(level, pos, player);
                level.playSound(
                        null,
                        pos,
                        sound.getPlaceSound(),
                        SoundSource.BLOCKS,
                        (sound.getVolume() + 1.0F) / 2.0F,
                        sound.getPitch() * 0.8F
                );
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (!level.isClientSide
                && player instanceof ServerPlayer serverPlayer
                && level.getBlockEntity(pos) instanceof FloatingDrawerBlockEntity counter) {
            NetworkHooks.openScreen(serverPlayer, counter,
                    buffer -> buffer.writeBoolean(state.getValue(DOUBLE)));
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }


    @Override
    public void tick(BlockState state, ServerLevel level,
                     BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
            return;
        }

        if (level.getBlockEntity(pos) instanceof FloatingDrawerBlockEntity counter) {
            counter.recheckOpen();
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = new ArrayList<>(super.getDrops(state, builder));

        if (state.getValue(HAS_SLAB)
                && builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY)
                instanceof FloatingDrawerBlockEntity drawer) {
            BlockState slab = drawer.getSlabState();

            if (!slab.isAir()) {
                if (slab.requiresCorrectToolForDrops()
                        && builder.getOptionalParameter(LootContextParams.THIS_ENTITY)
                        instanceof Player player
                        && !player.hasCorrectToolForDrops(slab)) {
                    return drops;
                }

                LootParams.Builder slabBuilder = new LootParams.Builder(builder.getLevel())
                        .withParameter(LootContextParams.ORIGIN,
                                builder.getParameter(LootContextParams.ORIGIN))
                        .withParameter(LootContextParams.TOOL,
                                builder.getParameter(LootContextParams.TOOL))
                        .withOptionalParameter(LootContextParams.THIS_ENTITY,
                                builder.getOptionalParameter(LootContextParams.THIS_ENTITY))
                        .withOptionalParameter(LootContextParams.EXPLOSION_RADIUS,
                                builder.getOptionalParameter(LootContextParams.EXPLOSION_RADIUS));

                drops.addAll(slab.getDrops(slabBuilder));
            }
        }

        return drops;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos,
                         BlockState newState, boolean moving) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide
                    && level.getBlockEntity(pos) instanceof FloatingDrawerBlockEntity counter) {
                Containers.dropContents(level, pos, counter);
                level.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, level, pos, newState, moving);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level,
                               BlockPos pos, CollisionContext context) {
        return state.getValue(DOUBLE) || state.getValue(HAS_SLAB)
                ? Shapes.block()
                : SINGLE_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level,
                                        BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}
