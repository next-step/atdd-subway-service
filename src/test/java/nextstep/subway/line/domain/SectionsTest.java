package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {
    @DisplayName("구간을 추가하고 정렬된 역을 가져온다")
    @Test
    void testGetStations() {
        // given
        Station 광명역 = new Station("광명역");
        Station 박달삼거리역 = new Station("박달삼거리역");
        Station 안양역 = new Station("안양역");
        Line line = new Line();
        Sections sections = new Sections();
        sections.add(line, 광명역, 박달삼거리역, 10);
        sections.add(line, 박달삼거리역, 안양역, 10);

        // when
        List<Station> stations = sections.getStations();

        // then
        assertThat(stations).containsExactly(광명역, 박달삼거리역, 안양역);
    }

    @DisplayName("구간을 삭제한다")
    @Test
    void testDeleteSectionBy() {
        // given
        Station 광명역 = new Station("광명역");
        Station 박달삼거리역 = new Station("박달삼거리역");
        Station 안양역 = new Station("안양역");
        Line line = new Line();
        Sections sections = new Sections();
        sections.add(line, 광명역, 박달삼거리역, 10);
        sections.add(line, 박달삼거리역, 안양역, 10);

        // when
        sections.deleteSectionBy(line, 박달삼거리역);

        // then
        assertThat(sections.getStations())
                .map(Station::getName)
                .containsExactly("광명역", "안양역");
    }
}
