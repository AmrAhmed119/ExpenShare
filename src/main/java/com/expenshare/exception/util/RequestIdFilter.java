package com.expenshare.exception.util;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.slf4j.MDC;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * Filter to generate and attach a unique requestId to each incoming HTTP request.
 */
@Filter("/**")
@Singleton
public class RequestIdFilter implements HttpServerFilter {

    private static final String REQUEST_ID_HEADER = "X-Request-ID";

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        String requestId = request.getHeaders().get(REQUEST_ID_HEADER) != null
                ? request.getHeaders().get(REQUEST_ID_HEADER)
                : UUID.randomUUID().toString();

        MDC.put(REQUEST_ID_HEADER, requestId);

        return Flux.from(chain.proceed(request)).doFinally(signalType -> MDC.clear());
    }
}
