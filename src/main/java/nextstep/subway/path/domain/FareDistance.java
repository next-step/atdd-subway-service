package nextstep.subway.path.domain;

import java.util.Arrays;
import nextstep.subway.common.exception.SubwayException;

public enum FareDistance {

    BASIC(0, 10),
    MIDDLE(11, 50),
    LONG(51, Integer.MAX_VALUE);

    private int start;
    private int end;

    FareDistance(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public static FareDistance findByDistance(int distance) {
        return Arrays.stream(FareDistance.values())
                .filter(fareDistance -> fareDistance.isBetween(distance))
                .findFirst()
                .orElseThrow(SubwayException::new);
    }

    private boolean isBetween(int distance) {
        return start <= distance && distance <= end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
