package nextstep.subway.utils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamUtils {

    private StreamUtils() {}

    public static <T, R> List<R> mapToList(List<T> collections,  Function<T, R> function) {
        return collections.stream()
            .map(function)
            .collect(Collectors.toList());
    }
}
