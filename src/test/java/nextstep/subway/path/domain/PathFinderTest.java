package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathFinderTest {

  private Station 강남역 = Station.stationStaticFactoryForTestCode(1L, "강남역");
  private Station 양재역 = Station.stationStaticFactoryForTestCode(2L, "양재역");
  private Station 교대역 = Station.stationStaticFactoryForTestCode(3L, "교대역");
  private Station 남부터미널역 = Station.stationStaticFactoryForTestCode(4L, "남부터미널역");

  private Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, Distance.from(10));
  private Line 이호선 = new Line("이호선", "green", 교대역, 강남역, Distance.from(10));
  private Line 삼호선 = new Line("삼호선", "orange", 교대역, 양재역, Distance.from(5));

  private List<Station> wholeStations;
  private List<Section> wholeSections;

  @BeforeEach
  void setUp() {
    wholeStations = Arrays.asList(강남역, 양재역, 교대역, 남부터미널역);
    Section 강남_양재_구간 = new Section(신분당선, 강남역, 양재역, Distance.from(10));
    Section 교대_강남_구간 = new Section(이호선, 교대역, 강남역, Distance.from(10));
    Section 교대_남부터미널_구간 = new Section(삼호선, 교대역, 남부터미널역, Distance.from(3));
    Section 남부터미널_양재_구간 = new Section(삼호선, 남부터미널역, 양재역, Distance.from(2));
    wholeSections = Arrays.asList(강남_양재_구간, 교대_강남_구간, 교대_남부터미널_구간, 남부터미널_양재_구간);
  }

  @DisplayName("출발역에서 도착역까지 최단거리로 갈 수 있는 역들을 반환한다.")
  @Test
  void findShortestPathTest() {
    //given
    PathFinder pathFinder = PathFinder.init(wholeStations, wholeSections);

    //when
    Path shortestPath = pathFinder.findShortestPath(교대역.getId(), 양재역.getId());

    //then
    assertThat(shortestPath).isEqualTo(new Path(Arrays.asList(교대역.getId(), 남부터미널역.getId(), 양재역.getId()), 5));
  }

}
