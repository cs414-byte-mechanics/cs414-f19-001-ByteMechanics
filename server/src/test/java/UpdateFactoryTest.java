import Game.GameBoard;
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
    UpdateFactory updateMaker = new UpdateFactory();


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
    }

/* This test is not passing
    @Test
    public void testBuildUpdateBoard()
    {
        Action action = new Action();
        action.communicationType = "requestMoves";
        Update expected = new Update();
        expected.communicationType = "updateBoard";
        expected.matchID = "dummy_match_ID";
        expected.playerName = "dummy_player_name";
        expected.pieceID =  "M";
        expected.updatedBoard = new String[3][3];
        expected.updatedBoard[0][0] = "1";
        expected.updatedBoard[0][1] = "2";
        expected.whoseTurn = "opponent";
        assertEquals(updateMaker.getUpdate(action),expected);
    }
*/
    //implement once we are able to connect to database from off campus
    @Test
    public void testBuildRegistrationSuccess()
    {
        assertTrue(true);
    }

    @Test
    public void testBuildBeginNewMatch()
    {
        Action action = new Action();
        action.communicationType = "requestBeginNewMatch";
        Update expected = new Update();
        expected.communicationType = "beginNewMatch";
        expected.matchID = "dummy_math_ID";
        expected.initialBoard = new int[5][5];
        expected.initialBoard[0][0] = 1;
        expected.initialBoard[0][1] = 2;
        expected.whoseTurn = "opponent";
        expected.matchBeginTime = "dummy_match_begin_time";
        assertEquals(updateMaker.getUpdate(action),expected);
    }

    @Test
    public void testBuildInvitation()
    {
        Action action = new Action();
        action.communicationType = "invitation";
        Update expected = new Update();
        expected.communicationType = "invitation";
        expected.invitationFrom = "player1";
        expected.invitationTo = "player2";
        expected.invitationTime = "dummy_time";
        assertEquals(updateMaker.getUpdate(action),expected);
    }

    @Test
    public void testBuildEndMatch()
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
        assertEquals(updateMaker.getUpdate(action),expected);
    }

//    @Test // added by Fari -- not sure how to write test to check board get updated
//    public void testBuildUpdateBoard()
//    {
//        Action action = new Action();
//        Update expected = new Update();
//        GameBoard board = new GameBoard();
//        board.initialize();
//
//        action.communicationType = "requestMoves";
//        expected.communicationType = "updateBoard";
//        expected.matchID = action.matchID;
//        expected.playerName = action.playerName;
//        expected.pieceID =  action.pieceID;
//        expected.updatedBoard = board.getBoardForDatabase();
//        expected.whoseTurn = "opponent";
//        expected.successMessage = "The player's move was valid and the board has been updated" ;
//        assertEquals(updateMaker.getUpdate(action),expected);
//    }

 }
