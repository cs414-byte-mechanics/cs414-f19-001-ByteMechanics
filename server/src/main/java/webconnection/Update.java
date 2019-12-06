package webconnection;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Update {

    public String communicationType;
    public String[] recipients;
    public String matchID;
    public String playerName;
    public String pieceID;
    public String[][] updatedBoard;
    public String whoseTurn;
    public String userName;
    public String userEmail;
    public String statusMessage;
    public String playerOneName;
    public String playerTwoName;

    public String[][] initialBoard;
    public String matchBeginTime;
    public String invitationFrom;
    public String invitationTo;
    public String invitationTime;
    public String endCondition;
    public String winnerName;
    public String loserName;
    public String matchEndTime;

    public String[] searchResults;
//    public boolean userFound;
    public boolean invitationSent;
    public String[][] invitations;
    public Object[][] matchesInProgress;
    public String[][] matchesCompleted;
    public List<String> sentToNames;
    public List<String> sentToTimes;
    public List<String> receivedFromNames;
    public List<String> receivedFromTimes;


    @Override
    public String toString() {

        String stringRepresentation = "communicationType: " + this.communicationType + "\n"
                                    + "matchID: " + this.matchID + "\n"
                                    + "playerName: " + this.playerName + "\n"
                                    + "pieceID: " + this.pieceID + "\n"
                                    + "updatedBoard: " + Arrays.deepToString(this.updatedBoard) + "\n"
                                    + "whoseTurn: " + this.whoseTurn + "\n"
                                    + "userName: " + this.userName + "\n"
                                    + "userEmail: " + this.userEmail + "\n"
                                    + "initialBoard: " + Arrays.deepToString(this.initialBoard) + "\n"
                                    + "matchBeginTime: " + this.matchBeginTime + "\n"
                                    + "invitationFrom: " + this.invitationFrom + "\n"
                                    + "invitationTo: " + this.invitationTo + "\n"
                                    + "invitationTime: " + this.invitationTime + "\n"
                                    + "endCondition: " + this.endCondition + "\n"
                                    + "winnerName: " + this.winnerName + "\n"
                                    + "loserName: " + this.loserName + "\n"
                                    + "matchEndTime: " + this.matchEndTime + "\n"
                                    + "statusMessage: " + this.statusMessage + "\n"
                                    + "invitationSent: " + this.invitationSent + "\n"
                                    + "invitation: " + Arrays.deepToString(this.invitations) + "\n"
                                    + "matchesInProgress: " + Arrays.deepToString(this.matchesInProgress) + "\n"
                                    + "matchesCompleted: " + Arrays.deepToString(this.matchesCompleted) + "\n"
                                    + "sentToNames: " + this.sentToNames + "\n"
                                    + "sentToTimes: " + this.sentToTimes + "\n"
                                    + "receivedFromNames" + this.receivedFromNames + "\n"
                                    + "receivedFromTimes" + this.receivedFromTimes;

        return stringRepresentation;

    }

    @Override
    public boolean equals(Object o) {

        if (this.toString().equals(o.toString())) {
            return true;
        }
        else {
            return false;
        }

    }

}
