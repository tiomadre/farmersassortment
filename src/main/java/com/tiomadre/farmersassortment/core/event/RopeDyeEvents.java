package com.tiomadre.farmersassortment.core.event;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.entity.RopeColorBlockEntity;
import com.tiomadre.farmersassortment.core.util.DyeColors;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import java.util.List;

@Mod.EventBusSubscriber(modid = FarmersAssortment.MOD_ID)
public final class RopeDyeEvents {
    private RopeDyeEvents() {}

    @SubscribeEvent
    public static void dyeRope(PlayerInteractEvent.RightClickBlock event) {
        ItemStack held = event.getItemStack();
        boolean washing = held.is(Items.WATER_BUCKET);
        if (!washing && !(held.getItem() instanceof DyeItem)) return;
        var level = event.getLevel();
        var pos = event.getPos();
        var state = level.getBlockState(pos);
        if (!state.is(ModBlocks.ROPE_FENCE.get()) && !state.is(ModBlocks.ROPE_FENCE_GATE.get())) return;
        var player = event.getEntity();
        if (!player.mayBuild() || !level.mayInteract(player, pos)
                || event.getFace() == null || !player.mayUseItemAt(pos, event.getFace(), held)) return;
        if (!(level.getBlockEntity(pos) instanceof RopeColorBlockEntity rope)) return;
        if (washing && rope.getColor() < 0) return;

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
        if (level.isClientSide) return;
        if (washing) {
            rope.setColor(-1);
            level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            return;
        }
        DyeItem dye = (DyeItem) held.getItem();
        ItemStack previous = new ItemStack(state.getBlock());
        DyeColors.setColor(previous, rope.getColor());
        int color = DyeColors.blend(List.of(previous), dye);
        if (color == rope.getColor()) return;
        rope.setColor(color);
        level.playSound(null, pos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (!player.getAbilities().instabuild) held.shrink(1);
    }
}
