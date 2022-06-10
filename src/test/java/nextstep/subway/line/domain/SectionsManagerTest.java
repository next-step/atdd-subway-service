package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SectionsManagerTest {

    SectionsManager sectionsManager;
    Station station1;
    Station station2;
    Station station3;
    Line line;

    @BeforeEach
    public void init() {
        sectionsManager = new SectionsManager();
        line = new Line("테스트노선", "테스트색깔");
        station1 = new Station("1");
        station2 = new Station("2");
        station3 = new Station("3");

        sectionsManager.add(new Section(line, station3, station1, 10));
    }

    @Test
    public void 구간찾기_윗정거장_기반() {
        //when
        Optional<Section> sectionByUpStation1 = sectionsManager.getSectionByUpStation(station1);
        Optional<Section> sectionByUpStation2 = sectionsManager.getSectionByUpStation(station2);
        Optional<Section> sectionByUpStation3 = sectionsManager.getSectionByUpStation(station3);

        //then
        assertAll(
            () -> assertThat(sectionByUpStation1.isPresent()).isFalse(),
            () -> assertThat(sectionByUpStation2.isPresent()).isFalse(),
            () -> assertThat(sectionByUpStation3.isPresent()).isTrue()
        );
    }

    @Test
    public void 구간찾기_아래정거장_기반() {
        //when
        Optional<Section> sectionByDownStation1 = sectionsManager.getSectionByDownStation(station1);
        Optional<Section> sectionByDownStation2 = sectionsManager.getSectionByDownStation(station2);
        Optional<Section> sectionByDownStation3 = sectionsManager.getSectionByDownStation(station3);

        //then
        assertAll(
            () -> assertThat(sectionByDownStation1.isPresent()).isTrue(),
            () -> assertThat(sectionByDownStation2.isPresent()).isFalse(),
            () -> assertThat(sectionByDownStation3.isPresent()).isFalse()
        );
    }

    @Test
    public void 순서대로_역_받아오기() {
        //when
        List<Station> stationsOrdered = sectionsManager.getStationsOrdered(station3);

        //then
        assertThat(stationsOrdered).extracting("name").containsExactly("3", "1");
    }

    @Test
    public void 가장앞에_역_더하기() {
        //given
        sectionsManager.addStation(station1, station2, 10, line, station3);

        //when
        List<Station> stationsOrdered = sectionsManager.getStationsOrdered(station3);

        //then
        assertThat(stationsOrdered).extracting("name").containsExactly("3", "1", "2");

    }

    @Test
    public void 가장뒤에_역_더하기() {
        //given
        sectionsManager.addStation(station2, station3, 10, line, station3);

        //when
        List<Station> stationsOrdered = sectionsManager.getStationsOrdered(station2);

        //then
        assertThat(stationsOrdered).extracting("name").containsExactly("2", "3", "1");
    }

    @Test
    public void 중간에_역_더하기() {
        //given
        sectionsManager.addStation(station3, station2, 3, line, station3);

        //when
        List<Station> stationsOrdered = sectionsManager.getStationsOrdered(station3);

        //then
        assertThat(stationsOrdered).extracting("name").containsExactly("3", "2", "1");
    }

    @Test
    public void 중간에_역_더하기_거리초과시_에러발생() {
        assertThatThrownBy(() -> sectionsManager.addStation(station3, station2, 10, line, station3))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void 역_삭제하기() {
        //given
        sectionsManager.addStation(station3, station2, 3, line, station3);

        //when
        sectionsManager.removeSectionWithStation(station2);
        List<Station> stationsOrdered = sectionsManager.getStationsOrdered(station3);

        //then
        assertThat(stationsOrdered).extracting("name").containsExactly("3", "1");
    }

    @Test
    public void 구간_한개일때_역_삭제_불가() {
        assertThatThrownBy(() -> sectionsManager.removeSectionWithStation(station3))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void 없는_역_삭제불가() {
        assertThatThrownBy(() -> sectionsManager.removeSectionWithStation(station2))
            .isInstanceOf(RuntimeException.class);
    }
}
