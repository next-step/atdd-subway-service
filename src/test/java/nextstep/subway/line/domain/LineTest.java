package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class LineTest {

    private Station 삼성역;
    private Station 교대역;
    private Station 선릉역;
    private Station 잠실역;
    private Line 호선2;

    @BeforeEach
    void setup() {
        삼성역 = new Station(1L, "삼성역");
        교대역 = new Station(2L, "교대역");
        선릉역 = new Station(3L, "선릉역");
        잠실역 = new Station(4L, "잠실역");
        호선2 = new Line();
        호선2.getSections().add(new Section(호선2, 교대역, 삼성역, 20));
        호선2.getSections().add(new Section(호선2, 삼성역, 선릉역, 60));
        호선2.getSections().add(new Section(호선2, 선릉역, 잠실역, 100));

    }

    @Test
    @DisplayName("stations 상행 > 하행선으로 가져오는 것 테스트")
    void getStationsTest() {
        List<StationResponse> stationList = 호선2.getStations();
        assertThat(getStationId(stationList)).containsExactly(교대역.getId(), 삼성역.getId(), 선릉역.getId(), 잠실역.getId());
    }

    private List<Long> getStationId(List<StationResponse> stationList) {
        return stationList.stream()
            .map(response -> response.getId())
            .collect(Collectors.toList());
    }

}
