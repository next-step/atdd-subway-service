package nextstep.subway.station.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StationsTest {
    private Station 강남역;
    private Station 역삼역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
    }

    @Test
    @DisplayName("지하철 노선의 정보 변경 테스트")
    void distinctStations() {
        Stations 지하철역_정보 = new Stations();
        지하철역_정보.add(강남역);
        지하철역_정보.add(역삼역);
        지하철역_정보.add(강남역);

        List<Station> 중복되지않은_지하철역_정보 = 지하철역_정보.distinctStations();

        Assertions.assertAll(
                () -> assertThat(중복되지않은_지하철역_정보).size().isEqualTo(2),
                () -> assertThat(중복되지않은_지하철역_정보).contains(강남역, 역삼역)
        );
    }


    @Test
    @DisplayName("지하철 노선의 정보 변경 테스트")
    void addStations() {
        Stations 지하철역_정보 = new Stations();

        지하철역_정보.add(강남역);
        지하철역_정보.add(역삼역);

        Assertions.assertAll(
                () -> assertThat(지하철역_정보.getStations()).size().isEqualTo(2),
                () -> assertThat(지하철역_정보.getStations()).contains(강남역, 역삼역)
        );
    }
}
