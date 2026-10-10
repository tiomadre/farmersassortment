package com.tiomadre.farmersassortment.client;

import com.tiomadre.farmersassortment.core.FarmersAssortment;
import com.tiomadre.farmersassortment.core.block.StoolBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.IdentityHashMap;
import java.util.Map;

@Mod.EventBusSubscriber(
        modid = FarmersAssortment.MOD_ID,
        value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class StoolSeatClientEvents {
    private static final Map<Player, float[]> SAVED_ROTATIONS =
            new IdentityHashMap<>();

    private StoolSeatClientEvents() {
    }

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        if (event.isCanceled()) {
            return;
        }

        Player player = event.getEntity();
        if (!(player.getVehicle() instanceof ArmorStand seat)) {
            return;
        }

        BlockPos pos = BlockPos.containing(
                seat.getX(), seat.getY(), seat.getZ()
        );
        BlockState state = player.level().getBlockState(pos);

        if (!(state.getBlock() instanceof StoolBlock)
                || !seat.isInvisible()
                || !seat.isNoGravity()
                || !seat.isMarker()) {
            return;
        }

        SAVED_ROTATIONS.put(player, new float[]{
                player.yHeadRot,
                player.yHeadRotO,
                player.yBodyRot,
                player.yBodyRotO
        });

        float yaw = seat.getYRot();

        seat.setYBodyRot(yaw);
        seat.yBodyRotO = yaw;
        player.setYBodyRot(yaw);
        player.yBodyRotO = yaw;

        player.yHeadRot = yaw + Mth.clamp(
                Mth.wrapDegrees(player.yHeadRot - yaw),
                -75.0F, 75.0F
        );
        player.yHeadRotO = yaw + Mth.clamp(
                Mth.wrapDegrees(player.yHeadRotO - yaw),
                -75.0F, 75.0F
        );
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        Player player = event.getEntity();
        float[] rotations = SAVED_ROTATIONS.remove(player);

        if (rotations == null) {
            return;
        }

        player.yHeadRot = rotations[0];
        player.yHeadRotO = rotations[1];
        player.yBodyRot = rotations[2];
        player.yBodyRotO = rotations[3];
    }
}
