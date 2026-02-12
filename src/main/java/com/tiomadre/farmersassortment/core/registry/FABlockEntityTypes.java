package com.tiomadre.farmersassortment.core.registry;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.entity.ButcherBlockCabinetBlockEntity;
import com.tiomadre.farmersassortment.core.registry.compat.FAxCrabbersBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public final class FABlockEntityTypes {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FarmersAssortment.MOD_ID);

    public static final RegistryObject<BlockEntityType<ButcherBlockCabinetBlockEntity>> BUTCHER_BLOCK_CABINET =
            BLOCK_ENTITY_TYPES.register("butcher_block_cabinet", () -> BlockEntityType.Builder.of(
                    ButcherBlockCabinetBlockEntity::new,
                    butcherBlockCabinetBlocks()
            ).build(null));

    private FABlockEntityTypes() {
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }

    private static Block[] butcherBlockCabinetBlocks() {
        List<Block> blocks = new ArrayList<>(List.of(
                FABlocks.OAK_BUTCHER_BLOCK_CABINET.get(),
                FABlocks.SPRUCE_BUTCHER_BLOCK_CABINET.get(),
                FABlocks.BIRCH_BUTCHER_BLOCK_CABINET.get(),
                FABlocks.JUNGLE_BUTCHER_BLOCK_CABINET.get(),
                FABlocks.ACACIA_BUTCHER_BLOCK_CABINET.get(),
                FABlocks.DARK_OAK_BUTCHER_BLOCK_CABINET.get(),
                FABlocks.MANGROVE_BUTCHER_BLOCK_CABINET.get(),
                FABlocks.CHERRY_BUTCHER_BLOCK_CABINET.get(),
                FABlocks.BAMBOO_BUTCHER_BLOCK_CABINET.get(),
                FABlocks.CRIMSON_BUTCHER_BLOCK_CABINET.get(),
                FABlocks.WARPED_BUTCHER_BLOCK_CABINET.get()
        ));

        if (ModList.get().isLoaded("crabbersdelight")) {
            blocks.add(FAxCrabbersBlocks.PALM_BUTCHER_BLOCK_CABINET.get());
        }

        return blocks.toArray(new Block[0]);
    }
}