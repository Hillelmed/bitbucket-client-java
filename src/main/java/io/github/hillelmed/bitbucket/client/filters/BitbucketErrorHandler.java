package io.github.hillelmed.bitbucket.client.filters;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import io.github.hillelmed.bitbucket.client.domain.common.BitbucketError;
import io.github.hillelmed.bitbucket.client.domain.common.Veto;
import io.github.hillelmed.bitbucket.client.exception.BitbucketAppException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Handle errors and propagate exception.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BitbucketErrorHandler {

    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    /**
     * Handler exchange filter function.
     *
     * @return the exchange filter function
     */
    public static ExchangeFilterFunction handler() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            HttpStatusCode statusCode = clientResponse.statusCode();
            if (statusCode.is5xxServerError() || statusCode.is4xxClientError()) {
                return clientResponse.bodyToMono(String.class)
                        .defaultIfEmpty("{}")
                        .flatMap(errorBody -> Mono.error(new BitbucketAppException(errorBody, getErrors(errorBody, statusCode), statusCode)));
            } else {
                return Mono.just(clientResponse);
            }
        });
    }

    /**
     * Gets errors.
     *
     * @param output     the output
     * @param statusCode the status code
     * @return the errors
     */
    public static List<BitbucketError> getErrors(final String output, HttpStatusCode statusCode) {

        final List<BitbucketError> errors = new ArrayList<>();

        try {

            final JsonNode element = mapper.readValue(output, JsonNode.class);

            // in most cases Bitbucket will hand us back a list of `BitbucketError` objects
            // but in other cases we will simply be handed back the singular
            // `BitbucketError` object (i.e. not as a list).
            if (element.has("errors")) {
                final JsonNode errorsArray = element.get("errors");
                for (JsonNode jsonElement : errorsArray) {
                    final BitbucketError error = getErrorFromJsonObject(jsonElement);
                    errors.add(error);
                }
            } else if (element.has("context")) {
                errors.add(getErrorFromJsonObject(element));
            } else {
                errors.add(getErrorFromHttpStatus(statusCode.value()));
            }
        } catch (final Exception e) {
            final BitbucketError error = new BitbucketError(output,
                    "Failed to parse output: message=" + e.getMessage(),
                    e.getClass().getName(),
                    false,
                    null);
            errors.add(error);
        }

        return errors;
    }

    private static BitbucketError getErrorFromHttpStatus(int value) {
        HttpStatus code = HttpStatus.valueOf(value);
        BitbucketError bitbucketError = new BitbucketError();
        bitbucketError.setMessage(code.getReasonPhrase());
        bitbucketError.setContext(code.name());
        bitbucketError.setConflicted(false);
        return bitbucketError;
    }

    /**
     * Gets error from json object.
     *
     * @param obj the obj
     * @return the error from json object
     */
    public static BitbucketError getErrorFromJsonObject(final JsonNode obj) {
        final JsonNode context = obj.get("context");
        final JsonNode message = obj.get("message");
        final JsonNode exceptionName = obj.get("exceptionName");
        final JsonNode conflicted = obj.get("conflicted");

        final List<Veto> vetos = buildVetosFromJsonNode(obj.get("vetoes"));

        return new BitbucketError(!(context instanceof NullNode) ? context.asText() : null,
                !(message instanceof NullNode) ? message.asText() : null,
                !(exceptionName instanceof NullNode) ? exceptionName.asText() : null,
                conflicted != null && !(conflicted instanceof NullNode) && conflicted.asBoolean(),
                vetos);
    }

    private static List<Veto> buildVetosFromJsonNode(JsonNode possibleVetoesArray) {
        final List<Veto> vetos = new ArrayList<>();
        if (possibleVetoesArray != null && !(possibleVetoesArray instanceof NullNode)) {
            final boolean isArray = possibleVetoesArray.isArray();
            if (isArray) {
                Iterator<JsonNode> vetoesArray = possibleVetoesArray.elements();
                for (Iterator<JsonNode> it = vetoesArray; it.hasNext(); ) {
                    JsonNode jsonElement = it.next();
                    final JsonNode summary = jsonElement.get("summaryMessage");
                    final JsonNode detailed = jsonElement.get("detailedMessage");
                    final Veto veto = new Veto(!(summary instanceof NullNode) ? summary.asText() : null,
                            !(detailed instanceof NullNode) ? detailed.asText() : null);
                    vetos.add(veto);
                }
            }
        }
        return vetos;
    }


}
