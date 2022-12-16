package nextstep.subway.station.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StationTest {

    @DisplayName("지하철역을 생성한다.")
    @Test
    void 지하철역을_생성한다() {
        Station 강남역 = new Station("강남역");
        assertThat(강남역.getName()).isEqualTo("강남역");
    }
}
