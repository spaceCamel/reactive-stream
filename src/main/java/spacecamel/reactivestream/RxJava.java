package spacecamel.reactivestream;

import rx.Observable;
import rx.Observer;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class RxJava<T>
{
    private final Observable<T> messages;

    private final Observer<T> consumer;

    private final Predicate<T> filter;

    @SafeVarargs
    public RxJava(Observable<T> items, Observer<T> consumer, Predicate<T>... filters)
    {
        this.messages = items;
        this.filter = Stream.of(filters).reduce(__ -> true, Predicate::and);
        this.consumer = consumer;
    }

    public void run()
    {
        messages.filter(filter::test).subscribe(consumer);
    }
}
