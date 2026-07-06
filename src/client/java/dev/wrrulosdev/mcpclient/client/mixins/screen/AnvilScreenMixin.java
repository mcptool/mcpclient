package dev.wrrulosdev.mcpclient.client.mixins.screen;

import dev.wrrulosdev.mcpclient.client.MCPClient;
import dev.wrrulosdev.mcpclient.client.screens.gui.CustomButton;
import dev.wrrulosdev.mcpclient.client.settings.ClientSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ItemCombinerScreen<AnvilMenu> {

    @Shadow private EditBox name;

    @Unique private final List<CustomButton> presetButtons = new ArrayList<>();
    @Unique private double customScrollAmount = 0;
    @Unique private int maxScroll = 0;

    @Unique private int panelX;
    @Unique private int panelY;
    @Unique private final int panelWidth = 120;
    @Unique private int panelHeight;
    @Unique private long handCursor = 0;

    public AnvilScreenMixin(AnvilMenu menu, Inventory inventory, Component title, Identifier imageLocation) {
        super(menu, inventory, title, imageLocation);
    }

    /**
     * Initializes the custom scrollable panel and populates it with preset buttons.
     * @param ci The callback info object from the injection.
     */
    @Inject(method = "subInit", at = @At("TAIL"))
    private void onSubInit(CallbackInfo ci) {
        ClientSettings clientSettings = MCPClient.getSettingsManager().getClientSettings();
        if (!clientSettings.isAnvilButtonsEnabled()) return;

        int guiLeft = (this.width - this.imageWidth) / 2;
        int guiTop = (this.height - this.imageHeight) / 2;
        this.panelX = guiLeft + this.imageWidth + 4;
        this.panelY = guiTop;
        this.panelHeight = this.imageHeight;
        this.handCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR);
        this.presetButtons.clear();

        for (Map.Entry<String, String> entry : clientSettings.getAnvilButtons().entrySet()) {
            String label = entry.getKey();
            String value = entry.getValue();

            CustomButton btn = CustomButton.builder(Component.literal(label))
                .size(this.panelWidth - 14, 20)
                .onPress(button -> {
                    if (((AnvilMenu) this.menu).getSlot(0).hasItem() && this.name != null) {
                        this.name.setValue(value);
                    }
                })
                .build();

            this.presetButtons.add(btn);
        }

        int totalContentHeight = (this.presetButtons.size() * 24) + 10;
        this.maxScroll = Math.max(0, totalContentHeight - this.panelHeight);
        this.customScrollAmount = 0;
    }

    /**
     * Updates the activation state of all buttons based on anvil slot availability.
     */
    @Unique
    private void updateButtonStates() {
        boolean hasItem = ((AnvilMenu) this.menu).getSlot(0).hasItem();
        for (CustomButton btn : presetButtons) {
            btn.active = hasItem;
        }
    }

    /**
     * Handles the rendering of the scrollable panel and its contained buttons.
     * Also updates the cursor shape if hovering over an active button.
     * @param graphics The graphics extractor instance for rendering primitives.
     * @param mouseX The current horizontal mouse position.
     * @param mouseY The current vertical mouse position.
     * @param delta The frame interpolation value.
     * @param ci The callback info object.
     */
    @Inject(method = "extractBackground", at = @At("TAIL"))
    private void onExtractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ClientSettings clientSettings = MCPClient.getSettingsManager().getClientSettings();
        if (!clientSettings.isAnvilButtonsEnabled()) return;

        updateButtonStates();
        boolean isHoveringButton = false;
        graphics.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, 0x88000000);
        graphics.enableScissor(panelX, panelY, panelX + panelWidth, panelY + panelHeight);
        int currentY = panelY + 5;

        for (CustomButton btn : presetButtons) {
            btn.setX(panelX + 5);
            btn.setY(currentY - (int) customScrollAmount);

            if (btn.getY() + btn.getHeight() > panelY && btn.getY() < panelY + panelHeight) {
                btn.render(graphics, mouseX, mouseY, delta);

                if (btn.isActive() && mouseX >= btn.getX() && mouseX <= btn.getX() + btn.getWidth()
                    && mouseY >= btn.getY() && mouseY <= btn.getY() + btn.getHeight()) {
                    isHoveringButton = true;
                }
            }

            currentY += 24;
        }

        graphics.disableScissor();

        long window = Minecraft.getInstance().getWindow().handle();
        GLFW.glfwSetCursor(window, isHoveringButton ? handCursor : 0);

        if (maxScroll > 0) {
            int scrollbarX = panelX + panelWidth - 4;
            int thumbHeight = Math.max(10, (int) (((float) panelHeight / (panelHeight + maxScroll)) * panelHeight));
            int scrollbarY = panelY + (int) ((customScrollAmount / maxScroll) * (panelHeight - thumbHeight));

            graphics.fill(scrollbarX, panelY, scrollbarX + 2, panelY + panelHeight, 0x55FFFFFF);
            graphics.fill(scrollbarX, scrollbarY, scrollbarX + 2, scrollbarY + thumbHeight, 0xFFFFFFFF);
        }
    }

    /**
     * Handles mouse click events to route interactions to the preset buttons.
     * @param event The mouse button event information.
     * @param doubleClick Whether the action was a double click.
     * @return True if the event was consumed, false otherwise.
     */
    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double mouseX = event.x();
        double mouseY = event.y();

        if (mouseX >= panelX && mouseX <= panelX + panelWidth &&
            mouseY >= panelY && mouseY <= panelY + panelHeight) {

            for (CustomButton btn : presetButtons) {
                if (btn.isActive() && btn.mouseClicked(event, doubleClick)) {
                    return true;
                }
            }

            return true;
        }

        return super.mouseClicked(event, doubleClick);
    }

    /**
     * Handles mouse scroll events to update the custom panel scroll offset.
     * @param mouseX The current horizontal mouse position.
     * @param mouseY The current vertical mouse position.
     * @param scrollX The horizontal scroll movement.
     * @param scrollY The vertical scroll movement.
     * @return True if the event was consumed, false otherwise.
     */
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (maxScroll > 0 && mouseX >= panelX && mouseX <= panelX + panelWidth &&
            mouseY >= panelY && mouseY <= panelY + panelHeight) {
            this.customScrollAmount -= scrollY * 16.0;
            this.customScrollAmount = Math.clamp(this.customScrollAmount, 0, this.maxScroll);
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }
}