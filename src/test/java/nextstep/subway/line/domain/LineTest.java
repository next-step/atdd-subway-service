package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import nextstep.subway.station.domain.Station;
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
        assertThat(stations).containsExactly(givenUpStation, givenDownStation);
    }

    @Test
    void 빈_노선이면_역_목록_조회_시_빈_목록이_반환되어야_한다() {
        // given
        final Line emptyLine = new Line("empty line", "black");

        // when
        final List<Station> stations = emptyLine.getStations();

        // then
        assertThat(stations.isEmpty()).isTrue();
    }

    @Test
    void 두_역이_이미_등록된_구간을_추가하면_에러가_발생해야_한다() {
        // when and then
        assertThatThrownBy(() -> givenLine.addSection(givenUpStation, givenDownStation, givenLineDistance / 2))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 두_역이_모두_등록되지_않은_구간을_추가하면_에러가_발생해야_한다() {
        // when and then
        assertThatThrownBy(() -> givenLine.addSection(givenUpStation, givenDownStation, givenLineDistance / 2))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 빈_노선에_구간을_추가할_수_있어야_한다() {
        // given
        final Line emptyLine = new Line("empty line", "black");

        // when
        emptyLine.addSection(givenUpStation, givenDownStation, givenLineDistance);

        // then
        assertThat(givenLine.getStations()).containsExactly(givenUpStation, givenDownStation);
    }

    @Test
    void 하행역이_신규_역인_구간을_추가할_수_있어야_한다() {
        // given
        final Station newDownStation = new Station("양재역");

        // when
        givenLine.addSection(givenUpStation, newDownStation, givenLineDistance / 2);

        // then
        assertThat(givenLine.getStations()).containsExactly(givenUpStation, newDownStation, givenDownStation);
    }

    @Test
    void 상행역이_신규_역인_구간을_추가할_수_있어야_한다() {
        // given
        final Station newUpStation = new Station("양재역");

        // when
        givenLine.addSection(newUpStation, givenDownStation, givenLineDistance / 2);

        // then
        assertThat(givenLine.getStations()).containsExactly(givenUpStation, newUpStation, givenDownStation);
    }

    @Test
    void 구간_추가_시_신규_구간이_삽입되는_기존_구간보다_거리가_짧지_않으면_에러가_발생해야_한다() {
        // given
        final Station newDownStation = new Station("양재역");

        // when and then
        assertThatThrownBy(() -> givenLine.addSection(givenUpStation, newDownStation, givenLineDistance));
    }

    @Test
    void 등록된_역을_삭제할_수_있어야_한다() {
        // given
        final Station newDownStation = new Station("양재역");
        givenLine.addSection(givenUpStation, newDownStation, givenLineDistance / 2);

        // when
        givenLine.removeStation(newDownStation);

        // then
        assertThat(givenLine.getStations()).containsOnly(givenUpStation, givenDownStation);
    }

    @Test
    void 등록된_역이_2개일_때_역을_삭제하면_에러가_발생해야_한다() {
        // when and then
        assertThatThrownBy(() -> givenLine.removeStation(givenUpStation))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 추가요금을_등록할_수_있어야_한다() {
        // given
        final int extraCharge = 100;

        // when
        final Line line = new Line(
                givenLineName,
                givenLineColor,
                givenUpStation,
                givenDownStation,
                givenLineDistance,
                extraCharge);

        // then
        assertThat(line.getExtraCharge()).isEqualTo(extraCharge);
    }

    @Test
    void 추가요금이_0보다_작으면_에러가_발생해야_한다() {
        // when and then
        assertThatThrownBy(
                () -> new Line(givenLineName, givenLineColor, givenUpStation, givenDownStation, givenLineDistance, -1))
                .isInstanceOf(RuntimeException.class);
    }
}
