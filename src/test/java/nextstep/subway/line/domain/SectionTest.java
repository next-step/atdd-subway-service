package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionTest {

    @DisplayName("구간에서 해당 역이 반대편 역인지 확인")
    @Test
    void 반대편_역_여부() {
        Station 상행역 = new Station("강남역");
        Station 하행역 = new Station("양재역");
        Line line = new Line("신분당선", "red");

        Section section = new Section(line, 상행역, 하행역, 10);

        assertAll(
                () -> assertThat(section.isStationOppositeOf(상행역, StationPosition.DOWN_STATION)).isTrue(),
                () -> assertThat(section.isStationOppositeOf(상행역, StationPosition.UP_STATION)).isFalse(),
                () -> assertThat(section.isStationOppositeOf(하행역, StationPosition.UP_STATION)).isTrue(),
                () -> assertThat(section.isStationOppositeOf(하행역, StationPosition.DOWN_STATION)).isFalse()
        );
    }

    @DisplayName("구간에서 해당 위치의 지하철역 찾기")
    @Test
    void 위치로_역_찾기() {
        Station 상행역 = new Station("강남역");
        Station 하행역 = new Station("양재역");
        Line line = new Line("신분당선", "red");

        Section section = new Section(line, 상행역, 하행역, 10);
        Station 찾은_상행역 = section.getStationByPosition(StationPosition.UP_STATION);
        Station 찾은_하행역 = section.getStationByPosition(StationPosition.DOWN_STATION);

        assertAll(
                () -> assertThat(찾은_상행역).isEqualTo(상행역),
                () -> assertThat(찾은_하행역).isEqualTo(하행역)
        );
    }
}
