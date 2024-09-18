package dev.wrrulos.mcpclient.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.OrderedText;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

/**
 * Mixin that modifies the behavior of ChatHud to introduce custom effects like smooth scrolling
 * and adjustments in rendering, including mask height.
 * <p>
 * Code based on the SpigotRCE mixin for ChatHud.
 */
@Mixin(value = ChatHud.class, priority = 1001)
public class ChatHudMixin {

    /**
     * Stores the smooth scrolling offset value for the chat.
     */
    @Unique
    float scrollOffset;

    /**
     * A temporary buffer used to tweak the mask height during rendering.
     */
    @Unique
    float maskHeightBuffer;

    /**
     * Tracks if the chat is currently refreshing.
     */
    @Unique
    boolean refreshing = false;

    /**
     * Saves the scroll position before any adjustments.
     */
    @Unique
    int scrollValBefore;

    /**
     * Keeps a reference to the current rendering context.
     */
    @Unique
    DrawContext savedContext;

    /**
     * Holds the tick value for syncing rendering.
     */
    @Unique
    int savedCurrentTick;

    /**
     * Vector for handling matrix translation modifications.
     */
    @Unique
    Vec2f mtc = new Vec2f(0, 0);

    /**
     * Number of chat lines currently scrolled.
     */
    @Shadow
    private int scrolledLines;

    /**
     * List of messages visible in the chat.
     */
    @Final
    @Shadow
    private List<ChatHudLine.Visible> visibleMessages;

    /**
     * Injects at the start of the render method to save context and calculate scrolling.
     */
    @Inject(method = "render", at = @At("HEAD"))
    public void renderH(DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused, CallbackInfo ci) {
        savedContext = context;
        savedCurrentTick = currentTick;
        scrollOffset = (float) (scrollOffset * Math.pow(0.3f, getLastFrameDuration()));
        scrollValBefore = scrolledLines;
        scrolledLines -= getChatScrollOffset() / getLineHeight();
        if (scrolledLines < 0) scrolledLines = 0;
    }

    /**
     * Modifies matrix translation parameters for proper positioning.
     */
    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 0))
    private void matrixTranslateCorrector(Args args) {
        int x = (int) (float) args.get(0) - 4; // Get the X position
        int y = (int) (float) args.get(1);    // Get the Y position

        // Calculate the new Y position based on the matrix translation
        var newY = (float) ((mtc.y - y) * Math.pow(0.3f, getLastFrameDuration()) + y);
        args.set(1, (float) Math.round(newY));
        mtc = new Vec2f(x, newY);
    }

    /**
     * Adjusts the height of the mask based on the chat state.
     */
    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 7)
    private int mask(int m) {
        // Calculate the mask height based on the chat state
        var targetHeight = getTargetHeight();
        maskHeightBuffer = (float) ((maskHeightBuffer - targetHeight) * Math.pow(0.3f, getLastFrameDuration()) + targetHeight);
        var masktop = m - Math.round(maskHeightBuffer) + (int) mtc.y;
        var maskbottom = m + (int) mtc.y;

        // Adjust the mask height based on the chat scroll state
        if (getChatScrollOffset() == 0 && Math.round(maskHeightBuffer) != 0) {
            if (Math.round(maskHeightBuffer) == targetHeight) {
                maskbottom += 2;
                masktop -= 2;
            } else {
                maskbottom += 2;
            }
        }

        // Adjust the mask height based on the raised chat distance
        if (FabricLoader.getInstance().getObjectShare().get("raised:chat") instanceof Integer distance) {
            masktop -= distance;
            maskbottom -= distance;
        }

        // Enable the scissor test for the mask area and return the mask height
        savedContext.enableScissor(0, masktop, savedContext.getScaledWindowWidth(), maskbottom);
        return m;
    }

    @Unique
    private int getTargetHeight() {
        var shownLineCount = 0;  // Number of lines currently shown in the chat

        // Calculate the number of lines that are visible in the chat
        for (int r = 0; r + scrolledLines < visibleMessages.size() && r < getVisibleLineCount(); r++) {
            if (savedCurrentTick - visibleMessages.get(r).addedTime() < 200 || isChatFocused()) shownLineCount++;
        }

        // Calculate the target height for the mask based on the number of lines shown and the line height
        return shownLineCount * getLineHeight();
    }

    /**
     * Adjusts Y position to properly align chat during rendering.
     */
    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 18)
    private int changePosY(int y) {
        return y - getChatDrawOffset();
    }

    /**
     * Cleans up OpenGL state by disabling the scissor test post-render.
     */
    @ModifyVariable(method = "render", at = @At("STORE"))
    private long demask(long a) {
        savedContext.disableScissor();
        return a;
    }

    /**
     * Restores scroll value after rendering is completed.
     */
    @Inject(method = "render", at = @At("TAIL"))
    public void renderT(DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused, CallbackInfo ci) {
        scrolledLines = scrollValBefore;
    }

    /**
     * Changes scroll behavior when a new message is added to the chat.
     */
    @ModifyVariable(method = "addVisibleMessage", at = @At("STORE"), ordinal = 0)
    List<OrderedText> onNewMessage(List<OrderedText> ot) {
        if (refreshing) return ot;
        scrollOffset -= ot.size() * getLineHeight();
        return ot;
    }

    /**
     * Stores scroll state before modification.
     */
    @Inject(method = "scroll", at = @At("HEAD"))
    public void scrollH(int scroll, CallbackInfo ci) {
        scrollValBefore = scrolledLines;
    }

    /**
     * Updates the scroll offset after scrolling is done.
     */
    @Inject(method = "scroll", at = @At("TAIL"))
    public void scrollT(int scroll, CallbackInfo ci) {
        scrollOffset += (scrolledLines - scrollValBefore) * getLineHeight();
    }

    /**
     * Captures scroll value before reset.
     */
    @Inject(method = "resetScroll", at = @At("HEAD"))
    public void scrollResetH(CallbackInfo ci) {
        scrollValBefore = scrolledLines;
    }

    /**
     * Modifies scroll offset after resetting the scroll position.
     */
    @Inject(method = "resetScroll", at = @At("TAIL"))
    public void scrollResetT(CallbackInfo ci) {
        scrollOffset += (scrolledLines - scrollValBefore) * getLineHeight();
    }

    /**
     * Adds lines above based on mask height calculation.
     */
    @ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;getLineHeight()I"), ordinal = 3)
    private int addLinesAbove(int i) {
        return (int) Math.ceil(Math.round(maskHeightBuffer) / (float) getLineHeight()) + (getChatScrollOffset() < 0 ? 1 : 0);
    }

    /**
     * Subtracts a line if needed, depending on scroll state.
     */
    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 12)
    private int addLinesUnder(int r) {
        if (scrolledLines == 0 || getChatScrollOffset() <= 0) return r;
        return r - 1;
    }

    /**
     * Flags chat as refreshing during the process.
     */
    @Inject(method = "refresh", at = @At("HEAD"))
    private void refreshH(CallbackInfo ci) {
        refreshing = true;
    }

    /**
     * Indicates the end of the chat refresh process.
     */
    @Inject(method = "refresh", at = @At("TAIL"))
    private void refreshT(CallbackInfo ci) {
        refreshing = false;
    }

    /**
     * Shadow method to get the chat line height.
     */
    @Shadow
    private int getLineHeight() {
        return 0;
    }

    /**
     * Shadow method to get visible chat lines count.
     */
    @Shadow
    public int getVisibleLineCount() {
        return 0;
    }

    /**
     * Shadow method to check if chat is focused.
     */
    @Shadow
    public boolean isChatFocused() {
        return false;
    }

    /**
     * Returns the offset for drawing chat based on scroll position.
     */
    @Unique
    int getChatDrawOffset() {
        return Math.round(scrollOffset) - (Math.round(scrollOffset) / getLineHeight() * getLineHeight());
    }

    /**
     * Returns the current scroll offset.
     */
    @Unique
    int getChatScrollOffset() {
        return Math.round(scrollOffset);
    }

    /**
     * Retrieves the duration of the last frame for smooth animations.
     */
    @Unique
    public float getLastFrameDuration() {
        return MinecraftClient.getInstance().getRenderTickCounter().getLastFrameDuration();
    }
}