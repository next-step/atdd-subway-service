package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class SectionsTest {

    @DisplayName("구간을 추가하자(구간이 처음추가될때)")
    @Test
    void addFirstSection() {
        Station 강남역 = new Station("강남역");
        Station 잠실역 = new Station("잠실역");

        Sections sections = new Sections();
        sections.addFirstSection(new Section(강남역, 잠실역, 100));

        assertFalse(sections.isEmptySections());
        assertThat(sections.getStations()).containsExactly(강남역, 잠실역);
    }

    @DisplayName("구간을 추가하자(구간이 추가될때)")
    @Test
    void addSection() {
        Station 강남역 = new Station("강남역");
        Station 삼성역 = new Station("삼성역");
        Station 잠실역 = new Station("잠실역");

        Sections sections = new Sections();
        sections.addFirstSection(new Section(강남역, 잠실역, 100));

        sections.addSection(new Section(강남역, 삼성역, 40));

        assertFalse(sections.isEmptySections());
        assertThat(sections.getStations()).containsExactly(강남역, 삼성역, 잠실역);
    }

    @DisplayName("존재하지않는 구간은 등록할수 없음")
    @Test
    void unableAddSectionNotExist() {
        Station 강남역 = new Station("강남역");
        Station 삼성역 = new Station("삼성역");
        Station 종합운동장역 = new Station("종합운동장역");
        Station 잠실역 = new Station("잠실역");

        Sections sections = new Sections();
        sections.addFirstSection(new Section(강남역, 잠실역, 100));

        assertThatThrownBy(
                () -> sections.addSection(new Section(삼성역, 종합운동장역, 40))
        ).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("이미 등록된 구간은 등록할수 없음")
    @Test
    void unableAddSectionAlreadyRegist() {
        Station 강남역 = new Station("강남역");

        Station 잠실역 = new Station("잠실역");

        Sections sections = new Sections();
        sections.addFirstSection(new Section(강남역, 잠실역, 100));

        assertThatThrownBy(
                () -> sections.addSection(new Section(강남역, 잠실역, 40))
        ).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("구간에서 역을 삭제하자")
    @Test
    void removeStation() {
        Station 강남역 = new Station("강남역");
        Station 삼성역 = new Station("삼성역");
        Station 잠실역 = new Station("잠실역");

        Sections sections = new Sections();
        sections.addFirstSection(new Section(강남역, 잠실역, 100));
        sections.addSection(new Section(강남역, 삼성역, 40));

        sections.removeLineStation(삼성역);
        assertFalse(sections.isEmptySections());
        assertThat(sections.getStations()).containsExactly(강남역, 잠실역);
    }

    @DisplayName("구간이 한개밖에 존재하지 않으면 삭제할수 없음")
    @Test
    void removeFailStation() {
        Station 강남역 = new Station("강남역");
        Station 잠실역 = new Station("잠실역");

        Sections sections = new Sections();
        sections.addFirstSection(new Section(강남역, 잠실역, 100));

        assertThatThrownBy(
                () -> sections.removeLineStation(잠실역)
        ).isInstanceOf(RuntimeException.class);
    }
}