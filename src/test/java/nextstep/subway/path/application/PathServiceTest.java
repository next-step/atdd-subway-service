package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.CustomException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static nextstep.subway.exception.CustomExceptionMessage.SAME_SOURCE_AND_TARGET_STATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("경로 조회 Service 테스트")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @InjectMocks
    private PathService pathService;

    private final long 강남역 = 1l;
    private final long 남부터미널역 = 4l;

    Station station_강남역;
    Station station_양재역;
    Station station_교대역;
    Station station_남부터미널역;

    Line 이호선;
    Line 삼호선;
    Line 신분당선;

    /**
     * 교대역    --- *2호선*(10) --- 강남역
     * |                        |
     * *3호선*(3)                *신분당선*(10)
     * |                        |
     * 남부터미널역--- *3호선*(2) --- 양재
     * 독립역 --- *나홀로선*(10) --- 혼자역
     */
    @BeforeEach
    void setUp() {
        // given
        station_강남역 = new Station("강남역");
        station_양재역 = new Station("양재역");
        station_교대역 = new Station("교대역");
        station_남부터미널역 = new Station("남부터미널역");

        이호선 = new Line.Builder("이호선").color("RED").additionalFare(0).upStation(station_교대역).downStation(station_강남역).distance(10).build();

        삼호선 = new Line.Builder("삼호선").color("GREEN").additionalFare(0).upStation(station_교대역).downStation(station_남부터미널역).distance(3).build();
        삼호선.addSection(station_남부터미널역, station_양재역, 2);

        신분당선 = new Line.Builder("신분당선").color("BLUE").additionalFare(0).upStation(station_강남역).downStation(station_양재역).distance(10).build();
    }

    @DisplayName("출발 역과 도착 역 사이의 최단 거리 조회 테스트")
    @Test
    void getPathsTest() {
        // given
        Mockito.when(stationService.findStationById(강남역)).thenReturn(station_강남역);
        Mockito.when(stationService.findStationById(남부터미널역)).thenReturn(station_남부터미널역);
        Mockito.when(lineRepository.findAll()).thenReturn(Arrays.asList(이호선, 삼호선, 신분당선));

        // when
        PathResponse pathResponse = pathService.getPaths(new LoginMember(), 남부터미널역, 강남역);

        // then
        assertAll(() -> {
            assertThat(pathResponse).isNotNull();
            assertThat(pathResponse.getStations()).isNotEmpty().containsExactly(station_남부터미널역, station_양재역, station_강남역);
            assertThat(pathResponse.getDistance()).isEqualTo(12);
        });

        Mockito.verify(stationService).findStationById(남부터미널역);
        Mockito.verify(stationService).findStationById(강남역);
        Mockito.verify(lineRepository).findAll();
    }

    @DisplayName("같은 출발 역과 도착 역 조회 테스트")
    @Test
    void getSameSourceAndTargetTest() {
        // when
        assertThatThrownBy(() -> pathService.getPaths(new LoginMember(), 남부터미널역, 남부터미널역))
            .isInstanceOf(CustomException.class)
            .hasMessageContaining(SAME_SOURCE_AND_TARGET_STATION.getMessage());
    }
}
