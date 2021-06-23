package nextstep.subway.path.application;

import nextstep.subway.exception.StationNotExistException;
import nextstep.subway.exception.StationsNotConnectedException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static nextstep.subway.path.domain.PathFinderTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

  @Mock
  private StationRepository stationRepository;
  @Mock
  private SectionRepository sectionRepository;

  @DisplayName("출발역에서 도착역까지 최단거리로 갈 수 있는 역들을 조회")
  @Test
  void findShortestPath() {
    //given
    when(stationRepository.findAll()).thenReturn(Arrays.asList(강남역, 양재역, 교대역, 남부터미널역));
    when(sectionRepository.findAll()).thenReturn(Arrays.asList(강남_양재_구간, 교대_강남_구간, 교대_남부터미널_구간, 남부터미널_양재_구간));
    PathService 최단경로서비스 = new PathService(stationRepository, sectionRepository);

    //when
    PathResponse 최단거리응답 = 최단경로서비스.findShortestPath(교대역.getId(), 양재역.getId());

    //then
    assertThat(최단거리응답).isEqualTo(new PathResponse(Arrays.asList(StationResponse.of(교대역), StationResponse.of(남부터미널역), StationResponse.of(양재역)), 5D, 1_250));
  }

  @DisplayName("기존 최단거리역이 제거되었을 때")
  @Test
  void findNewShortestPath() {
    //given
    when(stationRepository.findAll()).thenReturn(Arrays.asList(강남역, 양재역, 교대역, 남부터미널역));
    when(sectionRepository.findAll()).thenReturn(Arrays.asList(강남_양재_구간, 교대_강남_구간, 교대_남부터미널_구간));
    PathService 최단경로서비스 = new PathService(stationRepository, sectionRepository);

    //when
    PathResponse 최단거리응답 = 최단경로서비스.findShortestPath(교대역.getId(), 양재역.getId());

    //then
    assertThat(최단거리응답).isEqualTo(new PathResponse(Arrays.asList(StationResponse.of(교대역), StationResponse.of(강남역), StationResponse.of(양재역)), 20D, 1_450));
  }

  @DisplayName("출발역과 도착역을 같은 역으로 조회")
  @Test
  void findShortestPathWithSingleStationTest() {
    //given
    when(stationRepository.findAll()).thenReturn(Arrays.asList(강남역, 양재역, 교대역, 남부터미널역));
    when(sectionRepository.findAll()).thenReturn(Arrays.asList(강남_양재_구간, 교대_강남_구간, 교대_남부터미널_구간, 남부터미널_양재_구간));
    PathService 최단거리서비스 = new PathService(stationRepository, sectionRepository);

    //when
    PathResponse 최단거리응답 = 최단거리서비스.findShortestPath(교대역.getId(), 교대역.getId());

    //then
    assertThat(최단거리응답).isEqualTo(new PathResponse(Arrays.asList(StationResponse.of(교대역)), 0D, 0));
  }

  @DisplayName("연결되지 않은 역과의 최단거리를 조회")
  @Test
  void findShortestPathWithNotConnectedStationTest() {
    //given
    Station 서울역 = Station.stationStaticFactoryForTestCode(5L, "서울역");
    Station 용산역 = Station.stationStaticFactoryForTestCode(6L, "용산역");
    Line 일호선 = new Line("일호선", "navy", 서울역, 용산역, Distance.from(3));
    Section 서울역_용산역_구간 = new Section(일호선, 서울역, 용산역, Distance.from(3));
    when(stationRepository.findAll()).thenReturn(Arrays.asList(강남역, 양재역, 교대역, 남부터미널역, 서울역, 용산역));
    when(sectionRepository.findAll()).thenReturn(Arrays.asList(강남_양재_구간, 교대_강남_구간, 교대_남부터미널_구간, 남부터미널_양재_구간, 서울역_용산역_구간));
    PathService 최단거리서비스 = new PathService(stationRepository, sectionRepository);

    //when & then
    assertThatThrownBy(() -> 최단거리서비스.findShortestPath(교대역.getId(), 서울역.getId())).isInstanceOf(StationsNotConnectedException.class);
  }

  @DisplayName("존재하지 않는 역과의 최단거리를 조회")
  @Test
  void findShortestPathWithNoneExistStationTest() {
    //given
    Station 서울역 = Station.stationStaticFactoryForTestCode(5L, "서울역");
    when(stationRepository.findAll()).thenReturn(Arrays.asList(강남역, 양재역, 교대역, 남부터미널역));
    when(sectionRepository.findAll()).thenReturn(Arrays.asList(강남_양재_구간, 교대_강남_구간, 교대_남부터미널_구간, 남부터미널_양재_구간));
    PathService 최단거리서비스 = new PathService(stationRepository, sectionRepository);

    //when & then
    assertThatThrownBy(() -> 최단거리서비스.findShortestPath(교대역.getId(), 서울역.getId())).isInstanceOf(StationNotExistException.class);
  }



}
