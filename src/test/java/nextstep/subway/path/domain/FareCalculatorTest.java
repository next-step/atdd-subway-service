package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class FareCalculatorTest {
    private final PathFinder pathFinder = new PathFinder();
    private final DistanceFarePolicy distancePolicy = new DistanceFarePolicy();
    private final LineFarePolicy linePolicy = new LineFarePolicy();
    private final AgeFarePolicy ageFarePolicy = new AgeFarePolicy();
    private final FareCalculator fareCalculator = new FareCalculator(distancePolicy, linePolicy, ageFarePolicy);
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 경강선;
    private List<Line> lines;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 세종대왕릉역;
    private Station 여주역;

    /**
     * 교대역    --- *2호선* ---   강남역      세종대왕릉역
     * |                          |            |
     * *3호선*                   *신분당선*     *경강선*
     * |                          |            |
     * 남부터미널역  --- *3호선* ---  양재        여주역
     */
    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        세종대왕릉역 = new Station("세종대왕릉역");
        여주역 = new Station("여주역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 4, 0);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 6, 0);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 50, 900);
        경강선 = new Line("경강선", "bg-red-600", 세종대왕릉역, 여주역, 80, 1200);
        lines = Arrays.asList(신분당선, 이호선, 삼호선, 경강선);

        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 3));
    }

    @DisplayName("요금 계산")
    @Test
    void calculate() {
        // given
        Path path = pathFinder.findPath(lines, 남부터미널역, 양재역);
        List<SectionEdge> sectionEdges = path.getSectionEdges();

        // when
        Fare fare = fareCalculator.calculate(sectionEdges, path.getDistance());

        // then
        assertThat(fare.getFare()).isEqualTo(2250);
    }

    @DisplayName("연령별 요금 할인 적용 테스트 (13세 미만은 350원 공제한 금액의 50%, 13-19세 미만은 350원 공제한 금액의 20% 할인 적용)")
    @Test
    void discountByAge() {
        // then
        assertAll(
                () -> assertThat(fareCalculator.discountByAge(Fare.of(1350), 8)).isEqualTo(Fare.of(850)),
                () -> assertThat(fareCalculator.discountByAge(Fare.of(1350), 13)).isEqualTo(Fare.of(1150)),
                () -> assertThat(fareCalculator.discountByAge(Fare.of(1350), 19)).isEqualTo(Fare.of(1350))
        );
    }
}
