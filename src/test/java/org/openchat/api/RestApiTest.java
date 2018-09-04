package org.openchat.api;

import org.json.JSONException;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.skyscreamer.jsonassert.JSONAssert;
import spark.Request;
import spark.Response;

import static org.mockito.BDDMockito.given;

public class RestApiTest {
    protected static final int CREATED = 201;
    protected static final int OK = 200;
    protected static final int BAD_REQUEST = 400;
    private static final boolean STRICT = true;
    @Mock
    protected Request request;
    @Mock
    protected Response response;
    protected InOrder inOrder;

    protected void givenPathParameter(String name, String value) {
        given(request.params(name)).willReturn(value);
    }

    protected void givenRequestBody(String body) {
        given(request.body()).willReturn(body);
    }

    protected void assertJson(String actual, String expected) throws JSONException {
        JSONAssert.assertEquals(expected, actual, STRICT);
    }
}
