package webconnection;

public class App 
{
    public static void main(String[] args) {
        System.out.println("Welcome to Congo Online!");
        WebsocketServer wss = new WebsocketServer();
        System.out.println("Starting server...");
        wss.start();
      }
}

