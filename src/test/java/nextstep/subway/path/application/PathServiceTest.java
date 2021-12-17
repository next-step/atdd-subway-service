package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.repository.SectionRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponses;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private StationService stationService;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    private Section 강남_교대_구간;
    private Section 강남_양재_구간;
    private Section 교대_남부터미널_구간;
    private Section 남부터미널_양재_구간;
    private List<Section> 모든_구간;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "red");
        이호선 = new Line("이호선", "green");
        삼호선 = new Line("삼호선", "yellow");

        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        강남_교대_구간 = new Section(이호선, 강남역, 교대역, 7);
        강남_양재_구간 = new Section(신분당선, 강남역, 양재역, 5);
        교대_남부터미널_구간 = new Section(삼호선, 교대역, 남부터미널역, 3);
        남부터미널_양재_구간 = new Section(신분당선, 남부터미널역, 양재역, 4);

        모든_구간 = Arrays.asList(강남_교대_구간, 강남_양재_구간, 교대_남부터미널_구간, 남부터미널_양재_구간);
    }

    @DisplayName("최단 경로를 가져온다")
    @Test
    void getPath() {
        //given
        PathService pathService = new PathService(sectionRepository, stationService);
        PathRequest pathRequest = new PathRequest(1L, 4L);
        StationResponses stationResponses = StationResponses.from(Arrays.asList(강남역, 양재역, 남부터미널역));
        PathResponse expected = new PathResponse(stationResponses.getResponses(), 9);

        when(sectionRepository.findAll()).thenReturn(모든_구간);
        when(stationService.findById(1L)).thenReturn(강남역);
        when(stationService.findById(4L)).thenReturn(남부터미널역);

        // when
        PathResponse response = pathService.getPath(pathRequest);

        // then
        assertThat(response).isEqualTo(expected);
    }
}