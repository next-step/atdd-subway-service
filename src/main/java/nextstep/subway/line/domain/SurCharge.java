package nextstep.subway.line.domain;

import nextstep.subway.common.exception.InvalidDataException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SurCharge {
    private static final String INVALID_SURCHARGE_EXCEPTION = "추가요금은 0 이상이어야 합니다.";

    @Column
    private int surCharge;

    public SurCharge() {
        this.surCharge = 0;
    }

    public SurCharge(int surCharge) {
        validateMinNumber(surCharge);
        this.surCharge = surCharge;
    }

    private void validateMinNumber(int surCharge) {
        if (surCharge < 0) {
            throw new InvalidDataException(INVALID_SURCHARGE_EXCEPTION);
        }
    }

    public int value() {
        return surCharge;
    }
}
