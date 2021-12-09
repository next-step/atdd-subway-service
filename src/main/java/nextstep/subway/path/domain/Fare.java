package nextstep.subway.path.domain;

public class Fare {
    public static final int BASIC_FARE = 1250;

    private final int fare;

    private Fare(int fare) {
        validate(fare);
        this.fare = fare;
    }

    public static Fare ofBasic() {
        return new Fare(BASIC_FARE);
    }

    private void validate(int fare) {
        if (fare < BASIC_FARE) {
            throw new IllegalArgumentException("기본운임 요금보다 작을 수 없습니다.");
        }
    }

    public static Fare of(int fare) {
        return new Fare(fare);
    }

    public int getFare() {
        return fare;
    }

    public boolean match(int target) {
        return this.fare == target;
    }
}
