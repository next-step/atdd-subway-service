package nextstep.subway.path.domain;

public class Fare {
    public static final int BASIC_FARE = 1250;
    public static final int MIN_FARE = 450;

    private final int fare;

    private Fare(int fare) {
        validate(fare);
        this.fare = fare;
    }

    public static Fare ofBasic() {
        return new Fare(BASIC_FARE);
    }

    private void validate(int fare) {
        if (fare < MIN_FARE) {
            throw new IllegalArgumentException(String.format("운임 요금은 %s 보다 커야 합니다.", MIN_FARE));
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

    public Fare minus(int amount) {
        return new Fare(this.fare - amount);
    }

    public Fare apply(double amount) {
        return new Fare((int) (this.fare * amount));
    }
}
