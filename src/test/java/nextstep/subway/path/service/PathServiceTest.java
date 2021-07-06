package nextstep.subway.path.service;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.service.SectionService;
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
import static nextstep.subway.station.dto.StationResponse.of;
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

    private static Station 양평역 = new Station(1L, "양평역");
    private static Station 영등포구청역 = new Station(2L, "영등포구청");
    private static Station 영등포시장역 = new Station(3L, "영등포시장");
    private static Station 신길역 = new Station(4L, "신길역");
    private static Station 여의도역 = new Station(5L, "여의도역");
    private static Station 당산역 = new Station(6L, "당산역");
    private static Station 영등포역 = new Station(7L, "영등포역");
    private static Station 야탑역 = new Station(8L, "야탑역");
    private static Station 모란역 = new Station(9L, "모란역");
    private static Station 대구역 = new Station(Integer.MAX_VALUE, "대구역");

    private static Line 오호선 = new Line(1L, "오호선", "bg-red-600", 양평역, 영등포구청역, new Distance(10));
    private static Line 이호선 = new Line(2L, "이호선", "bg-red-600", 영등포구청역, 당산역, new Distance(10));
    private static Line 일호선 = new Line(3L, "일호선", "bg-red-600", 신길역, 영등포역, new Distance(5));
    private static Line 신분당선 = new Line(4L, "신분당선", "bg-red-600", 야탑역, 모란역, new Distance(5));


    private static Section 구간_양평역_영등포구청역 = new Section(1L, 오호선, 양평역, 영등포구청역, new Distance(10));
    private static Section 구간_영등포구청역_영등포시장역 = new Section(2L, 오호선, 영등포구청역, 영등포시장역, new Distance(5));
    private static Section 구간_영등포시장역_신길역 = new Section(3L, 오호선, 영등포시장역, 신길역, new Distance(10));
    private static Section 구간_신길역_여의도역 = new Section(4L, 오호선, 신길역, 여의도역, new Distance(10));
    private static Section 구간_영등포구청역_당산역 = new Section(5L, 이호선, 영등포구청역, 당산역, new Distance(10));
    private static Section 구간_신길역_영등포역 = new Section(6L, 오호선, 신길역, 영등포역, new Distance(5));
    private static Section 구간_야탑역_모란역 = new Section(7L, 신분당선, 야탑역, 모란역, new Distance(5));

    private static Sections 전체_구간 = new Sections(asList(구간_양평역_영등포구청역
            , 구간_영등포구청역_영등포시장역
            , 구간_영등포시장역_신길역
            , 구간_신길역_여의도역
            , 구간_영등포구청역_당산역
            , 구간_영등포구청역_영등포시장역
            , 구간_신길역_영등포역
            , 구간_야탑역_모란역));

    @MethodSource("methodSource_findPath_성공")
    @ParameterizedTest
    void findPath_성공(Station source, Station target, List<StationResponse> expectedStationResponse, int expectedDistance) {
        // given
        when(sectionService.findSections()).thenReturn(전체_구간);
        when(stationService.findById(source.getId())).thenReturn(source);
        when(stationService.findById(target.getId())).thenReturn(target);

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
        when(stationService.findById(source.getId())).thenReturn(source);
        when(stationService.findById(target.getId())).thenReturn(target);
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
