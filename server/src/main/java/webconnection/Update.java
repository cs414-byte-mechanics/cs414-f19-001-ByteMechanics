package webconnection;

import java.util.Arrays;

public class Update {

    public final String objectType = "Update";
    public String communicationType;
    public int communicationVersion;
    public String matchID;
    public String playerName;
    public String pieceID;
    public int[][] updatedBoard;
    public String whoseTurn;
    public String successMessage;
    public String userName;
    public String userEmail;
    public int[][] initialBoard;
    public String matchBeginTime;
    public String invitationFrom;
    public String invitationTo;
    public String invitationTime;
    public String endCondition;
    public String winnerName;
    public String loserName;
    public String matchEndTime;
    public String[][] invitations;
    public Object[][] matchesInProgress;
    public String[][] matchesCompleted;

    @Override
    public String toString() {

        String stringRepresentation = "objectType: " + this.objectType + "\n"
                                    + "communicationType: " + this.communicationType + "\n"
                                    + "communicationVersion: " + this.communicationVersion + "\n"
                                    + "matchID: " + this.matchID + "\n"
                                    + "playerName: " + this.playerName + "\n"
                                    + "pieceID: " + this.pieceID + "\n"
                                    + "updatedBoard: " + Arrays.deepToString(this.updatedBoard) + "\n"
                                    + "whoseTurn: " + this.whoseTurn + "\n"
                                    + "successMessage: " + this.successMessage + "\n"
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
                                    + "invitation: " + Arrays.deepToString(this.invitations) + "\n"
                                    + "matchesInProgress: " + Arrays.deepToString(this.matchesInProgress) + "\n"
                                    + "matchesCompleted: " + Arrays.deepToString(this.matchesCompleted) + "\n";
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
