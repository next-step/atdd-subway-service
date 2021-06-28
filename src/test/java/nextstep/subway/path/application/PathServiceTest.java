package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private StationRepository stationRepository;
    @Mock
    private SectionRepository sectionRepository;
    private Line 삼호선;
    private Line 이호선;
    private Line 신분당선;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 양재역;
    private Station 강남역;
    private Section 교대역_남부터미널역;
    private Section 남부터미널역_양재역;
    private Section 교대역_강남역;
    private Section 강남역_양재역;

    @BeforeEach
    void setUp() {
        삼호선 = new Line();
        이호선 = new Line();
        신분당선 = new Line();

        교대역 = new Station(1L, "교대역");
        남부터미널역 = new Station(2L, "남부터미널역");
        양재역 = new Station(3L, "양재역");
        강남역 = new Station(4L, "강남역");

        교대역_남부터미널역 = new Section(삼호선, 교대역, 남부터미널역, 3);
        남부터미널역_양재역 = new Section(삼호선, 남부터미널역, 양재역, 2);
        교대역_강남역 = new Section(이호선, 교대역, 강남역, 10);
        강남역_양재역 = new Section(신분당선, 강남역, 양재역, 10);

    }

    /**
     * 교대역 - 양재
     * 교대역 --3--> 남부터미널 --2--> 양재   ==> 5
     * 교대역 --10--> 강남역 --10--> 양재    ==> 20
     */

    @Test
    void findPath() {
        //given
        Long sourceId = 교대역.getId();
        Long targetId = 양재역.getId();
        PathService pathService = new PathService(stationRepository, sectionRepository);

        //when
        when(stationRepository.findById(sourceId)).thenReturn(java.util.Optional.of(교대역));
        when(stationRepository.findById(targetId)).thenReturn(java.util.Optional.of(양재역));

        when(stationRepository.findAll()).thenReturn(Arrays.asList(교대역, 남부터미널역, 양재역, 강남역));
        when(sectionRepository.findAll()).thenReturn(Arrays.asList(교대역_남부터미널역, 남부터미널역_양재역, 교대역_강남역, 강남역_양재역));

        PathResponse pathResponse = pathService.findPath(sourceId, targetId);

        //then
        assertThat(pathResponse).isNotNull();
        assertThat(pathResponse.getStations()).isEqualTo(Arrays.asList(교대역, 남부터미널역, 양재역));
        assertThat(pathResponse.getDistance()).isEqualTo(5);
    }
}