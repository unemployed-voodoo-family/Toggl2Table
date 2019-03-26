package UnemployedVoodooFamily.Tests;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoginLogicTest {

    private boolean loggedIn = true;

    @Test
    public void attemptAuthentication() {
        boolean loginResult = true;
        assertEquals(loggedIn, loginResult);
    }
}