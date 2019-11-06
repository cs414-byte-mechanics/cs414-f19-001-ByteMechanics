package webconnection;

public class ServerError extends Update
{

    public String objectType;
    public int errorCode;
    public String errorMessage;

    public ServerError() { };

    public ServerError(int errorCode) {
        this.objectType = "ServerError";
        this.communicationType = "ServerError";
        this.errorCode = errorCode;
        this.errorMessage = getErrorMessage(this.errorCode);
    }

    @Override
    public String toString() {

        String stringRepresentation = ""
                + "objectType: " + this.objectType + "\n"
                + "errorCode: " + this.errorCode + "\n"
                + "errorMessage: " + this.errorMessage + "\n";
        return stringRepresentation;

    }

    @Override
    public boolean equals(Object o) {

        if (this.toString().equals(o.toString())) {
            return true;
        } else {
            return false;
        }
    }

    public static String getErrorMessage( int errorCode){
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
