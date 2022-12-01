package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathFinderTest {

    private final List<Station> stations = new ArrayList<>();
    private final List<Section> sections = new ArrayList<>();
    private Station 교대역;
    private Station 강남역;
    private Station 양재역;

    @BeforeEach
    public void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        Station 남부터미널역 = new Station("남부터미널역");
        stations.addAll(Arrays.asList(교대역, 강남역, 양재역, 남부터미널역));

        Section 강남_교대 = new Section(null, 강남역, 양재역, 10);
        Section 교대_강남 = new Section(null, 교대역, 강남역, 10);
        Section 교대_남부 = new Section(null, 교대역, 남부터미널역, 2);
        Section 남부_양재 = new Section(null, 남부터미널역, 양재역, 3);
        sections.addAll(Arrays.asList(강남_교대, 교대_강남, 교대_남부, 남부_양재));
    }

    @DisplayName("출발역과 도착역으로 최단거리를 조회할 수 있다.")
    @Test
    void shortest_path() {
        PathFinder pathFinder = PathFinder.of(stations, sections);

        Path path = pathFinder.findPath(교대역, 양재역);

        assertAll(
                () -> assertThat(path.getDistance()).isEqualTo(5),
                () -> assertThat(path.getStations()).extracting(Station::getName).containsExactly("교대역", "남부터미널역", "양재역")
        );

    }

    @DisplayName("출발역과 도착역이 같은 경우 IllegalArgumentException 이 발생한다.")
    @Test
    void same_source_target() {
        PathFinder pathFinder = PathFinder.of(stations, sections);

        assertThatThrownBy(() -> pathFinder.findPath(교대역, 교대역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 IllegalArgumentException 이 발생한다.")
    @Test
    void not_linked_line() {
        Station 인천터미널역 = new Station("인천터미널");
        Station 동춘역 = new Station("동춘역");
        Section 인터_동춘 = new Section(null, 인천터미널역, 동춘역, 10);
        stations.addAll(Arrays.asList(인천터미널역, 동춘역));
        sections.add(인터_동춘);
        PathFinder pathFinder = PathFinder.of(stations, sections);

        assertThatThrownBy(() -> pathFinder.findPath(강남역, 인천터미널역))
                .isInstanceOf(IllegalArgumentException.class);

    }

}
