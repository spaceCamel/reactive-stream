package net.spacecamel.project.reactivestream;

import net.spacecamel.project.reactivestream.util.Predicates;
import net.spacecamel.project.reactivestream.util.Predicates;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static net.spacecamel.project.reactivestream.util.Predicates.all;
import static net.spacecamel.project.reactivestream.util.Predicates.alwaysTrue;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

public class Application<T>
{

    private final Stream<T> messages;

    private final Consumer<T> publisher;

    private final Predicate<T> filter;

    @SafeVarargs
    public Application(Stream<T> items, Consumer<T> consumer, Predicate<T>... filters)
    {
        requireNonNull(consumer, "consumer is null");
        requireNonNull(items, "items is null");
        this.messages = items;
        this.filter = isEmpty(filters) ? Predicates.alwaysTrue : Predicates.all(filters);
        this.publisher = consumer;
    }

    public void run()
    {
        messages.filter(filter).forEachOrdered(publisher);
    }
}
