package net.spacecamel.project.reactivestream;

import net.spacecamel.project.reactivestream.util.Predicates;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;

public class Java8Stream<T>
{
    private final Stream<T> messages;

    private final Consumer<T> consumer;

    private final Predicate<T> filter;

    @SafeVarargs
    public Java8Stream(Stream<T> items, Consumer<T> consumer, Predicate<T>... filters)
    {
        this.messages = items;
        this.filter = isEmpty(filters) ? Predicates.alwaysTrue : Predicates.all(filters);
        this.consumer = consumer;
    }

    public void run()
    {
        messages.filter(filter).forEachOrdered(consumer);
    }
}
