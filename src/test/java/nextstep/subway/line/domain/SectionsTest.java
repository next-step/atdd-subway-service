package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class SectionsTest {
    // given
    Line 노선;
    Station 강남;
    Station 역삼;
    Station 선릉;
    Station 잠실나루;
    Station 잠실;

    @BeforeEach
    void setUp() {
        // given
        노선 = new Line("2호선", "green");
        강남 = new Station("강남");
        역삼 = new Station("역삼");
        선릉 = new Station("선릉");
        잠실나루 = new Station("잠실나루");
        잠실 = new Station("잠실");
    }

    @Test
    @DisplayName("노선에 여러 구간을 등록하고 항상 정렬 된 목록을 받을 수 있다.")
    public void addManySectionAndAlwaysSortedList() throws Exception {
        // when
        노선.addLineStation(잠실, 선릉, 10);
        노선.addLineStation(잠실, 잠실나루, 5);
        노선.addLineStation(선릉, 역삼, 10);
        노선.addLineStation(역삼, 강남, 10);

        // then
        List<Station> stations = 노선.getStations();
        Assertions.assertThat(stations).containsExactly(잠실, 잠실나루, 선릉, 역삼, 강남);
    }

    @Test
    @DisplayName("노선에 역을 제거할 수 있다.")
    public void stationRemoveSuccess() throws Exception {
        // given
        노선.addLineStation(잠실, 선릉, 10);
        노선.addLineStation(잠실, 잠실나루, 5);
        노선.addLineStation(선릉, 역삼, 10);
        노선.addLineStation(역삼, 강남, 10);

        // when
        노선.removeStation(잠실나루);
        노선.removeStation(강남);

        // then
        List<Station> stations = 노선.getStations();
        Assertions.assertThat(stations).containsExactly(잠실, 선릉, 역삼);
    }

    @Test
    @DisplayName("노선에 등록되어 있지 않은 역을 제거할 때 제거할 수 없다.")
    public void canNotRemoveStationInOneSection() throws Exception {
        // given
        노선.addLineStation(잠실, 선릉, 10);
        노선.addLineStation(잠실, 잠실나루, 5);
        노선.addLineStation(선릉, 역삼, 10);

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 노선.removeStation(강남))
                .withMessageMatching("제거 할 역이 노선에 없습니다.");
    }

    @Test
    @DisplayName("구간이 하나인 노선에서 마지막 구간을 제거할 때 제거할 수 없다.")
    public void canNotRemoveUnregisteredStation() throws Exception {
        // given
        노선.addLineStation(잠실, 선릉, 10);

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 노선.removeStation(선릉))
                .withMessageMatching("구간에서 역을 제거할 수 없습니다.");
    }
}
