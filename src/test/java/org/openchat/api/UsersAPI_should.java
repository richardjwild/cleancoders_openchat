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
    private static final String USER_ID = randomUUID().toString();
    private static final String POST_ID_1 = randomUUID().toString();
    private static final String POST_ID_2 = randomUUID().toString();
    private static final String USER_NAME = "a user name";
    private static final String ABOUT_USER = "something about the user";
    private static final String PASSWORD = "a password";

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
        givenPathParameter("userId", USER_ID);
        givenRequestBody("{\"text\":\"" + POST_TEXT_1 + "\"}");

        String actual = usersAPI.createPost(request, response);

        verify(postRepository).storePost(new Post(USER_ID, POST_TEXT_1, dateTime, POST_ID_1));
        verify(response).type("application/json");
        verify(response).status(CREATED);
        String timestamp = "2018-08-29T08:16:23Z";
        String expected = postAsJson(POST_ID_1, USER_ID, timestamp, POST_TEXT_1);
        assertJson(actual, expected);
    }

    @Test
    public void retrieve_the_post_given_one_has_been_posted() throws JSONException {
        LocalDateTime dateTime = LocalDateTime.of(2018, 8, 29, 8, 16, 23);
        given(postRepository.retrievePosts()).willReturn(
                singletonList(new Post(USER_ID, POST_TEXT_1, dateTime, POST_ID_1)));

        String actual = usersAPI.retrievePosts(request, response);

        String timestamp = "2018-08-29T08:16:23Z";
        String expected = "[" + postAsJson(POST_ID_1, USER_ID, timestamp, POST_TEXT_1) + "]";
        assertJson(actual, expected);
        verify(response).status(OK);
    }

    @Test
    public void retrieve_all_posts_given_multiple_have_been_posted() throws JSONException {
        LocalDateTime dateTime1 = LocalDateTime.of(2018, 8, 31, 12, 1, 16);
        LocalDateTime dateTime2 = LocalDateTime.of(2018, 9, 2, 23, 59, 59);
        given(postRepository.retrievePosts()).willReturn(asList(
                new Post(USER_ID, POST_TEXT_1, dateTime1, POST_ID_1),
                new Post(USER_ID, POST_TEXT_2, dateTime2, POST_ID_2)));

        String actual = usersAPI.retrievePosts(request, response);

        String timestamp1 = "2018-08-31T12:01:16Z";
        String timestamp2 = "2018-09-02T23:59:59Z";
        String expected = "[" +
                postAsJson(POST_ID_1, USER_ID, timestamp1, POST_TEXT_1) + "," +
                postAsJson(POST_ID_2, USER_ID, timestamp2, POST_TEXT_2) +
                "]";
        assertJson(actual, expected);
        verify(response).status(OK);
    }

    @Test
    public void register_a_user() throws JSONException {
        givenNextUserIdIs(USER_ID);
        givenRequestBody("{" +
                "\"username\":\"" + USER_NAME + "\"," +
                "\"about\":\"" + ABOUT_USER + "\"," +
                "\"password\":\"" + PASSWORD + "\"" +
                "}");

        String actual = usersAPI.registerNewUser(request, response);

        verify(response).status(CREATED);
        verify(userRepository).storeUser(new User(USER_ID, USER_NAME, ABOUT_USER, PASSWORD));
        assertJson(actual, "{" +
                "\"id\":\"" + USER_ID + "\"," +
                "\"username\":\"" + USER_NAME + "\"," +
                "\"about\":\"" + ABOUT_USER + "\"" +
                "}");
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
