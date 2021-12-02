package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineFixtures.이호선;
import static nextstep.subway.line.domain.StationFixtures.잠실;
import static nextstep.subway.line.domain.StationFixtures.잠실나루;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    @Test
    @DisplayName("구간 추가 후 구간 갯수 검증")
    void add() {
        // given
        Section section = Section.of(이호선, 잠실, 잠실나루, 100);
        Sections sections = Sections.of();

        sections.add(section);

        assertThat(sections.count()).isEqualTo(1);
    }

}
