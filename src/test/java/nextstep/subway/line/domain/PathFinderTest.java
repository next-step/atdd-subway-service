package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathFinderTest {

    private Station 강남역;
    private Station 삼성역;
    private Station 잠실역;
    private Station 덕소역;
    private Station 구리역;

    private Line 이호선;
    private Line 중앙선;
    private Lines 노선들;

    private Stations 모든역들;

    @BeforeEach
    void init() {
        강남역 = new Station("강남역");
        삼성역 = new Station("삼성역");
        잠실역 = new Station("잠실역");
        덕소역 = new Station("덕소역");
        구리역 = new Station("구리역");

        모든역들 = new Stations(Arrays.asList(강남역, 삼성역, 잠실역, 덕소역, 구리역));
        이호선 = new Line("2호선", "초록색");
        이호선.addSection(new Section(강남역, 삼성역, 100));
        이호선.addSection(new Section(삼성역, 잠실역, 50));
        중앙선 = new Line("중앙선", "하늘색");
        중앙선.addSection(new Section(덕소역, 구리역, 50));

        노선들 = new Lines(Arrays.asList(이호선, 중앙선));
    }

    @DisplayName("경로를 조회시 목록과 거리를 확인할수있다.")
    @Test
    void findPath() {
        PathFinder pathFinder = new PathFinder(노선들, 모든역들);

        assertThat(pathFinder.getPath(강남역, 잠실역)).containsExactly(강남역, 삼성역, 잠실역);
        assertThat(pathFinder.getWeight(강남역, 잠실역)).isEqualTo(150);
    }

    @DisplayName("연결이 안된 경로는 예외처리한다.")
    @Test
    void notConnectedPathException() {
        PathFinder pathFinder = new PathFinder(노선들, 모든역들);

        assertThatThrownBy(
                () -> pathFinder.getPath(강남역, 덕소역)
        ).isInstanceOf(NoSuchElementException.class);
    }
}