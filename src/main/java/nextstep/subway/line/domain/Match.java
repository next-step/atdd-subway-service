package nextstep.subway.line.domain;

public enum Match {
    UP, DOWN;

    public boolean isUp() {
        return this == UP;
    }

    public boolean isDown() {
        return this == DOWN;
    }
}
