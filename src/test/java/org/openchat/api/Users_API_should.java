package org.openchat.api;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.skyscreamer.jsonassert.JSONAssert;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class Users_API_should {

    private static final boolean STRICT = true;

    @Mock
    private Clock clock;

    private UsersAPI usersAPI;

    @Before
    public void setup() {
        usersAPI = new UsersAPI(clock);
    }

    @Test
    public void return_empty_list_of_messages_when_none_posted() throws JSONException {
        String actual = usersAPI.retrievePosts();

        String expected = "[]";
        JSONAssert.assertEquals(expected, actual, STRICT);
    }

    @Test
    public void return_the_message_when_one_has_been_posted() throws JSONException {
        String firstPostText = "first post!";
        timeIs(LocalDateTime.of(2018, 1, 10, 9, 0, 0));
        usersAPI.createPost(firstPostText);

        String actual = usersAPI.retrievePosts();

        String firstPostTimestamp = "2018-01-10T09:00:00Z";
        String expected = "[{" +
                "\"text\":\"" + firstPostText + "\"," +
                "\"dateTime\":\"" + firstPostTimestamp + "\"" +
                "}]";
        JSONAssert.assertEquals(expected, actual, STRICT);
    }

    private void timeIs(LocalDateTime time) {
        given(clock.now()).willReturn(time);
    }
}
