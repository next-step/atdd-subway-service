package nextstep.subway.fare.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fixture.LineFixture;
import nextstep.subway.fixture.MemberFixture;
import nextstep.subway.fixture.StationFixture;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.ShortestPathFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FareServiceTest {

    private final FareService fareService = new FareService();

    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        pathFinder = new ShortestPathFinder(Arrays.asList(LineFixture.이호선, LineFixture.삼호선, LineFixture.신분당선));
    }

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재  --- *3호선* --- 오금역
     *
     * 신분당선은 700원의 추가 요금이 있음
     */

    @ParameterizedTest(name = "[{0}] 회원에 대해서 [10km] 이내 거리에 해당되는 요금을 계산하여 반환한다")
    @MethodSource("회원_및_10키로_거리_미만_요금_파라미터")
    void calculate_fare_with_none_policy(String memberType, LoginMember member, Fare expectedFare) {
        // given
        // 최단 경로 : 남부터미널역 -*3호선*- 양재
        Path path = pathFinder.findPath(StationFixture.남부터미널역, StationFixture.양재역);

        // when
        Fare fare = fareService.calculateFare(member, path);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

    @ParameterizedTest(name = "[{0}] 회원에 대해서 [10km ~ 50km] 거리에 해당되는 요금을 계산하여 반환한다")
    @MethodSource("회원_및_50키로_거리_미만_요금_파라미터")
    void calculate_fare_with_over_ten_policy(String memberType, LoginMember member, Fare expectedFare) {
        // given
        // 최단 경로 : 남부터미널역 -*3호선*- 양재 -*신분당선*- 강남역
        // 신분당선은 700원의 추가 요금이 있음
        Path path = pathFinder.findPath(StationFixture.강남역, StationFixture.남부터미널역);

        // when
        Fare fare = fareService.calculateFare(member, path);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

    @ParameterizedTest(name = "[{0}] 회원에 대해서 50km 초과 거리에 해당되는 요금을 계산하여 반환한다")
    @MethodSource("회원_및_50키로_거리_초과_요금_파라미터")
    void calculate_fare_with_over_fifty_policy(String memberType, LoginMember member, Fare expectedFare) {
        // given
        // 최단 경로 : 교대역 -*3호선*- 남부터미널역 -*3호선*- 양재 -*3호선*- 오금역
        Path path = pathFinder.findPath(StationFixture.교대역, StationFixture.오금역);

        // when
        Fare fare = fareService.calculateFare(member, path);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> 회원_및_10키로_거리_미만_요금_파라미터() {
        return Stream.of(
                Arguments.of("어린이 회원", MemberFixture.로그인된_어린이_회원, Fare.of(450)),
                Arguments.of("청소년 회원", MemberFixture.로그인된_청소년_회원, Fare.of(720)),
                Arguments.of("일반 회원", MemberFixture.로그인된_일반_회원, Fare.of(1_250)),
                Arguments.of("익명", MemberFixture.익명, Fare.of(1250))
        );
    }

    private static Stream<Arguments> 회원_및_50키로_거리_미만_요금_파라미터() {
        return Stream.of(
                Arguments.of("어린이 회원", MemberFixture.로그인된_어린이_회원, Fare.of(900)),
                Arguments.of("청소년 회원", MemberFixture.로그인된_청소년_회원, Fare.of(1_440)),
                Arguments.of("일반 회원", MemberFixture.로그인된_일반_회원, Fare.of(2_150)),
                Arguments.of("익명", MemberFixture.익명, Fare.of(2_150))
        );
    }

    private static Stream<Arguments> 회원_및_50키로_거리_초과_요금_파라미터() {
        return Stream.of(
                Arguments.of("어린이 회원", MemberFixture.로그인된_어린이_회원, Fare.of(950)),
                Arguments.of("청소년 회원", MemberFixture.로그인된_청소년_회원, Fare.of(1_520)),
                Arguments.of("일반 회원", MemberFixture.로그인된_일반_회원, Fare.of(2_250)),
                Arguments.of("익명", MemberFixture.익명, Fare.of(2_250))
        );
    }
}
