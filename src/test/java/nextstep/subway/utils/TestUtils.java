package nextstep.subway.utils;

import java.util.UUID;

public final class TestUtils {
    private TestUtils() {}

    public static long getRandomId() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }
}
