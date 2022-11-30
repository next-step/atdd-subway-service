package nextstep.subway.path.applicaiton;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    StationRepository stationRepository;

    @Mock
    SectionRepository sectionRepository;

    @InjectMocks
    PathService pathService;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    private Section 강남_교대;
    private Section 교대_강남;
    private Section 교대_남부;
    private Section 남부_양재;
    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재역
     */

    @BeforeEach
    public void setUp() {

        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");

        강남_교대 = new Section(null, 강남역, 양재역, 10);
        교대_강남 = new Section(null, 교대역, 강남역, 10);
        교대_남부 = new Section(null, 교대역, 남부터미널역, 2);
        남부_양재 = new Section(null, 남부터미널역, 양재역, 3);
    }

    @DisplayName("출발역과 도착역으로 최단거리를 조회할 수 있다.")
    @Test
    void shortest_path() {
        //given
        given(stationRepository.findById(1L)).willReturn(Optional.of(교대역));
        given(stationRepository.findById(2L)).willReturn(Optional.of(양재역));
        given(stationRepository.findAll()).willReturn(Arrays.asList(교대역, 강남역, 양재역, 남부터미널역));
        given(sectionRepository.findAll()).willReturn(Arrays.asList(강남_교대, 교대_강남, 교대_남부, 남부_양재));

        //when
        PathResponse paths = pathService.findPaths(1L, 2L);

        //then
        assertAll(
                () -> assertThat(paths.getDistance()).isEqualTo(5),
                () -> assertThat(paths.getStations()
                        .stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList())).containsExactly("교대역", "남부터미널역", "양재역")
        );

    }

    @DisplayName("출발역과 도착역이 같은 경우 IllegalArgumentException 이 발생한다.")
    @Test
    void same_source_target() {
        //when & then
        assertThatThrownBy(() -> pathService.findPaths(1L, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 IllegalArgumentException 이 발생한다.")
    @Test
    void not_linked_line() {
        //given
        Station 인천터미널역 = new Station("인천터미널");
        Station 동춘역 = new Station("동춘역");
        Section 인터_동춘 = new Section(null, 인천터미널역, 동춘역, 10);

        given(stationRepository.findById(1L)).willReturn(Optional.of(교대역));
        given(stationRepository.findById(2L)).willReturn(Optional.of(인천터미널역));
        given(stationRepository.findAll()).willReturn(Arrays.asList(교대역, 강남역, 양재역, 남부터미널역, 인천터미널역, 동춘역));
        given(sectionRepository.findAll()).willReturn(Arrays.asList(강남_교대, 교대_강남, 교대_남부, 남부_양재, 인터_동춘));

        //when & then
        assertThatThrownBy(() -> pathService.findPaths(1L, 2L))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 IllegalArgumentException 이 발생한다.")
    @Test
    void none_station() {
        //given

        //when

        //then

    }
}
