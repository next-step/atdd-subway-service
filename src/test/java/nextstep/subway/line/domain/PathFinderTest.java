package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PathFinderTest {
    private PathFinder pathFinder;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 주안역;
    private Station 인하대역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 연결안된노선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line.Builder("신분당선", "bg-red-600")
                .upStation(강남역)
                .downStation(양재역)
                .distance(10)
                .build();
        이호선 = new Line.Builder("이호선", "bg-red-600")
                .upStation(교대역)
                .downStation(강남역)
                .distance(10)
                .build();
        삼호선 = new Line.Builder("삼호선", "bg-red-600")
                .upStation(교대역)
                .downStation(양재역)
                .distance(5)
                .build();

        삼호선.addSection(교대역, 남부터미널역, 3);

        주안역 = new Station("주안역");
        인하대역 = new Station("인하대역");

        연결안된노선 = new Line.Builder("연결안된노선", "bg-red-600")
                .upStation(주안역)
                .downStation(인하대역)
                .distance(5)
                .build();

        pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 연결안된노선));
    }

    @Test
    void 최단_경로() {
        Path path = pathFinder.findShortestPath(교대역, 양재역);

        assertAll(
                () -> assertThat(path.getDistance()).isEqualTo(5),
                () -> assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역)
        );
    }

    @Test
    void 없는_경로_예외() {
        assertThatThrownBy(
                () -> pathFinder.findShortestPath(교대역, 주안역)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 노선_추가() {
        Station 추가역1 = new Station("추가역1");
        Station 추가역2 = new Station("추가역2");

        Line 추가노선 = new Line.Builder("추가노선", "bg-red-600")
                .upStation(추가역1)
                .downStation(추가역2)
                .distance(7)
                .build();

        pathFinder.addLine(추가노선);

        Path path = pathFinder.findShortestPath(추가역1, 추가역2);
        assertAll(
                () -> assertThat(path.getDistance()).isEqualTo(7),
                () -> assertThat(path.getStations()).containsExactly(추가역1, 추가역2)
        );
    }
}