package com.bookstore.api.BookstoreAPI.Metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import org.springframework.stereotype.Component;

@Component
public class BookMetrics {

    private final Counter bookCreatedCounter;

    public BookMetrics(MeterRegistry meterRegistry) {
        this.bookCreatedCounter = meterRegistry.counter("books_created");
    }

    public void incrementBooksCreated() {
        bookCreatedCounter.increment();
    }
}
