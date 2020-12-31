package nextstep.subway.line.domain;

import nextstep.subway.line.domain.exceptions.InvalidMergeSectionException;
import nextstep.subway.station.domain.StationFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionTest {
    @DisplayName("Section 두개를 합칠 수 있다.")
    @Test
    void mergeByTwoSectionsTest() {
        Line line = new Line("2호선", "초록색");
        Section section1 = new Section(line, StationFixtures.삼성역, StationFixtures.강남역, 5);
        Section section2 = new Section(line, StationFixtures.잠실역, StationFixtures.삼성역, 5);

        assertThat(Section.mergeByTwoSections(section1, section2).getUpStation()).isEqualTo(StationFixtures.잠실역);
        assertThat(Section.mergeByTwoSections(section1, section2).getDownStation()).isEqualTo(StationFixtures.강남역);
        assertThat(Section.mergeByTwoSections(section1, section2).getDistance()).isEqualTo(new Distance(10));
    }

    @DisplayName("서로 다른 라인에 속한 Section끼리 합칠 수 없다.")
    @Test
    void mergeByTwoSectionFailWithDifferentLinesTest() {
        Line lineNumberTwo = new Line("2호선", "초록색");
        Line lineNumberOne = new Line("1호선", "파란색");
        Section section1 = new Section(lineNumberOne, StationFixtures.역삼역, StationFixtures.삼성역, 5);
        Section section2 = new Section(lineNumberTwo, StationFixtures.삼성역, StationFixtures.잠실역, 5);

        assertThatThrownBy(() -> Section.mergeByTwoSections(section1, section2))
                .isInstanceOf(InvalidMergeSectionException.class)
                .hasMessage("서로 다른 노선에 있는 구간끼리 병합할 수 없습니다.");
    }

    @DisplayName("겹치는 역이 없는 구간끼리 합칠 수 없다.")
    @Test
    void mergeByTwoSectionsFailWithoutConneciton() {
        Line line = new Line("2호선", "초록색");
        Section section1 = new Section(line, StationFixtures.삼성역, StationFixtures.강남역, 5);
        Section section2 = new Section(line, StationFixtures.잠실역, StationFixtures.역삼역, 5);

        assertThatThrownBy(() -> Section.mergeByTwoSections(section1, section2))
                .isInstanceOf(InvalidMergeSectionException.class)
                .hasMessage("겹치는 역이 없는 구간끼리 병합할 수 없습니다.");
    }
}