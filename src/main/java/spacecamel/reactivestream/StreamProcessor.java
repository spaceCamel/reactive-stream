package spacecamel.reactivestream;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class StreamProcessor<T>
{
    private final Stream<T> messages;

    private final Consumer<T> consumer;

    private final Predicate<T> filter;

    @SafeVarargs
    public StreamProcessor(Stream<T> items, Consumer<T> consumer, Predicate<T>... filters)
    {
        this.messages = items;
        this.filter = Stream.of(filters).reduce(__ -> true, Predicate::and);
        this.consumer = consumer;
    }

    public void run()
    {
        messages.filter(filter).forEachOrdered(consumer);
    }
}
