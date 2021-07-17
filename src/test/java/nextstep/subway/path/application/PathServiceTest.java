package nextstep.subway.path.application;

import nextstep.subway.line.application.SectionService;
import nextstep.subway.member.domain.Age;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.CannotReachableException;
import nextstep.subway.path.exception.IllegalFindingPathException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static nextstep.subway.line.fixture.SectionFixture.전체_구간;
import static nextstep.subway.station.dto.StationResponse.of;
import static nextstep.subway.station.fixture.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DisplayName("PathService 테스트 구현")
class PathServiceTest {
    @MockBean
    private SectionService sectionService;

    @MockBean
    private StationService stationService;

    private PathFinder pathFinder = new PathFinder();

    private static Station 대구역 = new Station(Integer.MAX_VALUE, "대구역");

    /*
    From nextstep.subway.line.fixture.LineFixture

    # 지하철 노선도 #
                            (2호선)                          (1호선)
     (5호선)  양평역 - 10 - 영등포구청역 - 5 - 영등포시장역 - 10 - 신길역 - 10 여의도 역  (5호선)
                              ㅣ                               ㅣ
                              10                               5
                              ㅣ                               ㅣ
                             당산역                           영등포역
                             (2호선)                          (1호선)

     # 노선별 추가요금 #
     - 5호선 : 500원
     - 2호선 : 200원
     - 1호선 : 100원
     */
    @MethodSource("methodSource_findPath_성공")
    @ParameterizedTest
    void findPath_성공(Station source, Station target, Age age, List<StationResponse> expectedStationResponse, int expectedDistance, int expectedFare) {
        // given
        when(sectionService.findSections()).thenReturn(전체_구간);
        when(stationService.findAllById(asList(source.getId(), target.getId())))
                .thenReturn(new Stations(asList(source, target)));

        PathService pathService = new PathService(sectionService, stationService, pathFinder);
        PathRequest pathRequest = new PathRequest(source.getId(), target.getId());

        // when
        PathResponse pathResponse = pathService.findPaths(pathRequest, age);

        // then
        assertThat(pathResponse.getStations()).isEqualTo(expectedStationResponse);
        assertThat(pathResponse.getDistance()).isEqualTo(expectedDistance);
        assertThat(pathResponse.getFare()).isEqualTo(expectedFare);
    }

    static Stream<Arguments> methodSource_findPath_성공() {
        return Stream.of(
                Arguments.of(영등포구청역, 신길역, new Age(20), of(new Stations(asList(영등포구청역, 영등포시장역, 신길역))), 15, 2050),
                Arguments.of(당산역, 양평역, new Age(6), of(new Stations(asList(당산역, 영등포구청역, 양평역))), 20, 1250),
                Arguments.of(영등포역, 당산역, new Age(13), of(new Stations(asList(영등포역, 신길역, 영등포시장역, 영등포구청역, 당산역))), 30, 1950)
        );
    }

    @MethodSource("methodSource_findPath_예외")
    @ParameterizedTest
    void findPath_예외(Station source, Station target, Class<? extends RuntimeException> expectedException) {
        // given
        when(sectionService.findSections()).thenReturn(전체_구간);
        when(stationService.findAllById(asList(source.getId(), target.getId())))
                .thenReturn(new Stations(asList(source, target)));

        Age age = new Age(30);
        PathService pathService = new PathService(sectionService, stationService, pathFinder);
        PathRequest pathRequest = new PathRequest(source.getId(), target.getId());

        // when, then
        assertThatExceptionOfType(expectedException)
                .isThrownBy(() -> pathService.findPaths(pathRequest, age));
    }

    static Stream<Arguments> methodSource_findPath_예외() {
        return Stream.of(
                Arguments.of(영등포구청역, 영등포구청역, IllegalFindingPathException.class),
                Arguments.of(영등포구청역, 모란역, CannotReachableException.class)
        );
    }
}
