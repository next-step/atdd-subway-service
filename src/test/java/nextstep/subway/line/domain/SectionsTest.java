package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class SectionsTest {
    // given
    Line 노선;
    Station 신림;
    Station 봉천;
    Station 서울대입구;
    Station 낙성대;
    Station 사당;

    @BeforeEach
    void setUp() {
        // given
        신림 = new Station("신림");
        봉천 = new Station("봉천");
        서울대입구 = new Station("서울대입구");
        낙성대 = new Station("낙성대");
        사당 = new Station("사당");
        노선 = new Line("2호선", "green", 사당, 서울대입구, 10);
    }

    @Test
    @DisplayName("노선에 여러 구간을 등록하고 항상 정렬 된 목록을 받을 수 있다.")
    public void addManySectionAndAlwaysSortedList() throws Exception {
        // when
        노선.addLineStation(사당, 낙성대, 5);
        노선.addLineStation(서울대입구, 봉천, 10);
        노선.addLineStation(봉천, 신림, 10);

        // then
        List<Station> stations = 노선.getStations();
        Assertions.assertThat(stations).containsExactly(사당, 낙성대, 서울대입구, 봉천, 신림);
    }

    @Test
    @DisplayName("노선에 역을 제거할 수 있다.")
    public void stationRemoveSuccess() throws Exception {
        // given
        노선.addLineStation(사당, 낙성대, 5);
        노선.addLineStation(서울대입구, 봉천, 10);
        노선.addLineStation(봉천, 신림, 10);

        // when
        노선.removeStation(낙성대);
        노선.removeStation(신림);

        // then
        List<Station> stations = 노선.getStations();
        Assertions.assertThat(stations).containsExactly(사당, 서울대입구, 봉천);
    }

    @Test
    @DisplayName("노선에 등록되어 있지 않은 역을 제거할 때 제거할 수 없다.")
    public void canNotRemoveStationInOneSection() throws Exception {
        // given
        노선.addLineStation(사당, 낙성대, 5);
        노선.addLineStation(서울대입구, 봉천, 10);

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 노선.removeStation(신림))
                .withMessageMatching("제거 할 역이 노선에 없습니다.");
    }

    @Test
    @DisplayName("구간이 하나인 노선에서 마지막 구간을 제거할 때 제거할 수 없다.")
    public void canNotRemoveUnregisteredStation() throws Exception {
        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 노선.removeStation(서울대입구))
                .withMessageMatching("구간에서 역을 제거할 수 없습니다.");
    }
}
