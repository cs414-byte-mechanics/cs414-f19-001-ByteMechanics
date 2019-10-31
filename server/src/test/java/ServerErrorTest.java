import org.junit.Before;
import org.junit.Test;
import webconnection.ServerError;
import static org.junit.Assert.*;
import com.google.gson.*;

public class ServerErrorTest
{
    private Gson gson;
    private ServerError testError0;
    private ServerError testError1;

    @Before
    public void initialize()
    {
        gson = new GsonBuilder().create();
        testError0 = new ServerError();
        testError1 = new ServerError();
    }

    @Test
    public void testEmptyServerError()
    {
        String testJSON = "" +
                "{\"objectType\": \"\"," +
                "\"errorCode\": \"\"," +
                "\"errorMessage\": \"\"}";

        testError0 = new ServerError();

        try {
            testError0 =  gson.fromJson(testJSON, ServerError.class);
        }catch(Exception e) {}

        testError1 = new ServerError();

        assertEquals(testError0,testError1);

    }

    @Test
    public void testFullServerError()
    {
        String testJSON = "" +
                "{\"objectType\": \"ServerError\"," +
                "\"errorCode\": \"100\"," +
                "\"errorMessage\": \"Invalid password\"}";

        testError0 = new ServerError();

        try {
            testError0 =  gson.fromJson(testJSON, ServerError.class);
        }catch(Exception e) {}

        testError1 = new ServerError();

        testError1.objectType = "ServerError";
        testError1.errorCode = 100;
        testError1.errorMessage = "Invalid password";

        assertEquals(testError0,testError1);

    }

    @Test
    public void partiallyFilledServerError()
    {
        String testJSON = "" +
                "{\"objectType\": \"\"," +
                "\"errorCode\": \"100\"," +
                "\"errorMessage\": \"Invalid password\"}";

        testError0 = new ServerError();

        try {
            testError0 =  gson.fromJson(testJSON, ServerError.class);
        }catch(Exception e) {}

        testError1 = new ServerError();

        testError1.objectType = "";
        testError1.errorCode = 100;
        testError1.errorMessage = "Invalid password";

        assertEquals(testError0,testError1);

    }

}
