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

class LineFareCalculatorTest {

    private Section 구간;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        Station 사당역 = new Station("사당역");
        Station 삼성역 = new Station("삼성역");
        Station 잠실역 = new Station("잠실역");
        이호선 = new Line("이호선", "green", 사당역, 삼성역, 10, 1000);
        구간 = new Section(이호선, 삼성역, 잠실역, 5);
        이호선.addSection(구간);
    }

    @DisplayName("지하철 노선 추가요금 계산")
    @Test
    void lineFare() {
        List<SectionWeightedEdge> edges = Arrays.asList(
                new SectionWeightedEdge(구간)
        );

        Fare fare = LineFareCalculator.calculateByLine(edges);

        assertThat(fare).isEqualTo(new Fare(이호선.getAdditionalFare()));
    }

}
