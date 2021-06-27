package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.exception.NotFoundException;
import nextstep.subway.station.domain.Station;

@SuppressWarnings("NonAsciiCharacters")
class PathFinderTest {

    private final Station 시청역 = new Station("시청역");
    private final Station 당산역 = new Station("당산역");
    private final Station 홍대입구역 = new Station("홍대입구역");
    private final Station 문래역 = new Station("문래역");
    private final Station 신도림역 = new Station("신도림역");
    private final Station 구로역 = new Station("구로역");
    private final Station 판교역 = new Station("판교역");
    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        final List<Section> sections = new ArrayList<>();
        sections.add(new Section(null, 시청역, 당산역, 10));
        sections.add(new Section(null, 당산역, 홍대입구역, 20));
        sections.add(new Section(null, 홍대입구역, 문래역, 30));
        sections.add(new Section(null, 신도림역, 구로역, 90));
        pathFinder = new PathFinder(sections);
    }

    @DisplayName("최단 경로 조회 테스트")
    @Test
    void given_TwoStations_when_FindShortestPath_then_ReturnCorrectPath() {
        // when
        final Path path = pathFinder.findShortestPath(시청역, 문래역);

        // then
        final int expectedDistance = 60;
        final List<Station> expectedStations = Arrays.asList(시청역, 당산역, 홍대입구역, 문래역);
        assertThat(path.getDistance()).isEqualTo(expectedDistance);
        assertThat(path.getStations()).isEqualTo(expectedStations);
    }

    @DisplayName("출발역과 도착역이 같은 경우 예외 테스트")
    @Test
    void given_SameStations_when_FindShortestPath_then_ThrownException() {
        // when
        final Throwable throwable = catchThrowable(() -> pathFinder.findShortestPath(시청역, 시청역));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("출발역과 도착역이 연결되지 않은 경우 예외 테스트")
    @Test
    void given_NotConnectedStations_when_FindShortestPath_then_ThrownException() {
        // when
        final Throwable throwable = catchThrowable(() -> pathFinder.findShortestPath(시청역, 신도림역));

        // then
        assertThat(throwable).isInstanceOf(NotFoundException.class);
    }

    @DisplayName("도착역이 구간에 등록되지 않은 경우 예외 테스트")
    @Test
    void given_NotRegisteredStation_when_FindShortestPath_then_ThrownException() {
        // when
        final Throwable throwable = catchThrowable(() -> pathFinder.findShortestPath(시청역, 판교역));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}
