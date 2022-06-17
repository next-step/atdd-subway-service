package nextstep.subway.line.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LineTest {
    final String givenLineName = "신분당선";
    final String givenLineColor = "red";
    final String givenUpStationName = "강남역";
    final String givenDownStationName = "판교역";
    final int givenLineDistance = 30;

    Line givenLine;
    Station givenUpStation;
    Station givenDownStation;

    @BeforeEach
    void setUp() {
        givenUpStation = new Station(givenUpStationName);
        givenDownStation = new Station(givenDownStationName);
        givenLine = new Line(
                givenLineName,
                givenLineColor,
                givenUpStation,
                givenDownStation,
                givenLineDistance);
    }

    @Test
    void 노선에_등록된_역_목록을_조회할_수_있어야_한다() {
        // when
        final List<Station> stations = givenLine.getStations();

        // then
        Assertions.assertThat(stations).containsExactly(givenUpStation, givenDownStation);
    }

    @Test
    void 등록된_역이_없으면_역_목록_조회_시_빈_목록이_반환되어야_한다() {
        // given
        final Line lineWithoutStations = new Line("line without stations", "black");

        // when
        final List<Station> stations = lineWithoutStations.getStations();

        // then
        Assertions.assertThat(stations.isEmpty()).isTrue();
    }
}
