package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.exception.SectionsNotRemovedException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {
    Line 신분당선;
    Station 강남역;
    Station 판교역;
    Station 정자역;
    Section 판교_정자;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "bg-red-100");
        강남역 = new Station("강남역");
        판교역 = new Station("판교역");
        정자역 = new Station("졍자역");
        판교_정자 = new Section(신분당선, 판교역, 정자역, 3);
    }

    @Test
    @DisplayName("초기 구간 등록 확인")
    void addSectionEmpty() {
        Sections sections = new Sections();

        sections.requestAddSection(판교_정자);
        assertThat(sections.getSections()).contains(판교_정자);
    }

    @Test
    @DisplayName("구간 아래역 일치 정상 등록 확인")
    void addSection() {
        Stations stations = new Stations();
        stations.add(강남역);
        stations.add(판교역);

        Sections sections = new Sections();

        sections.requestAddSection(판교_정자);
        assertThat(sections.getSections()).contains(판교_정자);
    }

    @Test
    @DisplayName("등록된 구간 유일할 시 구간 삭제 못함")
    void removeSectionOnlySection() {
        Stations stations = new Stations();
        Sections sections = new Sections();
        Section 강남_판교 = new Section(신분당선, 강남역, 판교역, 10);
        sections.add(강남_판교);

        stations.add(강남역);
        stations.add(판교역);

        Assertions.assertThatThrownBy(() -> sections.removeLineStation(판교역, 신분당선)).isInstanceOf(
                SectionsNotRemovedException.class);
    }

    @Test
    @DisplayName("상행역 하행역 모두 존재시 구간 정상 삭제 처리")
    void removeLineStation() {
        Sections sections = new Sections();
        Section 강남_판교 = new Section(신분당선, 강남역, 판교역, 10);

        sections.requestAddSection(강남_판교);

        sections.requestAddSection(판교_정자);

        sections.removeLineStation(정자역, 신분당선);
        assertThat(sections.getSections()).doesNotContain(판교_정자);
        assertThat(sections.getSections()).contains(강남_판교);
    }
}