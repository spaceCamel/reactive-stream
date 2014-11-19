package spacecamel.reactivestream;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;
import rx.Observer;

import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RxJavaTest extends TestCase
{
    @Mock
    Observer<Object> consumer;

    @Mock
    Predicate<Object> predicate;

    final int streamLength = new Random().nextInt(100) + 1;

    final Observable<Object> stream = Observable.from(Stream.generate(Object::new).limit(streamLength).toArray());

    RxJava<Object> rxJava;

    @Before
    public void instantiateApplication()
    {
        rxJava = new RxJava<>(stream, consumer, predicate);
    }

    @Test
    public void iteratesOverEveryElementOfTheInputList()
    {
        when(predicate.test(any())).thenReturn(true);
        rxJava.run();
        verify(consumer, times(streamLength)).onNext(any());
        verify(consumer).onCompleted();
    }

    @Test
    public void skipsAllElementsIfThePredicate()
    {
        when(predicate.test(any())).thenReturn(false);
        rxJava.run();
        verify(consumer, never()).onNext(any());
        verify(consumer).onCompleted();
    }

    @Test
    public void processesOnlyItemsForWhichThePredicateReturnsTrue()
    {
        final Object first = new Object();
        final Object second = new Object();
        final Observable<Object> stream = Observable.just(first, second);
        when(predicate.test(any())).thenReturn(false, true);
        new RxJava<>(stream, consumer, predicate).run();
        verify(consumer, never()).onNext(first);
        verify(consumer).onNext(second);
        verify(consumer).onCompleted();
    }

    @Test
    public void rethrowsCollaboratorException()
    {
        final TestException exception = new TestException();
        when(predicate.test(any())).thenThrow(exception);
        rxJava.run();
        verify(predicate).test(any());
        verify(consumer, never()).onNext(any());
        verify(consumer).onError(exception);
        verify(consumer, never()).onCompleted();
    }

    @Test
    public void noFiltersDefaultsToPassThrough()
    {
        new RxJava<>(stream, consumer).run();
        verify(consumer, times(streamLength)).onNext(any());
    }

    @Test
    public void ifEmptyInputThenNoInteractions()
    {
        new RxJava<>(Observable.empty(), consumer).run();
        verify(consumer).onCompleted();
        verifyNoMoreInteractions(predicate);
    }

    static class TestException extends RuntimeException
    {
    }
}