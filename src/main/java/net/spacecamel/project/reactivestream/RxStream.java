package net.spacecamel.project.reactivestream;

import net.spacecamel.project.reactivestream.util.Predicates;
import rx.Observable;
import rx.Observer;

import java.util.function.Predicate;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;

public class RxStream<T>
{
    private final Observable<T> messages;

    private final Observer<T> consumer;

    private final Predicate<T> filter;

    @SafeVarargs
    public RxStream(Observable<T> items, Observer<T> consumer, Predicate<T>... filters)
    {
        this.messages = items;
        this.filter = isEmpty(filters) ? Predicates.alwaysTrue : Predicates.all(filters);
        this.consumer = consumer;
    }

    public void run()
    {
        messages.filter(filter::test).subscribe(consumer);
    }
}
