package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {

    private Line line;
    private Station upStation;
    private Station downStation;
    private Station middleStation;

    @BeforeEach
    void setUp() {
        upStation = new Station(1L,"상행역");
        downStation = new Station(2L,"하행역");
        middleStation = new Station(3L,"중앙역");
        line = new Line("노선","색상",upStation,downStation,10);
    }
    @Test
    @DisplayName("구간이 0개일 찾을 수 없다.")
    void test0() {
        Sections sections = new Sections();

        List<StationResponse> stationResponses = sections.getStationResponse();

        assertThat(stationResponses.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("구간이 1개일 때 상행역/하행역을 찾을 수 있다.")
    void test1() {
        Sections sections = new Sections();
        sections.add(new Section(line,upStation,downStation,10));

        List<StationResponse> stationResponses = sections.getStationResponse();

        assertThat(stationResponses.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("구간이 2개일 때 모든 역을 찾을 수 있다.")
    void test2() {
        Sections sections = getSectionsHasTwoSection();

        List<StationResponse> stationResponses = sections.getStationResponse();

        assertThat(stationResponses.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("이미 구간으로 등록된 역은 새로운 구간으로 추가할 수 없음")
    void checkToAddSection() {
        Sections sections = getSectionsHasTwoSection();

        assertThatThrownBy(() -> sections.getSectionAdder(upStation,downStation))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("추가하려는 역이 모두 기존 구간에 존재하지 않으면 추가할 수 없음")
    void checkToAddSection2() {
        Sections sections = getSectionsHasTwoSection();

        Station 존재하지_않는_역1 = new Station(5L,"존재하지 않는 역1");
        Station 존재하지_않는_역2 = new Station(6L,"존재하지 않는 역2");

        assertThatThrownBy(() -> sections.getSectionAdder(존재하지_않는_역1,존재하지_않는_역2))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("구간이 비어있으면 EmptySectionAdder가 반환됨")
    void checkToAddSection3() {
        Sections sections = new Sections();

        Station 존재하지_않는_역1 = new Station(5L,"존재하지 않는 역1");
        Station 존재하지_않는_역2 = new Station(6L,"존재하지 않는 역2");

        SectionAdder sectionAdder = sections.getSectionAdder(존재하지_않는_역1, 존재하지_않는_역2);

        assertThat(sectionAdder).isInstanceOf(EmptySectionAdder.class);
    }

    @Test
    @DisplayName("상행역으로 시작하는 구간을 추가하려고 할 때 FromUpperSectionAdder가 반환됨")
    void checkToAddSection4() {
        Sections sections = getSectionsHasTwoSection();

        Station 존재하지_않는_역2 = new Station(6L,"존재하지 않는 역2");

        SectionAdder sectionAdder = sections.getSectionAdder(upStation, 존재하지_않는_역2);

        assertThat(sectionAdder).isInstanceOf(FromUpperSectionAdder.class);
    }

    @Test
    @DisplayName("하행역에서 끝나는 구간을 추가하려고 할 때 FromUpperSectionAdder가 반환됨")
    void checkToAddSection5() {
        Sections sections = getSectionsHasTwoSection();

        Station 존재하지_않는_역2 = new Station(6L,"존재하지 않는 역2");

        SectionAdder sectionAdder = sections.getSectionAdder(존재하지_않는_역2,downStation);

        assertThat(sectionAdder).isInstanceOf(FromDownSectionAdder.class);
    }

    private Sections getSectionsHasTwoSection() {
        Sections sections = new Sections();
        sections.add(new Section(line,upStation,middleStation,4));
        sections.add(new Section(line,middleStation,downStation,6));
        return sections;
    }
}