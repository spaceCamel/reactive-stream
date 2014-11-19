package spacecamel.reactivestream;

import rx.Observable;
import rx.Observer;

import java.util.function.Predicate;

public class RxProcessor<T>
{
    private final Observable<T> messages;

    private final Observer<T> consumer;

    private final Predicate<T> filter;

    public RxProcessor(Observable<T> items, Observer<T> consumer, Predicate<T> filter)
    {
        this.messages = items;
        this.filter = filter;
        this.consumer = consumer;
    }

    public void run()
    {
        messages.filter(filter::test).subscribe(consumer);
    }
}
