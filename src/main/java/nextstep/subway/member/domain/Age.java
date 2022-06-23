package nextstep.subway.member.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Age {
    @Column
    private int age;

    public Age(int age) {
        this.age = age;
    }

    public Age() {
    }

    public int getAge() {
        return age;
    }

    public int calculateAgeSale(int fare) {
        return AgeSale.of(age)
                .map(it -> it.calculate(fare))
                .orElse(fare);
    }
}
