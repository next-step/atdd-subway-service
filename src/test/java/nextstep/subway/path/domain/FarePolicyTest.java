package nextstep.subway.path.domain;

import static nextstep.subway.line.step.LineStep.line;
import static nextstep.subway.line.step.SectionStep.section;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Arrays;
import java.util.stream.Stream;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.domain.Age;
import nextstep.subway.common.domain.Email;
import nextstep.subway.common.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Sections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
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

    @ParameterizedTest(name = "[{index}] 게스트 사용자가 {0} 만큼 이동하면 요금은 {1}")
    @DisplayName("게스트 사용자 거리별 요금 계산")
    @CsvSource({"10,1250", "11,1350", "20,1450", "50,2150", "55,2150", "58,2250", "66,250"})
    void charge_perDistance(int distance, int expectedFare) {
        //given
        FarePolicy policy = FarePolicy.of(
            LoginMember.guest(),
            신분당선(distance, 0).sections());

        //when
        Fare fare = policy.fare();

        //then
        assertThat(fare).isEqualTo(Fare.from(expectedFare));
    }

    @ParameterizedTest(name = "[{index}] {0} 나이의 사용자가 50km 만큼 이동하면 요금은 {1}")
    @DisplayName("나이별 50km 거리의 요금 계산")
    @CsvSource({"6,900", "12,900", "13,1440", "19,2150"})
    void charge_perAge(int age, int expectedFare) {
        //given
        FarePolicy policy = FarePolicy.of(
            LoginMember.of(1, Email.from("email@email.com"), Age.from(age)),
            신분당선(50, 0).sections());

        //when
        Fare fare = policy.fare();

        //then
        assertThat(fare).isEqualTo(Fare.from(expectedFare));
    }

    @ParameterizedTest(name = "[{index}] 게스트 사용자가 {0} 추가 요금이 있으면 요금은 {1}")
    @DisplayName("게스트 사용자가 기본 추가 요금 500이 있는 상태로 추가 요금별 30km 거리 요금 계산")
    @CsvSource({"0,2150", "500,2150", "1000,2650"})
    void charge_perExtraPare(int extraFare, int expectedFare) {
        //given
        FarePolicy policy = FarePolicy.of(
            LoginMember.guest(),
            Lines.from(Arrays.asList(
                신분당선(10, 0),
                이호선(10, 500),
                삼호선(10, extraFare)
            )).sections()
        );

        //when
        Fare fare = policy.fare();

        //then
        assertThat(fare).isEqualTo(Fare.from(expectedFare));
    }

    private static Stream<Arguments> instance_nullArgument_thrownIllegalArgumentException() {
        return Stream.of(
            Arguments.of(LoginMember.guest(), null),
            Arguments.of(null, Sections.from(section("강남역", "양재역", 5)))
        );
    }

    private Line 신분당선(int distance, int extraFare) {
        return line("신분당선", "red", section("강남역", "양재역", distance), extraFare);
    }

    private Line 이호선(int distance, int extraFare) {
        return line("이호선", "red", section("교대역", "강남역", distance), extraFare);
    }

    private Line 삼호선(int distance, int extraFare) {
        return line("신분당선", "red", section("교대역", "양재역", distance), extraFare);
    }
}
