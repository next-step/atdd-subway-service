package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionTest {

    private Sections sections;
    private Line 이호선;
    private Station 강남역;
    private Station 교대역;
    private Station 서초역;
    private Section section;
    private Station 방배역;

    @BeforeEach
    void setUp() {
        sections = new Sections();
        이호선 = new Line("2호선", "green");
        강남역 = new Station("강남역");
        교대역 = new Station("교대역");
        서초역 = new Station("서초역");
        방배역 = new Station("방배역");
        section = new Section(이호선, 강남역, 교대역, 10);
        sections.add(section);
    }

    @Test
    @DisplayName("구간 사이에 역을 끼워 넣는 경우 상행역을 다음 구간에 연결한다")
    void updateSectionTest1() {
        // given
        Section newSection = new Section(이호선, 강남역, 서초역, 2);

        // when
        section.updateStationBySection(newSection, true);

        assertThat(section.getDistance()).isEqualTo(8);
        assertThat(section.getUpStation()).isSameAs(서초역);
        assertThat(section.getDownStation()).isSameAs(교대역);
    }

    @Test
    @DisplayName("구간 사이에 역을 끼워 넣는 경우 하행역을 이전 구간에 연결한다")
    void updateSectionTest2() {
        // given
        Section newSection = new Section(이호선, 서초역, 교대역, 2);

        // when
        section.updateStationBySection(newSection, false);

        assertThat(section.getDistance()).isEqualTo(8);
        assertThat(section.getUpStation()).isSameAs(강남역);
        assertThat(section.getDownStation()).isSameAs(서초역);
    }
}
