package com.tiomadre.farmersassortment.data.client;

import alabaster.crabbersdelight.common.block.CrabTrapBlock;
import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.StoolBlock;
import com.tiomadre.farmersassortment.core.block.TerracottaCookingPotBlock;
import com.tiomadre.farmersassortment.core.block.state.TerracottaCookingPotColor;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.FADynamicStools;
import com.tiomadre.farmersassortment.core.registry.FARugs;
import com.tiomadre.farmersassortment.core.registry.compat.FAxCrabbersBlocks;
import com.tiomadre.farmersassortment.core.registry.compat.FAxForagersBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import com.tiomadre.farmersassortment.core.block.ButcherBlockCabinetBlock;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.SkilletBlock;
import vectorwing.farmersdelight.common.block.state.CookingPotSupport;
import vectorwing.farmersdelight.common.block.StoveBlock;
import com.tiomadre.farmersassortment.core.block.state.StoolRugType;

import java.util.*;

public class FABlockStates extends BlockStateProvider {
    private final ExistingFileHelper fileHelper;

    public FABlockStates(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, FarmersAssortment.MOD_ID, existingFileHelper);
        this.fileHelper = existingFileHelper;
    }

    @Override
    protected void registerStatesAndModels() {
        registerCabinets();
        registerCuttingBoards();
        registerCookingPots();
        registerStoves();
        registerCrabTraps();
        registerSkillets();
        registerDiffusers();
        registerFloatingCounters();
        registerCanvasRugs();
        registerStools();
        registerRacks();
    }


    private void registerCabinets() {
        List<CabinetDefinition> cabinets = List.of(
                new CabinetDefinition(FABlocks.OAK_BUTCHER_BLOCK_CABINET, "oak", new ResourceLocation("minecraft", "block/oak_planks"), modLoc("block/oak_butcher_block_cabinet_top")),
                new CabinetDefinition(FABlocks.SPRUCE_BUTCHER_BLOCK_CABINET, "spruce", new ResourceLocation("minecraft", "block/spruce_planks"), modLoc("block/spruce_butcher_block_cabinet_top")),
                new CabinetDefinition(FABlocks.BIRCH_BUTCHER_BLOCK_CABINET, "birch", new ResourceLocation("minecraft", "block/birch_planks"), modLoc("block/birch_butcher_block_cabinet_top")),
                new CabinetDefinition(FABlocks.JUNGLE_BUTCHER_BLOCK_CABINET, "jungle", new ResourceLocation("minecraft", "block/jungle_planks"), modLoc("block/jungle_butcher_block_cabinet_top")),
                new CabinetDefinition(FABlocks.ACACIA_BUTCHER_BLOCK_CABINET, "acacia", new ResourceLocation("minecraft", "block/acacia_planks"), modLoc("block/acacia_butcher_block_cabinet_top")),
                new CabinetDefinition(FABlocks.DARK_OAK_BUTCHER_BLOCK_CABINET, "dark_oak", new ResourceLocation("minecraft", "block/dark_oak_planks"), modLoc("block/dark_oak_butcher_block_cabinet_top")),
                new CabinetDefinition(FABlocks.MANGROVE_BUTCHER_BLOCK_CABINET, "mangrove", new ResourceLocation("minecraft", "block/mangrove_planks"), modLoc("block/mangrove_butcher_block_cabinet_top")),
                new CabinetDefinition(FABlocks.CHERRY_BUTCHER_BLOCK_CABINET, "cherry", new ResourceLocation("minecraft", "block/cherry_planks"), modLoc("block/cherry_butcher_block_cabinet_top")),
                new CabinetDefinition(FABlocks.BAMBOO_BUTCHER_BLOCK_CABINET, "bamboo", new ResourceLocation("minecraft", "block/bamboo_planks"), modLoc("block/bamboo_butcher_block_cabinet_top")),
                new CabinetDefinition(FABlocks.CRIMSON_BUTCHER_BLOCK_CABINET, "crimson", new ResourceLocation("minecraft", "block/crimson_planks"), modLoc("block/crimson_butcher_block_cabinet_front_top")),
                new CabinetDefinition(FABlocks.WARPED_BUTCHER_BLOCK_CABINET, "warped", new ResourceLocation("minecraft", "block/warped_planks"), modLoc("block/warped_butcher_block_cabinet_top")),
                //Other Mods
                new CabinetDefinition(FAxCrabbersBlocks.PALM_BUTCHER_BLOCK_CABINET, "palm", fallbackTexture(new ResourceLocation("crabbersdelight", "block/palm_planks"), new ResourceLocation("minecraft", "block/oak_planks")), modLoc("block/palm_butcher_block_cabinet_top")),
                new CabinetDefinition(FAxForagersBlocks.LILAC_BUTCHER_BLOCK_CABINET, "lilac", fallbackTexture(new ResourceLocation("foragersinsight", "block/lilac_planks"), new ResourceLocation("minecraft", "block/oak_planks")), modLoc("block/lilac_butcher_block_cabinet_top"))
        );

        cabinets.forEach(cabinet -> registerButcherBlockCabinet(
                cabinet.block(),
                cabinet.woodType(),
                cabinet.bottomTexture(),
                cabinet.topTexture()
        ));
    }
    private void registerRacks() {
        List<RackDefinition> racks = List.of(
                new RackDefinition(FABlocks.OAK_RACK, "oak", false),
                new RackDefinition(FABlocks.SPRUCE_RACK, "spruce", false),
                new RackDefinition(FABlocks.BIRCH_RACK, "birch", false),
                new RackDefinition(FABlocks.JUNGLE_RACK, "jungle", false),
                new RackDefinition(FABlocks.ACACIA_RACK, "acacia", false),
                new RackDefinition(FABlocks.DARK_OAK_RACK, "dark_oak", false),
                new RackDefinition(FABlocks.MANGROVE_RACK, "mangrove", false),
                new RackDefinition(FABlocks.CHERRY_RACK, "cherry", false),
                new RackDefinition(FABlocks.BAMBOO_RACK, "bamboo", true),
                new RackDefinition(FABlocks.CRIMSON_RACK, "crimson", false),
                new RackDefinition(FABlocks.WARPED_RACK, "warped", false)
        );

        racks.forEach(rack -> registerRack(rack.block(), rack.woodType(), rack.bamboo()));
    }

    private void registerRack(RegistryObject<? extends Block> block, String woodType, boolean isBamboo) {
        String name = Objects.requireNonNull(block.getId()).getPath();
        ResourceLocation sideTexture = isBamboo
                ? new ResourceLocation("minecraft", "block/stripped_bamboo_block")
                : new ResourceLocation("minecraft", "block/stripped_" + woodType + "_log");
        ResourceLocation topTexture = isBamboo
                ? new ResourceLocation("minecraft", "block/bamboo_block_top")
                : new ResourceLocation("minecraft", "block/stripped_" + woodType + "_log_top");
        ResourceLocation particleTexture = isBamboo
                ? new ResourceLocation("minecraft", "block/stripped_bamboo_block_top")
                : topTexture;

        BlockModelBuilder model = models().getBuilder(name)
                .texture("4", sideTexture)
                .texture("5", topTexture)
                .texture("particle", particleTexture);

        BlockModelBuilder.ElementBuilder element = model.element()
                .from(0.0F, 8.0F, 5.0F)
                .to(16.0F, 12.0F, 17.0F)
                .rotation().angle(0.0F).axis(Direction.Axis.Y).origin(0.0F, 8.0F, 5.0F).end();

        if (isBamboo) {
            element.face(Direction.NORTH).uvs(0.0F, 8.0F, 16.0F, 12.0F).texture("#5").end()
                    .face(Direction.EAST).uvs(0.0F, 4.0F, 4.0F, 16.0F).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#4").end()
                    .face(Direction.SOUTH).uvs(16.0F, 8.0F, 0.0F, 12.0F).texture("#5").end()
                    .face(Direction.WEST).uvs(0.0F, 4.0F, 4.0F, 16.0F).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#4").end()
                    .face(Direction.UP).uvs(16.0F, 0.0F, 0.0F, 12.0F).texture("#4").end()
                    .face(Direction.DOWN).uvs(0.0F, 4.0F, 16.0F, 16.0F).texture("#4").end();
        } else {
            element.face(Direction.NORTH).uvs(0.0F, 6.0F, 16.0F, 10.0F).texture("#5").end()
                    .face(Direction.EAST).uvs(2.0F, 6.0F, 14.0F, 10.0F).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#5").end()
                    .face(Direction.SOUTH).uvs(16.0F, 6.0F, 0.0F, 10.0F).texture("#5").end()
                    .face(Direction.WEST).uvs(2.0F, 6.0F, 14.0F, 10.0F).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#5").end()
                    .face(Direction.UP).uvs(16.0F, 2.0F, 0.0F, 14.0F).texture("#4").end()
                    .face(Direction.DOWN).uvs(0.0F, 2.0F, 16.0F, 14.0F).texture("#4").end();
        }

        element.end();

        model.transforms()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(75.0F, 45.0F, 0.0F).translation(0.0F, 2.5F, 0.0F).scale(0.375F, 0.375F, 0.375F).end()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(75.0F, 45.0F, 0.0F).translation(0.0F, 2.5F, 0.0F).scale(0.375F, 0.375F, 0.375F).end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0.0F, 45.0F, 0.0F).scale(0.4F, 0.4F, 0.4F).end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0.0F, -135.0F, 0.0F).scale(0.4F, 0.4F, 0.4F).end()
                .transform(ItemDisplayContext.GROUND).translation(0.0F, 3.0F, 0.0F).scale(0.25F, 0.25F, 0.25F).end()
                .transform(ItemDisplayContext.GUI).rotation(30.0F, -135.0F, 0.0F).translation(1.25F, -4.0F, 0.0F).scale(0.625F, 0.625F, 0.625F).end()
                .transform(ItemDisplayContext.FIXED).scale(0.5F, 0.5F, 0.5F).end()
                .end();

        getVariantBuilder(block.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(model)
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                .build());
    }

    private record RackDefinition(RegistryObject<? extends Block> block, String woodType, boolean bamboo) {
    }



    private void registerFloatingCounters() {
        List<FloatingCounterDefinition> floatingCounters = List.of(
                new FloatingCounterDefinition(FABlocks.OAK_FLOATING_COUNTER, "oak", new ResourceLocation("minecraft", "block/oak_planks")),
                new FloatingCounterDefinition(FABlocks.SPRUCE_FLOATING_COUNTER, "spruce", new ResourceLocation("minecraft", "block/spruce_planks")),
                new FloatingCounterDefinition(FABlocks.BIRCH_FLOATING_COUNTER, "birch", new ResourceLocation("minecraft", "block/birch_planks")),
                new FloatingCounterDefinition(FABlocks.JUNGLE_FLOATING_COUNTER, "jungle", new ResourceLocation("minecraft", "block/jungle_planks")),
                new FloatingCounterDefinition(FABlocks.ACACIA_FLOATING_COUNTER, "acacia", new ResourceLocation("minecraft", "block/acacia_planks")),
                new FloatingCounterDefinition(FABlocks.DARK_OAK_FLOATING_COUNTER, "dark_oak", new ResourceLocation("minecraft", "block/dark_oak_planks")),
                new FloatingCounterDefinition(FABlocks.MANGROVE_FLOATING_COUNTER, "mangrove", new ResourceLocation("minecraft", "block/mangrove_planks")),
                new FloatingCounterDefinition(FABlocks.CHERRY_FLOATING_COUNTER, "cherry", new ResourceLocation("minecraft", "block/cherry_planks")),
                new FloatingCounterDefinition(FABlocks.BAMBOO_FLOATING_COUNTER, "bamboo", new ResourceLocation("minecraft", "block/bamboo_planks")),
                new FloatingCounterDefinition(FABlocks.CRIMSON_FLOATING_COUNTER, "crimson", new ResourceLocation("minecraft", "block/crimson_planks")),
                new FloatingCounterDefinition(FABlocks.WARPED_FLOATING_COUNTER, "warped", new ResourceLocation("minecraft", "block/warped_planks"))
        );

        floatingCounters.forEach(counter -> registerFloatingCounter(counter.block(), counter.woodType(), counter.bottomTexture()));
    }
    private void registerCanvasRugs() {
        FARugs.canvasRugs().forEach(rug -> {
            String name = Objects.requireNonNull(rug.getId()).getPath();
            ModelFile model = models().getBuilder(name)
                    .parent(new ModelFile.UncheckedModelFile(new ResourceLocation("farmersdelight", "block/canvas_rug")))
                    .renderType("minecraft:cutout")
                    .texture("body", modLoc("block/" + name))
                    .texture("extrudes", modLoc("block/" + name + "_extrudes"))
                    .texture("particle", modLoc("block/" + name));
            simpleBlock(rug.get(), model);
        });
    }

    private void registerFloatingCounter(RegistryObject<? extends Block> block, String woodType, ResourceLocation bottomTexture) {
        String name = block.getId().getPath();
        ResourceLocation topTexture = fallbackTexture(
                modLoc("block/" + woodType + "_butcher_block_cabinet_top"),
                modLoc("block/oak_butcher_block_cabinet_top")
        );
        ResourceLocation sideTexture = floatingCounterSideTexture(woodType);
        ResourceLocation frontTexture = floatingCounterFrontTexture(woodType);

        ModelFile model = models().getBuilder(name)
                .texture("2", topTexture)
                .texture("12", sideTexture)
                .texture("13", frontTexture)
                .texture("particle", topTexture)
                .texture("missing", bottomTexture)
                .element()
                .from(0.0F, 8.0F, 0.0F)
                .to(16.0F, 16.0F, 16.0F)
                .face(Direction.NORTH).uvs(0.0F, 0.0F, 16.0F, 8.0F).texture("#13").end()
                .face(Direction.EAST).uvs(0.0F, 0.0F, 16.0F, 8.0F).texture("#12").end()
                .face(Direction.SOUTH).uvs(0.0F, 0.0F, 16.0F, 8.0F).texture("#12").end()
                .face(Direction.WEST).uvs(0.0F, 0.0F, 16.0F, 8.0F).texture("#12").end()
                .face(Direction.UP).uvs(0.0F, 0.0F, 16.0F, 16.0F).texture("#2").end()
                .face(Direction.DOWN).uvs(0.0F, 0.0F, 16.0F, 16.0F).texture("#missing").end()
                .end();

        getVariantBuilder(block.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(model)
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                .build());
    }
    private record FloatingCounterDefinition(RegistryObject<? extends Block> block, String woodType, ResourceLocation bottomTexture) {
    }

    private ResourceLocation floatingCounterSideTexture(String woodType) {
        return fallbackTexture(
                modLoc("block/" + woodType + "_counter_side"),
                fallbackTexture(
                        modLoc("block/" + woodType + "_counter_front_side"),
                        fallbackTexture(
                                modLoc("block/" + woodType + "_butcher_block_cabinet_side"),
                                modLoc("block/oak_counter_side")
                        )
                )
        );
    }

    private ResourceLocation floatingCounterFrontTexture(String woodType) {
        return fallbackTexture(
                modLoc("block/" + woodType + "_counter_front"),
                fallbackTexture(
                        modLoc("block/" + woodType + "_butcher_block_cabinet_front"),
                        modLoc("block/oak_counter_front")
                )
        );
    }

    private ResourceLocation fallbackTexture(ResourceLocation primary, ResourceLocation fallback) {
        return fileHelper != null && fileHelper.exists(primary, PackType.CLIENT_RESOURCES, ".png", "textures")
                ? primary
                : fallback;
    }

    private void registerDiffusers() {
        FAxForagersBlocks.diffusers().forEach(this::registerDiffuser);
    }

    private void registerDiffuser(RegistryObject<? extends Block> diffuser) {
        String name = Objects.requireNonNull(diffuser.getId()).getPath();
        ModelFile model = models().getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(new ResourceLocation("foragersinsight", "block/diffuser")))
                .texture("1", modLoc("block/" + name + "_bottom"))
                .texture("2", modLoc("block/" + name + "_side"))
                .texture("3", modLoc("block/" + name + "_top"))
                .texture("particle", modLoc("block/" + name + "_side"));
        simpleBlock(diffuser.get(), model);
    }


    private void registerCuttingBoards() {
        FABlocks.allCuttingBoards().forEach(this::registerCuttingBoard);
    }

    private void registerCookingPots() {
        List<CookingPotDefinition> cookingPots = List.of(
                new CookingPotDefinition(FABlocks.COPPER_COOKING_POT, "copper", "block/copper_cooking_pot_bottom"),
                new CookingPotDefinition(FABlocks.GOLDEN_COOKING_POT, "golden", "block/golden_cooking_pot_bottom"),
                new CookingPotDefinition(FABlocks.ALABASTER_COOKING_POT, "alabaster", "block/alabaster_cooking_pot_bottom"),
                new CookingPotDefinition(FAxCrabbersBlocks.PEARLESCENT_COOKING_POT, "pearlescent", "block/pearlescent_cooking_pot_bottom")
        );
        cookingPots.forEach(pot -> registerCookingPot(pot.block(), pot.materialName(), modLoc(pot.bottomTexturePath())));
        registerTerracottaCookingPot();
    }

    private void registerSkillets() {
        FAxCrabbersBlocks.skillets().forEach(this::registerSkillet);
    }

    //STOVE VARIANTS
    private void registerStoves() {
        RegistryObject<? extends StoveBlock> stove = FABlocks.ALABASTER_STOVE;
        ModelFile offModel = models().orientableWithBottom(stove.getId().getPath(),
                modLoc("block/alabaster_stove_side"),
                modLoc("block/alabaster_stove_front"),
                modLoc("block/alabaster_stove_bottom"),
                modLoc("block/alabaster_stove_top"));
        ModelFile onModel = models().orientableWithBottom(stove.getId().getPath() + "_on",
                modLoc("block/alabaster_stove_side"),
                modLoc("block/alabaster_stove_on"),
                modLoc("block/alabaster_stove_bottom"),
                modLoc("block/alabaster_stove_top_on"));

        getVariantBuilder(stove.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(state.getValue(StoveBlock.LIT) ? onModel : offModel)
                .rotationY(((int) state.getValue(StoveBlock.FACING).toYRot() + 180) % 360)
                .build());
    }

    //TERRACOTTA COOKING POT
    private void registerTerracottaCookingPot() {
        RegistryObject<CookingPotBlock> block = FABlocks.TERRACOTTA_COOKING_POT;
        ResourceLocation handleTexture = modLoc("block/terracotta_cooking_pot_handle");

        Map<TerracottaCookingPotColor, ModelFile> baseModels = new EnumMap<>(TerracottaCookingPotColor.class);
        Map<TerracottaCookingPotColor, ModelFile> trayModels = new EnumMap<>(TerracottaCookingPotColor.class);
        Map<TerracottaCookingPotColor, ModelFile> handleModels = new EnumMap<>(TerracottaCookingPotColor.class);

        for (TerracottaCookingPotColor color : TerracottaCookingPotColor.values()) {
            String suffix = color.getSerializedName();
            String modelBaseName = suffix.equals(TerracottaCookingPotColor.NONE.getSerializedName()) ? "terracotta_cooking_pot" : "terracotta_cooking_pot_" + suffix;

            ResourceLocation sideTexture = color.sideTexture();
            ResourceLocation topTexture = color.topTexture();
            ResourceLocation bottomTexture = color.bottomTexture();
            ResourceLocation partsTexture = color.partsTexture();
            ResourceLocation trayTop = modLoc("block/cooking_pot_tray_top");
            ResourceLocation traySide = modLoc("block/cooking_pot_tray_side");

            baseModels.put(color, terracottaCookingPotModel(modelBaseName, sideTexture, topTexture, bottomTexture, partsTexture));
            trayModels.put(color, terracottaCookingPotTrayModel(modelBaseName + "_tray", sideTexture, topTexture, bottomTexture, partsTexture, trayTop, traySide));
            handleModels.put(color, terracottaCookingPotHandleModel(modelBaseName + "_handle", sideTexture, topTexture, bottomTexture, partsTexture, handleTexture));
        }

        getVariantBuilder(block.get()).forAllStates(state -> {
            Direction direction = state.getValue(CookingPotBlock.FACING);
            CookingPotSupport support = state.getValue(CookingPotBlock.SUPPORT);
            TerracottaCookingPotColor color = state.getValue(TerracottaCookingPotBlock.COLOR);
            ModelFile model = switch (support) {
                case TRAY -> trayModels.get(color);
                case HANDLE -> handleModels.get(color);
                default -> baseModels.get(color);
            };
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY((int) direction.toYRot())
                    .build();
        });
    }

    //CUTTING BOARD VARIANTS
    private void registerCuttingBoard(RegistryObject<CuttingBoardBlock> block) {
        String name = Objects.requireNonNull(block.getId()).getPath();
        ModelFile model = models()
                .getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("farmersdelight:block/cutting_board"))
                .texture("particle", modLoc("block/" + name))
                .texture("top", modLoc("block/" + name))
                .renderType("minecraft:cutout");
        FABlockStateHelper.horizontalFacingBlock(this, block.get(), model);
    }

    //BUTCHER BLOCK VARIANTS
    private void registerButcherBlockCabinet(RegistryObject<? extends Block> block, String woodType, ResourceLocation bottomTexture, ResourceLocation topTexture) {
        String name = block.getId().getPath();
        ModelFile closed = models().getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("minecraft:block/orientable_with_bottom"))
                .texture("front", modLoc("block/" + woodType + "_butcher_block_cabinet_front"))
                .texture("side", modLoc("block/" + woodType + "_butcher_block_cabinet_side"))
                .texture("top", topTexture)
                .texture("bottom", bottomTexture);
        ModelFile open = models().getBuilder(name + "_open")
                .parent(new ModelFile.UncheckedModelFile("minecraft:block/orientable_with_bottom"))
                .texture("front", modLoc("block/" + woodType + "_butcher_block_cabinet_front_open"))
                .texture("side", modLoc("block/" + woodType + "_butcher_block_cabinet_side"))
                .texture("top", topTexture)
                .texture("bottom", bottomTexture);

        getVariantBuilder(block.get()).forAllStates(state -> {
            Direction direction = state.getValue(ButcherBlockCabinetBlock.FACING);
            boolean openState = state.getValue(ButcherBlockCabinetBlock.OPEN);
            ModelFile model = openState ? open : closed;
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY(((int) direction.toYRot()) % 360)
                    .build();
        });
    }

    //COOKING POT VARIANT STUFF
    private void registerCookingPot(RegistryObject<CookingPotBlock> block, String materialName, ResourceLocation bottomTexture) {
        ModelFile pot = cookingPotModel(materialName, bottomTexture);
        ModelFile tray = cookingPotTrayModel(materialName, bottomTexture);
        ModelFile handle = cookingPotHandleModel(materialName, bottomTexture);

        getVariantBuilder(block.get()).forAllStates(state -> {
            Direction direction = state.getValue(CookingPotBlock.FACING);
            CookingPotSupport support = state.getValue(CookingPotBlock.SUPPORT);
            ModelFile model = switch (support) {
                case TRAY -> tray;
                case HANDLE -> handle;
                default -> pot;
            };
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY(((int) direction.toYRot()) % 360)
                    .build();
        });

    }

    private BlockModelBuilder cookingPotModel(String materialName, ResourceLocation bottomTexture) {
        BlockModelBuilder builder = baseCookingPotModel(materialName + "_cooking_pot", materialName, bottomTexture);
        addCoreCookingPotElements(builder);
        return builder;
    }

    private BlockModelBuilder cookingPotTrayModel(String materialName, ResourceLocation bottomTexture) {
        BlockModelBuilder builder = baseCookingPotModel(materialName + "_cooking_pot_tray", materialName, bottomTexture)
                .texture("tray_top", modLoc("block/" + materialName + "_cooking_pot_tray_top"))
                .texture("tray_side", modLoc("block/" + materialName + "_cooking_pot_tray_side"));
        addCoreCookingPotElements(builder);
        addCookingPotTrayElements(builder);
        return builder;
    }

    private BlockModelBuilder cookingPotHandleModel(String materialName, ResourceLocation bottomTexture) {
        BlockModelBuilder builder = baseCookingPotModel(materialName + "_cooking_pot_handle", materialName, bottomTexture)
                .texture("handle", modLoc("block/" + materialName + "_cooking_pot_handle"));
        addCoreCookingPotElements(builder);
        addCookingPotHandleElements(builder);
        return builder;
    }

    private BlockModelBuilder baseCookingPotModel(String name, String materialName, ResourceLocation bottomTexture) {
        ResourceLocation sideTexture = modLoc("block/" + materialName + "_cooking_pot_side");
        ResourceLocation topTexture = modLoc("block/" + materialName + "_cooking_pot_top");
        ResourceLocation partsTexture = modLoc("block/" + materialName + "_cooking_pot_parts");
        return baseCookingPotModel(name, sideTexture, topTexture, bottomTexture, partsTexture);
    }

    private BlockModelBuilder terracottaCookingPotModel(String name, ResourceLocation sideTexture, ResourceLocation topTexture, ResourceLocation bottomTexture, ResourceLocation partsTexture) {
        BlockModelBuilder builder = baseCookingPotModel(name, sideTexture, topTexture, bottomTexture, partsTexture);
        addCoreCookingPotElements(builder);
        return builder;
    }

    private BlockModelBuilder terracottaCookingPotTrayModel(String name, ResourceLocation sideTexture, ResourceLocation topTexture, ResourceLocation bottomTexture, ResourceLocation partsTexture, ResourceLocation trayTop, ResourceLocation traySide) {
        BlockModelBuilder builder = baseCookingPotModel(name, sideTexture, topTexture, bottomTexture, partsTexture)
                .texture("tray_top", trayTop)
                .texture("tray_side", traySide);
        addCoreCookingPotElements(builder);
        addCookingPotTrayElements(builder);
        return builder;
    }

    private BlockModelBuilder terracottaCookingPotHandleModel(String name, ResourceLocation sideTexture, ResourceLocation topTexture, ResourceLocation bottomTexture, ResourceLocation partsTexture, ResourceLocation handleTexture) {
        BlockModelBuilder builder = baseCookingPotModel(name, sideTexture, topTexture, bottomTexture, partsTexture)
                .texture("handle", handleTexture);
        addCoreCookingPotElements(builder);
        addCookingPotHandleElements(builder);
        return builder;
    }

    private BlockModelBuilder baseCookingPotModel(String name, ResourceLocation sideTexture, ResourceLocation topTexture, ResourceLocation bottomTexture, ResourceLocation partsTexture) {
        return models().getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("block/block"))
                .renderType("minecraft:cutout")
                .texture("particle", sideTexture)
                .texture("side", sideTexture)
                .texture("top", topTexture)
                .texture("parts", partsTexture)
                .texture("bottom", bottomTexture);
    }

    private void addCoreCookingPotElements(BlockModelBuilder builder) {
        builder.element()
                .from(2, 0, 2)
                .to(14, 10, 14)
                .face(Direction.NORTH).uvs(2, 6, 14, 16).texture("#side").end()
                .face(Direction.EAST).uvs(2, 6, 14, 16).texture("#side").end()
                .face(Direction.SOUTH).uvs(2, 6, 14, 16).texture("#side").end()
                .face(Direction.WEST).uvs(2, 6, 14, 16).texture("#side").end()
                .face(Direction.UP).uvs(2, 2, 14, 14).texture("#top").end()
                .face(Direction.DOWN).uvs(2, 2, 14, 14).texture("#bottom").cullface(Direction.DOWN).end()
                .end();

        BlockModelBuilder.ElementBuilder spoon = builder.element()
                .from(7, 3, 7)
                .to(9, 15, 9);
        spoon.rotation().angle(-22.5f).axis(Direction.Axis.Z).origin(8f, 3f, 8f).end();
        spoon.face(Direction.NORTH).uvs(2, 2, 0, 14).texture("#parts").end()
                .face(Direction.EAST).uvs(0, 2, 2, 14).texture("#parts").end()
                .face(Direction.SOUTH).uvs(2, 2, 0, 14).texture("#parts").end()
                .face(Direction.WEST).uvs(0, 2, 2, 14).texture("#parts").end()
                .face(Direction.UP).uvs(0, 0, 2, 2).texture("#parts").end()
                .face(Direction.DOWN).uvs(0, 0, 2, 2).texture("#parts").end()
                .end();

        builder.element()
                .from(14, 7, 5)
                .to(16, 9, 11)
                .face(Direction.NORTH).uvs(10, 0, 12, 2).texture("#parts").end()
                .face(Direction.EAST).uvs(4, 2, 10, 4).texture("#parts").end()
                .face(Direction.SOUTH).uvs(2, 0, 4, 2).texture("#parts").end()
                .face(Direction.UP).uvs(4, 0, 10, 2).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#parts").end()
                .face(Direction.DOWN).uvs(4, 2, 10, 4).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#parts").end()
                .end();

        builder.element()
                .from(0, 7, 5)
                .to(2, 9, 11)
                .face(Direction.NORTH).uvs(2, 0, 4, 2).texture("#parts").end()
                .face(Direction.SOUTH).uvs(10, 0, 12, 2).texture("#parts").end()
                .face(Direction.WEST).uvs(4, 2, 10, 4).texture("#parts").end()
                .face(Direction.UP).uvs(4, 0, 10, 2).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#parts").end()
                .face(Direction.DOWN).uvs(4, 2, 10, 4).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#parts").end()
                .end();
    }

    private void addCookingPotTrayElements(BlockModelBuilder builder) {
        builder.element()
                .from(0, -1, 0)
                .to(16, 0, 16)
                .face(Direction.NORTH).uvs(0, 0, 16, 1).texture("#tray_side").end()
                .face(Direction.EAST).uvs(0, 0, 16, 1).texture("#tray_side").end()
                .face(Direction.SOUTH).uvs(0, 0, 16, 1).texture("#tray_side").end()
                .face(Direction.WEST).uvs(0, 0, 16, 1).texture("#tray_side").end()
                .face(Direction.UP).uvs(0, 0, 16, 16).texture("#tray_top").end()
                .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#tray_top").end()
                .end();

        builder.element()
                .from(15, -16, 15)
                .to(16, -1, 16)
                .face(Direction.NORTH).uvs(0, 1, 1, 16).texture("#tray_side").end()
                .face(Direction.EAST).uvs(0, 1, 1, 16).texture("#tray_side").end()
                .face(Direction.SOUTH).uvs(15, 1, 16, 16).texture("#tray_side").end()
                .face(Direction.WEST).uvs(15, 1, 16, 16).texture("#tray_side").end()
                .face(Direction.UP).uvs(0, 0, 0.5f, 0.5f).texture("#tray_side").end()
                .face(Direction.DOWN).uvs(0, 0, 0.5f, 0.5f).texture("#tray_side").end()
                .end();

        builder.element()
                .from(0, -16, 15)
                .to(1, -1, 16)
                .face(Direction.NORTH).uvs(15, 1, 16, 16).texture("#tray_side").end()
                .face(Direction.EAST).uvs(0, 1, 1, 16).texture("#tray_side").end()
                .face(Direction.SOUTH).uvs(0, 1, 1, 16).texture("#tray_side").end()
                .face(Direction.WEST).uvs(15, 1, 16, 16).texture("#tray_side").end()
                .face(Direction.UP).uvs(0, 0, 0.5f, 0.5f).texture("#tray_side").end()
                .face(Direction.DOWN).uvs(0, 0, 0.5f, 0.5f).texture("#tray_side").end()
                .end();

        builder.element()
                .from(15, -16, 0)
                .to(16, -1, 1)
                .face(Direction.NORTH).uvs(0, 1, 1, 16).texture("#tray_side").end()
                .face(Direction.EAST).uvs(15, 1, 16, 16).texture("#tray_side").end()
                .face(Direction.SOUTH).uvs(15, 1, 16, 16).texture("#tray_side").end()
                .face(Direction.WEST).uvs(0, 1, 1, 16).texture("#tray_side").end()
                .face(Direction.UP).uvs(0, 0, 0.5f, 0.5f).texture("#tray_side").end()
                .face(Direction.DOWN).uvs(0, 0, 0.5f, 0.5f).texture("#tray_side").end()
                .end();

        builder.element()
                .from(0, -16, 0)
                .to(1, -1, 1)
                .face(Direction.NORTH).uvs(15, 1, 16, 16).texture("#tray_side").end()
                .face(Direction.EAST).uvs(15, 1, 16, 16).texture("#tray_side").end()
                .face(Direction.SOUTH).uvs(0, 1, 1, 16).texture("#tray_side").end()
                .face(Direction.WEST).uvs(0, 1, 1, 16).texture("#tray_side").end()
                .face(Direction.UP).uvs(0, 0, 0.5f, 0.5f).texture("#tray_side").end()
                .face(Direction.DOWN).uvs(0, 0, 0.5f, 0.5f).texture("#tray_side").end()
                .end();
    }

    private void addCookingPotHandleElements(BlockModelBuilder builder) {
        builder.element()
                .from(1, 8, 8)
                .to(15, 16, 8)
                .face(Direction.NORTH).uvs(1, 8, 15, 16).texture("#handle").end()
                .face(Direction.SOUTH).uvs(1, 8, 15, 16).texture("#handle").end()
                .end();

        builder.element()
                .from(15, 8, 7)
                .to(15, 16, 9)
                .face(Direction.EAST).uvs(2, 0, 0, 8).texture("#handle").end()
                .face(Direction.WEST).uvs(0, 0, 2, 8).texture("#handle").end()
                .end();

        builder.element()
                .from(1, 8, 7)
                .to(1, 16, 9)
                .face(Direction.EAST).uvs(2, 0, 0, 8).texture("#handle").end()
                .face(Direction.WEST).uvs(0, 0, 2, 8).texture("#handle").end()
                .end();

        builder.element()
                .from(1, 15.999f, 7)
                .to(15, 15.999f, 9)
                .face(Direction.UP).uvs(2, 0, 16, 2).texture("#handle").end()
                .face(Direction.DOWN).uvs(2, 0, 16, 2).texture("#handle").end()
                .end();
    }

    private void registerSkillet(RegistryObject<? extends Block> skillet) {
        String name = Objects.requireNonNull(skillet.getId()).getPath();
        ResourceLocation skilletModel = modLoc("block/" + name);
        ResourceLocation skilletTrayModel = modLoc("block/" + name + "_tray");

        ModelFile defaultModel = models().getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(new ResourceLocation("farmersdelight", "block/skillet")))
                .texture("particle", modLoc("block/" + name + "_top"))
                .texture("side", modLoc("block/" + name + "_side"))
                .texture("top", modLoc("block/" + name + "_top"))
                .texture("bottom", modLoc("block/" + name + "_bottom"));
        ModelFile trayModel = models().getBuilder(name + "_tray")
                .parent(new ModelFile.UncheckedModelFile(new ResourceLocation("farmersdelight", "block/skillet_tray")))
                .texture("particle", modLoc("block/" + name + "_side"))
                .texture("side", modLoc("block/" + name + "_side"))
                .texture("top", modLoc("block/" + name + "_top"))
                .texture("bottom", modLoc("block/" + name + "_bottom"))
                .texture("tray_top", modLoc("block/pearlescent_cooking_pot_tray_top"))
                .texture("tray_side", modLoc("block/pearlescent_cooking_pot_tray_side"));

        getVariantBuilder(skillet.get()).forAllStatesExcept(state -> {
            Direction direction = state.getValue(SkilletBlock.FACING);
            boolean support = state.getValue(SkilletBlock.SUPPORT);
            int rotation = ((int) direction.toYRot() + 180) % 360;

            return ConfiguredModel.builder()
                    .modelFile(support ? trayModel : defaultModel)
                    .rotationY(rotation)
                    .build();
        }, SkilletBlock.WATERLOGGED);
    }

    private record CabinetDefinition(RegistryObject<? extends Block> block, String woodType,
                                     ResourceLocation bottomTexture, ResourceLocation topTexture) {
    }

    private record CookingPotDefinition(RegistryObject<CookingPotBlock> block, String materialName,
                                        String bottomTexturePath) {
    }

    //CRABBERSDELIGHT CRAB TRAP VARIANTS
    private void registerCrabTraps() {
        ResourceLocation trapParent = new ResourceLocation("crabbersdelight", "block/crab_trap");
        ResourceLocation hangingParent = new ResourceLocation("crabbersdelight", "block/crab_trap_chain");
        FAxCrabbersBlocks.crabTraps().forEach(trap -> {
            String name = Objects.requireNonNull(trap.getId()).getPath();
            ModelFile trapModel = crabTrapModel(name, false, trapParent);
            ModelFile hangingModel = crabTrapModel(name, true, hangingParent);

            getVariantBuilder(trap.get()).forAllStates(state -> {
                Direction direction = state.getValue(CrabTrapBlock.FACING);
                boolean hanging = state.getValue(CrabTrapBlock.HANGING);
                ModelFile model = hanging ? hangingModel : trapModel;

                return ConfiguredModel.builder()
                        .modelFile(model)
                        .rotationY(((int) direction.toYRot()) % 360)
                        .build();
            });
        });
    }

    private ModelFile crabTrapModel(String name, boolean hanging, ResourceLocation parent) {
        String modelName = hanging ? name + "_chain" : name;
        ModelFile parentModel = new ModelFile.UncheckedModelFile(parent);

        ModelBuilder<?> builder = models().getBuilder(modelName)
                .parent(parentModel)
                .texture("0", modLoc("block/" + name + "_bottom"))
                .texture("1", modLoc("block/" + name + "_front"))
                .texture("3", modLoc("block/" + name + "_side"))
                .texture("4", modLoc("block/" + name + "_top"))
                .texture("7", modLoc("block/" + name + "_handles"))
                .texture("particle", modLoc("block/" + name + "_side"));

        if (hanging) {
            builder.texture("5", modLoc("block/" + name + "_handle_chain"));
        }

        return builder;
    }

private void registerStools() {
        List<StoolDefinition> stools = new ArrayList<>(List.of(
                new StoolDefinition(FABlocks.OAK_STOOL, "oak", new ResourceLocation("minecraft", "block/oak_log"), new ResourceLocation("minecraft", "block/stripped_oak_log")),
                new StoolDefinition(FABlocks.SPRUCE_STOOL, "spruce", new ResourceLocation("minecraft", "block/spruce_log"), new ResourceLocation("minecraft", "block/stripped_spruce_log")),
                new StoolDefinition(FABlocks.BIRCH_STOOL, "birch", new ResourceLocation("minecraft", "block/birch_log"), new ResourceLocation("minecraft", "block/stripped_birch_log")),
                new StoolDefinition(FABlocks.JUNGLE_STOOL, "jungle", new ResourceLocation("minecraft", "block/jungle_log"), new ResourceLocation("minecraft", "block/stripped_jungle_log")),
                new StoolDefinition(FABlocks.ACACIA_STOOL, "acacia", new ResourceLocation("minecraft", "block/acacia_log"), new ResourceLocation("minecraft", "block/stripped_acacia_log")),
                new StoolDefinition(FABlocks.DARK_OAK_STOOL, "dark_oak", new ResourceLocation("minecraft", "block/dark_oak_log"), new ResourceLocation("minecraft", "block/stripped_dark_oak_log")),
                new StoolDefinition(FABlocks.MANGROVE_STOOL, "mangrove", new ResourceLocation("minecraft", "block/mangrove_log"), new ResourceLocation("minecraft", "block/stripped_mangrove_log")),
                new StoolDefinition(FABlocks.CHERRY_STOOL, "cherry", new ResourceLocation("minecraft", "block/cherry_log"), new ResourceLocation("minecraft", "block/stripped_cherry_log")),
                new StoolDefinition(FABlocks.BAMBOO_STOOL, "bamboo", new ResourceLocation("minecraft", "block/bamboo_block"), new ResourceLocation("minecraft", "block/stripped_bamboo_block")),
                new StoolDefinition(FABlocks.CRIMSON_STOOL, "crimson", new ResourceLocation("minecraft", "block/crimson_stem"), new ResourceLocation("minecraft", "block/stripped_crimson_stem")),
                new StoolDefinition(FABlocks.WARPED_STOOL, "warped", new ResourceLocation("minecraft", "block/warped_stem"), new ResourceLocation("minecraft", "block/stripped_warped_stem")),

                new StoolDefinition(FAxForagersBlocks.LILAC_STOOL, "lilac", fallbackTexture(new ResourceLocation("foragersinsight", "block/lilac_log"), new ResourceLocation("minecraft", "block/oak_log")),
                        fallbackTexture(new ResourceLocation("farmersassortment", "block/stripped_lilac_log_big"), new ResourceLocation("minecraft", "block/stripped_oak_log")))));

        FADynamicStools.stoolDefinitions().forEach(definition -> stools.add(new StoolDefinition(
                definition.block(),
                Objects.requireNonNull(definition.block().getId()).getPath(),
                inferStoolLegTexture(definition.planksId()),
                fallbackTexture(definition.seatTexture(), new ResourceLocation("minecraft", "block/stripped_oak_log"))
        )));
        stools.forEach(this::registerStool);
    }
    private void registerStool(StoolDefinition stool) {
        String name = Objects.requireNonNull(stool.block().getId()).getPath();
        ModelFile baseModel = stoolModel(name, stool.legTexture(), stool.seatTexture());
        ModelFile pushedModel = stoolPushedModel(name + "_pushed", stool.legTexture(), stool.seatTexture());
        Map<StoolRugType, ModelFile> rugModels = Arrays.stream(StoolRugType.values())
                .filter(StoolRugType::hasRug)
                .collect(LinkedHashMap::new, (models, rugType) ->
                                models.put(rugType, stoolRugModel(name + "_" + rugType.getSerializedName(), stool.woodType(), stool.legTexture(), stool.seatTexture(), rugType)),
                        Map::putAll);
        Map<StoolRugType, ModelFile> pushedRugModels = Arrays.stream(StoolRugType.values())
                .filter(StoolRugType::hasRug)
                .collect(LinkedHashMap::new, (models, rugType) ->
                                models.put(rugType, stoolPushedRugModel(name + "_" + rugType.getSerializedName() + "_pushed", stool.woodType(), stool.legTexture(), stool.seatTexture(), rugType)),
                        Map::putAll);

        getVariantBuilder(stool.block().get()).forAllStates(state -> {
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            StoolRugType rugType = state.getValue(StoolBlock.RUG);
         boolean pushed = state.getValue(StoolBlock.PUSHED);
            ModelFile selectedModel;
            if (pushed) {
                selectedModel = rugType.hasRug() ? pushedRugModels.getOrDefault(rugType, pushedModel) : pushedModel;
            } else {
                selectedModel = rugType.hasRug() ? rugModels.getOrDefault(rugType, baseModel) : baseModel;
            }

            return ConfiguredModel.builder()
                    .modelFile(selectedModel)
                    .rotationY(((int) facing.toYRot() + 180) % 360)
                    .build();
        });
    }

    private BlockModelBuilder stoolModel(String name, ResourceLocation legTexture, ResourceLocation seatTexture) {
        BlockModelBuilder builder = models().getBuilder(name)
                .texture("5", seatTexture)
                .texture("7", legTexture)
                .texture("particle", seatTexture);
        addStoolCoreElements(builder, false, false, "#5", "#7");
        return builder;
    }

    private BlockModelBuilder stoolPushedModel(String name, ResourceLocation legTexture, ResourceLocation seatTexture) {
        BlockModelBuilder builder = models().getBuilder(name)
                .texture("5", seatTexture)
                .texture("7", legTexture)
                .texture("particle", seatTexture);
        addStoolCoreElements(builder, false, true, "#5", "#7");
        return builder;
    }

    private BlockModelBuilder stoolRugModel(String name, String woodType, ResourceLocation legTexture, ResourceLocation seatTexture, StoolRugType rugType) {
        boolean bamboo = "bamboo".equals(woodType);
        BlockModelBuilder builder = models().getBuilder(name)
                .renderType("minecraft:cutout")
                .texture(bamboo ? "2" : "6", seatTexture)
                .texture(bamboo ? "3" : "5", legTexture)
                .texture(bamboo ? "4" : "3", fallbackTexture(new ResourceLocation(rugType.extrudeTexturePath()), modLoc("block/white_canvas_rug_extrudes")))
                .texture(bamboo ? "5" : "4", fallbackTexture(new ResourceLocation(Objects.requireNonNull(rugType.texturePath())), modLoc("block/white_canvas_rug")))
                .texture("particle", legTexture);
        addStoolCoreElements(builder, true, false, bamboo ? "#2" : "#6", bamboo ? "#3" : "#5");
        return builder;
    }

    private BlockModelBuilder stoolPushedRugModel(String name, String woodType, ResourceLocation legTexture, ResourceLocation seatTexture, StoolRugType rugType) {
        boolean bamboo = "bamboo".equals(woodType);
        BlockModelBuilder builder = models().getBuilder(name)
                .renderType("minecraft:cutout")
                .texture(bamboo ? "2" : "6", seatTexture)
                .texture(bamboo ? "3" : "5", legTexture)
                .texture(bamboo ? "4" : "3", fallbackTexture(new ResourceLocation(rugType.extrudeTexturePath()), modLoc("block/white_canvas_rug_extrudes")))
                .texture(bamboo ? "5" : "4", fallbackTexture(new ResourceLocation(Objects.requireNonNull(rugType.texturePath())), modLoc("block/white_canvas_rug")))
                .texture("particle", legTexture);
        addStoolCoreElements(builder, true, true, bamboo ? "#2" : "#6", bamboo ? "#3" : "#5");
        return builder;
    }

    private void addStoolCoreElements(BlockModelBuilder builder, boolean rugged, boolean pushed, String strippedTexture, String logTexture) {
        int seatStart = pushed ? 0 : 6;
        int seatEnd = pushed ? 10 : 16;
        if (rugged) {
            String rugTexture = "#" + (strippedTexture.equals("#2") ? "5" : "4");
            boolean bamboo = strippedTexture.equals("#2");
            builder.element().from(0, 5, seatStart).to(16, 8, seatEnd)
                    .face(Direction.NORTH).uvs(0, 13, 16, 16).texture(rugTexture).end()
                    .face(Direction.EAST).uvs(0, 16, 10, 13).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture(rugTexture).end()
                    .face(Direction.SOUTH).uvs(0, 16, 16, 13).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture(rugTexture).end()
                    .face(Direction.WEST).uvs(0, 13, 10, 16).texture(rugTexture).end()
                    .face(Direction.UP).uvs(0, 0, 16, 10).texture(rugTexture).end()
                    .face(Direction.DOWN).uvs(0, 0, 16, 10).texture(rugTexture).end()
                    .end();
            builder.element().from(0, 4, seatStart).to(16, 5, seatEnd)
                    .face(Direction.NORTH).uvs(bamboo ? 4 : 16, bamboo ? 16 : 1, bamboo ? 3 : 0, 0).rotation(bamboo ? ModelBuilder.FaceRotation.CLOCKWISE_90 : ModelBuilder.FaceRotation.UPSIDE_DOWN).texture(strippedTexture).end()
                    .face(Direction.EAST).uvs(4, 10, 3, 0).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(strippedTexture).end()
                    .face(Direction.SOUTH).uvs(4, 16, 3, 0).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(strippedTexture).end()
                    .face(Direction.WEST).uvs(3, bamboo ? 6 : 0, 4, bamboo ? 16 : 10).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(strippedTexture).end()
                    .face(Direction.UP).uvs(0, 0, 16, 10).texture(strippedTexture).end()
                    .face(Direction.DOWN).uvs(0, 0, 16, 10).texture(strippedTexture).end()
                    .end();
        } else {
            builder.element().from(0, 4, seatStart).to(16, 8, seatEnd)
                    .face(Direction.NORTH).uvs(0, 0, 4, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(strippedTexture).end()
                    .face(Direction.EAST).uvs(0, 6, 4, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(strippedTexture).end()
                    .face(Direction.SOUTH).uvs(0, 0, 4, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(strippedTexture).end()
                    .face(Direction.WEST).uvs(0, 6, 4, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(strippedTexture).end()
                    .face(Direction.UP).uvs(0, 0, 16, 10).texture(strippedTexture).end()
                    .face(Direction.DOWN).uvs(0, 6, 16, 16).texture(strippedTexture).end()
                    .end();
        }

        builder.element().from(0, 3, seatStart).to(16, 4, seatEnd)
                .face(Direction.NORTH).uvs(0, 7, 16, 8).texture(logTexture).end()
                .face(Direction.EAST).uvs(13, 3, 3, 4).texture(logTexture).end()
                .face(Direction.SOUTH).uvs(0, 3, 16, 4).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture(logTexture).end()
                .face(Direction.WEST).uvs(3, 3, 13, 4).texture(logTexture).end()
                .face(Direction.UP).uvs(0, 0, 16, 10).texture(logTexture).end()
                .face(Direction.DOWN).uvs(3, 0, 13, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(logTexture).end()
                .end();

        if (rugged) {
            addStoolRugExtrudes(builder, strippedTexture.equals("#2") ? "#4" : "#3", 5, seatStart);
        }

        addStoolLeg(builder, 0, 1, seatEnd - 2, 2, 3, seatEnd, strippedTexture, logTexture);
        addStoolLeg(builder, 0, 1, seatStart, 2, 3, seatStart + 2, strippedTexture, logTexture);
        addStoolLeg(builder, 14, 1, seatStart, 16, 3, seatStart + 2, strippedTexture, logTexture);
        addStoolLeg(builder, 14, 1, seatEnd - 2, 16, 3, seatEnd, strippedTexture, logTexture);
    }

    private void addStoolLeg(BlockModelBuilder builder, int fromX, int fromY, int fromZ, int toX, int toY, int toZ,
                             String strippedTexture, String logTexture) {
        builder.element().from(fromX, fromY, fromZ).to(toX, toY, toZ)
                .face(Direction.NORTH).uvs(0, 0, 2, 2).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(strippedTexture).end()
                .face(Direction.EAST).uvs(0, 0, 2, 2).texture(strippedTexture).end()
                .face(Direction.SOUTH).uvs(2, 0, 0, 2).texture(strippedTexture).end()
                .face(Direction.WEST).uvs(0, 0, 2, 2).texture(strippedTexture).end()
                .face(Direction.UP).uvs(0, 0, 2, 2).texture(strippedTexture).end()
                .face(Direction.DOWN).uvs(0, 0, 2, 2).texture(strippedTexture).end()
                .end();

        builder.element().from(fromX, fromY - 1, fromZ).to(toX, fromY, toZ)
                .face(Direction.NORTH).uvs(0, 1, 2, 2).texture(logTexture).end()
                .face(Direction.EAST).uvs(0, 1, 2, 2).texture(logTexture).end()
                .face(Direction.SOUTH).uvs(0, 1, 2, 2).texture(logTexture).end()
                .face(Direction.WEST).uvs(0, 1, 2, 2).texture(logTexture).end()
                .face(Direction.UP).uvs(0, 0, 2, 2).texture(logTexture).end()
                .face(Direction.DOWN).uvs(6, 9, 8, 11).texture(logTexture).end()
                .end();
    }

    private void addStoolRugExtrudes(BlockModelBuilder builder, String extrudeTexture, int y, int seatStart) {
        int seatEnd = seatStart + 10;

        builder.element().from(0, y, seatStart - 2).to(16, y, seatStart).rotation().angle(-45).axis(Direction.Axis.X).origin(0, y, seatStart).end()
                .face(Direction.NORTH).uvs(0, 2, 16, 2).texture(extrudeTexture).end()
                .face(Direction.EAST).uvs(0, 2, 2, 2).texture(extrudeTexture).end()
                .face(Direction.SOUTH).uvs(0, 2, 16, 2).texture(extrudeTexture).end()
                .face(Direction.WEST).uvs(0, 2, 2, 2).texture(extrudeTexture).end()
                .face(Direction.UP).uvs(0, 2, 16, 0).texture(extrudeTexture).end()
                .face(Direction.DOWN).uvs(0, 0, 16, 2).texture(extrudeTexture).end()
                .end();

        builder.element().from(-2, y, seatStart).to(0, y, seatEnd).rotation().angle(45).axis(Direction.Axis.Z).origin(0, y, 0).end()
                .face(Direction.NORTH).uvs(2, 8, 12, 10).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(extrudeTexture).end()
                .face(Direction.EAST).uvs(2, 8, 12, 10).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(extrudeTexture).end()
                .face(Direction.SOUTH).uvs(2, 8, 12, 10).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(extrudeTexture).end()
                .face(Direction.WEST).uvs(2, 8, 12, 10).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(extrudeTexture).end()
                .face(Direction.UP).uvs(2, 8, 12, 10).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(extrudeTexture).end()
                .face(Direction.DOWN).uvs(12, 8, 2, 10).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(extrudeTexture).end()
                .end();

        builder.element().from(16, y, seatStart).to(18, y, seatEnd).rotation().angle(-45).axis(Direction.Axis.Z).origin(16, y, 0).end()
                .face(Direction.NORTH).uvs(2, 10, 12, 8).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(extrudeTexture).end()
                .face(Direction.EAST).uvs(2, 10, 12, 8).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(extrudeTexture).end()
                .face(Direction.SOUTH).uvs(2, 10, 12, 8).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(extrudeTexture).end()
                .face(Direction.WEST).uvs(2, 10, 12, 8).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(extrudeTexture).end()
                .face(Direction.UP).uvs(12, 10, 2, 8).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(extrudeTexture).end()
                .face(Direction.DOWN).uvs(2, 10, 12, 8).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture(extrudeTexture).end()
                .end();

        builder.element().from(0, y, seatEnd).to(16, y, seatEnd + 2).rotation().angle(45).axis(Direction.Axis.X).origin(0, y, seatEnd).end()
                .face(Direction.NORTH).uvs(0, 0, 16, 2).texture(extrudeTexture).end()
                .face(Direction.EAST).uvs(0, 0, 16, 2).texture(extrudeTexture).end()
                .face(Direction.SOUTH).uvs(0, 0, 16, 2).texture(extrudeTexture).end()
                .face(Direction.WEST).uvs(0, 0, 16, 2).texture(extrudeTexture).end()
                .face(Direction.UP).uvs(0, 8, 16, 10).texture(extrudeTexture).end()
                .face(Direction.DOWN).uvs(0, 2, 16, 0).texture(extrudeTexture).end()
                .end();
    }

    private ResourceLocation inferStoolLegTexture(ResourceLocation planksId) {
        String namespace = planksId.getNamespace();
        String path = planksId.getPath();
        String woodType = path.endsWith("_planks") ? path.substring(0, path.length() - "_planks".length()) : path;

        ResourceLocation log = new ResourceLocation(namespace, "block/" + woodType + "_log");
        ResourceLocation stem = new ResourceLocation(namespace, "block/" + woodType + "_stem");
        ResourceLocation wood = new ResourceLocation(namespace, "block/" + woodType + "_wood");
        ResourceLocation strippedBlock = new ResourceLocation(namespace, "block/" + woodType + "_block");
        ResourceLocation planks = new ResourceLocation(namespace, "block/" + path);

        return fallbackTexture(log, fallbackTexture(stem, fallbackTexture(wood, fallbackTexture(strippedBlock, fallbackTexture(planks, new ResourceLocation("minecraft", "block/oak_log"))))));
    }

    private record StoolDefinition(RegistryObject<StoolBlock> block, String woodType,
                                   ResourceLocation legTexture, ResourceLocation seatTexture) {
    }

}