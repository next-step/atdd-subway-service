package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.infra.RatePolicyByAddition;
import nextstep.subway.path.infra.RatePolicyByAge;
import nextstep.subway.path.infra.RatePolicyByDistance;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static nextstep.subway.utils.DomainInitUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("경로의 요금정책들을 관리한다.")
public class PathFinderFeeTest {
    private static Line line;

    private static Station a, b, c;

    private static PathFinder pathFinder;

    @BeforeAll
    public static void setup() {
        a = createStation("a");    b = createStation("b");    c = createStation("c");

        line = appendSectionByLine("lowerCaseLine", 0,
            createSection(a, b, 30),
            createSection(b, c, 36)
        );

        pathFinder = initPathFinder(line);
    }

    @ParameterizedTest
    @DisplayName("[이동거리 50KM 이하 요금 정책]과 [추가요금]을 계산한다.")
    @ValueSource(ints = {0, 100, 1000})
    public void adult_50km_under(int addition) {
        Path<Station> path = pathFinder.getPath(a, b);
        path.additionalFeeCalculate(new RatePolicyByDistance(path.getDistance()))
                .additionalFeeCalculate(new RatePolicyByAddition(addition))
                .additionalFeeCalculate(new RatePolicyByAge(30));

        assertThat(path.getDistance()).isEqualTo(30);
        assertThat(path.getFee()).isEqualTo(1650 + addition);
    }

    @Test
    @DisplayName("[이동거리 50KM 이하 요금 정책]과 [추가요금]과 [청소년 요금 할인]을 계산한다.")
    public void teenager_50km_under() {
        Path<Station> path = pathFinder.getPath(a, b);
        path.additionalFeeCalculate(new RatePolicyByDistance(path.getDistance()))
                .additionalFeeCalculate(new RatePolicyByAddition(100))
                .additionalFeeCalculate(new RatePolicyByAge(15));

        assertThat(path.getDistance()).isEqualTo(30);
        assertThat(path.getFee()).isEqualTo(1120);
    }

    @Test
    @DisplayName("[이동거리 50KM 이하 요금 정책]과 [추가요금]과 [어린이 요금 할인]을 계산한다.")
    public void children_50km_under() {
        Path<Station> path = pathFinder.getPath(a, b);
        path.additionalFeeCalculate(new RatePolicyByDistance(path.getDistance()))
                .additionalFeeCalculate(new RatePolicyByAddition(100))
                .additionalFeeCalculate(new RatePolicyByAge(10));

        assertThat(path.getDistance()).isEqualTo(30);
        assertThat(path.getFee()).isEqualTo(700);
    }

    @ParameterizedTest
    @DisplayName("[이동거리 50KM 이상 요금 정책]과 [추가요금]을 계산한다.")
    @ValueSource(ints = {0, 100, 1000})
    public void adult_50km_over(int addition) {
        Path<Station> path = pathFinder.getPath(a, c);
        path.additionalFeeCalculate(new RatePolicyByDistance(path.getDistance()))
                .additionalFeeCalculate(new RatePolicyByAddition(addition))
                .additionalFeeCalculate(new RatePolicyByAge(60));

        assertThat(path.getDistance()).isEqualTo(66);
        assertThat(path.getFee()).isEqualTo(2250 + addition);
    }

    @Test
    @DisplayName("[이동거리 50KM 이상 요금 정책]과 [추가요금]과 [청소년 요금 할인]을 계산한다.")
    public void teenager_50km_over() {
        Path<Station> path = pathFinder.getPath(a, c);
        path.additionalFeeCalculate(new RatePolicyByDistance(path.getDistance()))
                .additionalFeeCalculate(new RatePolicyByAddition(100))
                .additionalFeeCalculate(new RatePolicyByAge(15));

        assertThat(path.getDistance()).isEqualTo(66);
        assertThat(path.getFee()).isEqualTo(1600);
    }

    @Test
    @DisplayName("[이동거리 50KM 이상 요금 정책]과 [추가요금]과 [어린이 요금 할인]을 계산한다.")
    public void children_50km_over() {
        Path<Station> path = pathFinder.getPath(a, c);
        path.additionalFeeCalculate(new RatePolicyByDistance(path.getDistance()))
                .additionalFeeCalculate(new RatePolicyByAddition(100))
                .additionalFeeCalculate(new RatePolicyByAge(10));

        assertThat(path.getDistance()).isEqualTo(66);
        assertThat(path.getFee()).isEqualTo(1000);
    }

}
