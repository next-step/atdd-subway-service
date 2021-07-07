package nextstep.subway.auth.domain;

import nextstep.subway.auth.Policy.MemberPolicy;
import nextstep.subway.member.domain.Member;

public class BasicMember extends LoginMember{
    public BasicMember(MemberPolicy memberPolicy) {
        this.subwayCharge = memberPolicy.getMemberCharge();
        this.discountAmount = memberPolicy.getDiscountRate();
    }

    public BasicMember(Long id, String email, int age, int subwayCharge, double discountAmount) {
        this.id = id;
        this.email = email;
        this.age = age;
        this.subwayCharge = subwayCharge;
        this.discountAmount = discountAmount;
    }

    public static BasicMember of(Member member, MemberPolicy memberPolicy) {
        return new BasicMember(member.getId(), member.getEmail(), member.getAge(), memberPolicy.getMemberCharge(), memberPolicy.getDiscountRate());
    }
}
