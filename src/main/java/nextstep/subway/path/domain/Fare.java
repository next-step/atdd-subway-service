package nextstep.subway.path.domain;

public class Fare {

    private int value;

    public Fare(int value) {
        validate(value);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void validate(int value) {
        if (value < 0) {
            throw new RuntimeException("요금은 음수일 수 없습니다.");
        }
    }

    public Fare addFare(Fare fare) {
        this.value += fare.getValue();
        return this;
    }
}
