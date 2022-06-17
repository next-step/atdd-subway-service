package nextstep.subway.fare.domain;

import static nextstep.subway.DomainFixtureFactory.createLine;
import static nextstep.subway.DomainFixtureFactory.createPath;
import static nextstep.subway.DomainFixtureFactory.createSection;
import static nextstep.subway.DomainFixtureFactory.createStation;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.Age;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareCalculatorTest {
    Station 강남역;
    Station 양재역;
    Station 교대역;
    Station 남부터미널역;
    Line 신분당선;
    Line 이호선;
    Line 삼호선;

    @BeforeEach
    public void setUp() {
        강남역 = createStation(1L, "강남역");
        양재역 = createStation(2L, "양재역");
        교대역 = createStation(3L, "교대역");
        남부터미널역 = createStation(4L, "남부터미널역");

        신분당선 = createLine(1L, "신분당선", "bg-red-600", 강남역, 양재역, Distance.valueOf(10), Fare.valueOf(900));
        이호선 = createLine(2L, "이호선", "bg-red-600", 교대역, 강남역, Distance.valueOf(10));
        삼호선 = createLine(3L, "삼호선", "bg-red-600", 교대역, 양재역, Distance.valueOf(5), Fare.valueOf(500));
        삼호선.addSection(createSection(삼호선, 교대역, 남부터미널역, Distance.valueOf(3)));
    }

    @DisplayName("최단거리 요금 계산 테스트")
    @ParameterizedTest(name = "나이가 {0} 일때 요금 {1} 검증 테스트")
    @CsvSource(value = {"16, 1520", "7, 950"})
    void calculateFare(int input, int expect) {
        Set<Line> linesOfPath = new HashSet<>(Arrays.asList(신분당선, 삼호선));
        Path path = createPath(Lists.newArrayList(남부터미널역, 양재역, 강남역), Distance.valueOf(12), new HashSet<>(
                Arrays.asList(신분당선, 삼호선)));

        Fare fare = FareCalculator.calculateFare(path, Age.valueOf(input));
        assertThat(fare).isEqualTo(Fare.valueOf(expect));
    }
}
