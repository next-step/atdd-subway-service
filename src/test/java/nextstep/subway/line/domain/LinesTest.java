package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import nextstep.subway.fare.domain.Fare;
import org.junit.jupiter.api.Test;

class LinesTest {

    @Test
    void 노선별_요금중_가장_높은_요금_조회() {
        //given
        Lines lines = new Lines(Arrays.asList(
            new Line("일호선", "yellow", new Fare(1000)),
            new Line("이호선", "green", new Fare(500)),
            new Line("삼호선", "red", new Fare(750))
        ));

        //when
        Fare fare = lines.maxFares();

        //then
        assertThat(fare).isEqualTo(new Fare(1000));
    }
}