package com.tiomadre.farmersassortment.core.registry;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.recipe.GourdCuttingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = FarmersAssortment.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class FAGourds {
    public static final String VARIANT_TAG = "GourdVariant";
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FarmersAssortment.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FarmersAssortment.MOD_ID);
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FarmersAssortment.MOD_ID);
    public static final TagKey<Item> KNIVES = TagKey.create(net.minecraft.core.registries.Registries.ITEM, new ResourceLocation("forge", "tools/knives"));
    public static final RegistryObject<GourdCropBlock> CROP = BLOCKS.register("gourd_crop", () -> new GourdCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noLootTable()));
    public static final RegistryObject<GourdPileBlock> PILE = BLOCKS.register("gourd", () -> new GourdPileBlock(BlockBehaviour.Properties.copy(Blocks.PUMPKIN).strength(0.5F).noOcclusion().noLootTable()));
    public static final RegistryObject<Item> GOURD = ITEMS.register("gourd", () -> new GourdItem(PILE.get(), false));
    public static final RegistryObject<Item> SEEDS = ITEMS.register("gourd_seeds", () -> new GourdItem(CROP.get(), true));
    public static final RegistryObject<Item> GOURD_SHELL = ITEMS.register("gourd_shell", () -> new Item(new Item.Properties()));
    public static final RegistryObject<RecipeSerializer<CuttingBoardRecipe>> CUTTING_SERIALIZER = SERIALIZERS.register("gourd_cutting", GourdCuttingRecipe.Serializer::new);

    private FAGourds() {
    }

    public static void init(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        SERIALIZERS.register(bus);
    }

    public static boolean owns(Block block) {
        return block == CROP.get() || block == PILE.get();
    }

    public static Variant getVariant(ItemStack stack) {
        if (stack.hasTag()) {
            String name = stack.getTag().getString(VARIANT_TAG);
            for (Variant variant : Variant.values()) {
                if (variant.id.equals(name)) {
                    return variant;
                }
            }
        }
        return Variant.JARRAHDALE;
    }

    public static ItemStack stack(ItemLike item, int count, Variant variant) {
        ItemStack stack = new ItemStack(item, count);
        stack.getOrCreateTag().putString(VARIANT_TAG, variant.id);
        return stack;
    }

    public static boolean isKnife(@Nullable ItemStack stack) {
        return stack != null && !stack.isEmpty() && (stack.is(KNIVES) || stack.canPerformAction(ToolAction.get("knife_dig")));
    }

    public static List<ItemStack> chopped(Variant variant) {
        return List.of(new ItemStack(GOURD_SHELL.get(), 2), stack(SEEDS.get(), 1, variant));
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ComposterBlock.COMPOSTABLES.put(SEEDS.get(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(GOURD.get(), 0.65F);
        });
    }

    @SubscribeEvent
    public static void creativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == FATab.FA_TAB.get()) {
            for (Variant variant : Variant.values()) {
                event.accept(stack(GOURD.get(), 1, variant));
                event.accept(stack(SEEDS.get(), 1, variant));
            }
            event.accept(GOURD_SHELL.get());
        }
    }

    public enum Variant implements StringRepresentable {
        JARRAHDALE("jarrahdale", 0xB1CAC3),
        WAX("wax", 0xECD9B7),
        MIDNIGHT("midnight", 0x565C60),
        DELICATA("delicata", 0xEDC825),
        HONEYNUT("honeynut", 0xF9A26A);

        public final String id;
        public final int color;

        Variant(String id, int color) {
            this.id = id;
            this.color = color;
        }

        @Override
        public String getSerializedName() {
            return id;
        }
    }

    public static final class GourdItem extends BlockItem {
        private final boolean seed;

        public GourdItem(Block block, boolean seed) {
            super(block, new Item.Properties());
            this.seed = seed;
        }

        @Override
        public String getDescriptionId() {
            return seed ? "item.farmersassortment.gourd_seeds" : "block.farmersassortment.gourd";
        }

        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
            super.appendHoverText(stack, level, tooltip, flag);
            Variant variant = getVariant(stack);
            tooltip.add(Component.translatable("variant.farmersassortment.gourd." + variant.id)
                    .withStyle(style -> style.withColor(variant.color)));
        }
    }

    public static final class GourdCropBlock extends CropBlock {
        public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 8);
        public static final EnumProperty<Variant> VARIANT = EnumProperty.create("variant", Variant.class);

        public GourdCropBlock(BlockBehaviour.Properties properties) {
            super(properties);
            registerDefaultState(defaultBlockState().setValue(AGE, 0).setValue(VARIANT, Variant.JARRAHDALE));
        }

        @Override
        protected IntegerProperty getAgeProperty() {
            return AGE;
        }

        @Override
        public int getMaxAge() {
            return 8;
        }

        @Override
        protected ItemLike getBaseSeedId() {
            return SEEDS.get();
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(AGE, VARIANT);
        }

        @Override
        public BlockState getStateForPlacement(BlockPlaceContext context) {
            return defaultBlockState().setValue(VARIANT, getVariant(context.getItemInHand()));
        }

        @Override
        public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
            return Block.box(0, 0, 0, 16, Math.min(16, (getAge(state) + 1) * 2), 16);
        }

        @Override
        public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
            if (!level.isAreaLoaded(pos, 1) || level.getRawBrightness(pos, 0) < 9 || isMaxAge(state)) {
                return;
            }
            float speed = getGrowthSpeed(this, level, pos);
            boolean grow = random.nextInt((int) (25.0F / speed) + 1) == 0;
            if (ForgeHooks.onCropsGrowPre(level, pos, state, grow)) {
                level.setBlock(pos, state.setValue(AGE, getAge(state) + 1), 2);
                ForgeHooks.onCropsGrowPost(level, pos, state);
            }
        }

        @Override
        public void growCrops(Level level, BlockPos pos, BlockState state) {
            int age = Math.min(getMaxAge(), getAge(state) + getBonemealAgeIncrease(level));
            level.setBlock(pos, state.setValue(AGE, age), 2);
        }

        @Override
        public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            if (!isMaxAge(state)) {
                return InteractionResult.PASS;
            }
            if (!level.isClientSide) {
                Variant variant = state.getValue(VARIANT);
                ItemStack tool = player.getItemInHand(hand);
                boolean knife = isKnife(tool);
                if (!level.setBlock(pos, state.setValue(AGE, 6), 3)) {
                    return InteractionResult.FAIL;
                }
                if (knife) {
                    chopped(variant).forEach(drop -> popResource(level, pos, drop));
                    if (!player.getAbilities().instabuild) {
                        tool.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(hand));
                    }
                } else {
                    popResource(level, pos, stack(GOURD.get(), 2, variant));
                }
                level.playSound(null, pos, knife ? SoundEvents.PUMPKIN_CARVE : SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
                        SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
            Variant variant = state.getValue(VARIANT);
            if (isMaxAge(state) && isKnife(params.getOptionalParameter(LootContextParams.TOOL))) {
                return chopped(variant);
            }
            List<ItemStack> drops = new ArrayList<>();
            drops.add(stack(SEEDS.get(), 1, variant));
            if (isMaxAge(state)) {
                drops.add(stack(GOURD.get(), 2, variant));
            }
            return drops;
        }

        @Override
        public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
            return stack(SEEDS.get(), 1, state.getValue(VARIANT));
        }
    }

    public static final class GourdPileBlock extends Block {
        public static final int EMPTY = 5;
        public static final List<IntegerProperty> SLOTS = List.of(
                IntegerProperty.create("gourd_1", 0, EMPTY),
                IntegerProperty.create("gourd_2", 0, EMPTY),
                IntegerProperty.create("gourd_3", 0, EMPTY),
                IntegerProperty.create("gourd_4", 0, EMPTY));
        private static final VoxelShape[] SHAPES = {
                Shapes.empty(),
                Block.box(6, 0, 6, 10, 6, 10),
                Shapes.or(Block.box(5, 0, 8, 9, 7, 12), Block.box(8, 0, 4, 12, 4, 8)),
                Shapes.or(Block.box(5, 0, 9, 9, 7, 13), Block.box(4, 0, 3, 8, 5, 7), Block.box(8, 0, 5, 12, 4, 9)),
                Shapes.or(Block.box(2, 0, 7, 6, 9, 11), Block.box(6, 0, 9, 10, 7, 13),
                        Block.box(5, 0, 3, 9, 5, 7), Block.box(9, 0, 5, 13, 4, 9))
        };

        public GourdPileBlock(BlockBehaviour.Properties properties) {
            super(properties);
            BlockState state = defaultBlockState();
            for (IntegerProperty slot : SLOTS) {
                state = state.setValue(slot, EMPTY);
            }
            registerDefaultState(state.setValue(SLOTS.get(0), 0));
        }

        public static int count(BlockState state) {
            int count = 0;
            for (IntegerProperty slot : SLOTS) {
                if (state.getValue(slot) == EMPTY) {
                    break;
                }
                count++;
            }
            return count;
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            for (IntegerProperty slot : SLOTS) {
                builder.add(slot);
            }
        }

        @Override
        public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
            return !context.isSecondaryUseActive() && context.getItemInHand().is(GOURD.get()) && count(state) < 4;
        }

        @Nullable
        @Override
        public BlockState getStateForPlacement(BlockPlaceContext context) {
            BlockState existing = context.getLevel().getBlockState(context.getClickedPos());
            int variant = getVariant(context.getItemInHand()).ordinal();
            if (existing.is(this)) {
                int count = count(existing);
                return count < 4 ? existing.setValue(SLOTS.get(count), variant) : null;
            }
            return defaultBlockState().setValue(SLOTS.get(0), variant);
        }

        @Override
        public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
            BlockPos below = pos.below();
            return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
        }

        @Override
        public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                      LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
            if (direction == Direction.DOWN && !state.canSurvive(level, pos)) {
                return Blocks.AIR.defaultBlockState();
            }
            return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        }

        @Override
        public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
            return SHAPES[count(state)];
        }

        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
            List<ItemStack> drops = new ArrayList<>();
            for (IntegerProperty slot : SLOTS) {
                int variant = state.getValue(slot);
                if (variant != EMPTY) {
                    drops.add(stack(GOURD.get(), 1, Variant.values()[variant]));
                }
            }
            return drops;
        }

        @Override
        public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
            int variant = state.getValue(SLOTS.get(0));
            return stack(GOURD.get(), 1, Variant.values()[variant == EMPTY ? 0 : variant]);
        }
    }
}
