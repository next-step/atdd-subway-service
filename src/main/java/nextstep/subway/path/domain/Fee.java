package nextstep.subway.path.domain;

import java.util.Objects;
import java.util.Set;
import nextstep.subway.line.domain.ExtraCharge;
import nextstep.subway.line.domain.Line;

public class Fee {
    private static final int FIRST_SECTION_OVER_KR_STANDARD = 10;
    private static final int FIRST_SECTION_DISTANCE_PER_KM_STANDARD = 5;
    private static final int SECOND_SECTION_OVER_KR_STANDARD = 50;
    private static final int SECOND_SECTION_DISTANCE_PER_KM_STANDARD = 8;
    private static final int DEFAULT_FEE = 1250;
    private static final int EXTRA_CHARGE = 100;

    private int fee;

    protected Fee(int fee) {
        this.fee = fee;
    }

    public static Fee of(int distance) {
        int fee = calculateFee(distance);
        return new Fee(fee);
    }

    public static Fee of(int distance, Set<Line> lines) {
        int fee = calculateFee(distance);
        fee += calculateLineExtraCharge(lines);
        return new Fee(fee);
    }

    private static int calculateFee(int distance) {
        int fee = DEFAULT_FEE;
        fee += calculateOverFirstSection(Math.min(distance, SECOND_SECTION_OVER_KR_STANDARD));
        fee += calculateOverSecondSection(distance);
        return fee;
    }

    private static int calculateOverFirstSection(int distance) {
        return calculateExtraCharge(distance, FIRST_SECTION_OVER_KR_STANDARD, FIRST_SECTION_DISTANCE_PER_KM_STANDARD);
    }

    private static int calculateOverSecondSection(int distance) {
        return calculateExtraCharge(distance, SECOND_SECTION_OVER_KR_STANDARD, SECOND_SECTION_DISTANCE_PER_KM_STANDARD);
    }

    private static int calculateExtraCharge(int distance, int overKilometer, int perDistance) {
        if (distance <= overKilometer) {
            return 0;
        }
        distance -= overKilometer;
        return (int) (Math.ceil((distance - 1) / perDistance) + 1) * EXTRA_CHARGE;
    }

    private static int calculateLineExtraCharge(Set<Line> lines) {
        ExtraCharge extraCharge = ExtraCharge.of(0);
        for (Line line : lines) {
            extraCharge = ExtraCharge.max(extraCharge, line.getExtraCharge());
        }
        return extraCharge.getExtraCharge();
    }

    public int getFee() {
        return fee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fee fee1 = (Fee) o;
        return fee == fee1.fee;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fee);
    }
}
