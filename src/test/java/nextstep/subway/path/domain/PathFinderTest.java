package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PathFinderTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);

        삼호선.addSection(new Section(교대역, 남부터미널역, 3));
    }

    @Test
    public void findPath() {
        // given
        final Station source = 강남역;
        final Station target = 양재역;

        // when
        final Path actual = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선))
            .findPath(source, target);

        // then
        assertThat(actual.getStations()).containsExactly(강남역, 양재역);
        assertThat(actual.getDistance()).isEqualTo(10);
    }
}
