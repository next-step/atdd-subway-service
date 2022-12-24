package nextstep.subway.fee.domain;

public class PricePolicy {

    public final static int BASIC_PRICE = 1250;
    public final static int BASIC_DISTANCE = 10;

    private final int EXCESS_DISTANCE = 50;
    private final int CHILDREN_START_AGE = 6;
    private final int CHILDREN_END_AGE = 13;
    private final int TEENAGER_START_AGE = 13;
    private final int TEENAGER_END_AGE = 19;

    public boolean isChildren(int age) {
        return age >= CHILDREN_START_AGE && age < CHILDREN_END_AGE;
    }

    public boolean isTeenager(int age) {
        return age >= TEENAGER_START_AGE && age < TEENAGER_END_AGE;
    }

    public boolean isGatherExcessDistance(int distance) {
        return distance > EXCESS_DISTANCE;
    }

    public boolean isLessBasicDistance(int distance) {
        return distance <= BASIC_DISTANCE;
    }

}
