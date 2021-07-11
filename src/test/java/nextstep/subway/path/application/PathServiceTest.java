package nextstep.subway.path.service;

import nextstep.subway.line.application.SectionService;
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
import static nextstep.subway.station.domain.StationFixture.*;
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

    @MethodSource("methodSource_findPath_성공")
    @ParameterizedTest
    void findPath_성공(Station source, Station target, List<StationResponse> expectedStationResponse, int expectedDistance) {
        // given
        when(sectionService.findSections()).thenReturn(전체_구간);
        when(stationService.findAllById(asList(source.getId(), target.getId())))
                .thenReturn(new Stations(asList(source, target)));

        PathService pathService = new PathService(sectionService, stationService, pathFinder);
        PathRequest pathRequest = new PathRequest(source.getId(), target.getId());

        // when
        PathResponse pathResponse = pathService.findPaths(pathRequest);

        // then
        assertThat(pathResponse.getStations()).isEqualTo(expectedStationResponse);
        assertThat(pathResponse.getDistance()).isEqualTo(expectedDistance);
    }

    static Stream<Arguments> methodSource_findPath_성공() {
        return Stream.of(
                Arguments.of(영등포구청역, 신길역, of(new Stations(asList(영등포구청역, 영등포시장역, 신길역))), 15),
                Arguments.of(당산역, 양평역, of(new Stations(asList(당산역, 영등포구청역, 양평역))), 20),
                Arguments.of(영등포역, 당산역, of(new Stations(asList(영등포역, 신길역, 영등포시장역, 영등포구청역, 당산역))), 30)
        );
    }

    @MethodSource("methodSource_findPath_예외")
    @ParameterizedTest
    void findPath_예외(Station source, Station target, Class<? extends RuntimeException> expectedException) {
        // given
        when(sectionService.findSections()).thenReturn(전체_구간);
        when(stationService.findAllById(asList(source.getId(), target.getId())))
                .thenReturn(new Stations(asList(source, target)));

        PathService pathService = new PathService(sectionService, stationService, pathFinder);
        PathRequest pathRequest = new PathRequest(source.getId(), target.getId());

        // when, then
        assertThatExceptionOfType(expectedException)
                .isThrownBy(() -> pathService.findPaths(pathRequest));
    }

    static Stream<Arguments> methodSource_findPath_예외() {
        return Stream.of(
                Arguments.of(영등포구청역, 영등포구청역, IllegalFindingPathException.class),
                Arguments.of(영등포구청역, 모란역, CannotReachableException.class)
        );
    }
}
