package nextstep.subway.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListTestUtils {

    private ListTestUtils() {
    }

    public static <T> List<T> newList(T... args) {
        return new ArrayList<>(Arrays.asList(args));
    }
}
