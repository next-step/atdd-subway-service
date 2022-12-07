package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.ui.PathResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.Fixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 사호선;
    private Line 오호선;
    private Station 명동역;
    private Station 사당역;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 동대문역사공원역;
    private Station 광화문역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */

    @BeforeEach
    public void setUp() {
        강남역 = createStation("강남역", 1L);
        양재역 = createStation("양재역", 2L);
        교대역 = createStation("교대역", 3L);
        남부터미널역 = createStation("남부터미널역", 4L);
        명동역 = createStation("명동역", 5L);
        사당역 = createStation("사당역", 6L);
        동대문역사공원역 = createStation("동대문역사공원역", 7L);
        광화문역 = createStation("광화문역", 8L);
        신분당선 = createLine("신분당선", "bg-red-600", 강남역, 양재역, 10, 0);
        이호선 = createLine("이호선", "bg-red-600", 교대역, 강남역, 10, 0);
        삼호선 = createLine("삼호선", "bg-red-600", 교대역, 양재역, 10, 0);
        사호선 = createLine("사호선", "bg-red-600", 명동역, 사당역, 30, 0);
        오호선 = createLine("오호선", "bg-red-600", 동대문역사공원역, 광화문역, 10, 1000);
        삼호선.addSection(createSection(교대역, 남부터미널역, 8));
    }

    @DisplayName("10km 초과 ∼ 50km 이내 경로 조회 시 5km마다 100원 추가된 요금이 적용된다")
    @Test
    void getShortestPath_additionalFare_distance_level1() {
        List<Line> lines = Arrays.asList(신분당선, 이호선, 삼호선);
        PathFinder pathFinder = PathFinder.from(lines);
        PathResponse shortestPath = pathFinder.getShortestPath(강남역, 남부터미널역,
                fare -> FareCalculator.applyDiscountFare(fare, new Age(10)));
        List<Section> sections = getSections(lines);
        List<Station> stations = getStations(lines);

        Fare fare = FareCalculator.calculateAdditionalFare(
                sections, new ArrayList<>(stations), new Distance(shortestPath.getDistance())
        );

        assertThat(fare).isEqualTo(new Fare(1350));
    }

    @DisplayName("50km 초과 경로 조회 시 8km마다 500원 추가된 요금 정보이 적용된다")
    @Test
    void getShortestPath_additionalFare_distance_level2() {
        List<Line> lines = Collections.singletonList(사호선);
        PathFinder pathFinder = PathFinder.from(lines);
        PathResponse shortestPath = pathFinder.getShortestPath(명동역, 사당역,
                fare -> FareCalculator.applyDiscountFare(fare, new Age(10)));
        List<Section> sections = getSections(lines);
        List<Station> stations = getStations(lines);

        Fare fare = FareCalculator.calculateAdditionalFare(
                sections, new ArrayList<>(stations), new Distance(shortestPath.getDistance())
        );

        assertThat(fare).isEqualTo(new Fare(1650));
    }

    private List<Section> getSections(final List<Line> lines) {
        return lines.stream()
                .flatMap(collectionOfSections -> collectionOfSections.getSections().stream())
                .collect(Collectors.toList());
    }

    private List<Station> getStations(final List<Line> lines) {
        return lines.stream()
                .flatMap(section -> section.getStations().stream())
                .collect(Collectors.toList());
    }

    @DisplayName("경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금이 적용된다")
    @Test
    void calculateAdditionalFare_line() {
        PathFinder pathFinder = PathFinder.from(Collections.singletonList(오호선));
        PathResponse shortestPath = pathFinder.getShortestPath(동대문역사공원역, 광화문역,
                fare -> FareCalculator.applyDiscountFare(fare, new Age(10)));

        Fare fare = FareCalculator.calculateAdditionalFare(
                오호선.getSections(), new ArrayList<>(오호선.getStations()), new Distance(shortestPath.getDistance())
        );

        assertThat(fare).isEqualTo(new Fare(2250));
    }

    @DisplayName("어린이는 운임에서 350원을 공제한 금액의 50% 할인이 적용된다")
    @Test
    void applyDiscountFare_child() {
        assertThat(FareCalculator.applyDiscountFare(new Fare(1000), new Age(6))).isEqualTo(new Fare(675));
    }

    @DisplayName("청소년은 운임에서 350원을 공제한 금액의 20% 할인이 적용된다")
    @Test
    void applyDiscountFare_teen() {
        assertThat(FareCalculator.applyDiscountFare(new Fare(1000), new Age(13))).isEqualTo(new Fare(870));
    }
}
