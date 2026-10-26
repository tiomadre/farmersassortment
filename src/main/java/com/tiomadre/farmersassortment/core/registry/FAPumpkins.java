package com.tiomadre.farmersassortment.core.registry;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.PumpkinBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.FoodValues;
import net.minecraft.world.entity.EquipmentSlot;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public final class FAPumpkins {
    private static final SlabBlock SLAB = (SlabBlock) Blocks.OAK_SLAB;
    public static final Variant YELLOW = new Variant("yellow", 0, 8);
    public static final Variant WHITE = new Variant("white", 2, 12);
    public static final Variant RED = new Variant("red", 2, 16);
    public static final List<Variant> VARIANTS = List.of(YELLOW, WHITE, RED);

    private FAPumpkins() {
    }

    public static void init() {
    }

    public static boolean owns(Block block) {
        return VARIANTS.stream().anyMatch(variant ->
                block == variant.pumpkin.get()
                        || block == variant.carved.get()
                        || block == variant.lantern.get()
                        || block == variant.stem.get()
                        || block == variant.attachedStem.get());
    }

    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> VARIANTS.forEach(variant -> {
            ComposterBlock.COMPOSTABLES.put(variant.seeds.get(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(variant.slice.get(), 0.5F);
            ComposterBlock.COMPOSTABLES.put(variant.pumpkin.get().asItem(), 0.65F);
        }));
    }

    public static final class Variant {
        public final String color;
        public final RegistryObject<VariantPumpkinBlock> pumpkin;
        public final RegistryObject<VariantCarvedPumpkinBlock> carved;
        public final RegistryObject<VariantCarvedPumpkinBlock> lantern;
        public final RegistryObject<StemBlock> stem;
        public final RegistryObject<AttachedStemBlock> attachedStem;
        public final RegistryObject<Item> seeds;
        public final RegistryObject<Item> slice;

        private Variant(String color, int inset, int height) {
            this.color = color;
            String name = color + "_pumpkin";
            VoxelShape shape = Block.box(inset, 0, inset, 16 - inset, height, 16 - inset);

            pumpkin = FABlocks.BLOCKS.createBlock(name,
                    () -> color.equals("yellow")
                            ? new YellowPumpkinBlock(this, shape, BlockBehaviour.Properties.copy(Blocks.PUMPKIN).noOcclusion())
                            : new VariantPumpkinBlock(this, shape,
                            BlockBehaviour.Properties.copy(Blocks.PUMPKIN).noOcclusion()),
                    new Item.Properties());

            carved = FABlocks.BLOCKS.createBlock("carved_" + name,
                    () -> color.equals("yellow")
                            ? new YellowCarvedPumpkinBlock(shape, BlockBehaviour.Properties.copy(Blocks.CARVED_PUMPKIN).noOcclusion())
                            : new VariantCarvedPumpkinBlock(shape,
                            BlockBehaviour.Properties.copy(Blocks.CARVED_PUMPKIN).noOcclusion()),
                    new Item.Properties());

            lantern = FABlocks.BLOCKS.createBlock(color + "_jack_o_lantern",
                    () -> color.equals("yellow")
                            ? new YellowCarvedPumpkinBlock(shape, BlockBehaviour.Properties.copy(Blocks.JACK_O_LANTERN).noOcclusion().lightLevel(state -> 15))
                            : new VariantCarvedPumpkinBlock(shape,
                            BlockBehaviour.Properties.copy(Blocks.JACK_O_LANTERN)
                                    .noOcclusion().lightLevel(state -> 15)),
                    new Item.Properties());

            stem = FABlocks.BLOCKS.getDeferredRegister().register(name + "_stem",
                    () -> new StemBlock(pumpkin.get(), this::seedItem,
                            BlockBehaviour.Properties.copy(Blocks.PUMPKIN_STEM)));

            attachedStem = FABlocks.BLOCKS.getDeferredRegister().register("attached_" + name + "_stem",
                    () -> new AttachedStemBlock(pumpkin.get(), this::seedItem,
                            BlockBehaviour.Properties.copy(Blocks.ATTACHED_PUMPKIN_STEM)));

            seeds = FAItems.ITEMS.createItem(name + "_seeds",
                    () -> new ItemNameBlockItem(stem.get(), new Item.Properties()));

            slice = FAItems.ITEMS.createItem(name + "_slice",
                    () -> new Item(new Item.Properties().food(FoodValues.PUMPKIN_SLICE)));
        }

        private Item seedItem() {
            return seeds.get();
        }
    }

    public static class VariantPumpkinBlock extends PumpkinBlock {
        private final Variant variant;
        private final VoxelShape shape;

        private VariantPumpkinBlock(Variant variant, VoxelShape shape,
                                    BlockBehaviour.Properties properties) {
            super(properties);
            this.variant = variant;
            this.shape = shape;
        }

        @Override
        public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
                                   CollisionContext context) {
            return shape;
        }

        @Override
        public StemBlock getStem() {
            return variant.stem.get();
        }

        @Override
        public AttachedStemBlock getAttachedStem() {
            return variant.attachedStem.get();
        }

        @Override
        public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                     Player player, InteractionHand hand, BlockHitResult hit) {
            ItemStack tool = player.getItemInHand(hand);
            if (!tool.canPerformAction(ToolActions.SHEARS_CARVE)) {
                return InteractionResult.PASS;
            }

            if (!level.isClientSide) {
                Direction facing = hit.getDirection().getAxis() == Direction.Axis.Y
                        ? player.getDirection().getOpposite()
                        : hit.getDirection();

                level.playSound(null, pos, SoundEvents.PUMPKIN_CARVE,
                        SoundSource.BLOCKS, 1.0F, 1.0F);

                BlockState carvedState = variant.carved.get().defaultBlockState()
                        .setValue(CarvedPumpkinBlock.FACING, facing);
                if (state.hasProperty(BlockStateProperties.SLAB_TYPE)) {
                    carvedState = carvedState
                            .setValue(BlockStateProperties.SLAB_TYPE, state.getValue(BlockStateProperties.SLAB_TYPE))
                            .setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED));
                }
                level.setBlock(pos, carvedState, 11);

                ItemEntity seeds = new ItemEntity(level,
                        pos.getX() + 0.5D + facing.getStepX() * 0.65D,
                        pos.getY() + 0.1D,
                        pos.getZ() + 0.5D + facing.getStepZ() * 0.65D,
                        new ItemStack(variant.seeds.get(),
                                state.hasProperty(BlockStateProperties.SLAB_TYPE)
                                        && state.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.DOUBLE ? 8 : 4));

                seeds.setDeltaMovement(
                        0.05D * facing.getStepX() + level.random.nextDouble() * 0.02D,
                        0.05D,
                        0.05D * facing.getStepZ() + level.random.nextDouble() * 0.02D);

                level.addFreshEntity(seeds);
                player.awardStat(Stats.ITEM_USED.get(tool.getItem()));
                tool.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(hand));
                level.gameEvent(player, GameEvent.SHEAR, pos);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    public static class VariantCarvedPumpkinBlock extends CarvedPumpkinBlock {
        private final VoxelShape shape;
        private BlockPattern snowPattern;
        private BlockPattern ironPattern;

        private VariantCarvedPumpkinBlock(VoxelShape shape,
                                          BlockBehaviour.Properties properties) {
            super(properties);
            this.shape = shape;
        }

        @Override
        public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
                                   CollisionContext context) {
            return shape;
        }

        @Override
        public void onPlace(BlockState state, Level level, BlockPos pos,
                            BlockState oldState, boolean moving) {
            if (level.isClientSide || oldState.is(this)) {
                return;
            }

            if (snowPattern == null) {
                snowPattern = BlockPatternBuilder.start()
                        .aisle("^", "#", "#")
                        .where('^', BlockInWorld.hasState(
                                head -> head.getBlock() instanceof VariantCarvedPumpkinBlock))
                        .where('#', BlockInWorld.hasState(
                                snow -> snow.is(Blocks.SNOW_BLOCK)))
                        .build();

                ironPattern = BlockPatternBuilder.start()
                        .aisle("~^~", "###", "~#~")
                        .where('^', BlockInWorld.hasState(
                                head -> head.getBlock() instanceof VariantCarvedPumpkinBlock))
                        .where('#', BlockInWorld.hasState(
                                iron -> iron.is(Blocks.IRON_BLOCK)))
                        .where('~', block -> block.getState().isAir())
                        .build();
            }

            BlockPattern.BlockPatternMatch snow = snowPattern.find(level, pos);
            if (snow != null) {
                SnowGolem golem = EntityType.SNOW_GOLEM.create(level);
                if (golem != null) {
                    golem.setItemSlot(EquipmentSlot.HEAD,
                            new ItemStack(snow.getBlock(0, 0, 0).getState().getBlock()));
                    golem.setDropChance(EquipmentSlot.HEAD, 0.0F);
                    spawn(level, snow, golem, snow.getBlock(0, 2, 0).getPos());
                }
                return;
            }

            BlockPattern.BlockPatternMatch iron = ironPattern.find(level, pos);
            if (iron != null) {
                IronGolem golem = EntityType.IRON_GOLEM.create(level);
                if (golem != null) {
                    golem.setPlayerCreated(true);
                    spawn(level, iron, golem, iron.getBlock(1, 2, 0).getPos());
                }
            }
        }

        private static void spawn(Level level, BlockPattern.BlockPatternMatch match,
                                  Mob golem, BlockPos pos) {
            CarvedPumpkinBlock.clearPatternBlocks(level, match);
            golem.moveTo(pos.getX() + 0.5D, pos.getY() + 0.05D,
                    pos.getZ() + 0.5D, 0.0F, 0.0F);
            level.addFreshEntity(golem);

            for (ServerPlayer player : level.getEntitiesOfClass(
                    ServerPlayer.class, golem.getBoundingBox().inflate(5.0D))) {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(player, golem);
            }

            CarvedPumpkinBlock.updatePatternBlocks(level, match);
        }
    }

    public static final class YellowPumpkinBlock extends VariantPumpkinBlock implements SimpleWaterloggedBlock {
        private YellowPumpkinBlock(Variant variant, VoxelShape shape, BlockBehaviour.Properties properties) {
            super(variant, shape, properties);
            registerDefaultState(defaultBlockState().setValue(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM)
                    .setValue(BlockStateProperties.WATERLOGGED, false));
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            super.createBlockStateDefinition(builder);
            builder.add(BlockStateProperties.SLAB_TYPE, BlockStateProperties.WATERLOGGED);
        }

        @Override
        public BlockState getStateForPlacement(BlockPlaceContext context) {
            BlockState state = super.getStateForPlacement(context);
            BlockState existing = context.getLevel().getBlockState(context.getClickedPos());
            if (existing.is(this)) {
                return existing.setValue(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE)
                        .setValue(BlockStateProperties.WATERLOGGED, false);
            }
            BlockState slab = SLAB.getStateForPlacement(context);
            return state.setValue(BlockStateProperties.SLAB_TYPE, slab.getValue(BlockStateProperties.SLAB_TYPE))
                    .setValue(BlockStateProperties.WATERLOGGED, slab.getValue(BlockStateProperties.WATERLOGGED));
        }

        @Override
        public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
            SlabType type = state.getValue(BlockStateProperties.SLAB_TYPE);
            if (type == SlabType.DOUBLE || !context.getItemInHand().is(asItem())) {
                return false;
            }
            if (!context.replacingClickedOnBlock()) {
                return true;
            }
            boolean upper = context.getClickLocation().y - context.getClickedPos().getY() > 0.5D;
            Direction face = context.getClickedFace();
            return type == SlabType.BOTTOM
                    ? face == Direction.UP || face.getAxis().isHorizontal() && upper
                    : face == Direction.DOWN || face.getAxis().isHorizontal() && !upper;
        }

        @Override
        public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
                                   CollisionContext context) {
            return SLAB.getShape(state, level, pos, context);
        }

        @Override
        public FluidState getFluidState(BlockState state) {
            return state.getValue(BlockStateProperties.WATERLOGGED)
                    ? Fluids.WATER.getSource(false) : super.getFluidState(state);
        }

        @Override
        public boolean canPlaceLiquid(BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
            return state.getValue(BlockStateProperties.SLAB_TYPE) != SlabType.DOUBLE
                    && SimpleWaterloggedBlock.super.canPlaceLiquid(level, pos, state, fluid);
        }

        @Override
        public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluid) {
            return state.getValue(BlockStateProperties.SLAB_TYPE) != SlabType.DOUBLE
                    && SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluid);
        }

        @Override
        public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                      LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
            if (state.getValue(BlockStateProperties.WATERLOGGED)) {
                level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
            }
            return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        }

        @Override
        public boolean useShapeForLightOcclusion(BlockState state) {
            return state.getValue(BlockStateProperties.SLAB_TYPE) != SlabType.DOUBLE;
        }
    }

    public static final class YellowCarvedPumpkinBlock extends VariantCarvedPumpkinBlock implements SimpleWaterloggedBlock {
        private YellowCarvedPumpkinBlock(VoxelShape shape, BlockBehaviour.Properties properties) {
            super(shape, properties);
            registerDefaultState(defaultBlockState().setValue(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM)
                    .setValue(BlockStateProperties.WATERLOGGED, false));
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            super.createBlockStateDefinition(builder);
            builder.add(BlockStateProperties.SLAB_TYPE, BlockStateProperties.WATERLOGGED);
        }

        @Override
        public BlockState getStateForPlacement(BlockPlaceContext context) {
            BlockState state = super.getStateForPlacement(context);
            BlockState existing = context.getLevel().getBlockState(context.getClickedPos());
            if (existing.is(this)) {
                return existing.setValue(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE)
                        .setValue(BlockStateProperties.WATERLOGGED, false);
            }
            BlockState slab = SLAB.getStateForPlacement(context);
            return state.setValue(BlockStateProperties.SLAB_TYPE, slab.getValue(BlockStateProperties.SLAB_TYPE))
                    .setValue(BlockStateProperties.WATERLOGGED, slab.getValue(BlockStateProperties.WATERLOGGED));
        }

        @Override
        public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
            SlabType type = state.getValue(BlockStateProperties.SLAB_TYPE);
            if (type == SlabType.DOUBLE || !context.getItemInHand().is(asItem())) {
                return false;
            }
            if (!context.replacingClickedOnBlock()) {
                return true;
            }
            boolean upper = context.getClickLocation().y - context.getClickedPos().getY() > 0.5D;
            Direction face = context.getClickedFace();
            return type == SlabType.BOTTOM
                    ? face == Direction.UP || face.getAxis().isHorizontal() && upper
                    : face == Direction.DOWN || face.getAxis().isHorizontal() && !upper;
        }

        @Override
        public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
                                   CollisionContext context) {
            return SLAB.getShape(state, level, pos, context);
        }

        @Override
        public FluidState getFluidState(BlockState state) {
            return state.getValue(BlockStateProperties.WATERLOGGED)
                    ? Fluids.WATER.getSource(false) : super.getFluidState(state);
        }

        @Override
        public boolean canPlaceLiquid(BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
            return state.getValue(BlockStateProperties.SLAB_TYPE) != SlabType.DOUBLE
                    && SimpleWaterloggedBlock.super.canPlaceLiquid(level, pos, state, fluid);
        }

        @Override
        public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluid) {
            return state.getValue(BlockStateProperties.SLAB_TYPE) != SlabType.DOUBLE
                    && SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluid);
        }

        @Override
        public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                      LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
            if (state.getValue(BlockStateProperties.WATERLOGGED)) {
                level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
            }
            return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        }

        @Override
        public boolean useShapeForLightOcclusion(BlockState state) {
            return state.getValue(BlockStateProperties.SLAB_TYPE) != SlabType.DOUBLE;
        }
    }
}
