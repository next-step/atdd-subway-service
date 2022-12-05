package nextstep.subway.station.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StationResponseTest {

    @DisplayName("Station 을 StationResponse 로 변환할 수 있다.")
    @Test
    void factory_method() {
        String stationName = "강남역";
        Station 강남역 = new Station(stationName);

        StationResponse stationResponse = StationResponse.from(강남역);

        assertThat(stationResponse.getName()).isEqualTo(stationName);
    }

    @DisplayName("List<Station> 리스트를 List<StationResponse> 로 변환할 수 있다.")
    @Test
    void list_factory_method() {
        Station 강남역 = new Station("강남역");
        Station 광교역 = new Station("광교역");

        List<StationResponse> stationResponses = StationResponse.of(Arrays.asList(강남역, 광교역));

        assertThat(stationResponses.size()).isEqualTo(2);
        assertThat(stationResponses.get(0).getName()).isEqualTo("강남역");
        assertThat(stationResponses.get(1).getName()).isEqualTo("광교역");

    }
}
