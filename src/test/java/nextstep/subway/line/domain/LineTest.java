package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.StationFixtures.잠실;
import static nextstep.subway.line.domain.StationFixtures.잠실나루;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LineTest {

    @Test
    void update() {
        // given
        Line line = new Line("2호선", "RED");
        Line updateLine = new Line("3호선", "YELLOW");

        // when
        line.update(updateLine);

        // then
        assertThat(line.sameNameAndColor(updateLine)).isTrue();
    }

    @Test
    void addSection() {
        // given
        Line line = new Line("2호선", "RED");
        Section addSection = new Section(line, 잠실, 잠실나루, 100);

        // when
        line.addSection(addSection);

        // then
        assertThat(line.sectionsCount()).isEqualTo(1);
    }


}
