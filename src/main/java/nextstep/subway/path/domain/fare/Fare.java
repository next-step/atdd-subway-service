package nextstep.subway.path.domain.fare;

public class Fare {

    private int value;

    protected Fare() {
    }

    private Fare(FareStrategy fareStrategy) {
        this.value = fareStrategy.getFare();
    }

    public int value() {
        return value;
    }

    public static Fare of(FareStrategy fareStrategy) {
        return new Fare(fareStrategy);
    }


}
