package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathTest {
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private List<Line> lines = new ArrayList<>();

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10);
        Line 이호선 = new Line("이호선", "green", 교대역, 강남역, 10);
        //Line 삼호선 = new Line("삼호선", "orange", 교대역, 양재역, 5);

        lines.add(신분당선);
        lines.add(이호선);
       // lines.add(삼호선);
    }

    @DisplayName("경로 찾기")
    @Test
    void findPath() {
        Path path = new Path(lines, 양재역, 교대역);

        assertThat(path.findShortestPath()).containsExactly(양재역, 강남역, 교대역);
        assertThat(path.findWeight()).isEqualTo(20);
    }

    @DisplayName("경로 찾기 예외 - 출발역과 도착역이 같은 경우")
    @Test
    void isSameSourceAndTarget() {
        assertThatThrownBy(() -> {
            new Path(lines, 강남역, 강남역);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("경로 찾기 예외 -  존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void validateContain() {
        Station 왕십리역 = new Station("왕십리역");
        Station 군자 = new Station("군자역");

        assertThatThrownBy(() -> {
            new Path(lines, 왕십리역, 군자);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("경로 찾기 예외 -  출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void validateNotPath() {
        assertThatThrownBy(() -> {
            new Path(lines, 양재역, 남부터미널역);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
