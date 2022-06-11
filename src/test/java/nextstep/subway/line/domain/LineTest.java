package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

public class LineTest {
    @Test
    void addSection() {
        Station upStation = new Station("공덕역");
        Station downStation = new Station("마포역");
        Line line = new Line("오호선", "purple");

        line.addSection(new Section(line, upStation, downStation, 10));
        List<Section> sections = line.getSections();

        assertThat(sections.size()).isEqualTo(1);
    }
}
