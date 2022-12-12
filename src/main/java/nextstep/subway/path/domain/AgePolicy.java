package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.MemberType;
import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.AgeType;

public class AgePolicy {
    private final Age age;
    private final MemberType memberType;
    private final int fee;

    public static AgePolicy from(Age age, MemberType memberType, int fee) {
        return new AgePolicy(age, memberType, fee);
    }

    private AgePolicy(Age age, MemberType memberType, int fee) {
        this.age = age;
        this.memberType = memberType;
        this.fee = fee;
    }


    public int discount(AgeStrategy strategy) {
        if (memberType.isNotLogin()) {
            return fee;
        }

        return strategy.discount(fee, age);
    }
}
