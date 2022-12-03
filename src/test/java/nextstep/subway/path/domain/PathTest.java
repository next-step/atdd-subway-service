package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private List<Station> stations;

    @BeforeEach
    void setUp() {
        교대역 = 지하철역_생성("교대역");
        강남역 = 지하철역_생성("강남역");
        양재역 = 지하철역_생성("양재역");
        stations = Arrays.asList(교대역, 강남역, 양재역);
    }

    @Test
    @DisplayName("지하철역 리스트와 거리, 추가요금을 받아 생성")
    void create() {
        // given
        Path path = 경로_생성(stations, 10, 1000);

        // expect
        assertThat(path).isNotNull();
    }

    @Test
    @DisplayName("20 거리의 추가요금 1000원 지하철 요금 계산")
    void calculateFare() {
        // given
        Path path = 경로_생성(stations, 20, 1000);

        // when
        int fare = path.calculateFare(0);

        // then
        assertThat(fare).isEqualTo(1_450 + 1000);
    }

    @Test
    @DisplayName("기본 거리의 추가요금 100원 청소년 지하철 요금 계산")
    void calculateFare_청소년() {
        // given
        Path path = 경로_생성(stations, 5, 100);

        // when
        int fare = path.calculateFare(14);

        // then
        assertThat(fare).isEqualTo(1150);
    }

    private Station 지하철역_생성(String name) {
        return new Station.Builder()
                .name(name)
                .build();
    }

    public static Path 경로_생성(List<Station> stations, int distance, int extraFare) {
        return new Path.Builder()
                .stations(stations)
                .distance(distance)
                .extraFare(extraFare)
                .build();
    }
}
