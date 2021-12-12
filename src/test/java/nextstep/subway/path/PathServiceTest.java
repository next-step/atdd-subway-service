package nextstep.subway.path;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
  @InjectMocks
  private PathService pathService;

  @Mock
  private LineRepository lineRepository;

  @Mock
  private StationRepository stationRepository;

  @DisplayName("출발역과 도착역의 최단경로를 조회한다.")
  @Test
  void 최단_경로_조회() {
    // given
    Long 출발역_id = 1L;
    Long 도착역_id = 4L;

    Station 교대역 = new Station(1L, "교대역");
    Station 선릉역 = new Station(3L, "선릉역");
    Station 선정릉역 = new Station(4L, "선정릉역");
    Station 고속터미널역 = new Station(5L, "고속터미널역");

    Line 이호선 = new Line(1L, "이호선", "green", Section.of(1L, 교대역, 선릉역, Distance.of(10)));
    Line 삼호선 = new Line(2L, "삼호선", "orange", Section.of(1L, 교대역, 고속터미널역, Distance.of(10)));
    Line 구호선 = new Line(3L, "구호선", "brown", Section.of(1L, 고속터미널역, 선정릉역, Distance.of(50)));
    Line 수인분당선 = new Line(4L, "수인분당선", "yellow", Section.of(1L, 선릉역, 선정릉역, Distance.of(10)));

    List<Line> 노선_목록 = new ArrayList<>(Arrays.asList(이호선, 삼호선, 구호선, 수인분당선));

    given(stationRepository.findById(출발역_id)).willReturn(Optional.of(교대역));
    given(stationRepository.findById(도착역_id)).willReturn(Optional.of(선정릉역));
    given(lineRepository.findAll()).willReturn(노선_목록);

    // when
    PathResponse response = pathService.findShortestPath(출발역_id, 도착역_id);

    // then
    assertThat(response.getStations().stream()
            .map(StationResponse::toStation)
            .collect(Collectors.toList())).containsExactly(교대역, 선릉역, 선정릉역);
  }
}
