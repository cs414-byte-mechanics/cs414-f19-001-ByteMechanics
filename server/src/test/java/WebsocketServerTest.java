import org.junit.Before;
import webconnection.WebsocketServer;
import webconnection.Action;
import jdk.jfr.StackTrace;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.annotation.processing.SupportedAnnotationTypes;

import static org.junit.Assert.*;
import Game.GamePiece;
import Game.GameBoard;

import java.util.ArrayList;
import java.util.Arrays;


//@RunWith(JUnit4.class)
public class WebsocketServerTest {

    WebsocketServer wss;

    @Before
    public void initialize() {
        wss = new WebsocketServer();

    }

    @Test
    public void testFullMessage() {

        String testJSON = "{\"objectType\": \"Action\"," +
                           "\"communicationType\": \"requestMove\"," +
                           "\"communicationVersion\": 1," +
                           "\"matchID\": \"5654\"," +
                           "\"playerName\": \"JohnDoe\"," +
                           "\"pieceID\": 16," +
                           "\"desiredMoves\": [[1,2],[3,4]]," +
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
        correctResult.pieceID = 16;
        correctResult.desiredMoves = new int[2][2];
        correctResult.desiredMoves[0][0] = 1;
        correctResult.desiredMoves[0][1] = 2;
        correctResult.desiredMoves[1][0] = 3;
        correctResult.desiredMoves[1][1] = 4;
        correctResult.userName = "john123";
        correctResult.userPassword = "p@ssword";
        correctResult.userEmail = "cool@gmail.com";
        correctResult.playerOneName = "john1";
        correctResult.playerTwoName = "opponent";
        correctResult.invitationFrom = "johnInv";
        correctResult.invitationTo = "johnTo";
        correctResult.invitationTime = "9/2/2019 2:03PM";
        correctResult.playerQuitting = "quitter";

        Action result = wss.parseMessage(testJSON);
        assertEquals(result, correctResult);

    }

    @Test
    public void testEmptyMessage() {

        String testJSON = "{\"objectType\": \"\"," +
                           "\"communicationType\": \"\"," +
                           "\"communicationVersion\": 0," +
                           "\"matchID\": \"\"," +
                           "\"playerName\": \"\"," +
                           "\"pieceID\": 0," +
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
        correctResult.pieceID = 0;
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

        Action result = wss.parseMessage(testJSON);
        assertEquals(result, correctResult);

    }

    @Test
    public void testPartiallyFilledMessage() {

        String testJSON = "{\"objectType\": \"Action\"," +
                "\"communicationType\": \"requestMove\"," +
                "\"communicationVersion\": 1," +
                "\"matchID\": \"\"," +
                "\"playerName\": \"JohnDoe\"," +
                "\"pieceID\": 16," +
                "\"desiredMoves\": [[1,2],[3,4]]," +
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
        correctResult.pieceID = 16;
        correctResult.desiredMoves = new int[2][2];
        correctResult.desiredMoves[0][0] = 1;
        correctResult.desiredMoves[0][1] = 2;
        correctResult.desiredMoves[1][0] = 3;
        correctResult.desiredMoves[1][1] = 4;
        correctResult.userName = "john123";
        correctResult.userPassword = "";
        correctResult.userEmail = "";
        correctResult.playerOneName = "john1";
        correctResult.playerTwoName = "";
        correctResult.invitationFrom = "johnInv";
        correctResult.invitationTo = "";
        correctResult.invitationTime = "9/2/2019 2:03PM";
        correctResult.playerQuitting = "quitter";

        Action result = wss.parseMessage(testJSON);
        assertEquals(result, correctResult);


    }

}
