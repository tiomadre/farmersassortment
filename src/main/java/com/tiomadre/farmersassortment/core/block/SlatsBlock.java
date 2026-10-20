package com.tiomadre.farmersassortment.core.block;

import com.tiomadre.farmersassortment.core.block.entity.SlatsBlockEntity;
import com.tiomadre.farmersassortment.core.item.SlatsItem;
import com.tiomadre.farmersassortment.core.registry.FADynamicFurniture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import java.util.ArrayList;
import java.util.List;

public class SlatsBlock extends Block implements EntityBlock, BonemealableBlock, SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Mode> MODE = EnumProperty.create("mode", Mode.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final SoundType SLATS_SOUND = new SoundType(1.0F, 0.8F,
            SoundEvents.WOOD_BREAK, SoundEvents.BAMBOO_WOOD_STEP, SoundEvents.WOOD_PLACE,
            SoundEvents.WOOD_HIT, SoundEvents.WOOD_FALL);

    private static final VoxelShape FLOOR_SHAPE = box(0, 0, 0, 16, 2, 16);
    private static final VoxelShape SIDE_SHAPE = box(0, 14, 0, 16, 16, 16);
    private static final VoxelShape NORTH_SHAPE = box(0, 0, 14, 16, 16, 16);
    private static final VoxelShape EAST_SHAPE = box(0, 0, 0, 2, 16, 16);
    private static final VoxelShape SOUTH_SHAPE = box(0, 0, 0, 16, 16, 2);
    private static final VoxelShape WEST_SHAPE = box(14, 0, 0, 16, 16, 16);
    private final BlockState wood;

    public SlatsBlock(BlockState wood) {
        super(BlockBehaviour.Properties.copy(wood.getBlock()).sound(SLATS_SOUND)
                .noOcclusion().dynamicShape().randomTicks()
                .isRedstoneConductor((state, level, pos) -> embedded(state))
                .isSuffocating((state, level, pos) -> embedded(state))
                .isViewBlocking((state, level, pos) -> embedded(state)));
        this.wood = wood;
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH)
                .setValue(MODE, Mode.FLOOR).setValue(WATERLOGGED, false));
    }

    public static boolean embedded(BlockState state) {
        return state.getBlock() instanceof SlatsBlock && state.getValue(MODE) == Mode.EMBEDDED;
    }

    public static boolean canEmbed(BlockState state) {
        return state.is(FADynamicFurniture.SOILS) && !(state.getBlock() instanceof SlatsBlock)
                && !state.hasBlockEntity() && state.getFluidState().isEmpty();
    }

    public static BlockState soilAt(BlockGetter level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof SlatsBlockEntity slats
                ? slats.getSoil() : Blocks.DIRT.defaultBlockState();
    }

    public static BlockState unwrap(BlockGetter level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return embedded(state) ? soilAt(level, pos) : state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState placement = defaultBlockState().setValue(WATERLOGGED,
                context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
        Direction facing = context.getHorizontalDirection();

        if (context instanceof SlatsItem.EmbeddingContext) {
            return placement.setValue(FACING, facing).setValue(MODE, Mode.EMBEDDED);
        }

        Direction face = context.getClickedFace();

        if (face == Direction.UP) {
            return placement.setValue(FACING, facing);
        }

        if (!face.getAxis().isHorizontal()) {
            return null;
        }

        boolean sneaking = context.getPlayer() != null && context.getPlayer().isShiftKeyDown();
        return placement.setValue(FACING, face).setValue(MODE, sneaking ? Mode.SIDE : Mode.VERTICAL);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, MODE, WATERLOGGED);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SlatsBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(MODE)) {
            case FLOOR -> FLOOR_SHAPE;
            case SIDE, SUSPENDED -> SIDE_SHAPE;
            case EMBEDDED -> Shapes.block();
            case VERTICAL -> switch (state.getValue(FACING)) {
                case NORTH -> NORTH_SHAPE;
                case EAST -> EAST_SHAPE;
                case SOUTH -> SOUTH_SHAPE;
                case WEST -> WEST_SHAPE;
                default -> Shapes.empty();
            };
        };
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return true;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (embedded(state)) {
            level.scheduleTick(pos, this, 2);
            return state;
        }
        return state;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moved) {
        super.onPlace(state, level, pos, oldState, moved);
        if (embedded(state)) {
            level.scheduleTick(pos, this, 2);
        }
    }

    @Override
    public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return state.getValue(MODE) == Mode.VERTICAL;
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        return state.getValue(MODE) == Mode.VERTICAL ? SoundType.LADDER : SLATS_SOUND;
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        return embedded(state) ? soilAt(level, pos).getDestroyProgress(player, level, pos)
                : super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        return embedded(state) ? soilAt(level, pos).canHarvestBlock(level, pos, player)
                : super.canHarvestBlock(state, level, pos, player);
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return (embedded(state) ? soilAt(level, pos) : wood).getExplosionResistance(level, pos, explosion);
    }

    @Override
    public float getFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        return (embedded(state) ? soilAt(level, pos) : wood).getFriction(level, pos, entity);
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return (embedded(state) ? soilAt(level, pos) : wood).getFlammability(level, pos, direction);
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return (embedded(state) ? soilAt(level, pos) : wood).getFireSpreadSpeed(level, pos, direction);
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter level, BlockPos pos,
                                   Direction facing, IPlantable plantable) {
        if (!embedded(state)) {
            return false;
        }
        BlockPos soilPos = embedded(level.getBlockState(pos)) ? pos : pos.below();
        return soilAt(level, soilPos).canSustainPlant(level, soilPos, facing, plantable);
    }

    @Override
    public boolean isFertile(BlockState state, BlockGetter level, BlockPos pos) {
        return embedded(state) && soilAt(level, pos).isFertile(level, pos);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = new ArrayList<>(super.getDrops(state, builder));
        BlockEntity entity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (embedded(state) && entity instanceof SlatsBlockEntity slats) {
            try {
                drops.addAll(slats.getSoil().getDrops(builder.withParameter(LootContextParams.BLOCK_STATE, slats.getSoil())));
            } finally {
                builder.withParameter(LootContextParams.BLOCK_STATE, state);
            }
        }
        return drops;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!embedded(state) || !(level.getBlockEntity(pos) instanceof SlatsBlockEntity slats)) {
            return;
        }
        BlockState soil = slats.getSoil();
        if (soil.getBlock() instanceof FallingBlock && FallingBlock.isFree(level.getBlockState(pos.below()))) {
            FallingBlockEntity.fall(level, pos, soil);
            level.setBlock(pos, state.setValue(MODE, Mode.SUSPENDED), 3);
            return;
        }
        BlockState updated = soil.updateShape(Direction.UP, unwrap(level, pos.above()), level, pos, pos.above());
        if (updated != soil) {
            if (!canEmbed(updated)) {
                level.setBlock(pos, updated, 3);
                popResource(level, pos, new ItemStack(this));
                return;
            }
            slats.setSoil(updated);
            soil = updated;
        }
        soil.tick(level, pos, random);
        retainSlats(state, level, pos);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return embedded(state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (embedded(state) && level.getBlockEntity(pos) instanceof SlatsBlockEntity slats) {
            BlockState soil = slats.getSoil();
            if (soil.isRandomlyTicking()) {
                soil.randomTick(level, pos, random);
                retainSlats(state, level, pos);
            }
        }
    }

    private void retainSlats(BlockState state, Level level, BlockPos pos) {
        BlockState replacement = level.getBlockState(pos);
        if (replacement.is(this)) {
            return;
        }
        if (canEmbed(replacement)) {
            level.setBlock(pos, state, 2);
            if (level.getBlockEntity(pos) instanceof SlatsBlockEntity slats) {
                slats.setSoil(replacement);
            }
        } else if (!replacement.isAir()) {
            popResource(level, pos, new ItemStack(this));
        }
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float distance) {
        if (embedded(state) && level.getBlockEntity(pos) instanceof SlatsBlockEntity slats) {
            BlockState soil = slats.getSoil();
            soil.getBlock().fallOn(level, soil, pos, entity, distance);
            retainSlats(state, level, pos);
        } else {
            super.fallOn(level, state, pos, entity, distance);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (!embedded(state) || !(level.getBlockEntity(pos) instanceof SlatsBlockEntity slats)) {
            return InteractionResult.PASS;
        }
        ItemStack tool = player.getItemInHand(hand);
        if (tool.canPerformAction(ToolActions.SHOVEL_FLATTEN)) {
            if (!level.isClientSide) {
                BlockState soil = slats.getSoil();
                if (!level.setBlock(pos, soil, 3)) {
                    return InteractionResult.FAIL;
                }
                popResource(level, pos, new ItemStack(this));
                level.playSound(null, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, soil));
                tool.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(hand));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        ToolAction action = ToolActions.HOE_TILL;
        if (!tool.canPerformAction(action)) {
            return InteractionResult.PASS;
        }
        BlockState modified = slats.getSoil().getToolModifiedState(
                new UseOnContext(player, hand, hit), action, level.isClientSide);
        if (modified == null) {
            return InteractionResult.PASS;
        }
        level.playSound(player, pos, SoundEvents.HOE_TILL,
                SoundSource.BLOCKS, 1.0F, 1.0F);
        if (!level.isClientSide) {
            if (canEmbed(modified)) {
                slats.setSoil(modified);
            } else {
                level.setBlock(pos, modified, 3);
                popResource(level, pos, new ItemStack(this));
            }
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
            tool.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(hand));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean client) {
        BlockState soil = soilAt(level, pos);
        return embedded(state) && soil.getBlock() instanceof BonemealableBlock block
                && block.isValidBonemealTarget(level, pos, soil, client);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        BlockState soil = soilAt(level, pos);
        return embedded(state) && soil.getBlock() instanceof BonemealableBlock block
                && block.isBonemealSuccess(level, random, pos, soil);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        if (embedded(state) && level.getBlockEntity(pos) instanceof SlatsBlockEntity slats) {
            BlockState soil = slats.getSoil();
            if (soil.getBlock() instanceof BonemealableBlock block) {
                block.performBonemeal(level, random, pos, soil);
                retainSlats(state, level, pos);
            }
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return rotate(state, mirror.getRotation(state.getValue(FACING)));
    }

    public enum Mode implements StringRepresentable {
        FLOOR("floor"), SIDE("side"), VERTICAL("vertical"), EMBEDDED("embedded"), SUSPENDED("suspended");
        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
