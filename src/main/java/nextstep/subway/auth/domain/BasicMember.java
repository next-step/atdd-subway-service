package nextstep.subway.auth.domain;

import nextstep.subway.auth.Policy.MemberPolicy;
import nextstep.subway.member.domain.Member;

public class BasicMember extends LoginMember{
    public BasicMember() {
        this.subwayCharge = SUBWAY_CHARGE;
        this.discountAmount = 1;
    }

    public BasicMember(Long id, String email, int age, int subwayCharge, double discountAmount) {
        this.id = id;
        this.email = email;
        this.age = age;
        this.subwayCharge = subwayCharge;
        this.discountAmount = discountAmount;
    }

    public static BasicMember of(Member member, MemberPolicy memberPolicy) {
        return new BasicMember(member.getId(), member.getEmail(), member.getAge(), memberPolicy.getMemberCharge(SUBWAY_CHARGE), memberPolicy.getDiscountRate());
    }
}
