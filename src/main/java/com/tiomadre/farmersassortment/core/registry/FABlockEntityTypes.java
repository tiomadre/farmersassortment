package com.tiomadre.farmersassortment.core.registry;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.entity.ButcherBlockCabinetBlockEntity;
import net.minecraft.world.level.block.Block;
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
                    butcherBlockCabinetBlocks()
            ).build(null));

    private FABlockEntityTypes() {
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }

    private static Block[] butcherBlockCabinetBlocks() {
        return FABlocks.allButcherBlockCabinets()
                .map(RegistryObject::get)
                .toArray(Block[]::new);
    }
}