package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidSurchargeException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Surcharge {
    @Column(name = "surcharge", nullable = false)
    private Integer value;

    public Surcharge() {
        this.value = 0;
    }

    public Surcharge(Integer value) {
        if (value < 0) {
            throw new InvalidSurchargeException("노선 추가 요금은 0원보다 작을 수 없습니다.");
        }
        this.value = value;
    }
}
