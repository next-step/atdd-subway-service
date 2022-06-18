package nextstep.subway.fare.domain;

public class AgeFarePolicy {
    private static final Integer MIN_CHILDREN_AGE = 6;
    private static final Integer MAX_CHILDREN_AGE = 12;
    private static final Fare CHILDREN_DEDUCTED_FARE = Fare.from(350);
    private static final Double CHILDREN_DISCOUNT_RATE = new Double(50);

    private static final Integer MIN_TEENAGER_AGE = 13;
    private static final Integer MAX_TEENAGER_AGE = 19;
    private static final Fare TEENAGER_DEDUCTED_FARE = Fare.from(350);
    private static final Double TEENAGER_DISCOUNT_RATE = new Double(20);

    public Fare calculate(Fare fare, Integer age) {
        if (isChildren(age)) {
            return fare.minus(fare.minus(CHILDREN_DEDUCTED_FARE).getRateFare(CHILDREN_DISCOUNT_RATE));
        }
        if (isTeenager(age)) {
            return fare.minus(fare.minus(TEENAGER_DEDUCTED_FARE).getRateFare(TEENAGER_DISCOUNT_RATE));
        }
        return fare;
    }

    private boolean isChildren(Integer age) {
        return age > MIN_CHILDREN_AGE && age < MAX_CHILDREN_AGE;
    }

    private boolean isTeenager(Integer age) {
        return age > MIN_TEENAGER_AGE && age < MAX_TEENAGER_AGE;
    }

}
