package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.DiscountStrategy;
import nextstep.subway.path.domain.NoDiscountStrategy;
import nextstep.subway.path.domain.NotFoundPathException;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.NotFoundStationException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @InjectMocks
    private PathService pathService;

    @Mock
    private StationService stationService;

    @Mock
    private LineService lineService;

    private DiscountStrategy 할인없음;

    private Station 계양역;
    private Station 귤현역;
    private Station 김포공항역;
    private Station 마곡나루역;
    private Station 서울역;

    private List<Line> lines;

    @BeforeEach
    void setUp() {

        할인없음 = new NoDiscountStrategy();

        계양역 = new Station("계양역");
        귤현역 = new Station("귤현역");
        김포공항역 = new Station("김포공항역");
        마곡나루역 = new Station("마곡나루역");
        서울역 = new Station("서울역");

        Line 인천1호선 = new Line("인천1호선", "color", 계양역, 귤현역, 9);
        Line 공항철도 = new Line("공항철도", "color", 김포공항역, 계양역, 66);
        공항철도.addSection(new Section(인천1호선, 마곡나루역, 김포공항역, 23));

        lines = Lists.newArrayList(인천1호선, 공항철도);
    }

    @DisplayName("출발역과 도착역이 같으면 오류")
    @Test
    void findPathFail01() {

        // given
        long id = 1L;
        given(stationService.findById(id)).willReturn(계양역);
        given(lineService.findLines()).willReturn(lines);

        // when, then
        assertThatExceptionOfType(NotFoundPathException.class).isThrownBy(
            () -> pathService.findShortestPath(할인없음, id, id)
        );
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않으면 오류")
    @Test
    void findPathFail02() {

        // given
        long 계양역_id = 1L;
        long 서울역_id = 2L;

        given(stationService.findById(계양역_id)).willReturn(계양역);
        given(stationService.findById(서울역_id)).willReturn(서울역);
        given(lineService.findLines()).willReturn(lines);

        // when, then
        assertThatExceptionOfType(NotFoundPathException.class).isThrownBy(
            () -> pathService.findShortestPath(할인없음, 계양역_id, 서울역_id)
        );
    }

    @DisplayName("존재하지 않는 출발역이나 도착역을 조회하면 오류")
    @Test
    void findPathFail03() {
        // given
        long 부평역_id = 1L;
        long 서울역_id = 2L;

        given(stationService.findById(부평역_id)).willThrow(NotFoundStationException.class);

        // when, then
        assertThatExceptionOfType(NotFoundStationException.class).isThrownBy(
            () -> pathService.findShortestPath(할인없음, 부평역_id, 서울역_id)
        );

        verify(stationService).findById(부평역_id);
        verify(stationService, never()).findById(서울역_id);
        verify(lineService, never()).findLines();
    }

    @DisplayName("최단 거리 탐색 - 같은 노선")
    @Test
    void findPathSuccess01() {
        // given
        long 귤현역_id = 1L;
        long 계양역_id = 2L;

        given(stationService.findById(귤현역_id)).willReturn(귤현역);
        given(stationService.findById(계양역_id)).willReturn(계양역);
        given(lineService.findLines()).willReturn(lines);

        // when
        PathResponse response = pathService.findShortestPath(할인없음, 귤현역_id, 계양역_id);

        // then
        assertThat(response.getStations()).containsExactly(StationResponse.of(귤현역), StationResponse.of(계양역));
        assertThat(response.getDistance()).isEqualTo(9);
    }

    @DisplayName("최단 거리 탐색 - 다른 노선")
    @Test
    void findPathSuccess02() {
        // given
        long 귤현역_id = 1L;
        long 계양역_id = 2L;

        given(stationService.findById(귤현역_id)).willReturn(귤현역);
        given(stationService.findById(계양역_id)).willReturn(마곡나루역);
        given(lineService.findLines()).willReturn(lines);

        // when
        PathResponse response = pathService.findShortestPath(할인없음, 귤현역_id, 계양역_id);

        // then
        assertThat(response.getStations()).containsExactly(StationResponse.of(귤현역),
                                                           StationResponse.of(계양역),
                                                           StationResponse.of(김포공항역),
                                                           StationResponse.of(마곡나루역));
        assertThat(response.getDistance()).isEqualTo(98);
    }
}
