package nextstep.subway.station.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StationsTest {

    @DisplayName("저장된 역 목록과 일치하는 역 개수를 반환하는 테스트")
    @Test
    void given_Stations_when_CountMatch_then_ReturnCountOfMatchElement() {
        // given
        final Stations stations = new Stations(Arrays.asList(new Station("1"), new Station("2")));

        // when
        final long actual = stations.countMatch(Arrays.asList(new Station("1"), new Station("3")));

        // then
        assertThat(actual).isEqualTo(1);
    }

    @DisplayName("역 목록에 특정 역이 포함되어 있는지 테스트")
    @Test
    void given_Stations_when_ContainsStations_then_ReturnBoolean() {
        // given
        final Stations stations = new Stations(Arrays.asList(new Station("1"), new Station("2")));

        // when
        final boolean exist = stations.contains(new Station("1"));
        final boolean notExist = stations.contains(new Station("3"));

        // then
        assertThat(exist).isEqualTo(true);
        assertThat(notExist).isEqualTo(false);
    }

    @Test
    void given_Stations_when_toStationPairs_then_ReturnStationPairList() {
        // given
        final Station station1 = new Station("1");
        final Station station2 = new Station("2");
        final Station station3 = new Station("3");
        final List<Station> stationList = Arrays.asList(station1, station2, station3);
        final Stations stations = new Stations(stationList);

        // when
        final List<StationPair> stationPairs = stations.toStationPairs();

        // then
        assertThat(stationPairs).containsExactly(new StationPair(station1, station2),
            new StationPair(station2, station3));
    }
}
