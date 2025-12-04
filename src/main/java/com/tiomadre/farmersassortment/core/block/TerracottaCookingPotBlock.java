package com.tiomadre.farmersassortment.core.block;

import com.tiomadre.farmersassortment.core.block.state.TerracottaCookingPotColor;
import com.tiomadre.farmersassortment.core.item.TerracottaCookingPotItem;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.state.CookingPotSupport;

public class TerracottaCookingPotBlock extends CookingPotBlock {
    public static final EnumProperty<TerracottaCookingPotColor> COLOR = EnumProperty.create("color", TerracottaCookingPotColor.class);

    public TerracottaCookingPotBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, net.minecraft.core.Direction.NORTH)
                .setValue(SUPPORT, CookingPotSupport.NONE)
                .setValue(WATERLOGGED, Boolean.FALSE)
                .setValue(COLOR, TerracottaCookingPotColor.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(COLOR);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state != null) {
            TerracottaCookingPotColor color = TerracottaCookingPotItem.getColor(context.getItemInHand());
            return state.setValue(COLOR, color);
        }
        return null;
    }

    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);
        if (heldStack.getItem() instanceof DyeItem dyeItem) {
            TerracottaCookingPotColor color = TerracottaCookingPotColor.fromDyeColor(dyeItem.getDyeColor());
            if (color != state.getValue(COLOR)) {
                if (!level.isClientSide) {
                    level.setBlock(pos, state.setValue(COLOR, color), Block.UPDATE_ALL);
                    if (!player.isCreative()) {
                        heldStack.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            return InteractionResult.CONSUME;
        }

        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        return TerracottaCookingPotItem.applyColorToStack(stack, state.getValue(COLOR));
    }
}
