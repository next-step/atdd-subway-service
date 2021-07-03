package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class FarePolicyByLineTest {
    @Test
    void calculate() {
        //given
        Line line1 = getTestLineByExtraFare(900);
        Line line2 = getTestLineByExtraFare(500);
        Line line3 = getTestLineByExtraFare(0);

        FarePolicyByLine farePolicyByLine = new FarePolicyByLine(Arrays.asList(line1, line2, line3));
        double actualFare = farePolicyByLine.calculate(1250);
        double expectedFare = 2150;

        assertThat(actualFare).isEqualTo(expectedFare);
    }

    public static Line getTestLineByExtraFare(double extraFare) {
        return new Line("name", "black", new Station(), new Station(), 10, extraFare);
    }
}