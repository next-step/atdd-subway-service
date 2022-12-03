package nextstep.subway.path.domain;

import java.util.Arrays;

public enum FareAge {

    CHILD(6, 13, 0.5),

    TEENAGER(13, 19, 0.2),

    ADULT(19, Integer.MAX_VALUE, 0);

    private int start;
    private int end;
    private double rate;

    FareAge(int start, int end, double rate) {
        this.start = start;
        this.end = end;
        this.rate = rate;
    }

    public static FareAge findByAge(int age) {
        return Arrays.stream(FareAge.values())
                .filter(fareAge -> fareAge.isBetween(age))
                .findFirst()
                .orElse(ADULT);
    }

    private boolean isBetween(int age) {
        return start <= age && age < end;
    }

    public double getRate() {
        return rate;
    }
}
