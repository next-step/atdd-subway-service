package nextstep.subway.line.domain;

import java.util.Objects;

public enum Match {
    UP, DOWN;

    public boolean isUP() {
        return this == UP;
    }

    public boolean isDOWN() {
        return this == DOWN;
    }
}
