package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("역 목록 관련 기능")
public class StationsTest {
    @DisplayName("역 목록 응답 확인")
    @Test
    void toStationResponses() {
        Station station1 = new Station("신도림역");
        Station station2 = new Station("신정네거리역");
        Station station3 = new Station("까치산역");

        Stations stations = new Stations(Arrays.asList(station1, station2, station3));
        List<StationResponse> responses = stations.toResponses();
        assertThat(responses).containsExactly(StationResponse.of(station1),StationResponse.of(station2),StationResponse.of(station3));
    }
}
