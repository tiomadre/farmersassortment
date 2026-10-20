package com.tiomadre.farmersassortment.core.mixin;

import com.tiomadre.farmersassortment.core.block.entity.RopeColorBlockEntity;
import com.tiomadre.farmersassortment.core.util.DyeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import vectorwing.farmersdelight.common.block.RopeFenceBlock;
import vectorwing.farmersdelight.common.block.RopeFenceGateBlock;
import java.util.List;

@Mixin({RopeFenceBlock.class, RopeFenceGateBlock.class})
public abstract class RopeColorMixin extends Block implements EntityBlock {
    protected RopeColorMixin(Properties properties) { super(properties); }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RopeColorBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                            LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.getBlockEntity(pos) instanceof RopeColorBlockEntity rope) {
            rope.setColor(DyeColors.getColor(stack));
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof RopeColorBlockEntity rope) {
            for (ItemStack stack : drops) {
                if (stack.is(state.getBlock().asItem())) DyeColors.setColor(stack, rope.getColor());
            }
        }
        return drops;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        if (level.getBlockEntity(pos) instanceof RopeColorBlockEntity rope) {
            DyeColors.setColor(stack, rope.getColor());
        }
        return stack;
    }
}
