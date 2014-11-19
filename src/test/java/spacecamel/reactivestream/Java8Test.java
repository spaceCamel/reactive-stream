package spacecamel.reactivestream;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class Java8Test extends TestCase
{
    @Mock
    Consumer<Object> consumer;

    @Mock
    Predicate<Object> predicate;

    final int streamLength = new Random().nextInt(100) + 1;

    final Stream<Object> stream = Stream.generate(Object::new).limit(streamLength);

    Java8<Object> java8;

    @Before
    public void instantiateApplication()
    {
        java8 = new Java8<>(stream, consumer, predicate);
    }

    @Test
    public void iteratesOverEveryElementOfTheInputList()
    {
        when(predicate.test(any())).thenReturn(true);
        java8.run();
        verify(consumer, times(streamLength)).accept(any());
    }

    @Test
    public void skipsAllElementsIfThePredicate()
    {
        when(predicate.test(any())).thenReturn(false);
        java8.run();
        verify(consumer, never()).accept(any());
    }

    @Test
    public void processesOnlyItemsForWhichThePredicateReturnsTrue()
    {
        final Object first = new Object();
        final Object second = new Object();
        final Stream<Object> stream = Stream.of(first, second);
        when(predicate.test(any())).thenReturn(false, true);
        new Java8<>(stream, consumer, predicate).run();
        verify(consumer, never()).accept(first);
        verify(consumer).accept(second);
    }

    @Test(expected = TestException.class)
    public void rethrowsCollaboratorException()
    {
        when(predicate.test(any())).thenThrow(new TestException());
        java8.run();
        verify(predicate).test(any());
        verify(consumer, never()).accept(any());
    }

    @Test
    public void predicatesAreOptional()
    {
        new Java8<>(stream, consumer).run();
        verify(consumer, times(streamLength)).accept(any());
    }

    @Test
    public void ifEmptyInputThenNoInteractions()
    {
        new Java8<>(Stream.empty(), consumer).run();
        verifyNoMoreInteractions(consumer, predicate);
    }

    static class TestException extends RuntimeException
    {
    }
}