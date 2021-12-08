package nextstep.subway.line.acceptance;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionTest {

    @DisplayName("구간 합치기")
    @Test
    void combineTest() {

        //given
        Line 신분당선 = new Line("신분당선", "green");
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Station 강변역 = new Station("강변역");

        Section 구간1 = new Section(신분당선, 강남역, 교대역, 7);
        Section 구간2 = new Section(신분당선, 교대역, 강변역, 7);

        //when
        Section 구간3 = 구간1.combine(구간2);

        //then
        assertThat(구간3).isNotNull();
        assertThat(구간3.getUpStation()).isEqualTo(new Station("강남역"));
        assertThat(구간3.getDownStation()).isEqualTo(new Station("강변역"));
        assertThat(구간3.getDistance()).isEqualTo(new Distance(14));
    }

    @DisplayName("노선의 최상단 상행역을 조회한다.")
    @Test
    void findUpStationTest() {

        //given
        Line 신분당선 = new Line("신분당선", "green");
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Station 강변역 = new Station("강변역");

        신분당선.addSection(강남역, 교대역, 7);
        신분당선.addSection(교대역, 강변역, 4);

        //when
        List<Station> stations = 신분당선.getStationsByOrder();

        //then
        assertThat(stations).isNotEmpty();
        assertThat(stations.get(0)).isEqualTo(new Station("강남역"));
    }

    @DisplayName("구간에서 제외")
    @Test
    void removeTest() {

        //given
        Line 신분당선 = new Line("신분당선", "green");
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Station 강변역 = new Station("강변역");

        신분당선.addSection(강남역, 교대역, 7);
        신분당선.addSection(교대역, 강변역, 3);
        신분당선.getSections();

        //when
        신분당선.removeSection(강남역);

        //then
        assertThat(신분당선.getSections().size()).isEqualTo(1);
    }

    @DisplayName("Section 비교")
    @Test
    void correctSectionTest() {

        Line 신분당선 = new Line("신분당선", "green");
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");

        Section section1 = new Section(신분당선, 강남역, 교대역, 7);
        Section section2 = new Section(신분당선, 강남역, 교대역, 7);

        assertThat(section1).isEqualTo(section2);
    }

}
