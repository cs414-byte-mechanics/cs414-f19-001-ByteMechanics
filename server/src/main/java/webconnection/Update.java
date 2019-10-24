package webconnection;

import java.util.Arrays;

public class Update {

    public final String objectType = "Update";
    public String communicationType;
    public transient int communicationVersion;
    public transient String matchID;
    public transient String playerName;
    public transient int pieceID;
    public int[][] updatedBoard;
    public transient String whoseTurn;
    public transient String successMessage;
    public transient String userName;
    public transient String userEmail;
    public transient int[][] initialBoard;
    public transient String matchBeginTime;
    public transient String invitationFrom;
    public transient String invitationTo;
    public transient String invitationTime;
    public transient String endCondition;
    public transient String winnerName;
    public transient String loserName;
    public transient String matchEndTime;
    public transient String[][] invitations;
    public transient Object[][] matchesInProgress;
    public transient String[][] matchesCompleted;

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
