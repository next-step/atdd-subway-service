package nextstep.subway.path;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class PathFinderTest {

    Station 강남역;
    Station 양재역;
    Station 교대역;
    Station 남부터미널역;
    Station 사당역;
    Station 동작역;
    Line 신분당선;
    Line 이호선;
    Line 삼호선;
    Line 사호선;

    /**
     * 교대역     --- *2호선* ---  강남역
     * |                         |
     * *3호선*                  *신분당선*
     * |                         |
     * 남부터미널역 --- *3호선* ---  양재
     */
    @BeforeEach
    void init() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        사당역 = new Station("사당역");
        동작역 = new Station("동작역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
        사호선 = new Line("사호선", "bg-red-600", 사당역, 동작역, 3);

        삼호선.addSection(교대역, 남부터미널역, 3); // 남부터미널역-(2m)-양재역
    }

    @DisplayName("출발역부터 도착역까지 최단 경로를 조회한다.")
    @Test
    void 경로_조회_초기_상태() {
        // given
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));

        // when
        PathResponse path = pathFinder.getPath(강남역, 남부터미널역);

        // then
        assertThat(path.getStations().stream()
                .map(StationResponse::getName))
                .containsExactly(강남역.getName(), 양재역.getName(), 남부터미널역.getName());
        assertThat(path.getDistance()).isEqualTo(12);
    }

    @DisplayName("출발역과 도착역에 같은 역을 입력한다.")
    @Test
    void 경로_조회_예외_1() {
        // given
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선));

        // when
        assertThatThrownBy(() -> pathFinder.getPath(강남역, 강남역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("연결되어 있지 않은 출발역과 도착역을 입력한다.")
    @Test
    void 경로_조회_예외_2() {
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 사호선));

        assertThatThrownBy(() -> pathFinder.getPath(강남역, 사당역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 출발역이나 도착역을 입력한다.")
    @Test
    void 경로_조회_예외_3() {
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선));

        assertThatThrownBy(() -> pathFinder.getPath(강남역, 사당역))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> pathFinder.getPath(사당역, 강남역))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> pathFinder.getPath(사당역, 동작역))
                .isInstanceOf(IllegalArgumentException.class);

    }
}
