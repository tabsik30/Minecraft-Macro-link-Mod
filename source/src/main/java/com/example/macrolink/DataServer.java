package com.example.macrolink;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

/**
 * Lokalny serwer WebSocket. Celowo bindowany tylko na 127.0.0.1 —
 * nie chcemy, żeby ktokolwiek inny w sieci LAN mógł się podłączyć
 * i czytać dane gracza (albo w przyszłości wysyłać komendy).
 */
public class DataServer extends WebSocketServer {

    public DataServer(int port) {
        super(new InetSocketAddress("127.0.0.1", port));
        setReuseAddr(true);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("[MacroLink] Klient podlaczony: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("[MacroLink] Klient rozlaczony");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        // Na razie kanal jest jednokierunkowy (mod -> Macro Deck).
        // Miejsce na przyszlosc, gdybys chcial np. sterowac czyms w grze z Macro Deck.
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("[MacroLink] Serwer WebSocket wystartowal na porcie " + getPort());
    }

    public void broadcastJson(String json) {
        if (getConnections().isEmpty()) return;
        broadcast(json);
    }
}
