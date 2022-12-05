package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PathFinderTest {
    private final Line 신분당선 = Line.of("신분당선", "red");
    private final Line 수인분당선 = Line.of("수인분당선", "yellow");
    private final Line 이호선 = Line.of("이호선", "green");
    private final Line 일호선 = Line.of("일호선", "blue");
    private final Station 강남역 = new Station(1L, "강남역");
    private final Station 판교역 = new Station(2L, "판교역");
    private final Station 광교역 = new Station(3L, "광교역");
    private final Station 선릉역 = new Station(4L, "선릉역");
    private final Station 수원역 = new Station(5L, "수원역");
    private final Station 소요산역 = new Station(6L, "소요산역");
    private final Station 인천역 = new Station(7L, "인천역");
    private final Distance TEN = Distance.from(10);
    private final Distance FIVE = Distance.from(5);
    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        신분당선.addSection(강남역, 광교역, FIVE);
        수인분당선.addSection(선릉역, 수원역, TEN);
        이호선.addSection(강남역, 선릉역, FIVE);
        일호선.addSection(소요산역, 인천역, TEN);
        신분당선.setAdditionalFare(500);
        수인분당선.setAdditionalFare(800);
        pathFinder = new PathFinder(Arrays.asList(신분당선, 수인분당선, 이호선, 일호선));
    }

    @Test
    void 최단_경로_조회() {
        Path path = pathFinder.findPath(강남역.getId(), 광교역.getId());
        assertThat(path.getDistance()).isEqualTo(5);
    }

    @Test
    void 추가_요금_조회() {
        Path path = pathFinder.findPath(강남역.getId(), 광교역.getId());
        assertThat(path.getAdditionalFareByLine()).isEqualTo(500);
    }


    @Test
    void 추가_요금_조회_2() {
        Path path = pathFinder.findPath(광교역.getId(), 수원역.getId());
        assertThat(path.getAdditionalFareByLine()).isEqualTo(800);
    }

    @Test
    void 출발역과_도착역이_같은_경우() {
        assertThatThrownBy(() -> pathFinder.findPath(강남역.getId(), 강남역.getId())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 노선에_없는_역_조회() {
        assertThatThrownBy(() -> pathFinder.findPath(강남역.getId(), 판교역.getId())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 출발역과_도착역이_연결되어_있지_않은_경우() {
        assertThatThrownBy(() -> pathFinder.findPath(강남역.getId(), 인천역.getId())).isInstanceOf(IllegalArgumentException.class);
    }
}
