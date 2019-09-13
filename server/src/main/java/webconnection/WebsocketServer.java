
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;

public class WebsocketServer extends WebSocketServer {

    private static int TCP_PORT = 4444;
    private Set<WebSocket> conns;

    public WebsocketServer() {
        super(new InetSocketAddress(TCP_PORT));
        conns = new HashSet<>();
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
        System.out.println("Message from client: " + message);
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
}
