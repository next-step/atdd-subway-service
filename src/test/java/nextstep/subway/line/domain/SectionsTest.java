package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {

    Line 신분당선;
    Sections 모든구간;
    Station 광교역;
    Station 정자역;
    Station 강남역;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "bg-red-600");
        모든구간 = new Sections();
        광교역 = new Station("광교역");
        정자역 = new Station("정자역");
        강남역 = new Station("강남역");
    }

    @Test
    @DisplayName("지하철역을 구간 순으로 정렬한다.")
    void getStationsInOrder() {
        모든구간.addLineStation(new Section(신분당선, 정자역, 강남역, 20000));
        모든구간.addLineStation(new Section(신분당선, 광교역, 정자역, 10000));

        assertThat(모든구간.getStations())
                .containsExactly(광교역, 정자역, 강남역);
    }

}
