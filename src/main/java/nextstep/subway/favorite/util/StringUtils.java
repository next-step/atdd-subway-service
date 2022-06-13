package nextstep.subway.favorite.util;

import java.util.Optional;

public class StringUtils {
    public static Optional<Long> stringToLong(String s) {
        try {
            return Optional.of(Long.parseLong(s));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
