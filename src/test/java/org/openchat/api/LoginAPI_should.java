package org.openchat.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class LoginAPI_should extends RestApiTest {

    private LoginAPI loginAPI;

    @Before
    public void setup() {
        loginAPI = new LoginAPI();
    }

    @Test
    public void reject_login_by_unregistered_user() {
        String actual = loginAPI.login(request, response);

        Mockito.verify(response).status(404);
        assertThat(actual).isEqualTo("Invalid credentials.");
    }

}
