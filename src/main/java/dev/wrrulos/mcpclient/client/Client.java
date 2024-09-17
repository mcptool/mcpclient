package dev.wrrulos.mcpclient.client;

import net.fabricmc.api.ClientModInitializer;

public class Client implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        System.out.println("Client-side initialization");
    }
}
