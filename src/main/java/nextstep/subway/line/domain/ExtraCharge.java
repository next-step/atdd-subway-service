package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class ExtraCharge {
    private static final String INVALID_EXTRA_CHARGE = "추가 요금은 음수일 수 없습니다.";

    private int extraCharge;

    public ExtraCharge() {
    }

    public ExtraCharge(int extraCharge) {
        validateExtraCharge(extraCharge);
        this.extraCharge = extraCharge;
    }

    private void validateExtraCharge(int extraCharge) {
        if (extraCharge < 0) {
            throw new IllegalArgumentException(INVALID_EXTRA_CHARGE);
        }
    }

    public int extraCharge() {
        return extraCharge;
    }

}
