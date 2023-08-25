package io.github.hmedioni.bitbucket.client.exception;

import io.github.hmedioni.bitbucket.client.domain.common.*;
import org.springframework.http.*;

import java.io.*;
import java.util.*;

/**
 * Thrown when an action has breached the licensed user limit of the server, or
 * degrading the authenticated user's permission level.
 */
public class BitbucketAppException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1L;
    private final List<BitbucketError> errors;
    private final HttpStatusCode httpStatusCode;



    public BitbucketAppException(final String responseBody, final List<BitbucketError> errors, HttpStatusCode httpStatusCode) {
        super(responseBody);
        this.errors = errors;
        this.httpStatusCode = httpStatusCode;
    }

    public BitbucketAppException(final String responseBody, final List<BitbucketError> errors, HttpStatusCode httpStatusCode,final Throwable arg1) {
        super(responseBody, arg1);
        this.errors = errors;
        this.httpStatusCode = httpStatusCode;
    }

    public List<BitbucketError> errors() {
        return errors;
    }

    public HttpStatusCode code() {
        return httpStatusCode;
    }
}