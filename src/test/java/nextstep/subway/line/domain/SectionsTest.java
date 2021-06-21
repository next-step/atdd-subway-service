package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class SectionsTest {

    private Station 삼성역;
    private Station 교대역;
    private Station 선릉역;
    private Station 잠실역;
    private Station 건대입구;
    private Station 강변역;
    private Line 호선2;

    @BeforeEach
    void setup() {
        삼성역 = new Station(1L, "삼성역");
        교대역 = new Station(2L, "교대역");
        선릉역 = new Station(3L, "선릉역");
        잠실역 = new Station(4L, "잠실역");
        건대입구 = new Station(5L, "건대입구");
        강변역 = new Station(6L, "강변역");
        호선2 = new Line("2호선", "초록", 교대역, 잠실역, 180);
        호선2.addSection(new Section(호선2, 교대역, 삼성역, 20));
        호선2.addSection(new Section(호선2, 삼성역, 선릉역, 60));
    }

    @Test
    @DisplayName("line에 속하는 station을 상행 > 하행선순으로 순차적으로 가져온다.")
    void getStationsTest() {
        List<StationResponse> stationList = 호선2.getStationsResponse();
        assertThat(getStationId(stationList)).containsExactly(교대역.getId(), 삼성역.getId(), 선릉역.getId(), 잠실역.getId());
    }

    @Test
    @DisplayName("line에 하나의 구간을 추가한다.")
    void addSecctionTest() {
        호선2.addSection(new Section(호선2, 잠실역, 건대입구, 100));
        호선2.addSection(new Section(호선2, 잠실역, 강변역, 80));
        List<StationResponse> stationList = 호선2.getStationsResponse();
        assertThat(getStationId(stationList)).containsExactly(교대역.getId(), 삼성역.getId(), 선릉역.getId(), 잠실역.getId(),
            강변역.getId(), 건대입구.getId());
    }

    @Test
    @DisplayName("line에 없거나 이미 등록된 역을 등록하면 에러가 발생한다.")
    void addSecctionExcetionTest() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            호선2.addSection(new Section(호선2, 강변역, 건대입구, 100));
        });
        assertThat(exception.getMessage()).isEqualTo("등록할 수 없는 구간 입니다.");

        exception = assertThrows(RuntimeException.class, () -> {
            호선2.addSection(new Section(호선2, 교대역, 삼성역, 100));
        });
        assertThat(exception.getMessage()).isEqualTo("이미 등록된 구간 입니다.");
    }

    @Test
    @DisplayName("교대역-삼성역-선릉역-잠실역-건대입구 중 교대역 > 선릉역 > 건대입구  순으로 삭제 시 삭제 후 순서로 노출되어야 한다.")
    void removeStationTest() {
        호선2.addSection(new Section(호선2, 잠실역, 건대입구, 10));
        호선2.removeStation(교대역);
        List<StationResponse> stationList = 호선2.getStationsResponse();
        assertThat(getStationId(stationList)).containsExactly(삼성역.getId(), 선릉역.getId(), 잠실역.getId(), 건대입구.getId());
        호선2.removeStation(선릉역);
        stationList = 호선2.getStationsResponse();
        assertThat(getStationId(stationList)).containsExactly(삼성역.getId(), 잠실역.getId(), 건대입구.getId());
        호선2.removeStation(건대입구);
        stationList = 호선2.getStationsResponse();
        assertThat(getStationId(stationList)).containsExactly(삼성역.getId(), 잠실역.getId());

    }

    @Test
    @DisplayName("구간이 하나 밖에 없는데 삭제를 요청 시 Runtime에러를 뱉는다.")
    void removeNoSections() {
        Line 신분당선 = new Line();
        assertThrows(RuntimeException.class, () -> {
            신분당선.removeStation(삼성역);
        });
    }

    @Test
    @DisplayName("교대역-삼성역-선릉역-잠실역 중 삼성역 삭제 시 두 구간을 연결해야 한다.")
    void removeStationCheckDistanceTest() {
        호선2.removeStation(삼성역);
        assertThat(호선2.getDistanceBetweenStations(교대역, 선릉역)).isEqualTo(80);
    }

    @Test
    @DisplayName("두 구간 사이의 거리를 구한다.")
    void getDistanceBetweenStationsTest() {
        assertThat(호선2.getDistanceBetweenStations(삼성역, 잠실역)).isEqualTo(160);
    }

    private List<Long> getStationId(List<StationResponse> stationList) {
        return stationList.stream()
            .map(response -> response.getId())
            .collect(Collectors.toList());
    }

}
