import org.java_websocket.drafts.Draft;
import org.java_websocket.framing.Framedata;
import org.junit.Before;
import webconnection.WebsocketServer;
import org.java_websocket.WebSocket;
import webconnection.Action;
import webconnection.Update;
// import jdk.jfr.StackTrace;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.annotation.processing.SupportedAnnotationTypes;

import static org.junit.Assert.*;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.util.ArrayList;
import java.util.Arrays;

//@RunWith(JUnit4.class)
public class WebsocketServerTest {

    WebsocketServer wss;
    WebSocket dummyClient;

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

    @Test
    public void testFullAction() {

        String testJSON = "{\"objectType\": \"Action\"," +
                           "\"communicationType\": \"requestMove\"," +
                           "\"communicationVersion\": 1," +
                           "\"matchID\": \"5654\"," +
                           "\"playerName\": \"JohnDoe\"," +
                           "\"pieceID\": \"G\"," +
                           "\"desiredMoves\": [1,2,3,4]," +
                           "\"userName\": \"john123\"," +
                           "\"userPassword\": \"p@ssword\"," +
                           "\"userEmail\": \"cool@gmail.com\"," +
                           "\"playerOneName\": \"john1\"," +
                           "\"playerTwoName\": \"opponent\"," +
                           "\"invitationFrom\": \"johnInv\"," +
                           "\"invitationTo\": \"johnTo\"," +
                           "\"invitationTime\": \"9/2/2019 2:03PM\"," +
                           "\"playerQuitting\": \"quitter\"}";

        Action correctResult = new Action();

        correctResult.objectType = "Action";
        correctResult.communicationType = "requestMove";
        correctResult.communicationVersion = 1;
        correctResult.matchID = "5654";
        correctResult.playerName = "JohnDoe";
        correctResult.pieceID = "G";
        correctResult.desiredMoves = new int[4];
        correctResult.desiredMoves[0] = 1;
        correctResult.desiredMoves[1] = 2;
        correctResult.desiredMoves[2] = 3;
        correctResult.desiredMoves[3] = 4;
        correctResult.userName = "john123";
        correctResult.userPassword = "p@ssword";
        correctResult.userEmail = "cool@gmail.com";
        correctResult.playerOneName = "john1";
        correctResult.playerTwoName = "opponent";
        correctResult.invitationFrom = "johnInv";
        correctResult.invitationTo = "johnTo";
        correctResult.invitationTime = "9/2/2019 2:03PM";
        correctResult.playerQuitting = "quitter";

        Action result = wss.handleClientAction(testJSON);
        assertEquals(result, correctResult);

    }

    @Test
    public void testEmptyAction() {

        String testJSON = "{\"objectType\": \"\"," +
                           "\"communicationType\": \"\"," +
                           "\"communicationVersion\": 0," +
                           "\"matchID\": \"\"," +
                           "\"playerName\": \"\"," +
                           "\"pieceID\": \"\"," +
                           "\"desiredMoves\": null," +
                           "\"userName\": \"\"," +
                           "\"userPassword\": \"\"," +
                           "\"userEmail\": \"\"," +
                           "\"playerOneName\": \"\"," +
                           "\"playerTwoName\": \"\"," +
                           "\"invitationFrom\": \"\"," +
                           "\"invitationTo\": \"\"," +
                           "\"invitationTime\": \"\"," +
                            "\"playerQuitting\": \"\"}";

        Action correctResult = new Action();

        correctResult.objectType = "";
        correctResult.communicationType = "";
        correctResult.communicationVersion = 0;
        correctResult.matchID = "";
        correctResult.playerName = "";
        correctResult.pieceID = "";
        correctResult.desiredMoves = null;
        correctResult.userName = "";
        correctResult.userPassword = "";
        correctResult.userEmail = "";
        correctResult.playerOneName = "";
        correctResult.playerTwoName = "";
        correctResult.invitationFrom = "";
        correctResult.invitationTo = "";
        correctResult.invitationTime = "";
        correctResult.playerQuitting = "";

        Action result = wss.handleClientAction(testJSON);
        assertEquals(result, correctResult);

    }

    @Test
    public void testPartiallyFilledAction() {

        String testJSON = "{\"objectType\": \"Action\"," +
                "\"communicationType\": \"requestMove\"," +
                "\"communicationVersion\": 1," +
                "\"matchID\": \"\"," +
                "\"playerName\": \"JohnDoe\"," +
                "\"pieceID\": \"m\"," +
                "\"desiredMoves\": [1,2,3,4]," +
                "\"userName\": \"john123\"," +
                "\"userPassword\": \"\"," +
                "\"userEmail\": \"\"," +
                "\"playerOneName\": \"john1\"," +
                "\"playerTwoName\": \"\"," +
                "\"invitationFrom\": \"johnInv\"," +
                "\"invitationTo\": \"\"," +
                "\"invitationTime\": \"9/2/2019 2:03PM\"," +
                "\"playerQuitting\": \"quitter\"}";

        Action correctResult = new Action();

        correctResult.objectType = "Action";
        correctResult.communicationType = "requestMove";
        correctResult.communicationVersion = 1;
        correctResult.matchID = "";
        correctResult.playerName = "JohnDoe";
        correctResult.pieceID = "m";
        correctResult.desiredMoves = new int[4];
        correctResult.desiredMoves[0] = 1;
        correctResult.desiredMoves[1] = 2;
        correctResult.desiredMoves[2] = 3;
        correctResult.desiredMoves[3] = 4;
        correctResult.userName = "john123";
        correctResult.userPassword = "";
        correctResult.userEmail = "";
        correctResult.playerOneName = "john1";
        correctResult.playerTwoName = "";
        correctResult.invitationFrom = "johnInv";
        correctResult.invitationTo = "";
        correctResult.invitationTime = "9/2/2019 2:03PM";
        correctResult.playerQuitting = "quitter";

        Action result = wss.handleClientAction(testJSON);
        assertEquals(result, correctResult);


    }

//    @Test
    public void testFullUpdate() {

        Update testUpdate = new Update();

//        testUpdate.objectType = "TestObjectType";
        testUpdate.communicationType = "TestCommunicationType";
        testUpdate.communicationVersion = 7;
        testUpdate.matchID = "TestMatchID";
        testUpdate.playerName = "TestPlayerName";
        testUpdate.pieceID = "E";
        testUpdate.updatedBoard = new String[2][2];
        testUpdate.updatedBoard[0][0] = "1";
        testUpdate.updatedBoard[0][1] = "2";
        testUpdate.updatedBoard[1][0] = "3";
        testUpdate.updatedBoard[1][1] = "4";
        testUpdate.whoseTurn = "TestWhoseTurn";
        testUpdate.message = "TestSuccessMessage";
        testUpdate.userName = "TestUserName";
        testUpdate.userEmail = "TestUserEmail";
        testUpdate.initialBoard = new String[2][2];
        testUpdate.initialBoard[0][0] = "5";
        testUpdate.initialBoard[0][1] = "6";
        testUpdate.initialBoard[1][0] = "7";
        testUpdate.initialBoard[1][1] = "8";
        testUpdate.matchBeginTime = "TestMatchBeginTime";
        testUpdate.invitationFrom = "TestInvitationFrom";
        testUpdate.invitationTo = "TestInvitationTo";
        testUpdate.invitationTime = "TestInvitationTime";
        testUpdate.endCondition = "TestEndCondition";
        testUpdate.winnerName = "TestWinnerName";
        testUpdate.loserName = "TestLoserName";
        testUpdate.matchEndTime = "TestMatchEndTime";
        testUpdate.invitations = new String[2][2];
        testUpdate.invitations[0][0] = "1";
        testUpdate.invitations[0][1] = "2";
        testUpdate.invitations[1][0] = "3";
        testUpdate.invitations[1][1] = "4";
        testUpdate.matchesInProgress = new String[2][2];
        testUpdate.matchesInProgress[0][0] = "5";
        testUpdate.matchesInProgress[0][1] = "6";
        testUpdate.matchesInProgress[1][0] = "7";
        testUpdate.matchesInProgress[1][1] = "8";
        testUpdate.matchesCompleted = new String[2][2];
        testUpdate.matchesCompleted[0][0] = "9";
        testUpdate.matchesCompleted[0][1] = "10";
        testUpdate.matchesCompleted[1][0] = "11";
        testUpdate.matchesCompleted[1][1] = "12";

        String correctJSON = "{\"objectType\":\"TestObjectType\","
                + "\"communicationType\":\"TestCommunicationType\","
                + "\"communicationVersion\":7,"
                + "\"matchID\":\"TestMatchID\","
                + "\"playerName\":\"TestPlayerName\","
                + "\"pieceID\":\"E\","
                + "\"updatedBoard\":[[1,2],[3,4]],"
                + "\"whoseTurn\":\"TestWhoseTurn\","
                + "\"successMessage\":\"TestSuccessMessage\","
                + "\"userName\":\"TestUserName\","
                + "\"userEmail\":\"TestUserEmail\","
                + "\"initialBoard\":[[5,6],[7,8]],"
                + "\"matchBeginTime\":\"TestMatchBeginTime\","
                + "\"invitationFrom\":\"TestInvitationFrom\","
                + "\"invitationTo\":\"TestInvitationTo\","
                + "\"invitationTime\":\"TestInvitationTime\","
                + "\"endCondition\":\"TestEndCondition\","
                + "\"winnerName\":\"TestWinnerName\","
                + "\"loserName\":\"TestLoserName\","
                + "\"matchEndTime\":\"TestMatchEndTime\","
                + "\"invitations\":[[\"1\",\"2\"],[\"3\",\"4\"]],"
                + "\"matchesInProgress\":[[\"5\",\"6\"],[\"7\",\"8\"]],"
                + "\"matchesCompleted\":[[\"9\",\"10\"],[\"11\",\"12\"]]}";


        String resultJSON = wss.sendUpdateToClient(dummyClient, testUpdate);
        assertEquals(correctJSON, resultJSON);

    }

//    @Test
    public void testPartiallyFilledUpdate() {

        Update testUpdate = new Update();

        testUpdate.communicationVersion = 7;
        testUpdate.matchID = "TestMatchID";
        testUpdate.playerName = "TestPlayerName";
        testUpdate.pieceID = "c";
        testUpdate.updatedBoard = new String[2][2];
        testUpdate.updatedBoard[0][0] = "1";
        testUpdate.updatedBoard[0][1] = "2";
        testUpdate.updatedBoard[1][0] = "3";
        testUpdate.updatedBoard[1][1] = "4";
        testUpdate.whoseTurn = "TestWhoseTurn";
        testUpdate.message = "TestSuccessMessage";
        testUpdate.matchBeginTime = "TestMatchBeginTime";
        testUpdate.endCondition = "TestEndCondition";
        testUpdate.winnerName = "TestWinnerName";
        testUpdate.loserName = "TestLoserName";
        testUpdate.matchEndTime = "TestMatchEndTime";
        testUpdate.invitations = new String[2][2];
        testUpdate.invitations[0][0] = "1";
        testUpdate.invitations[0][1] = "2";
        testUpdate.invitations[1][0] = "3";
        testUpdate.invitations[1][1] = "4";

        String correctJSON = "{\"objectType\":null,"
                + "\"communicationType\":null,"
                + "\"communicationVersion\":7,"
                + "\"matchID\":\"TestMatchID\","
                + "\"playerName\":\"TestPlayerName\","
                + "\"pieceID\":\"c\","
                + "\"updatedBoard\":[[1,2],[3,4]],"
                + "\"whoseTurn\":\"TestWhoseTurn\","
                + "\"successMessage\":\"TestSuccessMessage\","
                + "\"userName\":null,"
                + "\"userEmail\":null,"
                + "\"initialBoard\":null,"
                + "\"matchBeginTime\":\"TestMatchBeginTime\","
                + "\"invitationFrom\":null,"
                + "\"invitationTo\":null,"
                + "\"invitationTime\":null,"
                + "\"endCondition\":\"TestEndCondition\","
                + "\"winnerName\":\"TestWinnerName\","
                + "\"loserName\":\"TestLoserName\","
                + "\"matchEndTime\":\"TestMatchEndTime\","
                + "\"invitations\":[[\"1\",\"2\"],[\"3\",\"4\"]],"
                + "\"matchesInProgress\":null,"
                + "\"matchesCompleted\":null}";


        String resultJSON = wss.sendUpdateToClient(dummyClient, testUpdate);
        assertEquals(correctJSON, resultJSON);

    }

//    @Test
    public void testEmptyUpdate() {

        Update testUpdate = new Update();

        String correctJSON = "{\"objectType\":null,"
                + "\"communicationType\":null,"
                + "\"communicationVersion\":0,"
                + "\"matchID\":null,"
                + "\"playerName\":null,"
                + "\"pieceID\":null,"
                + "\"updatedBoard\":null,"
                + "\"whoseTurn\":null,"
                + "\"successMessage\":null,"
                + "\"userName\":null,"
                + "\"userEmail\":null,"
                + "\"initialBoard\":null,"
                + "\"matchBeginTime\":null,"
                + "\"invitationFrom\":null,"
                + "\"invitationTo\":null,"
                + "\"invitationTime\":null,"
                + "\"endCondition\":null,"
                + "\"winnerName\":null,"
                + "\"loserName\":null,"
                + "\"matchEndTime\":null,"
                + "\"invitations\":null,"
                + "\"matchesInProgress\":null,"
                + "\"matchesCompleted\":null}";


        String resultJSON = wss.sendUpdateToClient(dummyClient, testUpdate);
        assertEquals(correctJSON, resultJSON);

    }

}
