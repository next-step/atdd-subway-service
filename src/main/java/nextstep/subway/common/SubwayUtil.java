package nextstep.subway.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubwayUtil {
    private SubwayUtil() {
        throw new IllegalAccessError("이 클래스는 유틸 클래스입니다.");
    }

    public static String convertLocalDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "";
        }

        return localDateTime.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS"));
    }
}
