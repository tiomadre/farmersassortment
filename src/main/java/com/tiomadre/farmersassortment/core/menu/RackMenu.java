package com.tiomadre.farmersassortment.core.menu;

import com.tiomadre.farmersassortment.core.block.entity.RackBlockEntity;
import com.tiomadre.farmersassortment.core.registry.FAMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class RackMenu extends AbstractContainerMenu {
    private static final int RACK_SLOT_COUNT = 4;
    private final Container rackContainer;

    public RackMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(RACK_SLOT_COUNT));
    }

    private RackMenu(int id, Inventory playerInventory, Container rackContainer) {
        super(FAMenuTypes.RACK.get(), id);
        this.rackContainer = rackContainer;
        checkContainerSize(rackContainer, RACK_SLOT_COUNT);
        rackContainer.startOpen(playerInventory.player);

        int topY = 20;
        for (int i = 0; i < RACK_SLOT_COUNT; i++) {
            addSlot(new Slot(rackContainer, i, 52 + i * 18, topY));
        }

        int inventoryY = 84;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, inventoryY + row * 18));
            }
        }

        int hotbarY = 142;
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, hotbarY));
        }
    }

    public static RackMenu createServerMenu(int id, Inventory playerInventory, RackBlockEntity blockEntity) {
        return new RackMenu(id, playerInventory, blockEntity);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();
            if (index < RACK_SLOT_COUNT) {
                if (!this.moveItemStackTo(stackInSlot, RACK_SLOT_COUNT, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stackInSlot, 0, RACK_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return rackContainer.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.rackContainer.stopOpen(player);
    }
}