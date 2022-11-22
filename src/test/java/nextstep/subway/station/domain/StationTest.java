package nextstep.subway.station.domain;

import static nextstep.subway.station.domain.StationTestFixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철역 관련 도메인 테스트")
public class StationTest {

    @DisplayName("지하철역 이름이 동일하면 동일한 역이다.")
    @Test
    void checkStationNameEqual() {
        // given
        Station station = createStation("강남역");
        Station duplicateStation = createStation("강남역");

        // when & then
        assertThat(station.isSameStation(duplicateStation)).isTrue();
    }
}
