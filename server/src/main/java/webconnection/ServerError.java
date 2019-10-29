package webconnection;

public class ServerError
{

  public String objectType;
  public int errorCode;
  public String errorMessage;

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
