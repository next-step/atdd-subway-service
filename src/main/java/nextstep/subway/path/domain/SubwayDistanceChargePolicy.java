package nextstep.subway.path.domain;

import java.util.Arrays;

public enum SubwayDistanceChargePolicy {
    FIFTY(50, 8, 100){
        @Override
        public int calculateDistanceExtraCharge(int distance) {
            return calculateOverFare(distance - this.policyStartDistance(), this.perDistance(), this.extraCharge())
                    + TEN.calculateDistanceExtraCharge(this.policyStartDistance());
        }
    },
    TEN(10, 5, 100) {
        @Override
        public int calculateDistanceExtraCharge(int distance) {
            return calculateOverFare(distance - this.policyStartDistance(), this.perDistance(), this.extraCharge());
        }
    },
    DEFAULT(0, 0, 0) {
        @Override
        public int calculateDistanceExtraCharge(int distance) {
            return 0;
        }
    };

    private final int policyStartDistance;
    private final int perDistance;
    private final int extraCharge;

    SubwayDistanceChargePolicy(int policyStartDistance, int perDistance, int extraCharge) {
        this.policyStartDistance = policyStartDistance;
        this.perDistance = perDistance;
        this.extraCharge = extraCharge;
    }

    public static SubwayDistanceChargePolicy subwayChargePolicy(int distance) {
        return Arrays.stream(SubwayDistanceChargePolicy.values())
                .filter(it -> it.findChargePolicy(distance))
                .findFirst()
                .orElse(DEFAULT);
    }

    public abstract int calculateDistanceExtraCharge(int distance);

    private boolean findChargePolicy(int distance) {
        return distance > policyStartDistance;
    }

    private static int calculateOverFare(int distance, int perDistance, int extraCharge) {
        return (int) ((Math.ceil((distance - 1) / perDistance) + 1) * extraCharge);
    }

    public int policyStartDistance() {
        return policyStartDistance;
    }

    public int perDistance() {
        return perDistance;
    }

    public int extraCharge() {
        return extraCharge;
    }
}
