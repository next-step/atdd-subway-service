package nextstep.subway.station.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StationsTest {

    @DisplayName("저장된 역 목록과 일치하는 역 개수를 반환하는 테스트")
    @Test
    void given_Stations_when_CountMatch_then_ReturnCountOfMatchElement() {
        // given
        final Stations stations = new Stations(Arrays.asList(new Station("1"), new Station("2")));

        // when
        long actual = stations.countMatch(Arrays.asList(new Station("1"), new Station("3")));

        // then
        assertThat(actual).isEqualTo(1);
    }

    @DisplayName("역 목록에 특정 역이 포함되어 있는지 테스트")
    @Test
    void given_Stations_when_ContainsStations_then_ReturnBoolean() {
        // given
        final Stations stations = new Stations(Arrays.asList(new Station("1"), new Station("2")));

        // when
        boolean exist = stations.contains(new Station("1"));
        boolean notExist = stations.contains(new Station("3"));

        // then
        assertThat(exist).isEqualTo(true);
        assertThat(notExist).isEqualTo(false);
    }
}
