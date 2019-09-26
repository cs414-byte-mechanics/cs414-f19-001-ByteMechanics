package webconnection;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.*;
import database.*;

public class WebsocketServer extends WebSocketServer {

    private static int TCP_PORT = 4444;
    private Set<WebSocket> conns;
    private static Gson gson = new GsonBuilder().serializeNulls().create();
    private DatabaseHandler db;

    public WebsocketServer() {
        super(new InetSocketAddress(TCP_PORT));
        conns = new HashSet<>();
        db = new DatabaseHandler();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conns.add(conn);
        System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Closing connection.");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Action clientAction = handleClientAction(message);
        
        try {
        
            boolean result = db.performDBSearch(clientAction);
            
            if(result){
                System.out.println("humoring abby");
                //if successful, create update message and send to client
                Update update = createUpdateMessage(clientAction, result);
                sendUpdateToClient(conn, update);
            }
        
        } catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            System.out.println("ERROR from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
            conns.remove(conn);
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
            updateJSON = gson.toJson(update, Update.class);
            client.send(updateJSON);
        }catch(Exception e) {
            System.err.println("Unable to send client the update!\nReason: " + e);
        }

        return updateJSON;

    }
    
    public Update createUpdateMessage(Action action, boolean result){
        Update update = new Update();
    
        if(action.communicationType.equals("registerUser")){
            //registration was successful
            update.communicationType = "registrationSuccess";
            update.userEmail = action.userEmail;
            update.userName = action.userName;
            update.successMessage = "User account has been successfully created.";
            return update;
        }
        
        return null;
        
    }

}
