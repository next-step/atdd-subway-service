package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.SectionTest.distance;
import static nextstep.subway.line.domain.SectionTest.gangNam;
import static nextstep.subway.line.domain.SectionTest.panGyo;
import static nextstep.subway.line.domain.SectionTest.twoLine;
import static nextstep.subway.line.domain.SectionTest.yangJae;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {
    private Sections sections;

    @BeforeEach
    void setUp() {
        sections = new Sections();
        Section section = new Section(twoLine, gangNam, yangJae, distance);
        sections.initSection(section);
    }

    @Test
    @DisplayName("초기값 구간이 잘 들어갔는지 확인")
    void initSection() {
        assertThat(sections.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("추가적으로 구간을 넣고 제대로 업데이트 되었는지 확인")
    void addSection() {
        sections.addSection(new Section(twoLine, gangNam, panGyo, Distance.of(3)));

        assertAll(
                () -> assertThat(sections.size()).isEqualTo(2),
                () -> assertThat(sections.getStations()).containsExactly(gangNam, panGyo, yangJae)
        );
    }

    @Test
    @DisplayName("이미 등록된 구간을 또 넣으려고 할 때 에러 발생")
    void addDuplicateSection() {
        sections.addSection(new Section(twoLine, gangNam, panGyo, Distance.of(3)));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> sections.addSection(new Section(twoLine, gangNam, panGyo, Distance.of(3))))
                .withMessage("이미 등록된 구간 입니다.");
    }

    @Test
    @DisplayName("해당 노선에 모두 존재하지 않는 역을 등록하려고 할 때 에러 발생")
    void addNotExistSection() {
        Station guri = new Station("구리역");
        Station jamSil = new Station("잠실역");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> sections.addSection(new Section(twoLine, guri, jamSil, distance)))
                .withMessage("등록할 수 없는 구간 입니다.");
    }

    @Test
    @DisplayName("역을 삭제하고 제대로 업데이트 되었는지 확인")
    void deleteStation() {
        sections.addSection(new Section(twoLine, gangNam, panGyo, Distance.of(3)));
        sections.deleteSection(gangNam);

        assertAll(
                () -> assertThat(sections.size()).isEqualTo(1),
                () -> assertThat(sections.getStations()).containsExactly(panGyo, yangJae)
        );
    }

    @Test
    @DisplayName("유일한 구간인데 삭제하려고 하면 에러 발생")
    void invalidDeleteStation() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> sections.deleteSection(gangNam))
                .withMessage("노선의 구간이 존재하지 않거나 유일한 구간입니다.");
    }
}
