package nextstep.subway.line.domain;

import nextstep.subway.member.domain.AgeGroup;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LineFare {
    private static final int DEFAULT_FARE = 1_250;
    private static final int ADDITIONAL_FARE_UNIT = 100;
    private static final Distance MINIMUM_DEFAULT_ADDITIONAL_DISTANCE = new Distance(11);
    private static final Distance MAX_DEFAULT_ADDITIONAL_DISTANCE = new Distance(50);
    private static final Distance DEFAULT_ADDITIONAL_DISTANCE_UNIT = new Distance(5);
    private static final Distance MINIMUM_LONGER_ADDITIONAL_DISTANCE = new Distance(51);
    private static final Distance LONGER_ADDITIONAL_DISTANCE_UNIT = new Distance(8);

    @Column(nullable = false)
    private final int fare;

    public LineFare(int fare) {
        validateFare(fare);
        this.fare = fare;
    }

    protected LineFare() {
        this.fare = 0;
    }

    public int getFare() {
        return this.fare;
    }

    public int calculateTotalFare(Distance totalDistance, AgeGroup ageGroup) {
        int calculatedTotalLineFare = fare + DEFAULT_FARE
                + calculateDefaultAdditionalFare(totalDistance)
                + calculateLongerAdditionalFare(totalDistance);

        return ageGroup.discountLineFare(calculatedTotalLineFare);
    }

    private void validateFare(int fare) {
        if (fare < 0) {
            throw new IllegalArgumentException("운임 요금은 음수일 수 없습니다.");
        }
    }

    private int calculateDefaultAdditionalFare(Distance totalDistance) {
        if (totalDistance.isLessThan(MINIMUM_DEFAULT_ADDITIONAL_DISTANCE)) {
            return 0;
        }

        return totalDistance.getMinimumDistance(MAX_DEFAULT_ADDITIONAL_DISTANCE)
                .subtractThenReturnResult(MINIMUM_DEFAULT_ADDITIONAL_DISTANCE)
                .calculateDistanceRatio(DEFAULT_ADDITIONAL_DISTANCE_UNIT) * ADDITIONAL_FARE_UNIT;
    }

    private int calculateLongerAdditionalFare(Distance totalDistance) {
        if (totalDistance.isLessThan(MINIMUM_LONGER_ADDITIONAL_DISTANCE)) {
            return 0;
        }

        return totalDistance.subtractThenReturnResult(MINIMUM_LONGER_ADDITIONAL_DISTANCE)
                .calculateDistanceRatio(LONGER_ADDITIONAL_DISTANCE_UNIT) * ADDITIONAL_FARE_UNIT;
    }
}
