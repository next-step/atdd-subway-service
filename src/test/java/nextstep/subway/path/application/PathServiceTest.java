package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.NotExistException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    private PathService pathService;
    private Line 신분당선, 이호선, 삼호선;
    private Station 강남역, 양재역, 교대역, 남부터미널역, 멀리있는역;


    @Mock
    private StationService stationService;
    @Mock
    private LineService lineService;
    @Mock
    private PathFinder pathFinder;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재  ---- *3호선* ---- 멀리있는역
     */
    @BeforeEach
    void setUp() {
        pathService = new PathService(stationService, lineService, pathFinder);

        // given
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        멀리있는역 = new Station("멀리있는역");
        신분당선 = new Line("신분당선", "빨강", 강남역, 양재역, Distance.of(25));
        이호선 = new Line("이호선", "초록", 교대역, 강남역, Distance.of(8));
        삼호선 = new Line("삼호선", "주황", 500, 교대역, 양재역, Distance.of(15));
        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, Distance.of(10)));
        삼호선.addSection(new Section(삼호선, 양재역, 멀리있는역, Distance.of(70)));
    }

    @ParameterizedTest(name = "경로 조회시 최단거리 및 기본요금 결과 반환")
    @MethodSource("basicMemberAndExpectedFee")
    void searchBasicFeeAndShortestPath(LoginMember loginMember, int expectedFee) {
        // given
        Set<Line> throughLine = new HashSet<>(Collections.singletonList(이호선));
        when(stationService.findStationById(1L)).thenReturn(강남역);
        when(stationService.findStationById(2L)).thenReturn(교대역);
        when(lineService.findAllLines()).thenReturn(Arrays.asList(이호선, 신분당선, 삼호선));
        when(pathFinder.getDijkstraPath(강남역, 교대역))
                .thenReturn(new Path(Arrays.asList(강남역, 교대역), 8, throughLine));

        // when
        PathResponse pathResponse = pathService.searchShortestPath(loginMember, 1L, 2L);

        // then
        assertAll(
                () -> assertThat(pathResponse.getStations()).hasSize(2),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(8),
                () -> assertThat(pathResponse.getFee()).isEqualTo(expectedFee)
        );
    }

    public static Stream<Arguments> basicMemberAndExpectedFee() {
        return Stream.of(
                Arguments.of(LoginMember.guest(), 1250),
                Arguments.of(new LoginMember("mond@mond.com", 6), 450),
                Arguments.of(new LoginMember("mond@mond.com", 13), 720),
                Arguments.of(new LoginMember("mond@mond.com", 20), 1250)
        );
    }

    @ParameterizedTest(name = "경로 조회시 최단거리(10km ~ 50km 이내) 및 추가요금이 포함된 결과 반환")
    @MethodSource("lessThan50KmMemberAndExpectedFee")
    void searchExtraFeeLessThan50KmAndShortestPath(LoginMember loginMember, int expectedFee) {
        // given
        Set<Line> throughLine = new HashSet<>(Collections.singletonList(신분당선));
        when(stationService.findStationById(1L)).thenReturn(강남역);
        when(stationService.findStationById(3L)).thenReturn(양재역);
        when(lineService.findAllLines()).thenReturn(Arrays.asList(이호선, 신분당선, 삼호선));
        when(pathFinder.getDijkstraPath(강남역, 양재역))
                .thenReturn(new Path(Arrays.asList(강남역, 양재역), 25, throughLine));

        // when
        PathResponse pathResponse = pathService.searchShortestPath(loginMember, 1L, 3L);

        // then
        assertAll(
                () -> assertThat(pathResponse.getStations()).hasSize(2),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(25),
                () -> assertThat(pathResponse.getFee()).isEqualTo(expectedFee)
        );
    }

    public static Stream<Arguments> lessThan50KmMemberAndExpectedFee() {
        return Stream.of(
                Arguments.of(LoginMember.guest(), 1550), // 1250 + 300
                Arguments.of(new LoginMember("mond@mond.com", 6), 600),
                Arguments.of(new LoginMember("mond@mond.com", 13), 960),
                Arguments.of(new LoginMember("mond@mond.com", 20), 1550)
        );
    }

    @ParameterizedTest(name = "경로 조회시 최단거리(50km 초과) 및 추가요금이 포함된 결과 반환")
    @MethodSource("moreThan50KmMemberAndExpectedFee")
    void searchExtraFeeMoreThan50KmAndShortestPath(LoginMember loginMember, int expectedFee) {
        // given
        Set<Line> throughLine = new HashSet<>(Arrays.asList(신분당선, 삼호선));
        when(stationService.findStationById(1L)).thenReturn(강남역);
        when(stationService.findStationById(10L)).thenReturn(멀리있는역);
        when(lineService.findAllLines()).thenReturn(Arrays.asList(이호선, 신분당선, 삼호선));
        when(pathFinder.getDijkstraPath(강남역, 멀리있는역))
                .thenReturn(new Path(Arrays.asList(강남역, 양재역, 멀리있는역), 95, throughLine));

        // when
        PathResponse pathResponse = pathService.searchShortestPath(loginMember, 1L, 10L);

        // then
        assertAll(
                () -> assertThat(pathResponse.getStations()).hasSize(3),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(95),
                () -> assertThat(pathResponse.getFee()).isEqualTo(expectedFee)
        );
    }

    public static Stream<Arguments> moreThan50KmMemberAndExpectedFee() {
        return Stream.of(
                Arguments.of(LoginMember.guest(), 3150), // 1250 + 800 + 600 + 500
                Arguments.of(new LoginMember("mond@mond.com", 6), 1400),
                Arguments.of(new LoginMember("mond@mond.com", 13), 2240),
                Arguments.of(new LoginMember("mond@mond.com", 20), 3150)
        );
    }

    @Test
    @DisplayName("경로 조회시 역이 존재하지 않는 경우 예외 발생")
    void searchNotExistStationPath() {
        // given
        when(stationService.findStationById(99L)).thenThrow(NotExistException.class);

        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> pathService.searchShortestPath(LoginMember.guest(), 99L, 10L));
    }
}
