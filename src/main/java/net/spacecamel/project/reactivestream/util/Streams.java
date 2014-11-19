package net.spacecamel.project.reactivestream.util;

import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;

public class Streams
{
    public static <T> Stream<T> sequentialStream(final Iterable<T> messages)
    {
        return stream(messages.spliterator(), false);
    }
}
