package com.tiomadre.farmersassortment.client.screen;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.menu.FloatingDrawerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FloatingDrawerScreen extends AbstractContainerScreen<FloatingDrawerMenu> {
    private final ResourceLocation texture;

    public FloatingDrawerScreen(FloatingDrawerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 166;
        inventoryLabelY = imageHeight - 94;

        texture = new ResourceLocation(FarmersAssortment.MOD_ID,
                "textures/gui/container/"
                        + (menu.isDoubleCounter() ? "double_counter.png" : "counter.png"));
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(texture, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}