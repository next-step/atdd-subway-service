package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidSurchargeException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Surcharge {
    @Column(name = "surcharge", nullable = false)
    private Integer value;

    public Surcharge() {
        value = 0;
    }

    public Surcharge(Integer value) {
        if (value < 0) {
            throw new InvalidSurchargeException("노선 추가 요금은 0원보다 작을 수 없습니다.");
        }
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Surcharge surcharge = (Surcharge) o;
        return Objects.equals(value, surcharge.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
