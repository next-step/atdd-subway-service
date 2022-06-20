package nextstep.subway.path.domain;

public class Fare {
    private int value;

    private Fare(Path path, Integer age) {
        value = path.calculateDistanceFare() + path.calculateLineFare();
        if (age != null) {
            discountFareWithAge(age);
        }
    }

    public static Fare of(Path path, Integer age) {
        return new Fare(path, age);
    }

    private void discountFareWithAge(int age) {
        FareAgePolicyType policy = FareAgePolicyType.of(age);
        value = policy.discountFare(value);
    }

    public int getValue() {
        return value;
    }
}
