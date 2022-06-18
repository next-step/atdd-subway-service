package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SectionsTest {
    final String givenLineName = "신분당선";
    final String givenLineColor = "red";
    final String givenUpStationName = "강남역";
    final String givenDownStationName = "판교역";
    final int givenLineDistance = 30;

    Line givenLine;
    Station givenUpStation;
    Station givenDownStation;
    Sections givenSections;

    @BeforeEach
    void setUp() {
        givenUpStation = new Station(givenUpStationName);
        givenDownStation = new Station(givenDownStationName);
        givenLine = new Line(givenLineName, givenLineColor);
        final List<Section> sectionList = new ArrayList<>();
        sectionList.add(new Section(givenLine, givenUpStation, givenDownStation, givenLineDistance));
        givenSections = new Sections(sectionList);
    }

    @Test
    void 등록된_역_목록을_조회할_수_있어야_한다() {
        // when
        final List<Station> stations = givenSections.getStations();

        // then
        assertThat(stations).containsExactly(givenUpStation, givenDownStation);
    }

    @Test
    void 등록된_역이_없으면_역_목록_조회_시_빈_목록이_반환되어야_한다() {
        // given
        final Sections emptySections = new Sections();

        // when
        final List<Station> stations = emptySections.getStations();

        // then
        assertThat(stations.isEmpty()).isTrue();
    }

    @Test
    void 두_역이_이미_등록된_구간을_추가하면_에러가_발생해야_한다() {
        // when and then
        assertThatThrownBy(() -> givenSections.add(givenLine, givenUpStation, givenDownStation, givenLineDistance / 2))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 두_역이_모두_등록되지_않은_구간을_추가하면_에러가_발생해야_한다() {
        // when and then
        assertThatThrownBy(() -> givenSections.add(givenLine, givenUpStation, givenDownStation, givenLineDistance / 2))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 빈_노선에_구간을_추가할_수_있어야_한다() {
        // given
        final Sections emptySections = new Sections();

        // when
        emptySections.add(new Line(), givenUpStation, givenDownStation, givenLineDistance);

        // then
        final List<Station> stations = givenSections.getStations();
        assertThat(stations).containsExactly(givenUpStation, givenDownStation);
    }

    @Test
    void 하행역이_신규_역인_구간을_추가할_수_있어야_한다() {
        // given
        final Station newDownStation = new Station("양재역");

        // when
        givenSections.add(givenLine, givenUpStation, newDownStation, givenLineDistance / 2);

        // then
        final List<Station> stations = givenSections.getStations();
        assertThat(stations).containsExactly(givenUpStation, newDownStation, givenDownStation);
    }

    @Test
    void 상행역이_신규_역인_구간을_추가할_수_있어야_한다() {
        // given
        final Station newUpStation = new Station("양재역");

        // when
        givenSections.add(givenLine, newUpStation, givenUpStation, givenLineDistance / 2);

        // then
        final List<Station> stations = givenSections.getStations();
        assertThat(stations).containsExactly(newUpStation, givenUpStation, givenDownStation);
    }

    @Test
    void 구간_추가_시_신규_구간이_삽입되는_기존_구간보다_거리가_짧지_않으면_에러가_발생해야_한다() {
        // given
        final Station newDownStation = new Station("양재역");

        // when and then
        assertThatThrownBy(() -> givenSections.add(givenLine, givenUpStation, newDownStation, givenLineDistance));
    }
}
