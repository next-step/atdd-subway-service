package nextstep.subway.path;

import nextstep.subway.line.acceptance.SectionTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        assertThatThrownBy(() -> {
            new Path(잠실역, 잠실역);
        }).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("출발지와 도착지가 같은 경로는 검색할수 없습니다.");
    }

    @DisplayName("출발역 도착역 연결 되어있지 않을 경우 예외 발생")
    @Test
    void 출발역_도착역_미연결시_예외_발생() {
        List<Line> lines = Arrays.asList(신분당선, 이호선);
        Station 정자역 = new Station("정자역");
        Station 교대역 = new Station("교대역");
        Path path = new Path(정자역, 강남역);
        List<Station> Stations = path.assembleStations(lines);

        assertThatThrownBy(() -> {
            path.validStations(Stations);
        }).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("역이 연결되지 않았거나 등록되지 않았습니다.");
    }

    @DisplayName("출발역이나 도착역이 노선에 등록되어있지 않는경우 예외 발생")
    @Test
    void 출발역_도착역_존재하지_않은경우_예외_발생() {
        List<Line> lines = Arrays.asList(신분당선, 이호선);
        Station 정자역 = new Station("정자역");
        Station 교대역 = new Station("교대역");
        Path path = new Path(정자역, 교대역);
        List<Station> Stations = path.assembleStations(lines);

        assertThatThrownBy(() -> {
            path.validStations(Stations);
        }).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("역이 연결되지 않았거나 등록되지 않았습니다.");
    }

    /**
     * 잠실 - 20 - 강남 - 100 - 양재 - 10 - 판교
     * └---------- 10---------┘
     * 잠실에서 판교까지 최단 경로 구하기
     */
    @DisplayName("최단 경로 구하기")
    @Test
    void 최단경로_구하기() {
        List<Line> lines = Arrays.asList(신분당선, 이호선);
        Path path = new Path(잠실역, 판교역);
        List<Station> shortPaths = path.findShortPath(lines);

        assertThat(shortPaths).hasSize(3);
        assertThat(shortPaths).startsWith(잠실역);
        assertThat(shortPaths.get(1)).isEqualTo(양재역);
        assertThat(shortPaths).endsWith(판교역);
    }
}
