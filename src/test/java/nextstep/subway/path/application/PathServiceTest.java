package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.stream.Collectors;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private LineService lineService;

    @Mock
    private StationService stationService;

    @InjectMocks
    private PathService pathService;

    private Station 강남역;
    private Station 판교역;
    private Station 도곡역;

    private Line 신분당선;
    private Line 수인분당선;

    @BeforeEach
    void setUp() {

        강남역 = Station.from("강남역");
        판교역 = Station.from("판교역");
        도곡역 = Station.from("도곡역");
        Station 양재역 = Station.from("양재역");
        Station 한티역 = Station.from("한티역");

        Section 도곡_한티_구간 = Section.of(도곡역, 한티역, Distance.from(5));
        Section 양재_판교_구간 = Section.of(양재역, 판교역, Distance.from(5));
        Section 강남_판교_구간 = Section.of(강남역, 판교역, Distance.from(15));

        신분당선 = Line.of("신분당선", "bg-red-600", 강남_판교_구간);
        신분당선.addSection(양재_판교_구간);
        수인분당선 = Line.of("수인분당선", "bg-yellow-600", 도곡_한티_구간);
    }

    @Test
    @DisplayName("지하철 경로 조회를 하면 최단 거리의 경로가 조회된다.")
    void findShortestPath() {
        // given
        PathRequest pathRequest = new PathRequest(1L, 3L);
        when(stationService.findStationById(pathRequest.getDepartureId())).thenReturn(강남역);
        when(stationService.findStationById(pathRequest.getArrivalId())).thenReturn(판교역);
        when(lineService.findAll()).thenReturn(Arrays.asList(신분당선, 수인분당선));

        // when
        PathResponse pathResponse = pathService.findShortestPath(pathRequest);

        // then
        assertAll(
                () -> assertThat(pathResponse.getStations().stream().map(StationResponse::getName).collect(Collectors.toList()))
                        .containsExactly("강남역", "양재역", "판교역"),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(15)
        );
    }

    @Test
    @DisplayName("출발/도착역을 같은 역으로 조회할 수 없다.")
    void findPathByDepartureArrivalSameStation() {
        // given
        PathRequest pathRequest = new PathRequest(1L, 1L);
        when(stationService.findStationById(pathRequest.getDepartureId())).thenReturn(강남역);
        when(stationService.findStationById(pathRequest.getArrivalId())).thenReturn(강남역);
        when(lineService.findAll()).thenReturn(Arrays.asList(신분당선, 수인분당선));

        // when & then
        assertThatThrownBy(() -> pathService.findShortestPath(pathRequest))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("출발역과 도착역은 같을 수 없습니다.");
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되지 않은 경우 조회할 수 없다.")
    void findPathByDepartureArrivalNotConnectStation() {
        // given
        PathRequest pathRequest = new PathRequest(1L, 4L);
        when(stationService.findStationById(pathRequest.getDepartureId())).thenReturn(강남역);
        when(stationService.findStationById(pathRequest.getArrivalId())).thenReturn(도곡역);
        when(lineService.findAll()).thenReturn(Arrays.asList(신분당선, 수인분당선));

        // when & then
        assertThatThrownBy(() -> pathService.findShortestPath(pathRequest))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("출발역과 도착역이 연결되지 않았습니다.");
    }

    @Test
    @DisplayName("존재하지 않은 출발/도착역은 조회할 수 없다.")
    void findPathByNoneLineStation() {
        // given
        PathRequest pathRequest = new PathRequest(1L, 6L);
        when(stationService.findStationById(pathRequest.getDepartureId())).thenReturn(강남역);
        given(stationService.findStationById(pathRequest.getArrivalId())).willThrow(NotFoundException.class);

        // when & then
        assertThatThrownBy(() -> pathService.findShortestPath(pathRequest))
                .isInstanceOf(NotFoundException.class);
    }
}
