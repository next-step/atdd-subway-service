package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @DisplayName("지하철 노선들 중에 가장 큰 추가요금을 가져온다")
    @Test
    void testGetMaxExtraFare() {
        // given
        Line lineNumberOne = new Line("1호선", "파랑", new Station("안양"), new Station("명학"), 5, 100);
        Line lineNumberTwo = new Line("2호선", "초록", new Station("신도림"), new Station("대림"), 5, 200);
        List<Section> sectionList = Arrays.asList(
                new Section(lineNumberOne, new Station(), new Station(), 5),
                new Section(lineNumberTwo, new Station(), new Station(), 10));
        Sections sections = new Sections(sectionList);
        // when
        int maxExtraFare = sections.getMaxExtraFare();
        // then
        assertThat(maxExtraFare).isEqualTo(200);
    }
}
