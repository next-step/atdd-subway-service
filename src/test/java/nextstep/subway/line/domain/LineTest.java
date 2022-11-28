package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

class LineTest {

    Station 강남 = new Station("강남");
    Station 판교 = new Station("판교");
    Station 모란 = new Station("모란");
    Station 서현 = new Station("서현");

    @Test
    void 구간_목록을_가져온다() {
        Line line = new Line("신분당선", "red", 강남, 판교, 10);

        List<Section> sections = line.getSections();

        assertThat(sections).hasSize(1);
    }

    @Test
    void 가장_앞구간_상행역을_가져온다() {
        Line line = new Line("신분당선", "red", 강남, 판교, 10);
        List<Section> sections = line.getSections();
        sections.add(new Section(line, 판교, 모란, 5));

        Station upStation = line.findUpStation();

        assertThat(upStation.getName()).isEqualTo("강남");
    }

}
