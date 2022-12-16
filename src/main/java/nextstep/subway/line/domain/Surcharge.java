package nextstep.subway.line.domain;

import nextstep.subway.common.exception.InvalidDataException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Surcharge {
    private static final String INVALID_SURCHARGE_EXCEPTION = "추가요금은 0 이상이어야 합니다.";

    @Column
    private int surcharge;

    public Surcharge() {
        this.surcharge = 0;
    }

    public Surcharge(int surcharge) {
        validateMinNumber(surcharge);
        this.surcharge = surcharge;
    }

    private void validateMinNumber(int surcharge) {
        if (surcharge < 0) {
            throw new InvalidDataException(INVALID_SURCHARGE_EXCEPTION);
        }
    }

    public int value() {
        return surcharge;
    }
}
