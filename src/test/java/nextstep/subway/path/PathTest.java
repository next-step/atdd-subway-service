package nextstep.subway.path;

import nextstep.subway.line.acceptance.SectionTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("지하철 경로 조회 도메인 테스트")
public class PathTest {
    private Line 신분당선;
    private Line 이호선;
    private Station 잠실역;
    private Station 강남역;
    private Station 양재역;
    private Station 판교역;

    @BeforeEach
    void setUp() {
        잠실역 = new Station("잠실역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        판교역 = new Station("판교역");

        신분당선 = new Line("신분당선", "red", 강남역, 양재역, 100);
        신분당선.addSection(SectionTest.구간을_생성함(양재역, 판교역, 10));

        이호선 = new Line("이호선", "green", 잠실역, 강남역, 20);
        이호선.addSection(SectionTest.구간을_생성함(잠실역, 양재역, 10));
    }

    @DisplayName("출발역 도착역 동일할 경우 예외 발생")
    @Test
    void 출발역_도착역_동일할경우_예외_발생() {
        출발역_도착역_동일할경우_예외_발생함();
    }

    @DisplayName("출발역 도착역 연결 되어있지 않을 경우 예외 발생")
    @Test
    void 출발역_도착역_미연결시_예외_발생() {
        List<Line> lines = 노선_목록_생성();
        Station 정자역 = new Station("정자역");

        Path path = new Path(정자역, 강남역);
        List<Station> stations = 모든_노선의_역_리스트_조회(path, lines);

        출발역_도착역_예외_발생함(path, stations);
    }

    @DisplayName("출발역이나 도착역이 노선에 등록되어있지 않는경우 예외 발생")
    @Test
    void 출발역_도착역_존재하지_않은경우_예외_발생() {
        List<Line> lines = 노선_목록_생성();
        Station 정자역 = new Station("정자역");
        Station 교대역 = new Station("교대역");

        Path path = new Path(정자역, 교대역);
        List<Station> stations = 모든_노선의_역_리스트_조회(path, lines);

        출발역_도착역_예외_발생함(path, stations);
    }

    /**
     * 잠실 - 20 - 강남 - 100 - 양재 - 10 - 판교
     * └---------- 10---------┘
     *
     */
    @DisplayName("최단 경로 구하기")
    @Test
    void 최단경로_구하기() {
        List<Line> lines = 노선_목록_생성();
        Path path = new Path(잠실역, 판교역);
        List<Station> shortPaths = path.findShortPath(lines);

        최단경로_구함(shortPaths);
    }

    /**
     * 잠실 - 20 - 강남 - 100 - 양재 - 10 - 판교
     * └---------- 10---------┘
     *
     */
    @DisplayName("최단 거리값 구하기")
    @Test
    void 최단거리값_구하기() {
        List<Line> lines = 노선_목록_생성();
        Path path = new Path(잠실역, 판교역);
        List<Station> paths = path.findShortPath(lines);

        최단거리값_구함(path, paths.size());
    }

    @DisplayName("최단 거리 요금 구하기")
    @ParameterizedTest
    @CsvSource(value = {"6:550", "18:880", "30:1450"}, delimiter = ':')
    void 최단거리_요금_구하기(int age, int resultFare) {
        List<Line> lines = 노선_목록_생성();
        Path path = new Path(잠실역, 판교역);
        path.findShortPath(lines);

        int distance = path.calculateDistance(lines.size());
        int fare = path.calculateFare(distance, age);

        최단거리_요금_구함(fare, resultFare);
    }

    @DisplayName("노선별 추가요금 최단 거리 요금 구하기")
    @ParameterizedTest
    @CsvSource(value = {"6:1150", "18:1480", "30:2050"}, delimiter = ':')
    void 노선별_추가요금_최단거리_요금_구하기(int age, int resultFare) {
        신분당선.addFare(300);
        이호선.addFare(600);

        List<Line> lines = 노선_목록_생성();
        Path path = new Path(잠실역, 판교역);
        path.findShortPath(lines);

        int distance = path.calculateDistance(lines.size());
        int fare = path.calculateFare(distance, age);

        최단거리_요금_구함(fare, resultFare);
    }

    private List<Line> 노선_목록_생성() {
        return Arrays.asList(신분당선, 이호선);
    }

    private void 출발역_도착역_동일할경우_예외_발생함() {
        assertThatThrownBy(() -> {
            new Path(잠실역, 잠실역);
        }).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("출발지와 도착지가 같은 경로는 검색할수 없습니다.");
    }

    private void 출발역_도착역_예외_발생함(Path path, List<Station> station) {
        assertThatThrownBy(() -> {
            path.validStations(station);
        }).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("역이 연결되지 않았거나 등록되지 않았습니다.");
    }

    private List<Station> 모든_노선의_역_리스트_조회(Path path, List<Line> lines) {
        return path.assembleStations(lines);
    }

    private void 최단경로_구함(List<Station> shortPaths) {
        assertThat(shortPaths).hasSize(3);
        assertThat(shortPaths).startsWith(잠실역);
        assertThat(shortPaths.get(1)).isEqualTo(양재역);
        assertThat(shortPaths).endsWith(판교역);
    }

    private void 최단거리값_구함(Path path, int k) {
        assertThat(path.calculateDistance(k)).isEqualTo(20);
    }

    private void 최단거리_요금_구함(int fare, int resultFare) {
        assertThat(fare).isEqualTo(resultFare);
    }
}
