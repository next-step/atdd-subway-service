package nextstep.subway.line.domain;

import java.util.Objects;

public enum Match {
    UP, DOWN;

    public static boolean isUP(final Match match) {
        return Objects.equals(UP, match);
    }

    public static boolean isDOWN(final Match match) {
        return Objects.equals(DOWN, match);
    }
}
