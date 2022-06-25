package nextstep.subway.fare.domain;

public enum AgeType {
    TEENAGER(13, 19),
    KID(6, 13);

    private final int min;
    private final int max;

    AgeType(int min, int max) {
        this.min = min;
        this.max = max;
    }
}
