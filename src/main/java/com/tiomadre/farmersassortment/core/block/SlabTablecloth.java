package com.tiomadre.farmersassortment.core.block;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.state.StoolRugType;
import com.tiomadre.farmersassortment.core.block.state.SlabRugState;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = FarmersAssortment.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class SlabTablecloth {

    private SlabTablecloth() {
    }

    @SubscribeEvent
    public static void onRightClickSlab(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide) {
            return;
        }

        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        if (!(state.getBlock() instanceof SlabBlock) || state.getValue(SlabBlock.TYPE) != SlabType.TOP) {
            return;
        }

        Player player = event.getEntity();
        ItemStack heldStack = player.getItemInHand(event.getHand());
        if (heldStack.isEmpty()) {
            return;
        }

        StoolRugType currentRug = state.getValue(SlabRugState.RUG);
        StoolRugType heldRug = rugTypeFromItem(heldStack.getItem());

        if (heldRug != null && heldRug != currentRug) {
            if (currentRug.hasRug()) {
                dropRug(level, pos, currentRug);
            }
            level.setBlock(pos, state.setValue(SlabRugState.RUG, heldRug), 3);
            level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!player.getAbilities().instabuild) {
                heldStack.shrink(1);
            }
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
        }

        if (heldStack.getItem() instanceof ShearsItem && currentRug.hasRug()) {
            level.setBlock(pos, state.setValue(SlabRugState.RUG, StoolRugType.NONE), 3);
            dropRug(level, pos, currentRug);
            heldStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(event.getHand()));
            level.playSound(null, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    @SubscribeEvent
    public static void onBreakSlab(BlockEvent.BreakEvent event) {
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof SlabBlock)) {
            return;
        }

        StoolRugType rugType = state.getValue(SlabRugState.RUG);
        if (rugType.hasRug() && !level.isClientSide) {
            dropRug(level, pos, rugType);
        }
    }

    private static void dropRug(Level level, BlockPos pos, StoolRugType rugType) {
        Item item = rugType.rugItem();
        if (item != null && level instanceof ServerLevel serverLevel) {
            ItemEntity drop = new ItemEntity(serverLevel, pos.getX() + 0.5D, pos.getY() + 0.8D, pos.getZ() + 0.5D, new ItemStack(item));
            serverLevel.addFreshEntity(drop);
        }
    }

    @Nullable
    private static StoolRugType rugTypeFromItem(Item item) {
        if (!(item instanceof BlockItem blockItem)) {
            return null;
        }

        String path = blockItem.getBlock().builtInRegistryHolder().key().location().getPath();
        if (path.equals("canvas_rug")) {
            return StoolRugType.CANVAS;
        }
        for (StoolRugType value : StoolRugType.values()) {
            if (path.equals(value.getSerializedName() + "_canvas_rug")) {
                return value;
            }
        }
        return null;
    }
}