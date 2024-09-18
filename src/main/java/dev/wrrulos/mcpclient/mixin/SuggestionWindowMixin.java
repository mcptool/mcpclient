package dev.wrrulos.mcpclient.mixin;

import com.mojang.brigadier.suggestion.Suggestion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.util.math.Rect2i;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Mixin to modify the behavior of the SuggestionWindow in ChatInputSuggestor.
 * This alters how the suggestion window renders and scrolls in the Minecraft chat input.
 * <p>
 * Code based on the SpigotRCE mixin for SuggestionWindow.
 */
@Mixin(ChatInputSuggestor.SuggestionWindow.class)
public class SuggestionWindowMixin {
    /**
     * Holds the DrawContext for use in rendering.
     */
    @Unique
    DrawContext savedContext;

    /**
     * Keeps the current index before any scrolling occurs.
     */
    @Unique
    int indexBefore;

    /**
     * Smooth scrolling offset in pixels.
     */
    @Unique
    float scrollPixelOffset;

    /**
     * The index to scroll to, based on user input.
     */
    @Unique
    int targetIndex;

    /**
     * Shadow variable for the current index in the window.
     */
    @Shadow
    private int inWindowIndex;

    /**
     * Shadow variable holding the suggestions displayed.
     */
    @Final
    @Shadow
    private List<Suggestion> suggestions;

    /**
     * Shadow variable representing the area where the suggestions are rendered.
     */
    @Final
    @Shadow
    private Rect2i area;

    /**
     * Injects logic at the beginning of the render method to handle context saving and smooth scrolling adjustments.
     *
     * @param context The drawing context used during rendering.
     * @param mouseX  The current x position of the mouse.
     * @param mouseY  The current y position of the mouse.
     * @param ci      Callback info for the render method.
     */
    @Inject(method = "render", at = @At("HEAD"))
    private void renderHead(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        savedContext = context;
        scrollPixelOffset *= (float) Math.pow(0.3f, getLastFrameDuration());
        inWindowIndex = clamp(targetIndex - getScrollOffset() / 12, 0, suggestions.size() - 10);
    }

    /**
     * Adds a scissor mask before rendering suggestions, limiting the draw area.
     *
     * @param context The drawing context used during rendering.
     * @param mouseX  The current x position of the mouse.
     * @param mouseY  The current y position of the mouse.
     * @param ci      Callback info for the render method.
     */
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V", ordinal = 4))
    private void mask(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        savedContext.enableScissor(0, area.getY(), context.getScaledWindowWidth(), area.getY() + area.getHeight());
    }

    /**
     * Disables the scissor mask after text is rendered.
     *
     * @param context The drawing context used during rendering.
     * @param mouseX  The current x position of the mouse.
     * @param mouseY  The current y position of the mouse.
     * @param ci      Callback info for the render method.
     */
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)I", shift = At.Shift.AFTER))
    private void demask(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        context.disableScissor();
    }

    /**
     * At the end of the render method, reset the in-window index to the target index.
     *
     * @param context The drawing context used during rendering.
     * @param mouseX  The current x position of the mouse.
     * @param mouseY  The current y position of the mouse.
     * @param ci      Callback info for the render method.
     */
    @Inject(method = "render", at = @At("TAIL"))
    private void renderTail(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        inWindowIndex = targetIndex;
    }

    /**
     * Adjusts the Y position of the text when rendering, accounting for scrolling.
     *
     * @param s The original Y position.
     * @return The adjusted Y position.
     */
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)I"), index = 3)
    private int textPosY(int s) {
        return (s + getDrawOffset());
    }

    /**
     * Before the mouse scroll event, save the current index position.
     *
     * @param amount Scroll amount.
     * @param cir Callback information for the mouse scroll method.
     */
    @Inject(method = "mouseScrolled", at = @At("HEAD"))
    private void mScrollHead(double amount, CallbackInfoReturnable<Boolean> cir) {
        commonSH();
    }

    /**
     * After the mouse scroll event, update scrolling offset and target index.
     *
     * @param am Scroll amount.
     * @param ci Callback information for mouse scroll method.
     */
    @Inject(method = "mouseScrolled", at = @At("RETURN"))
    private void mScrollTail(double am, CallbackInfoReturnable<Boolean> ci) {
        commonST();
    }

    /**
     * Before scroll adjustments, save the current index.
     *
     * @param off Scroll offset.
     * @param ci  Callback information for the scroll method.
     */
    @Inject(method = "scroll", at = @At("HEAD"))
    private void scrollHead(int off, CallbackInfo ci) {
        commonSH();
    }

    /**
     * After scroll adjustments, update the scroll offset and restore the index.
     *
     * @param off Scroll offset.
     * @param ci  Callback information for the scroll method.
     */
    @Inject(method = "scroll", at = @At("TAIL"))
    private void scrollTail(int off, CallbackInfo ci) {
        commonST();
    }

    /**
     * Records the current index before scrolling.
     */
    @Unique
    private void commonSH() {
        indexBefore = inWindowIndex;
    }

    /**
     * Updates the pixel offset after scrolling, setting the target index.
     */
    @Unique
    private void commonST() {
        scrollPixelOffset += (inWindowIndex - indexBefore) * 12;
        targetIndex = inWindowIndex;
        inWindowIndex = indexBefore;
    }

    /**
     * Adds an extra line above when rendering if scrolling is active.
     *
     * @param r The original line index.
     * @return The adjusted line index.
     */
    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 4)
    private int addLineAbove(int r) {
        if (getScrollOffset() <= 0 || inWindowIndex <= 0) return (r);
        return (r - 1);
    }

    /**
     * Adds an extra line below when rendering if scrolling is active.
     *
     * @param i The original line index.
     * @return The adjusted line index.
     */
    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 2)
    private int addLineUnder(int i) {
        if (getScrollOffset() >= 0 || inWindowIndex >= suggestions.size() - 10) return (i);
        return (i + 1);
    }

    /**
     * Calculates the offset for drawing text based on the current scroll position.
     *
     * @return The calculated draw offset.
     */
    @Unique
    int getDrawOffset() {
        return Math.round(scrollPixelOffset) - (Math.round(scrollPixelOffset) / 12 * 12);
    }

    /**
     * Returns the total scroll offset in pixels.
     *
     * @return The pixel scroll offset.
     */
    @Unique
    int getScrollOffset() {
        return Math.round(scrollPixelOffset);
    }

    /**
     * Retrieves the time taken for the last frame, used in smooth scrolling calculations.
     *
     * @return The duration of the last frame.
     */
    @Unique
    public float getLastFrameDuration() {
        return MinecraftClient.getInstance().getRenderTickCounter().getLastFrameDuration();
    }

    /**
     * Clamps a value between a minimum and maximum.
     *
     * @param val Value to clamp.
     * @param min Minimum allowed value.
     * @param max Maximum allowed value.
     * @return Clamped value.
     */
    @Unique
    public int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
}
