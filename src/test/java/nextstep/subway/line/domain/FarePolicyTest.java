package nextstep.subway.line.domain;

import java.util.stream.Stream;
import nextstep.subway.auth.domain.LoginMember;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FarePolicyTest {

    @Test
    void 회원가입_안한경우_기본_요금정책_생성확인() {
        FarePolicy subwayPolicy = FarePolicyFactory.create(new LoginMember());
        Assertions.assertThat(subwayPolicy).isInstanceOf(FareGuest.class);
    }

    /**
     * @param distance
     * @param expectedFare 요금 = 기본요금 + 거리당(100원)
     */
    @ParameterizedTest
    @MethodSource("provideGuest")
    void 회원가입_안한경우_거리별_요금계산(Distance distance, Fare expectedFare) {
        FarePolicy subwayPolicy = FarePolicyFactory.create(new LoginMember());
        Fare policyFare = subwayPolicy.calculateFare(distance);
        Assertions.assertThat(policyFare).isEqualTo(expectedFare);
    }

    public static Stream<Arguments> provideGuest() {
        return Stream.of(
            Arguments.of(new Distance(21), new Fare(1550L)), // 11
            Arguments.of(new Distance(15), new Fare(1350L)), // 5
            Arguments.of(new Distance(59), new Fare(1450L))  // 9
        );
    }

    @Test
    void 회원가입_한경우_멤버_요금_정책_생성확인() {
        FarePolicy subwayPolicy = FarePolicyFactory.create(
            new LoginMember(1L, "email", 10));
        Assertions.assertThat(subwayPolicy).isInstanceOf(FareMember.class);
    }

    @ParameterizedTest
    @MethodSource("provideMember")
    void 회원가입_한경우_거리별와_나이에따른_요금_정책(int age, Distance distance, Fare fare) {
        FarePolicy subwayPolicy = FarePolicyFactory.create(new LoginMember(1L, "email", age));
        Fare policyFare = subwayPolicy.calculateFare(distance);
        Assertions.assertThat(policyFare).isEqualTo(fare);
    }

    public static Stream<Arguments> provideMember() {
        return Stream.of(
            Arguments.of(6, new Distance(10), new Fare(400L)),
            Arguments.of(6, new Distance(12), new Fare(500L)),
            Arguments.of(13, new Distance(10), new Fare(700L)),
            Arguments.of(13, new Distance(12), new Fare(800L))
        );
    }
}