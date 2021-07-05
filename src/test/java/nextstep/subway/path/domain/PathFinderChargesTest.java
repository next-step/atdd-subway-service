package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.infra.RatePolicyByAddition;
import nextstep.subway.path.infra.RatePolicyByAge;
import nextstep.subway.path.infra.RatePolicyByDistance;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static nextstep.subway.utils.DomainInitUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("경로의 요금을 관리한다.")
public class PathFinderChargesTest {
    private static Line line;

    private static Station a, b, c;

    private static PathFinder pathFinder;

    @BeforeAll
    public static void setup() {
        a = createStation("a");    b = createStation("b");    c = createStation("c");

        line = appendSectionByLine("lowerCaseLine", 0,
            createSection(a, b, 10),
            createSection(b, c, 10)
        );

        pathFinder = initPathFinder(line);
    }

    @Test
    @DisplayName("[거리별 요금 정책]과 [추가요금]과 [연령별 요금 할인]을 계산한다.")
    public void step1() {
        Path<Station> path = pathFinder.getPath(a, c);
        path.additionalChargesCalculate(new RatePolicyByDistance(path.getDistance()))
            .additionalChargesCalculate(new RatePolicyByAddition(100))
            .additionalChargesCalculate(new RatePolicyByAge(30));

        assertThat(path.getDistance()).isEqualTo(20);
        assertThat(path.getCharges()).isEqualTo(1550);
    }

}
