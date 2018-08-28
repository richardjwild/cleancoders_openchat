package org.openchat.api;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.skyscreamer.jsonassert.JSONAssert;

import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class Users_API_should {

    private static final boolean STRICT = true;

    @Mock
    private Clock clock;

    @Mock
    private PostIdGenerator postIdGenerator;

    private UsersAPI usersAPI;

    @Before
    public void setup() {
        usersAPI = new UsersAPI(clock, postIdGenerator);
    }

    @Test
    public void return_empty_list_of_messages_when_none_posted() throws JSONException {
        String actual = usersAPI.retrievePosts();

        String expected = "[]";
        assertJson(actual, expected);
    }

    @Test
    public void return_the_message_when_one_has_been_posted() throws JSONException {
        String text = "first post!";
        String postId = randomUUID().toString();
        String userId = randomUUID().toString();

        timeIs(2018, 1, 10, 9, 0, 0);
        nextPostIdIs(postId);
        usersAPI.createPost(userId, text);

        String actual = usersAPI.retrievePosts();

        String timestamp = "2018-01-10T09:00:00Z";
        String expected = "[" + postAsJson(postId, userId, timestamp, text) + "]";
        assertJson(actual, expected);
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

    private void nextPostIdIs(String randomUUID) {
        given(postIdGenerator.nextId()).willReturn(randomUUID);
    }

    private void timeIs(int year, int month, int day, int hour, int minute, int second) {
        given(clock.now()).willReturn(LocalDateTime.of(year, month, day, hour, minute, second));
    }
}
