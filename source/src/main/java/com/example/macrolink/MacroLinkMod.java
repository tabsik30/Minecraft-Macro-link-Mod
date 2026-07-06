package com.example.macrolink;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class MacroLinkMod implements ClientModInitializer {

    private static final int PORT = 25599;
    // Minecraft tickuje 20x/s. Wysylka co 5 tickow = ok. 4x/s - w zupelnosci
    // wystarczajaco czesto dla wskaznikow na Stream Decku, bez zbednego spamu.
    private static final int SEND_EVERY_N_TICKS = 5;

    public static DataServer dataServer;

    private String lastPayload = "";
    private int tickCounter = 0;

    @Override
    public void onInitializeClient() {
        dataServer = new DataServer(PORT);
        dataServer.start();

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(Minecraft client) {
        LocalPlayer player = client.player;
        if (player == null) {
            return; // np. jeszcze na ekranie tytulowym
        }

        tickCounter++;
        if (tickCounter < SEND_EVERY_N_TICKS) {
            return;
        }
        tickCounter = 0;

        String payload = PlayerDataCollector.collect(player);
        if (!payload.equals(lastPayload)) {
            dataServer.broadcastJson(payload);
            lastPayload = payload;
        }
    }
}
