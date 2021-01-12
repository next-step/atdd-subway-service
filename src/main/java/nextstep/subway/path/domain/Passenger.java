package nextstep.subway.path.domain;

import java.util.Arrays;

public enum Passenger {
    CHILD(6, 13),
    TEENAGER(13, 19),
    NONE();

    private int start;
    private int end;

    Passenger() {
    }

    Passenger(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public static Passenger checkAge(int age) {
        return Arrays.stream(Passenger.values())
                .filter(passenger -> passenger.start <= age && age < passenger.end)
                .findFirst()
                .orElse(NONE);
    }
}
