package nextstep.subway.auth.domain;

import nextstep.subway.auth.Policy.MemberPolicy;
import nextstep.subway.member.domain.Member;

public class TeenagerMember extends LoginMember {
    private TeenagerMember() {
    }

    public TeenagerMember(Long id, String email, int age, int subwayCharge, double discountAmount) {
        this.id = id;
        this.email = email;
        this.age = age;
        this.subwayCharge = subwayCharge;
        this.discountAmount = discountAmount;
    }

    public static TeenagerMember of(Member member, MemberPolicy memberPolicy) {
        return new TeenagerMember(member.getId(), member.getEmail(), member.getAge(), memberPolicy.getMemberCharge(), memberPolicy.getDiscountRate());
    }
}
