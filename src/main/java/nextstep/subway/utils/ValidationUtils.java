package nextstep.subway.utils;

import java.util.Objects;

public class ValidationUtils {

    private ValidationUtils() {}

    public static boolean isEmpty(String str) {
        return Objects.isNull(str) || str.isEmpty();
    }

    public static boolean isNull(Object obj) {
        return Objects.isNull(obj);
    }

}
