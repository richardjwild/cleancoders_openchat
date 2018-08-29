package org.openchat.api;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.skyscreamer.jsonassert.JSONAssert;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class Users_API_should {

    private static final boolean STRICT = true;

    @Mock
    private Clock clock;

    @Mock
    private PostIdGenerator postIdGenerator;

    @Mock
    private Request request;

    @Mock
    private Response response;

    private UsersAPI usersAPI;

    @Before
    public void setup() {
        usersAPI = new UsersAPI(clock, postIdGenerator);
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
    public void return_the_message_given_one_has_been_posted() throws JSONException {
        String text = "first post!";
        String postId = randomUUID().toString();
        String userId = randomUUID().toString();

        givenTimeIs(2018, 8, 29, 8, 16, 23);
        givenNextPostIdIs(postId);
        givenPathParameters(
                "id", userId);
        givenRequestBody(
                "{\"text\":\"" + text + "\"}");
        usersAPI.createPost(request, response);

        String actual = usersAPI.retrievePosts(request, response);

        String timestamp = "2018-08-29T08:16:23Z";
        String expected = "[" + postAsJson(postId, userId, timestamp, text) + "]";
        assertJson(actual, expected);
        verify(response).status(201);
    }

    private void givenPathParameters(String... pathParameters) {
        for (int paramNo = 0; paramNo < pathParameters.length; paramNo += 2)
            given(request.params(pathParameters[paramNo]))
                    .willReturn(pathParameters[paramNo + 1]);
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
