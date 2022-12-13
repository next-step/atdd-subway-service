package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionsTest {

    private Station 강남역;
    private Station 양재역;
    private Station 정자역;
    private Line 신분당선;
    private Section 구간;
    private Sections 구간목록;

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        정자역 = new Station("정자역");
        신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10);
        구간 = new Section(신분당선, 양재역, 정자역, 12);
        신분당선.getSections().add(구간);
        구간목록 = 신분당선.getSections();
    }

    @DisplayName("지정위치에 따라 다음역 찾기")
    @Test
    void 다음역_찾기() {
        assertAll(
                () -> assertThat(구간목록.nextStationOf(강남역, StationPosition.DOWN_STATION)).isEqualTo(양재역),
                () -> assertThat(구간목록.nextStationOf(양재역, StationPosition.DOWN_STATION)).isEqualTo(정자역),
                () -> assertThat(구간목록.nextStationOf(정자역, StationPosition.UP_STATION)).isEqualTo(양재역),
                () -> assertThat(구간목록.nextStationOf(양재역, StationPosition.UP_STATION)).isEqualTo(강남역)
        );
    }

    @DisplayName("상행종점 찾기")
    @Test
    void 상행종점_찾기() {
        Station 신논현역 = new Station("신논현역");
        Section 구간3 = new Section(신분당선, 신논현역, 강남역, 10);
        구간목록.add(구간3);

        assertThat(구간목록.findFinalUpStation()).isEqualTo(신논현역);
    }
}
