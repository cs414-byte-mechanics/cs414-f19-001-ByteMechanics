package webconnection;

public class ServerError
{
    public String objectType ;
    public int errorCode;
    public String errorMessage;

  /* to keep some tests passed this constructor is necessary*/
    public ServerError(){
        this.objectType = "ServerError";
    }

    public ServerError(int errorCode) {
        this.errorCode = errorCode; }

    @Override
    public String toString() {
        String stringRepresentation = ""
            + "objectType: " + this.objectType + "\n"
            + "errorCode: " + this.errorCode + "\n"
//            + "errorMessage: " + this.errorMessage + "\n";
            + "errorMessage: " + this.getErrorMessage(errorCode) + "\n";
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

    public String getErrorMessage( int errorCode){
    switch (errorCode){
        case 100: return "Invalid password when attempting a login";
        case 101: return " Username not found when attempting a login";
        case 102: return "Invalid move, select another move";
        case 103: return " Player not found in search (in search for who to invite)";

        default:
            System.err.println("This error is not handled yet!");
            return null;
        }
    }

}
