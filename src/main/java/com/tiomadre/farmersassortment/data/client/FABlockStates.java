package com.tiomadre.farmersassortment.data.client;

import alabaster.crabbersdelight.common.block.CrabTrapBlock;
import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.TerracottaCookingPotBlock;
import com.tiomadre.farmersassortment.core.block.state.TerracottaCookingPotColor;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.registry.compat.FAxCrabbersBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import com.tiomadre.farmersassortment.core.block.ButcherBlockCabinetBlock;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.SkilletBlock;
import vectorwing.farmersdelight.common.block.state.CookingPotSupport;
import vectorwing.farmersdelight.common.block.StoveBlock;

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
    }
    //CABINET VARIANTS
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
                new CabinetDefinition(FAxCrabbersBlocks.PALM_BUTCHER_BLOCK_CABINET, "palm", fallbackTexture(
                        new ResourceLocation("crabbersdelight", "block/palm_planks"),
                        new ResourceLocation("minecraft", "block/oak_planks")
                ), modLoc("block/palm_butcher_block_cabinet_top"))
        );

        cabinets.forEach(cabinet -> registerButcherBlockCabinet(
                cabinet.block(),
                cabinet.woodType(),
                cabinet.bottomTexture(),
                cabinet.topTexture()
        ));
    }
    private ResourceLocation fallbackTexture(ResourceLocation primary, ResourceLocation fallback) {
        return fileHelper != null && fileHelper.exists(primary, PackType.CLIENT_RESOURCES, ".png", "textures")
                ? primary
                : fallback;
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
            int rotation = (int) direction.toYRot();

            return ConfiguredModel.builder()
                    .modelFile(support ? trayModel : defaultModel)
                    .rotationY(rotation)
                    .build();
        }, SkilletBlock.WATERLOGGED);
    }

    private record CabinetDefinition(RegistryObject<? extends Block> block, String woodType, ResourceLocation bottomTexture, ResourceLocation topTexture) {
    }

    private record CookingPotDefinition(RegistryObject<CookingPotBlock> block, String materialName, String bottomTexturePath) {
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

        return models().getBuilder(modelName)
                .parent(parentModel)
                .texture("bottom", modLoc("block/" + name + "_bottom"))
                .texture("front", modLoc("block/" + name + "_front"))
                .texture("handles", modLoc("block/" + name + "_handles"))
                .texture("handle", modLoc("block/" + name + "_handles"))
                .texture("handle_chain", modLoc("block/" + name + "_handle_chain"))
                .texture("side", modLoc("block/" + name + "_side"))
                .texture("top", modLoc("block/" + name + "_top"))
                .texture("particle", modLoc("block/" + name + "_side"));
    }
}

