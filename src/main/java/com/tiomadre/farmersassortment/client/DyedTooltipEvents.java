package com.tiomadre.farmersassortment.client;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.registry.FABlocks;
import com.tiomadre.farmersassortment.core.util.DyeColors;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import java.util.List;

@Mod.EventBusSubscriber(modid = FarmersAssortment.MOD_ID, value = Dist.CLIENT)
public final class DyedTooltipEvents {
    private DyedTooltipEvents() {}

    @SubscribeEvent
    public static void addColorTooltip(ItemTooltipEvent event) {
        var stack = event.getItemStack();
        if (!stack.is(ModBlocks.ROPE_FENCE.get().asItem())
                && !stack.is(ModBlocks.ROPE_FENCE_GATE.get().asItem())
                && !stack.is(FABlocks.DIVIDER.get().asItem())) return;
        int color = DyeColors.getColor(stack);
        if (color < 0) return;
        Component name = Component.translatable("tooltip.farmersassortment.mixed_color");
        for (DyeColor dyeColor : DyeColor.values()) {
            DyeItem dye = DyeItem.byColor(dyeColor);
            if (DyeColors.blend(List.of(), dye) == color) {
                name = Component.translatable(dye.getDescriptionId());
                break;
            }
        }
        event.getToolTip().add(1, Component.translatable("tooltip.farmersassortment.rope_color",
                name)
                .withStyle(style -> style.withColor(TextColor.fromRgb(color))));
    }
}
