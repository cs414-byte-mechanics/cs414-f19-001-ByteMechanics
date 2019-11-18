package webconnection;

import java.util.Arrays;

public class Update {

    public String communicationType;
    public String matchID;
    public String playerName;
    public String pieceID;
    public String[][] updatedBoard;
    public String whoseTurn;
    public String userName;
    public String userEmail;
    public String statusMessage;

    public String[][] initialBoard;
    public String matchBeginTime;
    public String invitationFrom;
    public String invitationTo;
    public String invitationTime;
    public String endCondition;
    public String winnerName;
    public String loserName;
    public String matchEndTime;

    public boolean userFound;
    public boolean invitationSent;
    public String[][] invitations;
    public Object[][] matchesInProgress;
    public String[][] matchesCompleted;


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
                                    + "userFound: " + this.userFound + "\n"
                                    + "invitationSent: " + this.invitationSent + "\n"
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
