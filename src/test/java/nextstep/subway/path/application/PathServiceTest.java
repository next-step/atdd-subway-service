package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.ShortestPathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @InjectMocks
    private PathService pathService;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineRepository lineRepository;

    @BeforeEach
    public void setUp() {
        pathService = new PathService(stationRepository, lineRepository);
    }

    @Test
    @DisplayName("최단 경로를 조회한다.")
    public void findShortestPath() throws Exception {
        // given
        long sourceStationId = 3L;
        long targetStationId = 1L;

        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");

        Line 신분당선 = Line.of("신분당선", "red", 강남역, 양재역, 2);
        Section 양재_판교_구간 = Section.of(신분당선, 양재역, 판교역, 2);
        신분당선.addSection(양재_판교_구간);

        Line 이호선 = Line.of("2호선", "green", 강남역, 판교역, 100);

        List<Line> lines = new ArrayList<>(Arrays.asList(신분당선, 이호선));

        given(stationRepository.findById(sourceStationId)).willReturn(Optional.ofNullable(판교역));
        given(stationRepository.findById(targetStationId)).willReturn(Optional.ofNullable(강남역));
        given(lineRepository.findAll()).willReturn(lines);

        // when
        ShortestPathResponse result = pathService.findShortestPath(sourceStationId, targetStationId, new LoginMember(20));

        // then
        assertThat(result.getStations()).containsExactly(판교역, 양재역, 강남역);
        assertThat(result.getDistance()).isEqualTo(4);
    }

    @Test
    @DisplayName("존재하지 않는 역의 최단 경로를 조회한다.")
    public void findShortestPath_not_found() throws Exception {
        // given
        long sourceStationId = 3L;
        long targetStationId = 1L;

        given(stationRepository.findById(sourceStationId)).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> pathService.findShortestPath(sourceStationId, targetStationId, new LoginMember()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 지하철역입니다.");
    }
}