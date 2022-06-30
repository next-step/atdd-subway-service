package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.infrastructure.InMemoryLineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationFinder;
import nextstep.subway.station.infrastructure.InMemoryStationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 최단 경로 조회 로직")
public class PathServiceTest {
    private static final LoginMember 어린이 = new LoginMember(1L, "a@com", 12);
    private static final LoginMember 청소년 = new LoginMember(1L, "b@com", 18);
    private static final LoginMember 일반 = new LoginMember(1L, "c@com", 19);
    private static final LoginMember 비회원 = new LoginMember();

    private PathService pathService;

    @BeforeEach
    void setUp() {
        StationFinder stationFinder = new StationFinder(new InMemoryStationRepository());
        LineRepository lineRepository = new InMemoryLineRepository();

        pathService = new PathService(stationFinder, lineRepository);
    }

    @ParameterizedTest
    @MethodSource("할인혜택을_적용하기_위해_다양한_회원을_조회한다")
    void 최단_경로를_조회한다(LoginMember loginMember, int fare) {
        // when
        PathResponse response = pathService.findShortestPath(loginMember, 1L, 4L);

        // then
        assertAll(
                () -> assertThat(response.getDistance()).isEqualTo(9),
                () -> assertThat(response.getFare()).isEqualTo(fare)
        );
    }

    private static Stream<Arguments> 할인혜택을_적용하기_위해_다양한_회원을_조회한다() {
        return Stream.of(
                Arguments.of(
                        어린이, 700
                ),
                Arguments.of(
                        청소년, 1120
                ),
                Arguments.of(
                        일반, 1750
                ),
                Arguments.of(
                        비회원, 1750
                )
        );
    }
}
