package nextstep.subway.path.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.sun.media.sound.InvalidDataException;
import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathFinderTest {
    Station 양재역 = new Station("양재역");
    Station 교대역 = new Station("교대역");
    Station 강남역 = new Station("강남역");
    Station 마곡역 = new Station("마곡역");
    Station 남부터미널역 = new Station("남부터미널역");

    List<Section> sections;


    @BeforeEach
    void setUp() {
        sections = Arrays.asList(
                new Section(null, 강남역, 양재역, 10),
                new Section(null, 교대역, 강남역, 10),
                new Section(null, 교대역, 남부터미널역, 3),
                new Section(null, 남부터미널역, 양재역, 2)
        );
    }

    @Test
    @DisplayName("PathFinder 를 사용하여 최적경로 조회")
    void findShortestPath() {
        Path path = PathFinder.of(sections).findShortestPath(교대역, 양재역);

        assertThat(path.getStations()).containsSequence(교대역, 남부터미널역, 양재역);
    }

    @DisplayName("동일역으로 최단 경로를 조회한다.")
    @Test
    void sameStationFindPath() {
        assertThatThrownBy(
                () -> PathFinder.of(sections).findShortestPath(강남역, 강남역)
        ).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("연결되지 않은 역으로 최단 경로를 생성한다.")
    @Test
    void nonStationFindPath() {
        assertThatThrownBy(
                () -> PathFinder.of(sections).findShortestPath(강남역, 마곡역)
        ).isInstanceOf(RuntimeException.class);
    }

}
