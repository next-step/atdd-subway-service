package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsUnitTest {
    private Station 청담역;
    private Station 건대입구역;
    private Station 어린이대공원역;
    private Line 칠호선;

    @BeforeEach
    void init() {
        청담역 = new Station("청담역");
        건대입구역 = new Station("건대입구역");
        어린이대공원역 = new Station("어린이대공원역");

        칠호선 = new Line("칠호선", "yellow", 청담역, 건대입구역, 20);
    }

    @Test
    @DisplayName("노선에 포함된 역 리스트를 조회한다.")
    void getStations() {
        //given
        칠호선.addLineStation(건대입구역, 어린이대공원역, 10);
        Sections sections = 칠호선.getSections();

        //then
        assertThat(sections.getStations()).containsExactly(청담역, 건대입구역, 어린이대공원역);
    }

    @Test
    @DisplayName("노선에 포함된 가장 상행 역을 조회한다.")
    void findUpStation() {
        //given
        칠호선.addLineStation(건대입구역, 어린이대공원역, 10);
        Sections sections = 칠호선.getSections();

        //then
        assertThat(sections.findUpStation()).isEqualTo(청담역);
    }

    @Test
    @DisplayName("새로운 구간을 추가한다.")
    void addSection() {
        //when
        칠호선.getSections().addSection(칠호선, 건대입구역, 어린이대공원역, 10);

        //then
        assertThat(칠호선.getStations()).contains(청담역, 건대입구역, 어린이대공원역);
    }

    @Test
    @DisplayName("가장 상행역을 제거한다.")
    void removeSectionUpStation() {
        //given
        칠호선.addLineStation(건대입구역, 어린이대공원역, 10);

        //when
        칠호선.getSections().removeSection(칠호선, 청담역);

        //then
        assertThat(칠호선.getStations()).containsExactly(건대입구역, 어린이대공원역);
        assertThat(칠호선.getStations()).doesNotContain(청담역);
    }

    @Test
    @DisplayName("노선의 가장 상행역을 제거한다.")
    void removeSectionMiddleStation() {
        //given
        칠호선.addLineStation(건대입구역, 어린이대공원역, 10);

        //when
        칠호선.getSections().removeSection(칠호선, 어린이대공원역);

        //then
        assertThat(칠호선.getStations()).containsExactly(청담역, 건대입구역);
        assertThat(칠호선.getStations()).doesNotContain(어린이대공원역);
    }

    @Test
    @DisplayName("노선의 중간역을 제거한다.")
    void removeSectionDownStation() {
        //given
        칠호선.addLineStation(건대입구역, 어린이대공원역, 10);

        //when
        칠호선.getSections().removeSection(칠호선, 건대입구역);

        //then
        assertThat(칠호선.getStations()).containsExactly(청담역, 어린이대공원역);
        assertThat(칠호선.getStations()).doesNotContain(건대입구역);
    }
}
