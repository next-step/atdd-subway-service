package nextstep.subway.path.domain;

import nextstep.subway.exception.NotValidatePathException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationsResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PathFinderTest {

    private Station 강남역;
    private Station 까치산역;
    private Station 발산역;
    private Line sourceLine;
    private Line targetLine;

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        까치산역 = new Station("까치산역");
        발산역 = new Station("발산역");
        sourceLine = new Line("2호선", "green", 강남역, 까치산역, 10, 800);
        targetLine = new Line("5호선", "purple", 까치산역, 발산역, 12, 1200);
    }

    @DisplayName("최적 경로를 검색한다.")
    @Test
    void findShortestPath_test() {
        //when
        PathFinder pathFinder = new PathFinder(강남역, 발산역, List.of(sourceLine, targetLine));
        PathResponse response = pathFinder.findShortestPathToResponse(45);

        //then
        StationsResponse stationsResponse = response.getStations();
        assertThat(stationsResponse.getStations()).containsExactly(
                StationResponse.of(강남역),
                StationResponse.of(까치산역),
                StationResponse.of(발산역)
        );
    }

    @DisplayName("최적 경로의 거리를 계산한다.")
    @Test
    void calculateShortestDistance_test() {
        //when
        PathFinder pathFinder = new PathFinder(강남역, 발산역, List.of(sourceLine, targetLine));
        PathResponse response = pathFinder.findShortestPathToResponse(45);

        //then
        assertThat(response.getDistance()).isEqualTo(22);
    }


    @DisplayName("최적 경로 검색 시 출발역과 도착역은 같을 수 없다.")
    @Test
    void validateEquals_test() {
        //when
        assertThrows(NotValidatePathException.class,
                () -> new PathFinder(강남역, 강남역, List.of())
        );
    }

    @DisplayName("최적 경로 검색 시 출발역과 도착역이 연결이 되어 있어야 한다.")
    @Test
    void validateConnected_test() {
        //given
        Station 김포공항역 = new Station("김포공항역");
        Line sourceLine = new Line("2호선", "green", 강남역, 까치산역, 20);
        Line targetLine = new Line("5호선", "purple", 김포공항역, 발산역, 10);

        //when
        assertThrows(NotValidatePathException.class,
                () -> new PathFinder(강남역, 발산역, List.of(sourceLine, targetLine))
        );
    }

    @DisplayName("최적 경로 검색 시 존재하지 않는 출발역이나 도착역은 조회할 수 없다.")
    @Test
    void validateNotExist_test() {
        //given
        Station 김포공항역 = new Station("김포공항역");

        Line sourceLine = new Line("2호선", "green", 강남역, 까치산역, 20);
        Line targetLine = new Line("5호선", "purple", 까치산역, 발산역, 10);

        //when
        assertThrows(IllegalArgumentException.class,
                () -> new PathFinder(강남역, 김포공항역, List.of(sourceLine, targetLine))
        );
    }

    @DisplayName("최적 경로에 대한 운임비용을 계산한다.")
    @Test
    void calculateFare_test() {
        //given
        Line sourceLine = new Line("2호선", "green", 강남역, 까치산역, 10, 800);
        Line targetLine = new Line("5호선", "purple", 까치산역, 발산역, 12, 900);

        //when
        PathFinder pathFinder = new PathFinder(강남역, 발산역, List.of(sourceLine, targetLine));
        PathResponse response = pathFinder.findShortestPathToResponse(45);

        assertThat(response.getFare()).isEqualTo(2450);
    }

    @DisplayName("추가 운임이 없는 노선에 대한 최적 경로에 대한 운임비용을 계산한다.")
    @Test
    void calculateFareWithNotSurcharge_test() {
        //given
        Line sourceLine = new Line("2호선", "green", 강남역, 까치산역, 10);
        Line targetLine = new Line("5호선", "purple", 까치산역, 발산역, 12);

        //when
        PathFinder pathFinder = new PathFinder(강남역, 발산역, List.of(sourceLine, targetLine));
        PathResponse response = pathFinder.findShortestPathToResponse(45);

        assertThat(response.getFare()).isEqualTo(1550);
    }

    @DisplayName("노선들 중 가장 큰 추가운임을 가져온다.")
    @Test
    void maxSurcharge_test() {
        //given
        Line sourceLine = new Line("2호선", "green", 강남역, 까치산역, 10, 800);
        Line targetLine = new Line("5호선", "purple", 까치산역, 발산역, 12, 1200);

        //when
        PathFinder pathFinder = new PathFinder(강남역, 발산역, List.of(sourceLine, targetLine));

        assertThat(pathFinder.getFare()).isEqualTo(2450);
    }

    @DisplayName("로그인한 사용자의 age가 15세인 경우 20 할인된 금액으로 계산된다.")
    @Test
    void discountFareByAgeWithChild_test() {
        //given
        Line sourceLine = new Line("2호선", "green", 강남역, 까치산역, 10, 800);
        Line targetLine = new Line("5호선", "purple", 까치산역, 발산역, 12, 1200);

        //when
        PathFinder pathFinder = new PathFinder(강남역, 발산역, List.of(sourceLine, targetLine));
        PathResponse response = pathFinder.findShortestPathToResponse(15);

        assertThat(response.getFare()).isEqualTo(2200);
    }
}
