package io.github.hillelmed.bitbucket.client.exception;

import io.github.hillelmed.bitbucket.client.domain.common.BitbucketError;
import org.springframework.http.HttpStatusCode;

import java.io.Serial;
import java.util.List;

/**
 * Thrown when an action has breached the licensed user limit of the server, or
 * degrading the authenticated user's permission level.
 */
public class BitbucketAppException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1L;
    private final List<BitbucketError> errors;
    private final HttpStatusCode httpStatusCode;


    /**
     * Instantiates a new Bitbucket app exception.
     *
     * @param responseBody   the response body
     * @param errors         the errors
     * @param httpStatusCode the http status code
     */
    public BitbucketAppException(final String responseBody, final List<BitbucketError> errors, HttpStatusCode httpStatusCode) {
        super(responseBody);
        this.errors = errors;
        this.httpStatusCode = httpStatusCode;
    }

    /**
     * Instantiates a new Bitbucket app exception.
     *
     * @param responseBody   the response body
     * @param errors         the errors
     * @param httpStatusCode the http status code
     * @param arg1           the arg 1
     */
    public BitbucketAppException(final String responseBody, final List<BitbucketError> errors, HttpStatusCode httpStatusCode, final Throwable arg1) {
        super(responseBody, arg1);
        this.errors = errors;
        this.httpStatusCode = httpStatusCode;
    }

    /**
     * Errors list.
     *
     * @return the list
     */
    public List<BitbucketError> errors() {
        return errors;
    }

    /**
     * Code http status code.
     *
     * @return the http status code
     */
    public HttpStatusCode code() {
        return httpStatusCode;
    }
}