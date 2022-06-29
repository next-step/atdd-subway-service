package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ExtraFare {

    private static final int MIN = 0;

    @Column(name = "extra_fare")
    private long value;

    protected ExtraFare() {
    }

    public ExtraFare(long value) {
        if (value < MIN) {
            throw new IllegalArgumentException("추가 요금이 0보다 작습니다.");
        }
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
