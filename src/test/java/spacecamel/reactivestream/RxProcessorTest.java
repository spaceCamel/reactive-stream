package spacecamel.reactivestream;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;
import rx.Observer;
import rx.schedulers.TestScheduler;

import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RxProcessorTest extends TestCase
{
    @Mock
    Observer<Object> consumer;

    @Mock
    Predicate<Object> predicate;

    final int inputLength = new Random().nextInt(100) + 1;

    private final TestScheduler scheduler = new TestScheduler();

    final Observable<Object> input =
            Observable.from(() -> Stream.generate(Object::new).limit(inputLength).iterator()).subscribeOn(scheduler);

    RxProcessor<Object> processor;

    @Before
    public void instantiateApplication()
    {
        processor = new RxProcessor<>(input, consumer, predicate);
    }

    @Test
    public void iteratesOverEveryElementOfTheInputList()
    {
        when(predicate.test(any())).thenReturn(true);
        runAndWait(processor);
        verify(consumer, times(inputLength)).onNext(any());
        verify(consumer).onCompleted();
    }

    @Test
    public void skipsAllElementsIfThePredicate()
    {
        when(predicate.test(any())).thenReturn(false);
        runAndWait(processor);
        verify(consumer, never()).onNext(any());
        verify(consumer).onCompleted();
    }

    @Test
    public void processesOnlyItemsForWhichThePredicateReturnsTrue()
    {
        final Object first = new Object();
        final Object second = new Object();
        when(predicate.test(any())).thenReturn(false, true);
        runAndWait(new RxProcessor<>(Observable.just(first, second), consumer, predicate));
        verify(consumer, never()).onNext(first);
        verify(consumer).onNext(second);
        verify(consumer).onCompleted();
    }

    @Test
    public void rethrowsCollaboratorException()
    {
        final TestException exception = new TestException();
        when(predicate.test(any())).thenThrow(exception);
        runAndWait(processor);
        verify(consumer, never()).onNext(any());
        verify(consumer).onError(exception);
        verify(consumer, never()).onCompleted();
    }

    @Test
    public void ifEmptyInputThenNoInteractions()
    {
        runAndWait(new RxProcessor<>(Observable.empty(), consumer, predicate));
        verify(consumer, never()).onNext(any());
        verify(predicate, never()).test(any());
        verify(consumer).onCompleted();
    }

    private void runAndWait(final RxProcessor<Object> processor)
    {
        processor.run();
        scheduler.triggerActions();
    }

    static class TestException extends RuntimeException
    {
    }
}