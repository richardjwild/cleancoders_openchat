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

import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FollowingsAPI_should extends RestApiTest {

    private static final String FOLLOWER_ID = UUID.randomUUID().toString();
    private static final String FOLLOWING_USER_1_ID = UUID.randomUUID().toString();
    private static final String FOLLOWING_USER_2_ID = UUID.randomUUID().toString();
    private static final String FOLLOWER_NAME = "a user";
    private static final String FOLLOWING_USER_1_NAME = "a user being followed";
    private static final String FOLLOWING_USER_2_NAME = "another user being followed";
    private static final String ABOUT_FOLLOWER = "about this user";
    private static final String ABOUT_USER_1_FOLLOWING = "about the user being followed";
    private static final String ABOUT_USER_2_FOLLOWING = "about the other user being followed";
    private static final String FOLLOWER_PASSWORD = "a password";
    private static final String FOLLOWING_USER_1_PASSWORD = "following user password";
    private static final String FOLLOWING_USER_2_PASSWORD = "other following user password";

    @Mock
    private UserRepository userRepository;

    private FollowingsAPI followingsAPI;

    @Before
    public void setup() {
        UserService userService = new UserService(new UserIdGenerator(), userRepository);
        followingsAPI = new FollowingsAPI(userService);
    }

    @Test
    public void return_no_users_given_none_are_being_followed() throws JSONException {
        given(userRepository.retrieveUsers()).willReturn(singletonList(
                new User(FOLLOWER_ID, FOLLOWER_NAME, ABOUT_FOLLOWER, FOLLOWER_PASSWORD)));
        givenPathParameter("followerId", FOLLOWER_ID);

        String actual = followingsAPI.usersFollowing(request, response);

        verify(response).status(OK);
        verify(response).type("application/json");
        assertJson(actual, "[]");
    }

    @Test
    public void return_users_being_followed() throws JSONException {
        User follower = new User(FOLLOWER_ID, FOLLOWER_NAME, ABOUT_FOLLOWER, FOLLOWER_PASSWORD);
        User following1 = new User(FOLLOWING_USER_1_ID, FOLLOWING_USER_1_NAME, ABOUT_USER_1_FOLLOWING, FOLLOWING_USER_1_PASSWORD);
        User following2 = new User(FOLLOWING_USER_2_ID, FOLLOWING_USER_2_NAME, ABOUT_USER_2_FOLLOWING, FOLLOWING_USER_2_PASSWORD);
        follower.follow(following1);
        follower.follow(following2);
        given(userRepository.retrieveUsers()).willReturn(asList(follower, following1, following2));
        givenPathParameter("followerId", FOLLOWER_ID);

        String actual = followingsAPI.usersFollowing(request, response);

        verify(response).status(OK);
        verify(response).type("application/json");
        assertJson(actual, "[" +
                "{" +
                "\"id\":\"" + FOLLOWING_USER_1_ID + "\"," +
                "\"username\":\"" + FOLLOWING_USER_1_NAME + "\"," +
                "\"about\":\"" + ABOUT_USER_1_FOLLOWING + "\"" +
                "}," +
                "{" +
                "\"id\":\"" + FOLLOWING_USER_2_ID + "\"," +
                "\"username\":\"" + FOLLOWING_USER_2_NAME + "\"," +
                "\"about\":\"" + ABOUT_USER_2_FOLLOWING + "\"" +
                "}" +
                "]");
    }

    @Test
    public void create_a_following_relationship() throws JSONException {
        User follower = new User(FOLLOWER_ID, FOLLOWER_NAME, ABOUT_FOLLOWER, FOLLOWER_PASSWORD);
        User toFollow = new User(FOLLOWING_USER_1_ID, FOLLOWING_USER_1_NAME, ABOUT_USER_1_FOLLOWING, FOLLOWING_USER_1_PASSWORD);
        given(userRepository.retrieveUsers()).willReturn(asList(follower, toFollow));
        givenRequestBody("{" +
                "\"followerId\":\"" + FOLLOWER_ID + "\"," +
                "\"followeeId\":\"" + FOLLOWING_USER_1_ID + "\"" +
                "}");

        String actual = followingsAPI.createFollowingRelationship(request, response);

        follower.follow(toFollow);
        verify(userRepository).updateUser(follower);
        verify(response).status(CREATED);
        verify(response).type("application/json");
        assertJson(actual, "");
    }

}
