package dev.wrrulosdev.mcpclient.client.mixins.render;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.cheats.WallHack;
import dev.wrrulosdev.mcpclient.client.cheats.esp.BlockScanner;
import dev.wrrulosdev.mcpclient.client.cheats.esp.EspRenderer;
import dev.wrrulosdev.mcpclient.client.settings.CheatsSettings;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.joml.Matrix4fc;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    /**
     * Injects at the end of the world rendering pipeline to render custom ESP overlays
     * such as player wallhack boxes and animated stickman visualizations.
     * <p>
     * This method executes after the vanilla level rendering has completed, ensuring that
     * custom rendering elements are drawn on top of all world geometry.
     * <p>
     * It delegates rendering responsibilities to the cheat rendering modules, including:
     * - Player bounding box and Blocks ESP rendering
     * - Animated stickman visualization for player entities
     *
     * @param resourceAllocator        Graphics resource allocator used by the rendering pipeline.
     * @param deltaTracker             Frame timing tracker used for smooth animation interpolation.
     * @param renderOutline            Whether outline rendering is enabled for the current frame.
     * @param cameraState              Current camera render state containing view transformation data.
     * @param modelViewMatrix          Model-view transformation matrix used for world rendering.
     * @param terrainFog               GPU buffer slice containing fog rendering data.
     * @param fogColor                 Current fog color vector applied to the scene.
     * @param shouldRenderSky          Whether sky rendering is enabled for this frame.
     * @param chunkSectionsToRender    Chunk section visibility and rendering selection data.
     * @param ci                       Callback info used to interact with the injection point.
     */
    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void onRenderLevel(
        GraphicsResourceAllocator resourceAllocator,
        DeltaTracker deltaTracker,
        boolean renderOutline,
        CameraRenderState cameraState,
        Matrix4fc modelViewMatrix,
        GpuBufferSlice terrainFog,
        Vector4f fogColor,
        boolean shouldRenderSky,
        ChunkSectionsToRender chunkSectionsToRender,
        CallbackInfo ci
    ) {
        CheatsSettings cheatsSettings = MCPClient.getSettingsManager().getCheatsSettings();

        if (WallHack.INSTANCE.isEnabled()) {
            WallHack.INSTANCE.run();
        }

        if (cheatsSettings.isBlockTrackerEnabled()) {
            BlockScanner.update(Minecraft.getInstance().player.blockPosition());
            EspRenderer.render();
        }
    }
}