package org.openchat.api;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class Users_API_should {

    public static final boolean STRICT = true;

    @Test
    public void return_empty_list_of_messages_when_none_posted() throws JSONException {
        UsersAPI usersAPI = new UsersAPI();
        String expected = "[]";

        String actual = usersAPI.retrievePosts();

        JSONAssert.assertEquals(expected, actual, STRICT);
    }

}
