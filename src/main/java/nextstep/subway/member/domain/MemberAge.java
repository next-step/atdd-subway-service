package nextstep.subway.member.domain;

import nextstep.subway.path.domain.FareAgePolicy;

import javax.persistence.Embeddable;
import java.util.Optional;

@Embeddable
public class MemberAge {
    private Integer age;

    protected MemberAge() {
    }

    public MemberAge(Integer age) {
        this.age = age;
    }

    public int calculateDiscountFare(int fare) {
        Optional<FareAgePolicy> farePolicy = FareAgePolicy.findFarePolicyByAge(age);

        return farePolicy
                .map(fareAgePolicy -> (int) ((fare - fareAgePolicy.getDeduction()) * fareAgePolicy.getDiscountFare() + fareAgePolicy.getDeduction()))
                .orElse(fare);
    }

    public Integer findAge() {
        return age;
    }
}
