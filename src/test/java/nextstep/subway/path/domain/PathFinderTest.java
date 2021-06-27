package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

@SuppressWarnings("NonAsciiCharacters")
class PathFinderTest {

    private final Station 시청역 = new Station("시청역");
    private final Station 당산역 = new Station("당산역");
    private final Station 홍대입구역 = new Station("홍대입구역");
    private final Station 문래역 = new Station("문래역");

    @Test
    void findShortestPath() {
        // given
        final List<Section> sections = new ArrayList<>();
        sections.add(new Section(null, 시청역, 당산역, 10));
        sections.add(new Section(null, 당산역, 홍대입구역, 20));
        sections.add(new Section(null, 홍대입구역, 문래역, 30));
        final PathFinder pathFinder = new PathFinder(sections);

        // when
        final Path path = pathFinder.findShortestPath(시청역, 문래역);

        // then
        final int expectedDistance = 60;
        final List<Station> expectedStations = Arrays.asList(시청역, 당산역, 홍대입구역, 문래역);
        assertThat(path.getDistance()).isEqualTo(expectedDistance);
        assertThat(path.getStations()).isEqualTo(expectedStations);
    }
}
