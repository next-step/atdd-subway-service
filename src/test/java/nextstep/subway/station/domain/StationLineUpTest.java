package nextstep.subway.station.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("지하철역 목록 일급 컬렉션 테스트")
class StationLineUpTest {

    @DisplayName("생성 성공")
    @Test
    void create_stationLineUp_success() {
        assertThatNoException().isThrownBy(() -> new StationLineUp(new ArrayList<>()));
    }

    @DisplayName("지하철역 목록에 포함되는 역 검사")
    @Test
    void stationExisted_station_success() {
        //givne:
        Station 삼전역 = Station.from("삼전역");
        Station 잠실역 = Station.from("잠실역");
        StationLineUp stationLineUp = new StationLineUp(Arrays.asList(삼전역, 잠실역));

        //when, then:
        assertThat(stationLineUp.stationExisted(삼전역)).isTrue();
    }

    @DisplayName("지하철역 목록에 포함되지 않는 역 검사")
    @Test
    void unKnownStation_station_success() {
        //givne:
        Station 삼전역 = Station.from("삼전역");
        Station 잠실역 = Station.from("잠실역");
        Station 목록에_포함되지_않는_역 = Station.from("목록에 포함되지 않는 역");
        StationLineUp stationLineUp = new StationLineUp(Arrays.asList(삼전역, 잠실역));

        //when, then:
        assertThat(stationLineUp.unKnownStation(목록에_포함되지_않는_역)).isTrue();
    }
}
