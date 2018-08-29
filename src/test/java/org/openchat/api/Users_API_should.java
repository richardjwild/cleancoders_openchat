package org.openchat.api;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.openchat.environment.Clock;
import org.openchat.environment.PostIdGenerator;
import org.skyscreamer.jsonassert.JSONAssert;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class Users_API_should {

    private static final boolean STRICT = true;
    private static final String POST_TEXT_1 = "first post!";
    private static final String POST_TEXT_2 = "here is some more";
    private static final String USER_ID = randomUUID().toString();
    private static final String POST_ID_1 = randomUUID().toString();
    private static final String POST_ID_2 = randomUUID().toString();
    public static final int CREATED = 201;
    public static final int OK = 200;

    @Mock
    private Clock clock;
    @Mock
    private PostIdGenerator postIdGenerator;
    @Mock
    private Request request;
    @Mock
    private Response response;

    private UsersAPI usersAPI;

    private InOrder inOrder;

    @Before
    public void setup() {
        usersAPI = new UsersAPI(clock, postIdGenerator);
        inOrder = Mockito.inOrder(response);
    }

    @Test
    public void return_empty_list_of_messages_given_none_posted() throws JSONException {
        String actual = usersAPI.retrievePosts(request, response);

        String expected = "[]";
        assertJson(actual, expected);
        verify(response).type("application/json");
        verify(response).status(200);
    }

    @Test
    public void echo_back_the_created_post() throws JSONException {
        givenTimeIs(2018, 8, 29, 8, 16, 23);
        String actual = createPost(POST_ID_1, USER_ID, POST_TEXT_1);

        String timestamp = "2018-08-29T08:16:23Z";
        String expected = postAsJson(POST_ID_1, USER_ID, timestamp, POST_TEXT_1);
        assertJson(actual, expected);
        verify(response).type("application/json");
        verify(response).status(CREATED);
    }

    @Test
    public void retrieve_the_message_given_one_has_been_posted() throws JSONException {
        givenTimeIs(2018, 8, 29, 8, 16, 23);
        createPost(POST_ID_1, USER_ID, POST_TEXT_1);

        givenTimeIs(2018, 8, 30, 14, 25, 47);
        givenNextPostIdIs(POST_ID_2);
        String actual = usersAPI.retrievePosts(request, response);

        String timestamp = "2018-08-29T08:16:23Z";
        String expected = "[" + postAsJson(POST_ID_1, USER_ID, timestamp, POST_TEXT_1) + "]";
        assertJson(actual, expected);
        inOrder.verify(response).status(CREATED);
        inOrder.verify(response).status(OK);
    }

    @Test
    public void retrieve_all_messages_given_multiple_have_been_posted() throws JSONException {
        givenTimeIs(2018, 8, 31, 12, 1, 16);
        createPost(POST_ID_1, USER_ID, POST_TEXT_1);
        givenTimeIs(2018, 9, 2, 23, 59, 59);
        createPost(POST_ID_2, USER_ID, POST_TEXT_2);

        String actual = usersAPI.retrievePosts(request, response);

        String timestamp1 = "2018-08-31T12:01:16Z";
        String timestamp2 = "2018-09-02T11:59:59Z";
        String expected = "[" +
                postAsJson(POST_ID_1, USER_ID, timestamp1, POST_TEXT_1) + "," +
                postAsJson(POST_ID_2, USER_ID, timestamp2, POST_TEXT_2) +
                "]";
        assertJson(actual, expected);
        inOrder.verify(response).status(CREATED);
        inOrder.verify(response).status(CREATED);
        inOrder.verify(response).status(OK);
    }

    private String createPost(String postId, String userId, String text) {
        givenNextPostIdIs(postId);
        givenPathParameter("userId", userId);
        givenRequestBody("{\"text\":\"" + text + "\"}");
        return usersAPI.createPost(request, response);
    }

    private void givenPathParameter(String name, String value) {
        given(request.params(name)).willReturn(value);
    }

    private void givenRequestBody(String body) {
        given(request.body()).willReturn(body);
    }

    private String postAsJson(String postId, String userId, String timestamp, String text) {
        return "{" +
                "\"text\":\"" + text + "\"," +
                "\"dateTime\":\"" + timestamp + "\"," +
                "\"userId\":\"" + userId + "\"," +
                "\"postId\":\"" + postId + "\"" +
                "}";
    }

    private void assertJson(String actual, String expected) throws JSONException {
        JSONAssert.assertEquals(expected, actual, STRICT);
    }

    private void givenNextPostIdIs(String randomUUID) {
        given(postIdGenerator.nextId()).willReturn(randomUUID);
    }

    private void givenTimeIs(int year, int month, int day, int hour, int minute, int second) {
        given(clock.now()).willReturn(LocalDateTime.of(year, month, day, hour, minute, second));
    }
}
