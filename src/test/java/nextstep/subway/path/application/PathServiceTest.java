package nextstep.subway.path.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.NoSuchElementException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationDto;
import nextstep.subway.path.infrastructure.PathAnalysis;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@DisplayName("지하철 경로 조회 서비스 관련 기능")
@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    @Mock
    private StationService stationService;

    @Mock
    private LineService lineService;

    @InjectMocks
    private PathService pathService;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 역삼역;
    private Station 선릉역;

    private Section 강남_양재_구간;
    private Section 교대_강남_구간;
    private Section 교대_양재_구간;
    private Section 역삼_선릉_구간;

    @BeforeEach
    public void setUp() {
        // given
        강남역 = new Station(3L, "강남역");
        양재역 = new Station(2L, "양재역");
        교대역 = new Station(1L, "교대역");
        역삼역 = new Station(4L, "역삼역");
        선릉역 = new Station(5L, "선릉역");

        강남_양재_구간 = new Section(null, 강남역, 양재역, Distance.of(3));
        교대_강남_구간 = new Section(null, 교대역, 강남역, Distance.of(1));
        교대_양재_구간 = new Section(null, 교대역, 양재역, Distance.of(5));
        역삼_선릉_구간 = new Section(null, 역삼역, 선릉역, Distance.of(10));
    }

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void search_shortestPath() {
        // given
        Sections sections = Sections.of(강남_양재_구간, 교대_강남_구간, 교대_양재_구간);

        when(stationService.findById(교대역.getId())).thenReturn(교대역);
        when(stationService.findById(강남역.getId())).thenReturn(강남역);
        when(stationService.findById(양재역.getId())).thenReturn(양재역);
        when(lineService.findAllSections()).thenReturn(sections);
        
        // when
        PathResponse asdf =  pathService.searchShortestPath(교대역.getId(), 양재역.getId());

        // then
        assertAll(
            () -> Assertions.assertThat(asdf.getStations()).isEqualTo(List.of(PathStationDto.of(교대역),
                                                                                PathStationDto.of(강남역),
                                                                                PathStationDto.of(양재역))),
            () -> Assertions.assertThat(asdf.getDistance()).isEqualTo(4)
        );
    }

    @DisplayName("최단 경로를 조회시 출발역과 도착역이 같은 경우 예외가 발생된다.")
    @Test
    void exception_sameSourceAndTarget() {
        // given
        Sections sections = Sections.of(교대_강남_구간, 강남_양재_구간);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> pathService.searchShortestPath(교대역.getId(), 교대역.getId()));
    }

    @DisplayName("최단 경로를 조회시 출발역과 도착역이 연결되지 않은 경우 예외가 발생된다.")
    @Test
    void exception_notConnectSourceAndTarget() {
        // given
        Sections sections = Sections.of(교대_강남_구간, 강남_양재_구간, 역삼_선릉_구간);

        when(stationService.findById(강남역.getId())).thenReturn(강남역);
        when(stationService.findById(선릉역.getId())).thenReturn(선릉역);
        when(lineService.findAllSections()).thenReturn(sections);

        // when
        // then
        Assertions.assertThatExceptionOfType(NoSuchElementException.class)
                    .isThrownBy(() -> pathService.searchShortestPath(강남역.getId(), 선릉역.getId()));
    }

    @DisplayName("최단 경로를 조회시 출발역이나 도착역이 등록되지 않은 경우 예외가 발생한다..")
    @Test
    void exception_notExistSourceOrTarget() {
        // given
        Sections sections = Sections.of(교대_강남_구간, 강남_양재_구간);

        when(stationService.findById(강남역.getId())).thenReturn(강남역);
        when(stationService.findById(선릉역.getId())).thenThrow(NoSuchElementException.class);


        // when
        // then
        Assertions.assertThatExceptionOfType(NoSuchElementException.class)
                    .isThrownBy(() -> pathService.searchShortestPath(강남역.getId(), 선릉역.getId()));
    }

    @DisplayName("최단 경로를 조회시 출발역이나 도착역이 경로에 없는 경우 예외가 발생한다..")
    @Test
    void exception_notExistSourceOrTargetInSections() {
        // given
        Sections sections = Sections.of(교대_강남_구간, 강남_양재_구간);

        when(stationService.findById(강남역.getId())).thenReturn(강남역);
        when(stationService.findById(역삼역.getId())).thenReturn(역삼역);
        when(lineService.findAllSections()).thenReturn(sections);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> pathService.searchShortestPath(강남역.getId(), 역삼역.getId()));
    }
}
