package webconnection;

public class ServerError extends Update
{


//  public int errorCode;
//  public String errorMessage;

    public ServerError() { };

    public ServerError(int errorCode, String errorMessage) {
        this.objectType = "ServerError";
        this.communicationType = "ServerError";
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
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
        }
        else {
            return false;
        }

    }
}
