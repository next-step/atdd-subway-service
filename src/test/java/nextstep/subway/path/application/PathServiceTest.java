package nextstep.subway.path.application;

import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static nextstep.subway.line.domain.SectionTestFixture.createSection;
import static nextstep.subway.station.domain.StationTestFixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.line.domain.AgeFarePolicy;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
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

@DisplayName("지하철 경로 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineRepository lineRepository;

    @InjectMocks
    private PathService pathService;

    private Line 칠호선;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 이수역;
    private Station 반포역;
    private Station 강남역;
    private Station 역삼역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    /**
     * 이수역 --- *7호선*[20] --- 반포역
     *
     * 교대역 --- *2호선*[10] --- 강남역 --- *2호선*[5] --- 역삼역
     *   |                      |
     * *3호선*[3]           *신분당선*[10]
     *   |                         |
     * 남부터미널역 --- *3호선*[2] --- 양재역
     */
    @BeforeEach
    public void setUp() {
        이수역 = createStation("이수역");
        반포역 = createStation("반포역");
        강남역 = createStation("강남역");
        역삼역 = createStation("역삼역");
        양재역 = createStation("양재역");
        교대역 = createStation("교대역");
        남부터미널역 = createStation("남부터미널역");
        칠호선 = createLine("칠호선", "bg-khaki", 이수역, 반포역, 20, 1000);
        신분당선 = createLine("신분당선", "bg-red", 강남역, 양재역, 10, 700);
        이호선 = createLine("이호선", "bg-green", 교대역, 강남역, 10, 500);
        삼호선 = createLine("삼호선", "bg-orange", 교대역, 양재역, 5);
        삼호선.addSection(createSection(삼호선, 교대역, 남부터미널역, 3));
        이호선.addSection(createSection(이호선, 강남역, 역삼역, 5));
    }

    @DisplayName("지하철 경로 조회를 하면 최단 거리의 경로가 조회된다.")
    @Test
    void findShortestPath() {
        // given
        Long source = 1L;
        Long target = 4L;
        when(stationRepository.findById(source))
                .thenReturn(Optional.of(교대역));
        when(stationRepository.findById(target))
                .thenReturn(Optional.of(양재역));
        when(lineRepository.findAll())
                .thenReturn(Arrays.asList(이호선, 신분당선, 삼호선));

        // when
        PathResponse pathResponse = pathService.findShortestPath(AgeFarePolicy.ADULT, source, target);

        // then
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(5),
                () -> assertThat(pathResponse.getStations().stream().map(StationResponse::getName).collect(Collectors.toList()))
                        .containsExactlyElementsOf(Arrays.asList("교대역", "남부터미널역", "양재역"))
        );
    }

    @DisplayName("최단 경로에 속한 노선에 추가 요금이 존재하면, 해당 금액이 추가된 요금이 반환된다.")
    @Test
    void findShortestPathWithLineFare() {
        // given
        Long source = 1L;
        Long target = 4L;
        when(stationRepository.findById(source))
                .thenReturn(Optional.of(이수역));
        when(stationRepository.findById(target))
                .thenReturn(Optional.of(반포역));
        when(lineRepository.findAll())
                .thenReturn(Arrays.asList(이호선, 신분당선, 삼호선, 칠호선));

        // when
        PathResponse pathResponse = pathService.findShortestPath(AgeFarePolicy.ADULT, source, target);

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
        when(stationRepository.findById(source))
                .thenReturn(Optional.of(양재역));
        when(stationRepository.findById(target))
                .thenReturn(Optional.of(역삼역));
        when(lineRepository.findAll())
                .thenReturn(Arrays.asList(이호선, 신분당선, 삼호선, 칠호선));

        // when
        PathResponse pathResponse = pathService.findShortestPath(AgeFarePolicy.ADULT, source, target);

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
        when(stationRepository.findById(source))
                .thenReturn(Optional.of(양재역));
        when(stationRepository.findById(target))
                .thenReturn(Optional.of(역삼역));
        when(lineRepository.findAll())
                .thenReturn(Arrays.asList(이호선, 신분당선, 삼호선, 칠호선));

        // when
        PathResponse pathResponse = pathService.findShortestPath(AgeFarePolicy.CHILD, source, target);

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
        when(stationRepository.findById(source))
                .thenReturn(Optional.of(이수역));
        when(stationRepository.findById(target))
                .thenReturn(Optional.of(반포역));
        when(lineRepository.findAll())
                .thenReturn(Arrays.asList(이호선, 신분당선, 삼호선, 칠호선));

        // when
        PathResponse pathResponse = pathService.findShortestPath(AgeFarePolicy.TEENAGER, source, target);

        // then
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(20),
                () -> assertThat(pathResponse.getStations().stream().map(StationResponse::getName).collect(Collectors.toList()))
                        .containsExactlyElementsOf(Arrays.asList("이수역", "반포역")),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1680)
        );
    }

    @DisplayName("아기는 지하철 요금을 내지 않는다.")
    @Test
    void findShortestPathWithBabyFare() {
        // given
        Long source = 1L;
        Long target = 4L;
        when(stationRepository.findById(source))
                .thenReturn(Optional.of(교대역));
        when(stationRepository.findById(target))
                .thenReturn(Optional.of(양재역));
        when(lineRepository.findAll())
                .thenReturn(Arrays.asList(이호선, 신분당선, 삼호선));

        // when
        PathResponse pathResponse = pathService.findShortestPath(AgeFarePolicy.BABY, source, target);

        // then
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(5),
                () -> assertThat(pathResponse.getStations().stream().map(StationResponse::getName).collect(Collectors.toList()))
                        .containsExactlyElementsOf(Arrays.asList("교대역", "남부터미널역", "양재역")),
                () -> assertThat(pathResponse.getFare()).isEqualTo(0)
        );
    }

    @DisplayName("존재하지 않는 출발역으로 지하철 경로 조회를 하면 경로를 조회할 수 없다.")
    @Test
    void findShortestPathThrowErrorWhenSourceIsNotExist() {
        // given
        Long source = 1L;
        Long target = 4L;
        when(stationRepository.findById(source))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> pathService.findShortestPath(AgeFarePolicy.ADULT, source, target))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.존재하지_않는_역.getErrorMessage());
    }

    @DisplayName("존재하지 않는 도착역으로 지하철 경로 조회를 하면 경로를 조회할 수 없다.")
    @Test
    void findShortestPathThrowErrorWhenTargetIsNotExist() {
        // given
        Long source = 1L;
        Long target = 4L;
        when(stationRepository.findById(source))
                .thenReturn(Optional.of(양재역));
        when(stationRepository.findById(target))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> pathService.findShortestPath(AgeFarePolicy.ADULT, source, target))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.존재하지_않는_역.getErrorMessage());
    }

    @DisplayName("연결되지 않은 출발역과 도착역 사이의 경로를 조회할 수 없다.")
    @Test
    void findShortestPathThrowErrorWhenPathIsNotExist() {
        // given
        Long source = 1L;
        Long target = 4L;
        when(stationRepository.findById(source))
                .thenReturn(Optional.of(이수역));
        when(stationRepository.findById(target))
                .thenReturn(Optional.of(교대역));
        when(lineRepository.findAll())
                .thenReturn(Arrays.asList(칠호선, 이호선, 신분당선, 삼호선));

        // when & then
        assertThatThrownBy(() -> pathService.findShortestPath(AgeFarePolicy.ADULT, source, target))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.출발역과_도착역은_연결되지_않음.getErrorMessage());
    }

    @DisplayName("출발역과 도착역이 같으면 경로를 조회할 수 없다.")
    @Test
    void findShortestPathThrowErrorWhenSourceIsEqualToTarget() {
        // given
        Long source = 1L;
        when(stationRepository.findById(source))
                .thenReturn(Optional.of(남부터미널역));
        when(lineRepository.findAll())
                .thenReturn(Arrays.asList(이호선, 신분당선, 삼호선));

        // when & then
        assertThatThrownBy(() -> pathService.findShortestPath(AgeFarePolicy.ADULT, source, source))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.출발역과_도착역이_서로_같음.getErrorMessage());
    }
}
