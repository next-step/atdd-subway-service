package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.AuthMember;
import nextstep.subway.line.domain.Line;

public class Fare {
    private static final int DEFAULT_FARE = 1250;

    private Path path;
    private AuthMember member;

    public Fare() {
    }

    public Fare(Path path, AuthMember member) {
        this.path = path;
        this.member = member;
    }

    public int getFare() {
        int fare = DEFAULT_FARE;
        fare = addDistanceExtraCharge(fare);
        fare = addLineExtraCharge(fare);
        return applyAgeDiscount(fare);
    }

    private int addDistanceExtraCharge(int fare) {
        if (path.isLongerThanTenAndLessThanFiftyKilometers()) {
            return fare + getDistanceExtraCharge(path.getDistance() - 10, DistanceFarePolicy.DISTANCE_UNIT_AFTER_TEN.value());
        }
        if (path.isLongerThanFiftyKilometers()) {
            return fare
                    + DistanceFarePolicy.EXTRA_CHARGE_FROM_TEN_TO_FIFTY.value()
                    + getDistanceExtraCharge(path.getDistance() - 50, DistanceFarePolicy.DISTANCE_UNIT_AFTER_FIFTY.value());
        }
        return fare;
    }

    private int getDistanceExtraCharge(final int distance, final int distanceUnit) {
        final int ceil = (int) Math.ceil((distance - 1) / distanceUnit);
        return (ceil + 1) * DistanceFarePolicy.EXTRA_CHARGE_BY_DISTANCE_UNIT.value();
    }

    private int addLineExtraCharge(final int fare) {
        final int lineExtraCharge = path.getLines()
                .stream()
                .map(Line::getExtraCharge)
                .reduce(Integer::sum)
                .get();
        return fare + lineExtraCharge;
    }

    private int applyAgeDiscount(final int fare) {
        if (member.isTeenager()) {
            return ((fare - AgeFarePolicy.NOT_ADULT_DEDUCTION.value())
                    * (100 - AgeFarePolicy.TEENAGER_DISCOUNT_RATE.value())) / 100;
        }
        if (member.isKid()) {
            return ((fare - AgeFarePolicy.NOT_ADULT_DEDUCTION.value())
                    * (100 - AgeFarePolicy.KID_DISCOUNT_RATE.value())) / 100;
        }
        return fare;
    }
}
