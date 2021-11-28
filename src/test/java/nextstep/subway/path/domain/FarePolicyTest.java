package nextstep.subway.path.domain;

import static nextstep.subway.line.step.SectionStep.section;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.stream.Stream;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Sections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("요금 정책")
class FarePolicyTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> FarePolicy.of(
                LoginMember.guest(),
                Sections.from(section("강남", "양재", 5)))
            );
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 값 으로 객체화 할 수 없다.")
    @MethodSource
    @DisplayName("로그인 유저와 구간들은 필수")
    void instance_nullArgument_thrownIllegalArgumentException(
        LoginMember loginMember, Sections sections) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> FarePolicy.of(loginMember, sections))
            .withMessageEndingWith("필수입니다.");
    }

    @Test
    @DisplayName("거리별 요금")
    void charge_perDistance() {
        FarePolicy.of(
            LoginMember.guest(),
            Sections.from(section("강남", "양재", 5)));
    }

    private static Stream<Arguments> instance_nullArgument_thrownIllegalArgumentException() {
        return Stream.of(
            Arguments.of(LoginMember.guest(), null),
            Arguments.of(null, Sections.from(section("강남", "양재", 5)))
        );
    }
}
