package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import nextstep.subway.member.domain.MemberType;

@Embeddable
public class Fare {

    private final long EXTRA_FARE = 100;
    private final long DEFAULT_FARE = 1_250;
    private final long DEFAULT_FARE_DISTANCE_LIMIT = 10;
    private final long SHORT_EXTRA_FARE_DISTANCE_LIMIT = 50;
    private final long SHORT_EXTRA_FARE_CHARGE_DISTANCE = 5;
    private final long LONG_EXTRA_FARE_CHARGE_DISTANCE = 8;
    private long fare;

    private Fare(long fare) {
        this.fare = fare;
    }

    public static Fare of(long fare) {
        return new Fare(fare);
    }

    protected Fare() {

    }

    public Fare calculateFare(long distance) {
        if (distance <= DEFAULT_FARE_DISTANCE_LIMIT) {
            return new Fare(DEFAULT_FARE + fare);
        } else if (distance <= SHORT_EXTRA_FARE_DISTANCE_LIMIT) {
            return new Fare(shortExtraDistanceCalculate(distance) + DEFAULT_FARE + fare);
        } else {
            return new Fare(longExtraDistanceCalculate(distance) + DEFAULT_FARE + fare);
        }
    }


    public Fare calculateFareWithMemberType(MemberType memberType) {
        if(memberType.equals(MemberType.GUEST)){
            return this;
        }

        return Fare.of((long) ((fare - memberType.getDeductedAmount()) * ( 1 - memberType.getDiscountRate())));
    }


    public long shortExtraDistanceCalculate(long distance) {
        return extraDistanceCalculate(distance - DEFAULT_FARE_DISTANCE_LIMIT, SHORT_EXTRA_FARE_CHARGE_DISTANCE) * EXTRA_FARE;
    }

    public long longExtraDistanceCalculate(long distance) {
        return shortExtraDistanceCalculate(SHORT_EXTRA_FARE_DISTANCE_LIMIT) + extraDistanceCalculate(distance - SHORT_EXTRA_FARE_DISTANCE_LIMIT, LONG_EXTRA_FARE_CHARGE_DISTANCE) * EXTRA_FARE;
    }

    public long extraDistanceCalculate(long distance, long extraChargeDistance) {

        if (distance % extraChargeDistance == 0) {
            return distance / extraChargeDistance;
        }
        return distance / extraChargeDistance + 1;
    }

    public long value() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
