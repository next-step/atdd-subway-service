package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.auth.domain.AgeGroup;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathResponse.PathStationResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private SectionRepository sectionRepository;
    @Mock
    private StationService stationService;

    private Station 강남역;
    private Station 역삼역;

    @BeforeEach
    public void setUp() {
        강남역 = new Station(1L, "강남역");
        역삼역 = new Station(2L, "역삼역");
    }

    @DisplayName("최단경로 조회 시 결과로 Station 리스트와 최단거리를 반환")
    @Test
    void find1() {
        PathService pathService = new PathService(sectionRepository, stationService);
        when(stationService.findStationById(eq(1L))).thenReturn(강남역);
        when(stationService.findStationById(eq(2L))).thenReturn(역삼역);
        when(sectionRepository.findAll()).thenReturn(
            Collections.singletonList(new Section(new Line(), 강남역, 역삼역, new Distance(10))));

        PathResponse pathResult = pathService.findPath(1L, 2L, AgeGroup.NO_SALE_AGE);

        assertThat(pathResult.getDistance()).isEqualTo(10);
        eqStation(pathResult.getStations().get(0), 강남역);
        eqStation(pathResult.getStations().get(1), 역삼역);
    }

    private void eqStation(PathStationResponse stationResponse, Station station) {
        assertThat(stationResponse.getId()).isEqualTo(station.getId());
        assertThat(stationResponse.getName()).isEqualTo(station.getName());
        assertThat(stationResponse.getCreatedAt()).isEqualTo(station.getCreatedDate());
    }
}
