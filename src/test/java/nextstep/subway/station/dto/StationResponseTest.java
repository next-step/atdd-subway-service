package nextstep.subway.station.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static nextstep.subway.station.dto.StationResponse.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("StationResponse 테스트")
class StationResponseTest {

    private Station 양평역;
    private Station 영등포구청역;
    private Station 영등포시장역;
    private Station 신길역;
    private Station 오목교역;

    @BeforeEach
    void 구간_생성() {
        양평역 = new Station(1L, "양평역");
        영등포구청역 = new Station(2L, "영등포구청역");
        영등포시장역 = new Station(3L, "영등포시장역");
        신길역 = new Station(4L, "신길역");
        오목교역 = new Station(5L, "오목교역");
    }

    @Test
    void of_성공_케이스1() {
        StationResponse stationResponse = of(양평역);

        assertAll(
                () -> assertThat(stationResponse.getId()).isEqualTo(양평역.getId()),
                () -> assertThat(stationResponse.getName()).isEqualTo(양평역.getName()),
                () -> assertThat(stationResponse.getCreatedDate()).isEqualTo(양평역.getCreatedDate()),
                () -> assertThat(stationResponse.getModifiedDate()).isEqualTo(양평역.getModifiedDate())
        );
    }

    @Test
    void of_성공_케이스2() {
        Stations stations = new Stations(asList(양평역, 영등포구청역, 영등포시장역));
        List<StationResponse> stationResponses = of(stations);

        for(int i = 0; i < stationResponses.size(); i++) {
            int finalI = i;
            assertAll(
                    () -> assertThat(stationResponses.get(finalI).getId()).isEqualTo(stations.get(finalI).getId()),
                    () -> assertThat(stationResponses.get(finalI).getName()).isEqualTo(stations.get(finalI).getName()),
                    () -> assertThat(stationResponses.get(finalI).getCreatedDate()).isEqualTo(stations.get(finalI).getCreatedDate()),
                    () -> assertThat(stationResponses.get(finalI).getModifiedDate()).isEqualTo(stations.get(finalI).getModifiedDate())
            );
        }
    }
}