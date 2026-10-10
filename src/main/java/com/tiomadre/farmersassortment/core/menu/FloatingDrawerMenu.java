package com.tiomadre.farmersassortment.core.menu;

import com.tiomadre.farmersassortment.core.registry.FAMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FloatingDrawerMenu extends AbstractContainerMenu {
    private final Container container;
    private final boolean doubleCounter;
    private final int counterSlots;

    public FloatingDrawerMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        this(id, inventory, data.readBoolean());
    }

    private FloatingDrawerMenu(int id, Inventory inventory, boolean doubleCounter) {
        this(id, inventory, new SimpleContainer(doubleCounter ? 8 : 4), doubleCounter);
    }

    public FloatingDrawerMenu(int id, Inventory inventory,
                              Container container, boolean doubleCounter) {
        super(FAMenuTypes.FLOATING_COUNTER.get(), id);
        this.container = container;
        this.doubleCounter = doubleCounter;
        this.counterSlots = doubleCounter ? 8 : 4;

        checkContainerSize(container, counterSlots);
        container.startOpen(inventory.player);

        int columns = doubleCounter ? 4 : 2;
        int startX = doubleCounter ? 52 : 70;

        for (int row = 0; row < 2; row++) {
            for (int column = 0; column < columns; column++) {
                addSlot(new Slot(container, row * columns + column,
                        startX + column * 18, 20 + row * 18));
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, 9 + row * 9 + column,
                        8 + column * 18, 84 + row * 18));
            }
        }

        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(inventory, column, 8 + column * 18, 142));
        }
    }

    public Container getContainer() {
        return container;
    }

    public boolean isDoubleCounter() {
        return doubleCounter;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.getContainerSize() == counterSlots && container.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        container.stopOpen(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (index < 0 || index >= slots.size()) {
            return ItemStack.EMPTY;
        }

        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();

        if (index < counterSlots) {
            if (!moveItemStackTo(stack, counterSlots, slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, 0, counterSlots, false)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stack);
        return original;
    }
}