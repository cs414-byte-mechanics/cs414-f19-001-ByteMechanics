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
                "{\"communicationType\": \"\"," +
                "\"errorCode\": \"\"," +
                "\"statusMessage\": \"\"}";

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
                "{\"communicationType\": \"error\"," +
                "\"errorCode\": \"100\"," +
                "\"statusMessage\": \"Invalid password\"}";

        try {
            testError0 =  gson.fromJson(testJSON, ServerError.class);
            System.out.println("\ntest0:\n" + testError0);
        }catch(Exception e) {
            System.out.println(e);
        }

        testError1 = new ServerError(100, "Invalid password");
        System.out.println("\ntest1:\n" + testError1);

        assertEquals(testError0,testError1);

    }

    @Test
    public void partiallyFilledServerError()
    {
        String testJSON = "" +
                "{\"communicationType\": \"error\"," +
                "\"errorCode\": \"100\"," +
                "\"statusMessage\": \"\"}";

        testError0 = new ServerError();

        try {
            testError0 =  gson.fromJson(testJSON, ServerError.class);
        }catch(Exception e) {}

        testError1 = new ServerError(100, "");

        assertEquals(testError0,testError1);

    }

}
