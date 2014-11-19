package net.spacecamel.project.reactivestream;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationConstructorTest extends TestCase
{
    @Mock
    Consumer<Object> consumer;

    @Mock
    Predicate<Object> predicate;

    @Mock
    Runnable onClose;

    final Stream<Object> stream = Stream.empty();

    Application<Object> application;

    @Before
    public void instantiateApplication()
    {
        application = new Application<>(stream, consumer, predicate);
    }

    @Test
    public void predicatesAreOptional()
    {
        new Application<>(stream, consumer);
    }

    @Test(expected = NullPointerException.class)
    public void throwsExceptionForNullIterable()
    {
        new Application<>(null, consumer);
    }

    @Test(expected = NullPointerException.class)
    public void throwsExceptionForNullConsumer()
    {
        new Application<>(Stream.empty(), null);
    }

    @Test(expected = NullPointerException.class)
    public void throwsExceptionForNullOnClose()
    {
        new Application<>(Stream.empty(), consumer);
    }
}