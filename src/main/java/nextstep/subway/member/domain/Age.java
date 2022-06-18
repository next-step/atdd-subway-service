package nextstep.subway.member.domain;

import nextstep.subway.exception.InvalidAgeException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Age {
    @Column(name = "age", nullable = false)
    private Integer value;

    public Age() {
        value = 6;
    }

    public Age(Integer value) {
        if (value < 6) {
            throw new InvalidAgeException("6세 이상부터 지하철 회원이 될 수 있습니다.");
        }
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
