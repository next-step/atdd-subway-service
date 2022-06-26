package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.LoginMember;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DiscountTypeTest {

    private LoginMember 사용자;
    private LoginMember 청소년_사용자;
    private LoginMember 어린이_사용자;
    private int 기본_운임 = 1_250;

    @BeforeEach
    void setUp() {
        사용자 = new LoginMember(1L, "20km@github.com", 20);
        청소년_사용자 = new LoginMember(2L, "14km@github.com", 14);
        어린이_사용자 = new LoginMember(3L, "12km@github.com", 12);
    }

    @Test
    @DisplayName("기본 운임 계산")
    void generalDiscount() {
        DiscountPolicy 기본_정책 = DiscountType.ofDiscountPolicy(사용자.getAge());

        int fare = 기본_정책.discountedFare(기본_운임);

        Assertions.assertThat(fare).isEqualTo(1_250);
    }

    @Test
    @DisplayName("청소년 운임 계산")
    void youthDiscount() {
        DiscountPolicy 청소년_정책 = DiscountType.ofDiscountPolicy(청소년_사용자.getAge());

        int fare = 청소년_정책.discountedFare(기본_운임);

        Assertions.assertThat(fare).isEqualTo(720);
    }

    @Test
    @DisplayName("어린이 운임 계산")
    void childDiscount() {
        DiscountPolicy 어린이_정책 = DiscountType.ofDiscountPolicy(어린이_사용자.getAge());

        int fare = 어린이_정책.discountedFare(기본_운임);

        Assertions.assertThat(fare).isEqualTo(450);
    }
}
