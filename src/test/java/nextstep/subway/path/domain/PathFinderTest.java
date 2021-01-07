package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("경로 조회")
class PathFinderTest {

    private Station 교대역;
    private Station 양재역;
    private Station 사당역;
    private Lines lines;

    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        양재역 = new Station("양재역");
        Station 남부터미널 = new Station("남부터미널");
        Station 강남역 = new Station("강남역");
        사당역 = new Station("이수역");
        Station 이수역 = new Station("사당역");

        Line 신분당선 = new Line("신분당선", "orange", 강남역, 양재역, 5);
        Line 삼호선 = new Line("3호선", "orange", 교대역, 남부터미널, 1);
        Line 이호선 = new Line("2호선", "orange", 교대역, 강남역, 5);
        Line 사호선 = new Line("사호선", "orange", 사당역, 이수역, 5);

        lines = new Lines(삼호선, 이호선, 신분당선, 사호선);
    }

    @DisplayName("경로를 조회하고 순서가 일치하는지 확인한다.")
    @Test
    void getShortestPath() {
        // when
        PathResponse pathResponse = new PathFinder().ofPathResponse(new PathRequest(lines.allSection(), 교대역, 양재역));

        // then
        assertThat(pathResponse.getDistance()).isEqualTo(10);
        assertThat(pathResponse.getStations())
                .extracting("name")
                .containsExactly("교대역", "강남역", "양재역");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void findPathWhenNotConnectedStation() {
        // when then
        assertThatThrownBy(() -> new PathFinder().ofPathResponse(new PathRequest(lines.allSection(), 교대역, 사당역)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역이 연결 되어 있지 않습니다.");
    }
}