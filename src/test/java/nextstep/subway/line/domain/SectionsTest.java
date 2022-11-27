package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

class SectionsTest {


    @Test
    void 구간_목록에_등록한다() {
        Sections sections = new Sections();
        Station 강남 = new Station("강남");
        Station 판교 = new Station("판교");
        Line line = new Line("신분당선", "red", 강남, 판교, 10);

        sections.add(line, new Station("모란"), new Station("서현"), 5);

        assertThat(sections.getSections()).hasSize(1);
    }

    @Test
    void 구간_목록을_가져온다() {
        Sections sections = new Sections();

        List<Section> sectionList = sections.getSections();

        assertThat(sectionList).hasSize(0);
    }

}
