package nextstep.subway.line.application;

import nextstep.subway.fee.domain.StationFeeRepository;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    Station 강남역;
    Station 교대역;
    Station 남부터미널역;
    Station 양재역;
    Section section1;
    Section section2;
    Section section3;
    Section section4;
    Section section5;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private StationFeeRepository stationFeeRepository;

    @Mock
    private LineRepository lineRepository;

    @Test
    @DisplayName("최적경로 조회 - mock")
    void getShortestPathTest() {
        PathService pathService = new PathService(stationRepository, sectionRepository, stationFeeRepository, lineRepository);

        List<Station> stations = Mock_지하철역_등록됨();
        List<Section> sections = Mock_구간역_등록됨();

        // when
        Mock_최소거리_조건_등록됨(stations, sections);
        List<StationResponse> stationResponses = Mock_최소거리_조회됨(pathService);

        // then
        Mock_최소거리_검증됨(stationResponses);
    }

    private List Mock_지하철역_등록됨() {
        강남역 = new Station(1L, "강남역");
        교대역 = new Station(2L, "교대역");
        남부터미널역 = new Station(3L, "남부터미널역");
        양재역 = new Station(4L, "양재역");

        return Arrays.asList(강남역, 교대역, 남부터미널역, 양재역);
    }

    private List Mock_구간역_등록됨() {
        section1 = new Section(강남역, 양재역, 10);
        section2 = new Section(교대역, 강남역, 10);
        section3 = new Section(교대역, 양재역, 5);
        section4 = new Section(교대역, 남부터미널역, 3);
        section5 = new Section(남부터미널역, 양재역, 1);

        return Arrays.asList(section1, section2, section3, section4, section5);
    }

    private List Mock_최소거리_조회됨(PathService pathService) {
        return pathService.getShortestPath(String.valueOf(3), String.valueOf(2));
    }

    private void Mock_최소거리_검증됨(List stationResponses) {
        assertThat(stationResponses.size()).isEqualTo(3);
    }

    private void Mock_최소거리_조건_등록됨(List stations, List sections) {
        when(stationRepository.findById(Long.valueOf(3))).thenReturn(Optional.ofNullable(교대역));
        when(stationRepository.findById(Long.valueOf(2))).thenReturn(Optional.ofNullable(양재역));
        when(stationRepository.findAll()).thenReturn(stations);
        when(sectionRepository.findAll()).thenReturn(sections);
    }

    @Test
    @DisplayName("최적경로 조회 - 실제 객체")
    void getShortestPathTest2() {
        PathService pathService = new PathService(stationRepository, sectionRepository, stationFeeRepository, lineRepository);

        List<Station> stations = Mock_지하철역_등록됨();
        List<Section> sections = Mock_구간역_등록됨();

        // when
        Mock_최소거리_조건_등록됨(stations, sections);
        List<StationResponse> stationResponses = Mock_최소거리_조회됨(pathService);

        // then
        Mock_최소거리_검증됨(stationResponses);
    }
}