package org.openchat.api;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class Users_API_should {

    private static final boolean STRICT = true;

    private UsersAPI usersAPI;

    @Before
    public void setup() {
        usersAPI = new UsersAPI();
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
        usersAPI.createPost(firstPostText);

        String actual = usersAPI.retrievePosts();

        String expected = "[{\"text\":\"" + firstPostText + "\"}]";
        JSONAssert.assertEquals(expected, actual, STRICT);
    }
}
