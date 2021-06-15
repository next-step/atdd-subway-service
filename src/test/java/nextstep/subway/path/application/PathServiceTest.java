package nextstep.subway.path.application;

import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.dto.PathResponse;
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
    PathService pathService = new PathService(stationRepository, sectionRepository);
    PathResponse shortestPath = pathService.findShortestPath(교대역.getId(), 양재역.getId());
    assertThat(shortestPath).isEqualTo(new PathResponse(Arrays.asList(StationResponse.of(교대역), StationResponse.of(남부터미널역), StationResponse.of(양재역)), 5D));
  }

  @DisplayName("기존 최단거리역이 제거되었을 때")
  @Test
  void findNewShortestPath() {
    //given
    when(stationRepository.findAll()).thenReturn(Arrays.asList(강남역, 양재역, 교대역, 남부터미널역));
    when(sectionRepository.findAll()).thenReturn(Arrays.asList(강남_양재_구간, 교대_강남_구간, 교대_남부터미널_구간));
    PathService pathService = new PathService(stationRepository, sectionRepository);
    PathResponse shortestPath = pathService.findShortestPath(교대역.getId(), 양재역.getId());
    assertThat(shortestPath).isEqualTo(new PathResponse(Arrays.asList(StationResponse.of(교대역), StationResponse.of(강남역), StationResponse.of(양재역)), 20D));
  }



}
