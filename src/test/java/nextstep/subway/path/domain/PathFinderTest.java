package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.NotConnectedPathException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-06
 */
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("경로 탐색 로직 테스트 클래스")
class PathFinderTest {

    private PathFinder pathFinder = new PathFinder();

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;

    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;

    private Lines lines;

    @BeforeEach
    void setup() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");

        이호선 = new Line("이호선", "green");
        이호선.addSection(new Section(이호선, 교대역, 강남역, 10));

        삼호선 = new Line("삼호선", "orange");
        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 100));
        삼호선.addSection(new Section(삼호선, 남부터미널역, 양재역, 100));

        신분당선 = new Line("신분당선", "red");
        신분당선.addSection(new Section(신분당선, 강남역, 양재역, 10));

        lines = new Lines(Arrays.asList(이호선, 삼호선, 신분당선));
    }


    @DisplayName("경로 탐색 테스트")
    @Test
    void findPathTest() {

        Path path = pathFinder.findPath(lines, 교대역, 양재역);

        List<Station> pathStations = path.getPathStations();

        assertThat(pathStations.get(0).getName()).isEqualTo("교대역");
        assertThat(pathStations.get(1).getName()).isEqualTo("강남역");
        assertThat(pathStations.get(2).getName()).isEqualTo("양재역");
        assertThat(path.getDistance()).isEqualTo(20);
    }

    @DisplayName("연결이 안되어있는 경로 탐색 테스트")
    @Test
    void findPathNotConnectedStationTest() {

        Station 사당역 = new Station("사당역");

        assertThatThrownBy(() -> pathFinder.findPath(lines, 교대역, 사당역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("graph must contain the sink vertex");
    }

}
