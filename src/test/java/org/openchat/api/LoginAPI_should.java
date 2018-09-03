package org.openchat.api;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openchat.domain.User;
import org.openchat.domain.UserService;
import org.openchat.environment.UserIdGenerator;
import org.openchat.repository.UserRepository;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoginAPI_should extends RestApiTest {

    private static final String USER_NAME = "a user name";
    private static final String USER_ID = "a user id";
    private static final String ABOUT = "about the user";
    private static final String PASSWORD = "a password";

    @Mock
    private UserRepository userRepository;

    private LoginAPI loginAPI;

    @Before
    public void setup() {
        UserService userService = new UserService(new UserIdGenerator(), userRepository);
        loginAPI = new LoginAPI(userService);
    }

    @Test
    public void reject_login_by_unregistered_user() {
        given(userRepository.retrieveUsers()).willReturn(emptyList());
        givenRequestBody("{" +
                "\"username\":\"" + USER_NAME + "\"" +
                "}");

        String actual = loginAPI.login(request, response);

        verify(response).status(404);
        assertThat(actual).isEqualTo("Invalid credentials.");
    }

    @Test
    public void accept_correct_login_by_registered_user() throws JSONException {
        given(userRepository.retrieveUsers()).willReturn(singletonList(
                new User(USER_ID, USER_NAME, ABOUT, PASSWORD)));
        givenRequestBody("{" +
                "\"username\":\"" + USER_NAME + "\"," +
                "\"password\":\"" + PASSWORD + "\"" +
                "}");

        String actual = loginAPI.login(request, response);

        verify(response).status(200);
        verify(response).type("application/json");
        assertJson(actual, "{" +
                "\"username\":\"" + USER_NAME + "\"," +
                "\"id\":\"" + USER_ID + "\"," +
                "\"about\":\"" + ABOUT + "\"" +
                "}");
    }

    @Test
    public void reject_incorrect_login_by_registered_user() {
        given(userRepository.retrieveUsers()).willReturn(singletonList(
                new User(USER_ID, USER_NAME, ABOUT, PASSWORD)));
        givenRequestBody("{" +
                "\"username\":\"" + USER_NAME + "\"," +
                "\"password\":\"incorrect password\"" +
                "}");

        String actual = loginAPI.login(request, response);

        verify(response).status(404);
        assertThat(actual).isEqualTo("Invalid credentials.");
    }

}
