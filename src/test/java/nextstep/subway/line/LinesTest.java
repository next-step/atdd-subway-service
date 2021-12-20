package nextstep.subway.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LinesTest {
    private Line firstLine;
    private Line secondLine;
    private Line thirdLine;

    @BeforeEach
    void setUp() {
        firstLine = LineTestFixture.노선을_생성한다("1호선", "red", LineTestFixture.upStationFirstLine, LineTestFixture.downStationFirstLine, 10);
        firstLine.addStation(LineTestFixture.upStationFirstLine, LineTestFixture.addStationFirstLine, 5);
        secondLine = LineTestFixture.노선을_생성한다("2호선", "red", LineTestFixture.upStationSecondLine, LineTestFixture.downStationSecondLine, 20);
        thirdLine = LineTestFixture.노선을_생성한다("3호선", "red", LineTestFixture.upStationThirdLine, LineTestFixture.downStationThirdLine, 15);
    }

    @Test
    @DisplayName("모든 역을 조회한다. (중복 제외)")
    void getStations() {
        Lines lines = new Lines(Lists.newArrayList(firstLine, secondLine, thirdLine));
        assertThat(lines.getStations()).hasSize(4);
    }

    @Test
    @DisplayName("여러개 노선의 구간을 조회한다. (중복 제외)")
    void getSections() {
        Lines lines = new Lines(Lists.newArrayList(firstLine, secondLine, thirdLine));
        assertThat(lines.getSections()).hasSize(4);
    }
}
