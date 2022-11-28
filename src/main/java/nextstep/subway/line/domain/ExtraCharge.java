package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ExtraCharge {
    private static final int MINIMUM = 0;

    @Column(name = "extra_charge", nullable = false)
    private int value;

    protected ExtraCharge() {}

    public ExtraCharge(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < MINIMUM) {
            throw new IllegalArgumentException("추가요금은 0 보다 작을 수 없습니다.");
        }
    }
}
