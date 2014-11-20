package spacecamel.reactivestream.util;

import rx.Observer;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class WaitingObserverFinishes<T> implements Observer<T>
{

    public static final String TIMEOUT_MSG = "Observer was not called within timeout of %s %s";

    private final Semaphore semaphore = new Semaphore(0);

    private final Observer<T> delegate;

    private final int timeout;

    private final TimeUnit timeUnit;

    public WaitingObserverFinishes(Observer<T> delegate)
    {
        this(delegate, 5, TimeUnit.SECONDS);
    }

    public WaitingObserverFinishes(Observer<T> delegate, final int timeout, final TimeUnit timeUnit)
    {
        this.delegate = delegate;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    @Override
    public void onCompleted()
    {
        delegate.onCompleted();
        semaphore.release();
    }

    @Override
    public void onError(final Throwable e)
    {
        delegate.onError(e);
        semaphore.release();
    }

    @Override
    public void onNext(final T t)
    {
        delegate.onNext(t);
    }

    public void waitTermination()
    {
        try
        {
            assertThat(semaphore.tryAcquire(timeout, timeUnit)).overridingErrorMessage(TIMEOUT_MSG, timeout, timeUnit)
                                                               .isTrue();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}