package webconnection;

public class ServerError extends Update
{
    int errorCode;

    public ServerError() { };

    public ServerError(int errorCode, String message) {
        this.communicationType = "error";
        this.errorCode = errorCode;
        this.statusMessage = message;
    }

    @Override
    public String toString() {

        String stringRepresentation = ""
                + "communicationType: " + this.communicationType + "\n"
                + "errorCode: " + this.errorCode + "\n"
                + "errorMessage: " + this.statusMessage + "\n";
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
}
