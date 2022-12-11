package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineTest {

    @Test
    @DisplayName("Line을 수정한다.")
    void updateLine() {
        // given
        Station 상행종점역 = new Station("상행종점역");
        Station 하행종점역 = new Station("하행종점역");
        Line originLine = new Line("기본라인","red",상행종점역, 하행종점역, 10);
        Line updateLine = new Line("새로운라인","blue");

        // when
        originLine.update(updateLine);

        // then
        assertAll(
                () -> assertThat(originLine.getName()).isEqualTo("새로운라인"),
                () -> assertThat(originLine.getColor()).isEqualTo("blue")
        );
    }

    @Test
    @DisplayName("Line 내 stations 을 조회한다.")
    void getStationsFromLine() {
        // given
        Station 상행종점역 = new Station("상행종점역");
        Station 하행종점역 = new Station("하행종점역");

        // when
        Line originLine = new Line("기본라인","red",상행종점역, 하행종점역, 10);

        // then
        assertThat(originLine.getStations()).containsExactly(상행종점역, 하행종점역);
    }

    @Test
    @DisplayName("Line에 Section을 추가한다.")
    void addSectionLine() {
        // given
        Station 상행종점역 = new Station("상행종점역");
        Station 하행종점역 = new Station("하행종점역");
        Station 새로운역 = new Station("새로운역");
        Line originLine = new Line("기본라인","red",상행종점역, 하행종점역, 10);
        Section section = new Section(originLine, 상행종점역, 새로운역, 8);

        // when
        originLine.addSection(section);

        // then
        assertThat(originLine.getStations()).containsExactly(상행종점역, 새로운역, 하행종점역);
    }

    @Test
    @DisplayName("Line에 Station을 삭제한다.")
    void removeStationFromLine() {
        // given
        Station 상행종점역 = new Station("상행종점역");
        Station 하행종점역 = new Station("하행종점역");
        Station 새로운역 = new Station("새로운역");
        Line originLine = new Line("기본라인","red",상행종점역, 하행종점역, 10);
        Section section = new Section(originLine, 상행종점역, 새로운역, 8);
        originLine.addSection(section);

        // when
        originLine.removeStation(상행종점역);

        // then
        assertThat(originLine.getStations()).containsExactly(새로운역, 하행종점역);
    }

}
