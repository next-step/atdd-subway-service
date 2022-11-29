package nextstep.subway.station.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class StationsTest {
    @ParameterizedTest
    @CsvSource(value = {"1:false", "2:true"}, delimiter = ':')
    @DisplayName("역 목록 사이즈 비교")
    void stationsSizeLessThan(int size, boolean expect) {
        // given
        Stations stations = Stations.from(Collections.singletonList(Station.from("강남역")));

        // when
        boolean actual = stations.isLessThan(size);

        // then
        assertThat(actual).isEqualTo(expect);
    }
}
