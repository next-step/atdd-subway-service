package nextstep.subway.path.domain.enums;

public enum FeeDistanceRangeType {

    DEFAULT_FEE(1250),
    ADD_PER_KM_FEE(100),
    MIN_KM_LIMIT(10),
    MAX_KM_LIMIT(50),
    MAX_KM_PER(8),
    MIN_KM_PER(5),
    CORRECTION_VALUE(1);

    private final int kmPerFee;

    FeeDistanceRangeType(int kmPerFee) {
        this.kmPerFee = kmPerFee;
    }

    public int getKmPerFee() {
        return kmPerFee;
    }
}
