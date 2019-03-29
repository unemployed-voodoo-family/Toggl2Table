package UnemployedVoodooFamily.Tests;

import UnemployedVoodooFamily.Logic.LoginLogic;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class LoginLogicTest extends LoginLogic {

    private LoginLogic loginLogic = new LoginLogic();

    @Test
    public void successfulAttemptAuthentication() {
        //Not yet implemented
    }

    @Test
    public void failedAttemptAuthentication() {
        boolean testResult = loginLogic.attemptAuthentication("Lars", "password", false, false);
        assertFalse(testResult);
    }

    @Test
    public void successfulSaveUsernameAndPassword() {
        //Not yet implemented
    }
}