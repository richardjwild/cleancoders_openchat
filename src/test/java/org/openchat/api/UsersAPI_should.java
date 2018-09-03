package org.openchat.api;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.openchat.domain.Post;
import org.openchat.domain.PostService;
import org.openchat.domain.User;
import org.openchat.domain.UserService;
import org.openchat.environment.Clock;
import org.openchat.environment.PostIdGenerator;
import org.openchat.environment.UserIdGenerator;
import org.openchat.repository.PostRepository;
import org.openchat.repository.UserRepository;

import java.time.LocalDateTime;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UsersAPI_should extends RestApiTest {

    private static final String POST_TEXT_1 = "first post!";
    private static final String POST_TEXT_2 = "here is some more";
    private static final String USER_ID_1 = randomUUID().toString();
    private static final String USER_ID_2 = randomUUID().toString();
    private static final String POST_ID_1 = randomUUID().toString();
    private static final String POST_ID_2 = randomUUID().toString();
    private static final String USER_NAME_1 = "a user name";
    private static final String USER_NAME_2 = "another user name";
    private static final String ABOUT_USER_1 = "something about the user";
    private static final String ABOUT_USER_2 = "something about the other user";
    private static final String PASSWORD_1 = "a password";
    private static final String PASSWORD_2 = "another password";

    @Mock
    private Clock clock;
    @Mock
    private PostIdGenerator postIdGenerator;
    @Mock
    private UserIdGenerator userIdGenerator;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;

    private UsersAPI usersAPI;

    @Before
    public void setup() {
        PostService postService = new PostService(clock, postIdGenerator, postRepository);
        UserService userService = new UserService(userIdGenerator, userRepository);
        usersAPI = new UsersAPI(postService, userService);
        inOrder = Mockito.inOrder(response);
    }

    @Test
    public void return_empty_list_of_posts_given_none_posted() throws JSONException {
        given(postRepository.retrievePosts()).willReturn(emptyList());
        givenPathParameter("userId", USER_ID_1);

        String actual = usersAPI.retrievePosts(request, response);

        String expected = "[]";
        assertJson(actual, expected);
        verify(response).type("application/json");
        verify(response).status(200);
    }

    @Test
    public void create_a_post() throws JSONException {
        LocalDateTime dateTime = LocalDateTime.of(2018, 8, 29, 8, 16, 23);
        givenTimeIs(dateTime);
        givenNextPostIdIs(POST_ID_1);
        givenPathParameter("userId", USER_ID_1);
        givenRequestBody("{\"text\":\"" + POST_TEXT_1 + "\"}");

        String actual = usersAPI.createPost(request, response);

        verify(postRepository).storePost(new Post(USER_ID_1, POST_TEXT_1, dateTime, POST_ID_1));
        verify(response).type("application/json");
        verify(response).status(CREATED);
        String timestamp = "2018-08-29T08:16:23Z";
        String expected = postAsJson(POST_ID_1, USER_ID_1, timestamp, POST_TEXT_1);
        assertJson(actual, expected);
    }

    @Test
    public void retrieve_the_post_given_one_has_been_posted() throws JSONException {
        LocalDateTime dateTime = LocalDateTime.of(2018, 8, 29, 8, 16, 23);
        given(postRepository.retrievePosts()).willReturn(
                singletonList(new Post(USER_ID_1, POST_TEXT_1, dateTime, POST_ID_1)));
        givenPathParameter("userId", USER_ID_1);

        String actual = usersAPI.retrievePosts(request, response);

        String timestamp = "2018-08-29T08:16:23Z";
        String expected = "[" + postAsJson(POST_ID_1, USER_ID_1, timestamp, POST_TEXT_1) + "]";
        assertJson(actual, expected);
        verify(response).type("application/json");
        verify(response).status(OK);
    }

    @Test
    public void retrieve_all_posts_given_multiple_have_been_posted() throws JSONException {
        LocalDateTime earlier = LocalDateTime.of(2018, 8, 31, 12, 1, 16);
        LocalDateTime later = LocalDateTime.of(2018, 9, 2, 23, 59, 59);
        given(postRepository.retrievePosts()).willReturn(asList(
                new Post(USER_ID_1, POST_TEXT_2, later, POST_ID_2),
                new Post(USER_ID_1, POST_TEXT_1, earlier, POST_ID_1)));
        givenPathParameter("userId", USER_ID_1);

        String actual = usersAPI.retrievePosts(request, response);

        String earlierTimestamp = "2018-08-31T12:01:16Z";
        String laterTimestamp = "2018-09-02T23:59:59Z";
        String expected = "[" +
                postAsJson(POST_ID_2, USER_ID_1, laterTimestamp, POST_TEXT_2) + "," +
                postAsJson(POST_ID_1, USER_ID_1, earlierTimestamp, POST_TEXT_1) +
                "]";
        assertJson(actual, expected);
        verify(response).type("application/json");
        verify(response).status(OK);
    }

    @Test
    public void retrieve_a_specific_users_post() throws JSONException {
        LocalDateTime dateTime1 = LocalDateTime.of(2018, 8, 31, 12, 1, 16);
        LocalDateTime dateTime2 = LocalDateTime.of(2018, 9, 2, 23, 59, 59);
        given(postRepository.retrievePosts()).willReturn(asList(
                new Post(USER_ID_1, POST_TEXT_1, dateTime1, POST_ID_1),
                new Post(USER_ID_2, POST_TEXT_2, dateTime2, POST_ID_2)));
        givenPathParameter("userId", USER_ID_1);

        String actual = usersAPI.retrievePosts(request, response);

        String timestamp = "2018-08-31T12:01:16Z";
        String expected = "[" + postAsJson(POST_ID_1, USER_ID_1, timestamp, POST_TEXT_1) + "]";
        assertJson(actual, expected);
        verify(response).type("application/json");
        verify(response).status(OK);
    }

    @Test
    public void register_a_user() throws JSONException {
        givenNextUserIdIs(USER_ID_1);
        givenRequestBody("{" +
                "\"username\":\"" + USER_NAME_1 + "\"," +
                "\"about\":\"" + ABOUT_USER_1 + "\"," +
                "\"password\":\"" + PASSWORD_1 + "\"" +
                "}");

        String actual = usersAPI.registerNewUser(request, response);

        verify(userRepository).storeUser(new User(USER_ID_1, USER_NAME_1, ABOUT_USER_1, PASSWORD_1));
        verify(response).type("application/json");
        verify(response).status(CREATED);
        assertJson(actual, "{" +
                "\"id\":\"" + USER_ID_1 + "\"," +
                "\"username\":\"" + USER_NAME_1 + "\"," +
                "\"about\":\"" + ABOUT_USER_1 + "\"" +
                "}");
    }

    @Test
    public void return_all_registered_users() throws JSONException {
        given(userRepository.retrieveUsers()).willReturn(asList(
                new User(USER_ID_1, USER_NAME_1, ABOUT_USER_1, PASSWORD_1),
                new User(USER_ID_2, USER_NAME_2, ABOUT_USER_2, PASSWORD_2)));

        String actual = usersAPI.retrieveUsers(request, response);

        verify(response).type("application/json");
        verify(response).status(OK);
        assertJson(actual, "[" +
                "{" +
                "\"id\":\"" + USER_ID_1 + "\"," +
                "\"username\":\"" + USER_NAME_1 + "\"," +
                "\"about\":\"" + ABOUT_USER_1 + "\"" +
                "}," +
                "{" +
                "\"id\":\"" + USER_ID_2 + "\"," +
                "\"username\":\"" + USER_NAME_2 + "\"," +
                "\"about\":\"" + ABOUT_USER_2 + "\"" +
                "}" +
                "]");
    }

    @Test
    public void return_a_users_wall() throws JSONException {
        LocalDateTime earlier = LocalDateTime.of(2018, 8, 31, 12, 1, 16);
        LocalDateTime later = LocalDateTime.of(2018, 9, 2, 23, 59, 59);
        User user1 = new User(USER_ID_1, USER_NAME_1, ABOUT_USER_1, PASSWORD_1);
        User user2 = new User(USER_ID_2, USER_NAME_2, ABOUT_USER_2, PASSWORD_2);
        user1.follow(user2);
        given(userRepository.retrieveUsers()).willReturn(asList(user1, user2));
        given(postRepository.retrievePosts()).willReturn(asList(
                new Post(USER_ID_1, POST_TEXT_1, earlier, POST_ID_1),
                new Post(USER_ID_2, POST_TEXT_2, later, POST_ID_2)));
        givenPathParameter("userId", USER_ID_1);

        String actual = usersAPI.wall(request, response);

        verify(response).status(OK);
        verify(response).type("application/json");
        String earlierTimestamp = "2018-08-31T12:01:16Z";
        String laterTimestamp = "2018-09-02T23:59:59Z";
        assertJson(actual, "[" +
                postAsJson(POST_ID_2, USER_ID_2, laterTimestamp, POST_TEXT_2) + "," +
                postAsJson(POST_ID_1, USER_ID_1, earlierTimestamp, POST_TEXT_1) +
                "]");
    }

    private String postAsJson(String postId, String userId, String timestamp, String text) {
        return "{" +
                "\"text\":\"" + text + "\"," +
                "\"dateTime\":\"" + timestamp + "\"," +
                "\"userId\":\"" + userId + "\"," +
                "\"postId\":\"" + postId + "\"" +
                "}";
    }

    private void givenNextPostIdIs(String nextPostId) {
        given(postIdGenerator.nextId()).willReturn(nextPostId);
    }

    private void givenNextUserIdIs(String nextUserId) {
        given(userIdGenerator.nextId()).willReturn(nextUserId);
    }

    private void givenTimeIs(LocalDateTime dateTime) {
        given(clock.now()).willReturn(dateTime);
    }
}
