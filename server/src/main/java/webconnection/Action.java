package webconnection;

import java.util.Arrays;

public class Action {

  public String objectType = "Action";
  public String communicationType;
  public int communicationVersion;
  public String matchID;
  public String playerName;
  public String pieceID;
  public int[] desiredMoves;
  public String userName;
  public String userPassword;
  public String userEmail;
  public String playerOneName;
  public String playerTwoName;
  public String invitationFrom;
  public String invitationTo;
  public String invitationTime;
  public String playerQuitting;
  public String endCondition;

  @Override
  public String toString() {

    String stringRepresentation = "objectType: " + this.objectType + "\n"
                                  + "communicationType: " + this.communicationType + "\n"
                                  + "communicationVersion: " + this.communicationVersion + "\n"
                                  + "matchID: " + this.matchID + "\n"
                                  + "playerName: " + this.playerName + "\n"
                                  + "pieceID: " + this.pieceID + "\n"
                                  + "desiredMoves: " + Arrays.toString(this.desiredMoves) + "\n"
                                  + "userName: " + this.userName + "\n"
                                  + "userPassword: " + this.userPassword + "\n"
                                  + "userEmail: " + this.userEmail + "\n"
                                  + "playerOneName: " + this.playerOneName + "\n"
                                  + "playerTwoName: " + this.playerTwoName + "\n"
                                  + "invitationFrom: " + this.invitationFrom + "\n"
                                  + "invitationTo: " + this.invitationTo + "\n"
                                  + "invitationTime: " + this.invitationTime + "\n"
                                  + "playerQuitting: " + this.playerQuitting;
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

