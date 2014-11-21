package spacecamel.reactivestream.util;

import java.util.Iterator;
import java.util.function.Supplier;

public class InfiniteIterator<T> implements Iterator<T>
{
    private final Supplier<T> generator;

    private InfiniteIterator(Supplier<T> generator)
    {
        this.generator = generator;
    }

    @Override
    public boolean hasNext()
    {
        return true;
    }

    @Override
    public T next()
    {
        return generator.get();
    }

    public static <E> InfiniteIterator<E> of(Supplier<E> generator)
    {
        return new InfiniteIterator<E>(generator);
    }
}