package webconnection;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.*;
import database.*;

public class WebsocketServer extends WebSocketServer {

    private static int TCP_PORT = 4444;
    private HashMap<String, WebSocket> conns;
    private static Gson gson = new GsonBuilder().create();
    private UpdateFactory updateFactory = new UpdateFactory(this);

    public WebsocketServer() {
        super(new InetSocketAddress(TCP_PORT));
        conns = new HashMap<>();
    }
    public void addSession(String username, WebSocket conn) {
        conns.put(username, conn);
    }
    public void removeSession(String username) {
        conns.remove(username);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Closing connection.");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Action clientAction = handleClientAction(message);
        System.out.println(message);
        conns.put(clientAction.userName, conn);
        sendUpdateToClient(conn, updateFactory.getUpdate(clientAction, conn));
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            System.out.println("ERROR from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
        }
        else System.out.println("ERROR: Connection does not exist");
    }

    public Action handleClientAction(String message) {

        Action action = new Action();
        try {
            action =  gson.fromJson(message, Action.class);
        }catch(Exception e) {
            System.err.println("Unable to parse client action into object!\nReason: " + e);
        }


        return action;

    }

    public String sendUpdateToClient(WebSocket client, Update update) {

        String updateJSON = "";

        try {
            if(update.communicationType == "error") updateJSON = gson.toJson(update, ServerError.class);
            else updateJSON = gson.toJson(update, Update.class);
            if(update.recipients != null) {
                for (Map.Entry<String, WebSocket> entry : conns.entrySet()) {
                    System.out.println(entry.getKey() + ":" + entry.getValue().toString());
                }
                for(String user:update.recipients) {
                    System.out.println("Sending update to " + user);

                    try {
                        conns.get(user).send(updateJSON);
                    }
                    catch(Exception e) {
                        System.err.println("Unable to send recipient the update!\nReason: " + e);
                        e.printStackTrace();
                    }
                }
            }
            else client.send(updateJSON);
        }catch(Exception e) {
            System.err.println("Unable to send client the update!\nReason: " + e);
            e.printStackTrace();
        }

        return updateJSON;

    }


}
