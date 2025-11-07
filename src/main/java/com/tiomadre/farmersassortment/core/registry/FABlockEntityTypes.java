package com.tiomadre.farmersassortment.core.registry;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.entity.ButcherBlockCabinetBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class FABlockEntityTypes {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FarmersAssortment.MOD_ID);

    public static final RegistryObject<BlockEntityType<ButcherBlockCabinetBlockEntity>> BUTCHER_BLOCK_CABINET =
            BLOCK_ENTITY_TYPES.register("butcher_block_cabinet", () -> BlockEntityType.Builder.of(
                    ButcherBlockCabinetBlockEntity::new,
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
            ).build(null));

    private FABlockEntityTypes() {
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
