package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FareCalculatorTest {

    private List<Line> lines = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        lines.add(new Line(1L, "2호선", "green", new Station(), new Station(), 0L, 10));
        lines.add(new Line(2L, "3호선", "orange", new Station(), new Station(), 1500L, 10));
        lines.add(new Line(3L, "6호선", "orange", new Station(), new Station(), 880L, 10));
    }

    @DisplayName("노선별 추가요금 정책 테스트")
    @Test
    void additionalLineFareTest() {
        long resultFare = FareCalculator.additionalLineFare(lines);

        assertThat(resultFare).isEqualTo(1500L);
    }

    @DisplayName("거리별 추가요금 정책 테스트")
    @Test
    void additionalDistanceFareTest() {
        long resultFare = FareCalculator.additionalDistanceFare(59);
        assertThat(resultFare).isEqualTo(1000);
    }
}


