package warrook.lunamancy.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import warrook.lunamancy.Lunamancy;
import warrook.lunamancy.ModManifest.*;
import warrook.lunamancy.utils.network.LightContainer;

@Environment(EnvType.CLIENT) //irrelevant, pretty sure
public class MoonHud extends DrawableHelper {
    //private final MinecraftClient client = MinecraftClient.getInstance();

    public static void render(MatrixStack matrices, float partialTick) {
        if (!MinecraftClient.isHudEnabled())
            return;

        MinecraftClient.getInstance().getProfiler().push(Lunamancy.MOD_ID + " hud");
        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        assert client.player != null;
        ClientPlayerEntity player = client.player;

        matrices.push();

        //Hover over
        if (client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            assert client.world != null;
            BlockHitResult lookingAt = (BlockHitResult) client.crosshairTarget;
            Block block = client.world.getBlockState(lookingAt.getBlockPos()).getBlock();
            BlockEntity entity = client.world.getBlockEntity(lookingAt.getBlockPos());

            if (entity instanceof ToolHud) {
                if (entity instanceof LightContainer && player.isHolding(ModItems.SYMBOL)) {
                    ((ToolHud) entity).renderHud(matrices);
                }
            }
        }

        matrices.pop();
        MinecraftClient.getInstance().getProfiler().pop();
    }
}
