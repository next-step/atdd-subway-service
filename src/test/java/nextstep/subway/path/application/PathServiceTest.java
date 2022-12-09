package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.PathCannotFindException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
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

@DisplayName("지하철 경로 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    @Mock
    private StationService stationService;

    @Mock
    private LineRepository lineRepository;

    @InjectMocks
    private PathService pathService;

    private Line 칠호선;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 분당선;
    private Station 이수역;
    private Station 반포역;
    private Station 강남역;
    private Station 역삼역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 미금역;
    private Station 정자역;

    /**
     * 이수역 --- *7호선*[20] --- 반포역
     *
     * 교대역 --- *2호선*[10] --- 강남역 --- *2호선*[5] --- 역삼역
     *   |                      |
     * *3호선*[3]           *신분당선*[10]
     *   |                         |
     * 남부터미널역 --- *3호선*[2] --- 양재역
     *                              |
     *                          *신분당선*[12]
     *                            미금역
     *                             |
     *                           *신분당선*[12], *분당선*[10]
     *                             |
     *                            정자역
     *
     */
    @BeforeEach
    public void setUp() {
        이수역 = new Station("이수역");
        반포역 = new Station("반포역");
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        미금역 = new Station("미금역");
        정자역 = new Station("정자역");
        남부터미널역 = new Station("남부터미널역");
        칠호선 = new Line("칠호선", "bg-khaki", 이수역, 반포역, new Distance(20), 1000);
        신분당선 = new Line("신분당선", "bg-red", 강남역, 양재역, new Distance(10), 700);
        이호선 = new Line("이호선", "bg-green", 교대역, 강남역, new Distance(10), 500);
        분당선 = new Line("분당선", "bg-yellow", 미금역, 정자역, new Distance(10), 500);
        삼호선 = new Line("삼호선", "bg-orange", 교대역, 양재역, new Distance(5), 0);
        삼호선.addSection(교대역, 남부터미널역, new Distance(3));
        이호선.addSection(강남역, 역삼역, new Distance(5));
        신분당선.addSection(양재역, 미금역, new Distance(12));
        신분당선.addSection(미금역, 정자역, new Distance(12));
    }

    @DisplayName("지하철 경로 조회를 하면 최단 거리의 경로가 조회된다.")
    @Test
    void findShortestPath() {
        // given
        Long source = 1L;
        Long target = 4L;
        when(stationService.stationById(source)).thenReturn(교대역);
        when(stationService.stationById(target)).thenReturn(양재역);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(이호선, 신분당선, 삼호선));

        // when
        LoginMember adult = new LoginMember(1L, "email@email", 20);
        PathRequest pathRequest = new PathRequest(source, target);
        PathResponse pathResponse = pathService.getShortestPath(adult, pathRequest);

        // then
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(5),
                () -> assertThat(pathResponse.getStations().stream().map(StationResponse::getName).collect(Collectors.toList()))
                        .containsExactlyElementsOf(Arrays.asList("교대역", "남부터미널역", "양재역"))
        );
    }

    @DisplayName("노선만 다른 중복된 구역(신분당선의 미금역 - 정자역, 분당선의 미금역 - 정자역)이 존재할 때, 더 작은 거리를 가진 경로가 반환된다.")
    @Test
    void findShortestPathWhenDuplicationSectionUnlikeLine() {
        // given
        Long source = 1L;
        Long target = 4L;
        when(stationService.stationById(source)).thenReturn(양재역);
        when(stationService.stationById(target)).thenReturn(정자역);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(이호선, 신분당선, 삼호선, 분당선));

        // when
        LoginMember adult = new LoginMember(1L, "email@email", 20);
        PathRequest pathRequest = new PathRequest(source, target);
        PathResponse pathResponse = pathService.getShortestPath(adult, pathRequest);

        // then
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(22),
                () -> assertThat(pathResponse.getStations().stream().map(StationResponse::getName).collect(Collectors.toList()))
                        .containsExactlyElementsOf(Arrays.asList("양재역", "미금역", "정자역"))
        );
    }

    @DisplayName("최단 경로에 속한 노선에 추가 요금이 존재하면, 해당 금액이 추가된 요금이 반환된다.")
    @Test
    void findShortestPathWithLineFare() {
        // given
        Long source = 1L;
        Long target = 4L;
        when(stationService.stationById(source)).thenReturn(이수역);
        when(stationService.stationById(target)).thenReturn(반포역);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(이호선, 신분당선, 삼호선, 칠호선));

        // when
        LoginMember adult = new LoginMember(1L, "email@email", 20);
        PathRequest pathRequest = new PathRequest(source, target);
        PathResponse pathResponse = pathService.getShortestPath(adult, pathRequest);

        // then
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(20),
                () -> assertThat(pathResponse.getStations().stream().map(StationResponse::getName).collect(Collectors.toList()))
                        .containsExactlyElementsOf(Arrays.asList("이수역", "반포역")),
                () -> assertThat(pathResponse.getFare()).isEqualTo(2450)
        );
    }

    @DisplayName("최단 경로에 속한 노선들에 여러 추가 요금이 존재하면, 그 중 가장 큰 추가 요금이 더해진 요금이 반환된다.")
    @Test
    void findShortestPathWithMaxLineFare() {
        // given
        Long source = 1L;
        Long target = 4L;
        when(stationService.stationById(source)).thenReturn(양재역);
        when(stationService.stationById(target)).thenReturn(역삼역);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(이호선, 신분당선, 삼호선, 칠호선));

        // when
        LoginMember adult = new LoginMember(1L, "email@email", 20);
        PathRequest pathRequest = new PathRequest(source, target);
        PathResponse pathResponse = pathService.getShortestPath(adult, pathRequest);

        // then
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(15),
                () -> assertThat(pathResponse.getStations().stream().map(StationResponse::getName).collect(Collectors.toList()))
                        .containsExactlyElementsOf(Arrays.asList("양재역", "강남역", "역삼역")),
                () -> assertThat(pathResponse.getFare()).isEqualTo(2050)
        );
    }

    @DisplayName("어린이 요금으로 지하철 경로를 조회하면, (성인 요금 - 350)의 50%이다.")
    @Test
    void findShortestPathWithChildFare() {
        // given
        Long source = 1L;
        Long target = 4L;
        when(stationService.stationById(source)).thenReturn(양재역);
        when(stationService.stationById(target)).thenReturn(역삼역);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(이호선, 신분당선, 삼호선, 칠호선));

        // when
        LoginMember child = new LoginMember(1L, "email@email", 8);
        PathRequest pathRequest = new PathRequest(source, target);
        PathResponse pathResponse = pathService.getShortestPath(child, pathRequest);

        // then
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(15),
                () -> assertThat(pathResponse.getStations().stream().map(StationResponse::getName).collect(Collectors.toList()))
                        .containsExactlyElementsOf(Arrays.asList("양재역", "강남역", "역삼역")),
                () -> assertThat(pathResponse.getFare()).isEqualTo(850)
        );
    }

    @DisplayName("청소년 요금으로 지하철 경로를 조회하면, (성인 요금 - 350)의 80%이다.")
    @Test
    void findShortestPathWithTeenagerFare() {
        // given
        Long source = 1L;
        Long target = 4L;
        when(stationService.stationById(source)).thenReturn(이수역);
        when(stationService.stationById(target)).thenReturn(반포역);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(이호선, 신분당선, 삼호선, 칠호선));

        // when
        LoginMember teen = new LoginMember(1L, "email@email", 17);
        PathRequest pathRequest = new PathRequest(source, target);
        PathResponse pathResponse = pathService.getShortestPath(teen, pathRequest);

        // then
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(20),
                () -> assertThat(pathResponse.getStations().stream().map(StationResponse::getName).collect(Collectors.toList()))
                        .containsExactlyElementsOf(Arrays.asList("이수역", "반포역")),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1680)
        );
    }

    @DisplayName("연결되지 않은 출발역과 도착역 사이의 경로를 조회할 수 없다.")
    @Test
    void findShortestPathThrowErrorWhenPathIsNotExist() {
        // given
        Long source = 1L;
        Long target = 4L;
        when(stationService.stationById(source)).thenReturn(이수역);
        when(stationService.stationById(target)).thenReturn(교대역);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(칠호선, 이호선, 신분당선, 삼호선));

        // when & then
        LoginMember adult = new LoginMember(1L, "email@email", 20);
        PathRequest pathRequest = new PathRequest(source, target);
        assertThatThrownBy(() -> pathService.getShortestPath(adult, pathRequest))
                .isInstanceOf(PathCannotFindException.class);
    }

    @DisplayName("출발역과 도착역이 같으면 경로를 조회할 수 없다.")
    @Test
    void findShortestPathThrowErrorWhenSourceIsEqualToTarget() {
        // given
        Long source = 1L;
        when(stationService.stationById(source)).thenReturn(남부터미널역);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(이호선, 신분당선, 삼호선));

        // when & then
        LoginMember adult = new LoginMember(1L, "email@email", 20);
        PathRequest pathRequest = new PathRequest(source, source);
        assertThatThrownBy(() -> pathService.getShortestPath(adult, pathRequest))
                .isInstanceOf(PathCannotFindException.class);
    }
}
