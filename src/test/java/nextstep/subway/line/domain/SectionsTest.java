package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {

    @DisplayName("구간 추가")
    @Test
    void add() {

        Sections sections = new Sections();
        sections.add(SectionFixture.sectionAB());
        sections.add(SectionFixture.sectionBC());

        assertThat(sections.size()).isEqualTo(2);
    }
}
