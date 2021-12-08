package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.SectionService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.PathFindException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("경로 탐색 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    StationService stationService;

    @Mock
    SectionService sectionService;

    private LoginMember 게스트;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 용산역;
    private Station 동작역;
    private Station 이촌역;
    private Sections 전체_구간;

    @BeforeEach
    public void setup() {
        게스트 = LoginMember.GUEST;

        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        용산역 = new Station("용산역");
        동작역 = new Station("신림역");
        이촌역 = new Station("이촌역");

        Line 신분당선 = new Line("신분당선", "bg-red-600");
        Line 이호선 = new Line("이호선", "bg-red-600");
        Line 삼호선 = new Line("삼호선", "bg-red-600");
        Line 사호선 = new Line("사호선", "bg-red-600");

        List<Section> sections = new ArrayList<>();
        sections.add(new Section(신분당선, 강남역, 양재역, 10));
        sections.add(new Section(이호선, 교대역, 강남역, 10));
        sections.add(new Section(삼호선, 교대역, 남부터미널역, 2));
        sections.add(new Section(삼호선, 남부터미널역, 양재역, 3));
        sections.add(new Section(사호선, 동작역, 이촌역, 10));

        전체_구간 = Sections.of(sections);
    }

    @DisplayName("최단 경로 탐색")
    @Test
    void 최단_경로_탐색() {
        // given
        PathService pathService = new PathService(stationService, sectionService);
        when(stationService.findStationById(1L))
                .thenReturn(교대역);
        when(stationService.findStationById(2L))
                .thenReturn(양재역);
        when(sectionService.findAll())
                .thenReturn(전체_구간);

        // when
        PathResponse pathResponse = pathService.findPath(게스트, 1L, 2L);

        // then
        assertAll(
                () -> assertThat(pathResponse.getStations().size())
                        .isEqualTo(3),
                () -> assertThat(pathResponse.getDistance())
                        .isEqualTo(5),
                () -> 최단_경로_확인(pathResponse, Arrays.asList(교대역, 남부터미널역, 양재역))
        );
    }

    @DisplayName("실패 테스트")
    @Nested
    class FailTest {

        @DisplayName("출발역과_도착역이_같음")
        @Test
        void 출발지와_목적지가_같음() {
            // given
            PathService pathService = new PathService(stationService, sectionService);
            when(stationService.findStationById(1L))
                    .thenReturn(강남역);
            when(sectionService.findAll())
                    .thenReturn(전체_구간);

            // when
            ThrowableAssert.ThrowingCallable throwingCallable = () -> pathService.findPath(게스트, 1L, 1L);

            // then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(PathFindException.class);
        }
        @DisplayName("출발역이 전체 구간에 포함되지 않음")
        @Test
        void 출발역이_전체_구간에_포함되지_않음() {
            // given
            PathService pathService = new PathService(stationService, sectionService);
            when(stationService.findStationById(1L))
                    .thenReturn(용산역);
            when(stationService.findStationById(2L))
                    .thenReturn(강남역);
            when(sectionService.findAll())
                    .thenReturn(전체_구간);

            // when
            ThrowableAssert.ThrowingCallable throwingCallable = () -> pathService.findPath(게스트, 1L, 2L);

            // then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(PathFindException.class);
        }

        @DisplayName("도착역이 전체 구간에 포함되지 않음")
        @Test
        void 도착역이_전체_구간에_포함되지_않음() {
            // given
            PathService pathService = new PathService(stationService, sectionService);
            when(stationService.findStationById(1L))
                    .thenReturn(강남역);
            when(stationService.findStationById(2L))
                    .thenReturn(용산역);
            when(sectionService.findAll())
                    .thenReturn(전체_구간);

            // when
            ThrowableAssert.ThrowingCallable throwingCallable = () -> pathService.findPath(게스트, 1L, 2L);

            // then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(PathFindException.class);
        }

        @DisplayName("출발역과 도착역이 연결되어 있지 않음 - 출발역 기준")
        @Test
        void 출발역과_도착역이_연결되어_있지_않음_출발역_기준() {
            // given
            PathService pathService = new PathService(stationService, sectionService);
            when(stationService.findStationById(1L))
                    .thenReturn(동작역);
            when(stationService.findStationById(2L))
                    .thenReturn(강남역);
            when(sectionService.findAll())
                    .thenReturn(전체_구간);

            // when
            ThrowableAssert.ThrowingCallable throwingCallable = () -> pathService.findPath(게스트, 1L, 2L);

            // then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(PathFindException.class);
        }

        @DisplayName("출발역과 도착역이 연결되어 있지 않음 - 도착역 기준")
        @Test
        void 출발역과_도착역이_연결되어_있지_않음_도착역_기준() {
            // given
            PathService pathService = new PathService(stationService, sectionService);
            when(stationService.findStationById(1L))
                    .thenReturn(강남역);
            when(stationService.findStationById(2L))
                    .thenReturn(이촌역);
            when(sectionService.findAll())
                    .thenReturn(전체_구간);

            // when
            ThrowableAssert.ThrowingCallable throwingCallable = () -> pathService.findPath(게스트, 1L, 2L);

            // then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(PathFindException.class);
        }
    }

    public static void 최단_경로_확인(PathResponse pathResponse, List<Station> expectedStations) {
        List<Long> stationIds = pathResponse.getStations()
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(Station::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }
}
