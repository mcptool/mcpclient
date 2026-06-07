package dev.wrrulosdev.mcpclient.client.mixins.screen;

import com.viaversion.viafabricplus.ViaFabricPlus;
import dev.wrrulosdev.mcpclient.client.constants.TextureConstants;
import dev.wrrulosdev.mcpclient.client.mixins.accessor.JoinMultiplayerScreenAccessor;
import dev.wrrulosdev.mcpclient.client.screens.gui.CustomButton;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(JoinMultiplayerScreen.class)
public abstract class JoinMultiplayerScreenMixin extends Screen {

    @Unique private final List<CustomButton> mcpclient$buttons = new ArrayList<>();

    @Unique private CustomButton mcpclient$editButton;
    @Unique private CustomButton mcpclient$viaFabricPlusButton;
    @Unique private CustomButton mcpclient$refreshButton;
    @Unique private CustomButton mcpclient$deleteButton;
    @Unique private CustomButton mcpclient$joinButton;
    @Unique private CustomButton mcpclient$spoofingButton;
    @Unique private CustomButton mcpclient$addButton;
    @Unique private CustomButton mcpclient$homeButton;

    private final int BUTTON_SIZE = 27;

    protected JoinMultiplayerScreenMixin(Component title) {
        super(title);
    }

    /**
     * Initializes custom UI components after the vanilla screen setup.
     * Hides default buttons, creates custom replacements, and registers
     * additional client-side functionality such as server management,
     * direct join, and external integrations.
     *
     * @param ci callback information from Mixin injection
     */
    @Inject(method = "init", at = @At("TAIL"))
    private void afterInit(CallbackInfo ci) {
        JoinMultiplayerScreenAccessor acc = (JoinMultiplayerScreenAccessor) this;

        acc.getSelectButton().visible = false;
        acc.getEditButton().visible = false;
        acc.getDeleteButton().visible = false;

        /*
         * Bottom Buttons
         */

        // Edits the currently selected server entry
        this.mcpclient$editButton = createCustomButton(
            "Edit Server",
            TextureConstants.EDIT_ICON,
            TextureConstants.EDIT_HOVER_ICON,
            TextureConstants.EDIT_DISABLED_ICON,
            b -> {
                ServerSelectionList.Entry entry = acc.getServeSelectionList().getSelected();

                if (entry instanceof ServerSelectionList.OnlineServerEntry) {
                    ServerData current = ((ServerSelectionList.OnlineServerEntry) entry).getServerData();

                    acc.setEditingServer(new ServerData(current.name, current.ip, ServerData.Type.OTHER));
                    acc.getEditingServer().copyFrom(current);

                    this.minecraft.setScreen(
                        new ManageServerScreen(
                            this,
                            Component.translatable("manageServer.edit.title"),
                            acc::invokeAddServerCallback,
                            acc.getEditingServer()
                        )
                    );
                }
            }
        );

        // Refreshes the server list
        this.mcpclient$refreshButton = createCustomButton(
            "Refresh Servers",
            TextureConstants.SYNC_ICON,
            TextureConstants.SYNC_HOVER_ICON,
            b -> acc.invokeRefreshServerList()
        );

        // Deletes the selected server entry
        this.mcpclient$deleteButton = createCustomButton(
            "Delete Button",
            TextureConstants.DELETE_ICON,
            TextureConstants.DELETE_HOVER_ICON,
            TextureConstants.DELETE_DISABLED_ICON,
            b -> {
                ServerSelectionList.Entry entry = acc.getServeSelectionList().getSelected();

                if (entry instanceof ServerSelectionList.OnlineServerEntry) {
                    String serverName = ((ServerSelectionList.OnlineServerEntry) entry).getServerData().name;

                    Component title = Component.translatable("selectServer.deleteQuestion");
                    Component warning = Component.translatable("selectServer.deleteWarning", serverName);
                    Component yes = Component.translatable("selectServer.deleteButton");
                    Component no = CommonComponents.GUI_CANCEL;

                    this.minecraft.setScreen(
                        new ConfirmScreen(acc::invokeDeleteCallback, title, warning, yes, no)
                    );
                }
            }
        );

        // Opens direct connection screen
        this.mcpclient$joinButton = createCustomButton(
            "Direct connect to server",
            TextureConstants.JOIN_ICON,
            TextureConstants.JOIN_HOVER_ICON,
            b -> {
                if (acc.getEditingServer() == null) {
                    acc.setEditingServer(
                        new ServerData(
                            "",
                            "",
                            ServerData.Type.OTHER
                        )
                    );
                }

                this.minecraft.setScreen(
                    new DirectJoinServerScreen(
                        this,
                        acc::invokeDirectJoinCallback,
                        acc.getEditingServer()
                    )
                );
            }
        );

        // Opens spoofing configuration screen (placeholder)
        this.mcpclient$spoofingButton = createCustomButton(
            "Open spoofing settings",
            TextureConstants.SETTINGS_ICON,
            TextureConstants.SETTINGS_HOVER_ICON,
            b -> {}
        );

        // Creates a new server entry
        this.mcpclient$addButton = createCustomButton(
            "Add Server",
            TextureConstants.ADD_ICON,
            TextureConstants.ADD_HOVER_ICON,
            b -> {
                ServerData newServer = new ServerData("", "", ServerData.Type.OTHER);
                acc.setEditingServer(newServer);

                this.minecraft.setScreen(
                    new ManageServerScreen(
                        this,
                        Component.translatable("manageServer.add.title"),
                        acc::invokeAddServerCallback,
                        newServer
                    )
                );
            }
        );

        /*
         * Top Buttons
         */

        this.mcpclient$homeButton = CustomButton.builder(Component.empty())
            .size(this.BUTTON_SIZE, this.BUTTON_SIZE)
            .tooltip(Tooltip.create(Component.literal("Return to Menu")))
            .style(style -> style
                .border(false)
                .transparent(true)
                .image(
                    TextureConstants.HOME_ICON,
                    0, 0, this.BUTTON_SIZE, this.BUTTON_SIZE,
                    this.BUTTON_SIZE, this.BUTTON_SIZE
                )
                .hoverImage(TextureConstants.HOME_HOVER_ICON)
            )
            .onPress(button -> Minecraft.getInstance().setScreen(new TitleScreen()))
            .build();

        if (FabricLoader.getInstance().isModLoaded("viafabricplus")) {
            this.mcpclient$viaFabricPlusButton = CustomButton.builder(Component.empty())
                .size(this.BUTTON_SIZE, this.BUTTON_SIZE)
                .tooltip(Tooltip.create(Component.literal("Change version")))
                .style(style -> style
                    .border(false)
                    .transparent(true)
                    .image(
                        TextureConstants.NETWORK_ICON,
                        0, 0, this.BUTTON_SIZE, this.BUTTON_SIZE,
                        this.BUTTON_SIZE, this.BUTTON_SIZE
                    )
                    .hoverImage(TextureConstants.NETWORK_HOVER_ICON)
                )
                .onPress(button ->
                    ViaFabricPlus.getImpl().openProtocolSelectionScreen(this)
                )
                .build();

            this.addRenderableWidget(this.mcpclient$viaFabricPlusButton);
        }

        this.addRenderableWidget(this.mcpclient$homeButton);
        this.addRenderableWidget(this.mcpclient$editButton);
        this.addRenderableWidget(this.mcpclient$refreshButton);
        this.addRenderableWidget(this.mcpclient$deleteButton);
        this.addRenderableWidget(this.mcpclient$joinButton);
        this.addRenderableWidget(this.mcpclient$spoofingButton);
        this.addRenderableWidget(this.mcpclient$addButton);

        mcpclient$buttons.add(mcpclient$editButton);
        mcpclient$buttons.add(mcpclient$refreshButton);
        mcpclient$buttons.add(mcpclient$deleteButton);
        mcpclient$buttons.add(mcpclient$joinButton);
        mcpclient$buttons.add(mcpclient$spoofingButton);
        mcpclient$buttons.add(mcpclient$addButton);

        this.mcpclient$editButton.active = false;
        this.mcpclient$deleteButton.active = false;

        this.repositionElements();
    }

    /**
     * Creates a custom button with a disabled-state texture.
     *
     * @param tooltip Tooltip text displayed on hover
     * @param texture Default button texture
     * @param hoverTexture Hover state texture
     * @param disabledTexture Disabled state texture
     * @param action Click action handler
     * @return Configured CustomButton instance
     */
    @Unique
    private CustomButton createCustomButton(
        String tooltip,
        Identifier texture,
        Identifier hoverTexture,
        Identifier disabledTexture,
        CustomButton.PressAction action
    ) {
        return createBaseBuilder(tooltip, texture, hoverTexture, action)
            .style(style -> style.disabledImage(disabledTexture))
            .build();
    }

    /**
     * Creates a custom button without a disabled-state texture.
     *
     * @param tooltip Tooltip text displayed on hover
     * @param texture Default button texture
     * @param hoverTexture Hover state texture
     * @param action Click action handler
     * @return Configured CustomButton instance
     */
    @Unique
    private CustomButton createCustomButton(
        String tooltip,
        Identifier texture,
        Identifier hoverTexture,
        CustomButton.PressAction action
    ) {
        return createBaseBuilder(tooltip, texture, hoverTexture, action).build();
    }

    /**
     * Base builder used to construct all custom buttons with shared configuration.
     *
     * @param tooltip Tooltip text displayed on hover
     * @param texture Default texture
     * @param hoverTexture Hover texture
     * @param action Click handler
     * @return Configured builder instance
     */
    @Unique
    private CustomButton.Builder createBaseBuilder(
        String tooltip,
        Identifier texture,
        Identifier hoverTexture,
        CustomButton.PressAction action
    ) {
        return CustomButton.builder(Component.empty())
            .size(this.BUTTON_SIZE, this.BUTTON_SIZE)
            .tooltip(Tooltip.create(Component.literal(tooltip)))
            .style(style -> style
                .border(false)
                .transparent(true)
                .image(texture, 0, 0, this.BUTTON_SIZE, this.BUTTON_SIZE, this.BUTTON_SIZE, this.BUTTON_SIZE)
                .hoverImage(hoverTexture)
            )
            .onPress(action);
    }

    /**
     * Repositions all custom buttons after layout recalculation.
     * Handles bottom button spacing, adaptive centering, and conditional
     * positioning for optional integrations.
     *
     * @param ci callback information from Mixin injection
     */
    @Inject(method = "repositionElements", at = @At("TAIL"))
    private void onRepositionElements(CallbackInfo ci) {
        if (mcpclient$buttons.isEmpty()) return;

        int buttonWidth = 30;
        int maxGap = 22;
        int sideMargin = 20;
        int y = this.height - 45;

        int availableWidth = this.width - (sideMargin * 2);
        int totalButtonWidth = buttonWidth * mcpclient$buttons.size();

        int gap = 0;
        if (mcpclient$buttons.size() > 1) {
            gap = Math.min((availableWidth - totalButtonWidth) / (mcpclient$buttons.size() - 1), maxGap);
        }

        int actualRowWidth = totalButtonWidth + (gap * (mcpclient$buttons.size() - 1));
        int startX = (this.width - actualRowWidth) / 2;

        for (int i = 0; i < mcpclient$buttons.size(); i++) {
            mcpclient$buttons.get(i).setPosition(startX + (i * (buttonWidth + gap)), y);
        }

        this.mcpclient$homeButton.setPosition(5, 3);

        if (!FabricLoader.getInstance().isModLoaded("viafabricplus")) return;

        this.mcpclient$viaFabricPlusButton.setPosition(this.width - 22 - 12, 3);
    }

    /**
     * Updates button states when the selected server entry changes.
     * Enables or disables editing-related actions depending on selection type.
     *
     * @param ci callback information from Mixin injection
     */
    @Inject(method = "onSelectedChange", at = @At("TAIL"))
    protected void onSelectedChange(CallbackInfo ci) {
        var editBtn = this.mcpclient$editButton;
        var delBtn = this.mcpclient$deleteButton;

        if (editBtn == null || delBtn == null) return;

        JoinMultiplayerScreenAccessor acc = (JoinMultiplayerScreenAccessor) this;

        editBtn.active = false;
        delBtn.active = false;

        ServerSelectionList.Entry entry =
            (ServerSelectionList.Entry) acc.getServeSelectionList().getSelected();

        if (entry != null && !(entry instanceof ServerSelectionList.LANHeader)) {
            if (entry instanceof ServerSelectionList.OnlineServerEntry) {
                editBtn.active = true;
                delBtn.active = true;
            }
        }
    }
}