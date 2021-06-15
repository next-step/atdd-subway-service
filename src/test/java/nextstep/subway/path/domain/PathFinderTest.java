package nextstep.subway.path.domain;

import nextstep.subway.exception.StationNotExistException;
import nextstep.subway.exception.StationsNotConnectedException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathFinderTest {

  public static Station 강남역 = Station.stationStaticFactoryForTestCode(1L, "강남역");
  public static Station 양재역 = Station.stationStaticFactoryForTestCode(2L, "양재역");
  public static Station 교대역 = Station.stationStaticFactoryForTestCode(3L, "교대역");
  public static Station 남부터미널역 = Station.stationStaticFactoryForTestCode(4L, "남부터미널역");

  public static Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, Distance.from(10));
  public static Line 이호선 = new Line("이호선", "green", 교대역, 강남역, Distance.from(10));
  public static Line 삼호선 = new Line("삼호선", "orange", 교대역, 양재역, Distance.from(5));

  public static Section 강남_양재_구간 = new Section(신분당선, 강남역, 양재역, Distance.from(10));
  public static Section 교대_강남_구간 = new Section(이호선, 교대역, 강남역, Distance.from(10));
  public static Section 교대_남부터미널_구간 = new Section(삼호선, 교대역, 남부터미널역, Distance.from(3));
  public static Section 남부터미널_양재_구간 = new Section(삼호선, 남부터미널역, 양재역, Distance.from(2));

  private List<Station> wholeStations;
  private List<Section> wholeSections;

  @BeforeEach
  void setUp() {
    wholeStations = Arrays.asList(강남역, 양재역, 교대역, 남부터미널역);

    wholeSections = Arrays.asList(강남_양재_구간, 교대_강남_구간, 교대_남부터미널_구간, 남부터미널_양재_구간);
  }

  @DisplayName("출발역에서 도착역까지 최단거리로 갈 수 있는 역들을 조회")
  @Test
  void findShortestPathTest() {
    //given
    PathFinder pathFinder = PathFinder.init(wholeStations, wholeSections);

    //when
    Path shortestPath = pathFinder.findShortestPath(교대역.getId(), 양재역.getId());

    //then
    assertThat(shortestPath).isEqualTo(new Path(Arrays.asList(교대역, 남부터미널역, 양재역), 5D));
  }

  @DisplayName("기존 최단거리역이 제거되었을 때")
  @Test
  void temp() {
    //given
    Section 강남_양재_구간 = new Section(신분당선, 강남역, 양재역, Distance.from(10));
    Section 교대_강남_구간 = new Section(이호선, 교대역, 강남역, Distance.from(10));
    Section 교대_남부터미널_구간 = new Section(삼호선, 교대역, 남부터미널역, Distance.from(3));
    List<Section> newWholeSections = new ArrayList<>();
    newWholeSections.add(교대_강남_구간);
    newWholeSections.add(강남_양재_구간);
    newWholeSections.add(교대_남부터미널_구간);
    PathFinder pathFinder = PathFinder.init(wholeStations, newWholeSections);

    //when
    Path shortestPath = pathFinder.findShortestPath(교대역.getId(), 양재역.getId());

    //then
    assertThat(shortestPath).isEqualTo(new Path(Arrays.asList(교대역, 강남역, 양재역), 20D));
  }

  @DisplayName("출발역과 도착역을 같은 역으로 조회")
  @Test
  void findShortestPathWithSingleStationTest() {
    //given
    PathFinder pathFinder = PathFinder.init(wholeStations, wholeSections);

    //when
    Path shortestPath = pathFinder.findShortestPath(교대역.getId(), 교대역.getId());

    //then
    assertThat(shortestPath).isEqualTo(new Path(Arrays.asList(교대역), 0D));
  }

  @DisplayName("연결되지 않은 역과의 최단거리를 조회")
  @Test
  void findShortestPathWithNotConnectedStationTest() {
    //given
    Station 서울역 = Station.stationStaticFactoryForTestCode(5L, "서울역");
    Station 용산역 = Station.stationStaticFactoryForTestCode(6L, "용산역");
    Line 일호선 = new Line("일호선", "navy", 서울역, 용산역, Distance.from(5));
    List<Station> newWholeStations = new ArrayList<>(wholeStations);
    newWholeStations.add(서울역);
    newWholeStations.add(용산역);
    Section 서울역_용산역_구간 = new Section(일호선, 서울역, 용산역, Distance.from(3));
    List<Section> newWholeSections = new ArrayList<>(wholeSections);
    newWholeSections.add(서울역_용산역_구간);
    PathFinder pathFinder = PathFinder.init(newWholeStations, newWholeSections);

    //when & then
    assertThatThrownBy(() -> pathFinder.findShortestPath(교대역.getId(), 서울역.getId())).isInstanceOf(StationsNotConnectedException.class);
  }

  @DisplayName("존재하지 않는 역과의 최단거리를 조회")
  @Test
  void findShortestPathWithNoneExistStationTest() {
    //given
    Station 서울역 = Station.stationStaticFactoryForTestCode(5L, "서울역");
    PathFinder pathFinder = PathFinder.init(wholeStations, wholeSections);

    //when & then
    assertThatThrownBy(() -> pathFinder.findShortestPath(교대역.getId(), 서울역.getId())).isInstanceOf(StationNotExistException.class);
  }

}
