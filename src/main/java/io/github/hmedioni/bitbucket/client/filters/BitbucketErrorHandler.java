package io.github.hmedioni.bitbucket.client.filters;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import io.github.hmedioni.bitbucket.client.domain.common.*;
import io.github.hmedioni.bitbucket.client.exception.*;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.*;

import java.util.*;


/**
 * Handle errors and propagate exception.
 */
public class BitbucketErrorHandler {

    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    public static ExchangeFilterFunction handler() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            HttpStatusCode statusCode = clientResponse.statusCode();
            if (statusCode.is5xxServerError() || statusCode.is4xxClientError()) {
                return clientResponse.bodyToMono(String.class)
                        .defaultIfEmpty("{}")
                        .flatMap(errorBody -> Mono.error(new BitbucketAppException(errorBody, getErrors(errorBody), statusCode)));
            } else {
                return Mono.just(clientResponse);
            }
        });
    }

    public static List<BitbucketError> getErrors(final String output) {

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
                throw new RuntimeException(output);
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

    public static BitbucketError getErrorFromJsonObject(final JsonNode obj) {
        final JsonNode context = obj.get("context");
        final JsonNode message = obj.get("message");
        final JsonNode exceptionName = obj.get("exceptionName");
        final JsonNode conflicted = obj.get("conflicted");

        final List<Veto> vetos = new ArrayList<>();
        final JsonNode possibleVetoesArray = obj.get("vetoes");
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

        return new BitbucketError(!(context instanceof NullNode) ? context.asText() : null,
                !(message instanceof NullNode) ? message.asText() : null,
                !(exceptionName instanceof NullNode) ? exceptionName.asText() : null,
                conflicted != null && !(conflicted instanceof NullNode) && conflicted.asBoolean(),
                vetos);
    }


}
