package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LineTest {
    private Line line;

    @BeforeEach
    void setUp() {
        Station upStation = new Station("공덕역");
        Station downStation = new Station("마포역");
        line = new Line("오호선", "purple");
        line.addSection(Section.of(line, upStation, downStation, 10));
    }

    @Test
    @DisplayName("구간을 추가할 수 있다.")
    void addSection() {
        Station upStation = new Station("공덕역");
        Station downStation = new Station("여의나루역");

        line.addSection(Section.of(line, upStation, downStation, 5));
        List<Section> sections = line.getSections();

        assertThat(sections.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("구간을 제거할 수 있다.")
    void removeSection() {
        Station upStation = new Station("마포역");
        Station downStation = new Station("여의나루역");
        line.addSection(Section.of(line, upStation, downStation, 5));

        line.remove(upStation);
        List<Section> sections = line.getSections();

        assertThat(sections.size()).isEqualTo(1);
    }
}
