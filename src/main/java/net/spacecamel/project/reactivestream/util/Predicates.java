package net.spacecamel.project.reactivestream.util;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Predicates
{
    public static <T> Predicate<T> all(final Predicate<T>[] filters)
    {
        if (filters.length == 1)
        {
            return filters[0];
        }
        else
        {
            final List<Predicate<T>> predicates = Arrays.asList(filters);
            final Predicate<T> first = predicates.remove(0);
            return predicates.stream().reduce(first, Predicate::and);
        }
    }

    public static final Predicate alwaysTrue = s -> true;
}
