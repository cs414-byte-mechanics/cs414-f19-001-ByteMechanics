import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.framing.Framedata;
import org.junit.Before;
import org.junit.Test;
import webconnection.Update;
import webconnection.Action;
import webconnection.UpdateFactory;
import webconnection.WebsocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;

import static org.junit.Assert.*;

public class UpdateFactoryTest
{
    WebsocketServer wss;
    WebSocket dummyClient;
    UpdateFactory updateFactory;

    @Before
    public void initialize() {
        wss = new WebsocketServer();
        dummyClient = new WebSocket() {
            @Override
            public void close(int i, String s) {}
            @Override
            public void close(int i) {}
            @Override
            public void close() {}
            @Override
            public void closeConnection(int i, String s) {}
            @Override
            public void send(String s) throws NotYetConnectedException {}
            @Override
            public void send(ByteBuffer byteBuffer) throws IllegalArgumentException, NotYetConnectedException {}
            @Override
            public void send(byte[] bytes) throws IllegalArgumentException, NotYetConnectedException {}
            @Override
            public void sendFrame(Framedata framedata) {}
            @Override
            public boolean hasBufferedData() {return false;}
            @Override
            public InetSocketAddress getRemoteSocketAddress() {return null;}
            @Override
            public InetSocketAddress getLocalSocketAddress() {return null;}
            @Override
            public boolean isConnecting() {return false;}
            @Override
            public boolean isOpen() {return false;}
            @Override
            public boolean isClosing() {return false;}
            @Override
            public boolean isFlushAndClose() {return false;}
            @Override
            public boolean isClosed() {return false;}
            @Override
            public Draft getDraft() {return null;}
            @Override
            public READYSTATE getReadyState() {return null;}
            
        };
        
        updateFactory = new UpdateFactory();

    }

    @Test
    public void testPerformMove()
    {
        Action action = new Action();
        action.communicationType = "requestMoves";
        Update expected = new Update();
        expected.communicationType = "updateBoard";
        expected.matchID = "dummy_match_ID";
        expected.playerName = "dummy_player_name";
        expected.pieceID =  4;
        expected.updatedBoard = new int[3][3];
        expected.updatedBoard[0][0] = 1;
        expected.updatedBoard[0][1] = 2;
        expected.whoseTurn = "opponent";
        assertEquals(updateFactory.performAction(action),expected);
    }

    //implement once we are able to connect to database from off campus
    @Test
    public void testBuildRegistrationSuccess()
    {
        assertTrue(true);
    }

    @Test
    public void testBeginNewMatch()
    {
        Action action = new Action();
        action.communicationType = "requestBeginNewMatch";
        Update expected = new Update();
        expected.communicationType = "beginNewMatch";
        expected.matchID = "dummy_math_ID";
        expected.whoseTurn = "opponent";
        expected.matchBeginTime = "dummy_match_begin_time";
        assertEquals(updateFactory.performAction(action),expected);
    }

    @Test
    public void testSendInvite()
    {
        Action action = new Action();
        action.communicationType = "invitation";
        Update expected = new Update();
        expected.communicationType = "invitation";
        expected.invitationFrom = "player1";
        expected.invitationTo = "player2";
        expected.invitationTime = "dummy_time";
        assertEquals(updateFactory.performAction(action),expected);
    }

    @Test
    public void testEndMatch()
    {
        Action action = new Action();
        action.communicationType = "quitMatch";
        Update expected = new Update();
        expected.communicationType = "endMatch";
        expected.matchID = "dummy_match_ID";
        expected.endCondition = "quit";
        expected.winnerName = "player1";
        expected.loserName = "player2";
        expected.matchEndTime = "dummy_end_time";
        assertEquals(updateFactory.performAction(action),expected);
    }

    @Test
    public void testLoginUser()
    {
        Action action = new Action();
        action.communicationType = "attemptLogin";
        Update expected = new Update();
        expected.communicationType = "loginSuccess";
        expected.invitations = null;
        expected.matchesInProgress = null;
        expected.matchesCompleted = null;
        assertEquals(updateFactory.performAction(action),expected);
    }

}
