package nextstep.subway.station.domain;

import org.junit.jupiter.api.DisplayName;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Station 리스트 1급 컬렉션 테스트")
class StationsTest {

    private static Station 양평역 = new Station(1L, "양평역");
    private static Station 영등포구청역 = new Station(2L, "영등포구청역");
    private static Station 영등포시장역 = new Station(3L, "영등포시장역");
    private static Station 신길역 = new Station(4L, "신길역");
    private static Station 오목교역 = new Station(5L, "오목교역");

    void add_성공() {
        // given
        int expectedResult = 3;
        Stations stations = new Stations(asList(양평역, 영등포구청역, 신길역));

        // when, then
        assertThat(stations.size()).isEqualTo(expectedResult);
    }
}