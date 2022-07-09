package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class PathFinderTest {
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 교대역;

    private Line 신분당선;
    private Line 삼호선;
    private Line 이호선;

    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        강남역 = Station.of(1L, "강남역");
        양재역 = Station.of(2L, "양재역");
        남부터미널역 = Station.of(3L,"남부터미널역");
        교대역 = Station.of(4L,"교대역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역,3000, 1000);
        삼호선 = new Line("3호선", "bg-orange-600", 교대역, 남부터미널역, 2000, 0);
        삼호선.addLineStation(남부터미널역, 양재역, 2500);
        이호선 = new Line("2호선", "bg-green-600", 교대역, 강남역, 3000, 500);

        pathFinder = PathFinder.init(Arrays.asList(신분당선, 삼호선, 이호선));
    }

    @Test
    @DisplayName("최단경로 조회 시 출발역부터 도착지까지의 역을 순서대로 셋팅하고 총 거리를 계산한다.")
    void findShortestPath() {
        // when
        Path 최단경로 = pathFinder.findShortestPath(양재역.getId(), 교대역.getId());

        // then
        assertThat(최단경로)
                .isEqualTo(Path.of(Arrays.asList(양재역, 남부터미널역, 교대역), 4500, 0));
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우 최단경로를 조회할 수 없다.")
    void findShortestPathFailWhenSameSourceTargetStation() {
        // when & then
        assertThatThrownBy(() -> pathFinder.findShortestPath(양재역.getId(), 양재역.getId()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발역과 도착역은 같을 수 없습니다.");
    }

    @Test
    @DisplayName("출발역이나 도착역이 존재하지 않는 경우 최단경로를 조회할 수 없다.")
    void findShortestPathFailWhenNotExistingStation() {
        // given
        Station 정자역 = Station.of(5L, "정자역");
        Station 청량리역 = Station.of(6L, "청량리역");

        // when & then
        assertAll(
            () -> assertThatThrownBy(() -> pathFinder.findShortestPath(정자역.getId(), 양재역.getId()))
                    .isExactlyInstanceOf(NoSuchElementException.class)
                    .hasMessage("출발역 혹은 도착역이 존재하지 않습니다."),
            () -> assertThatThrownBy(() -> pathFinder.findShortestPath(양재역.getId(), 청량리역.getId()))
                    .isExactlyInstanceOf(NoSuchElementException.class)
                    .hasMessage("출발역 혹은 도착역이 존재하지 않습니다.")
        );
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되어 있지 않는 경우 최단경로를 조회할 수 없다.")
    void findShortestPathFailWhenNotConnected() {
        // when
        Station 정자역 = Station.of(5L, "정자역");
        Station 서현역 = Station.of(6L, "서현역");

        Line 분당선 = new Line("분당선", "bg-yellow-600", 정자역, 서현역, 1000);

        PathFinder pathFinder = PathFinder.init(Arrays.asList(신분당선, 삼호선, 이호선, 분당선));

        // when & then
        assertThatThrownBy(() -> pathFinder.findShortestPath(교대역.getId(), 정자역.getId()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발역과 도착역이 연결되어 있지 않습니다.");
    }

    @Test
    @DisplayName("노선의 추가요금이 있는 경우 추가요금을 리턴한다.")
    void additionalFareOfSingleLineInPath() {
        // when
        Path 신분당선_이용_경로 = pathFinder.findShortestPath(양재역.getId(), 강남역.getId());

        // then
        assertThat(신분당선_이용_경로.getAdditionalFare())
                .isEqualTo(1000);
    }

    @Test
    @DisplayName("경로 중 가장 높은 노선 추가요금을 리턴한다.")
    void additionalFareOfMultipleLineInPath() {
        // when
        Path 삼호선_이호선_이용_경로 = pathFinder.findShortestPath(남부터미널역.getId(), 강남역.getId());

        // then
        assertThat(삼호선_이호선_이용_경로.getAdditionalFare())
                .isEqualTo(500);
    }
}
