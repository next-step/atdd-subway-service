package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.AuthMember;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.policy.AgeDiscountPolicy;
import nextstep.subway.path.policy.BasicAgeDiscountPolicy;
import nextstep.subway.path.policy.KidsAgeDiscountPolicy;
import nextstep.subway.path.policy.TeenagersAgeDiscountPolicy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("나이에 따른 할인 정책 테스")
class AgeFareTest {


    @DisplayName("나이에 따른 할인요금 확인")
    @ParameterizedTest(name = "#{index} - {2}의 경우 {3}.")
    @MethodSource("age_discount_policy")
    void age_fare_check(AuthMember authMember, AgeDiscountPolicy ageDiscountPolicy, String member, String answer) {

        // when
        AgeDiscountPolicy discountPolicy = AgeFare.getDiscountPolicy(authMember);

        // when && then
        assertThat(discountPolicy).isInstanceOf(ageDiscountPolicy.getClass());
    }

    private static Stream<Arguments> age_discount_policy() {
        return Stream.of(
                Arguments.of(
                        new LoginMember(1L, "kid@kid.com", 6),
                        new KidsAgeDiscountPolicy(),
                        "어린이(6~13)",
                        "어린이 할인정책이 적용됩니다"
                ),
                Arguments.of(new LoginMember(1L, "teenager@kid.com", 15),
                        new TeenagersAgeDiscountPolicy(),
                        "청소년(13~19)",
                        "청소년 할인정책이 적용됩니다"
                ),
                Arguments.of(new LoginMember(1L, "normal@kid.com", 20),
                        new BasicAgeDiscountPolicy(),
                        "일반인(19~65)",
                        "일반인은 할인정책 없습니다"
                )
        );
    }


}
