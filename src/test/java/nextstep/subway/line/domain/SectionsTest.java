package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Sections 단위 테스트")
class SectionsTest {

    @Test
    @DisplayName("구간을 추가한다.")
    public void addSection() throws Exception {
        // given
        Sections sections = new Sections();

        // when
        sections.addSection(new Section());

        // then
        assertThat(sections.getSections().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("리스트의 첫번째 Section의 하행역을 리턴한다.")
    void findAnyDownStation() {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Section 강남역_구간 = new Section(강남역);
        Section 양재역_구간 = new Section(양재역);
        Sections sections = new Sections(Arrays.asList(강남역_구간, 양재역_구간));

        // when
        Station result = sections.findAnyDownStation();

        // then
        assertThat(result).isEqualTo(강남역);
    }
    
    @Test
    @DisplayName("입력된 조건에 맞는 구간을 리턴한다.")
    public void findSection() throws Exception {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Section 강남역_구간 = new Section(강남역);
        Section 양재역_구간 = new Section(양재역);
        Sections sections = new Sections(Arrays.asList(강남역_구간, 양재역_구간));
        
        // when
        Section result = sections.findSection(it -> it.isEqualsDownStation(양재역)).get();

        // then
        assertThat(result).isEqualTo(양재역_구간);
    }

    @Test
    @DisplayName("정렬된 지하철역 리스트를 리턴한다.")
    public void getStations() throws Exception {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");
        Section 양재_판교_구간 = new Section(양재역, 판교역);
        Section 강남_양재_구간 = new Section(강남역, 양재역);
        Sections sections = new Sections(Arrays.asList(양재_판교_구간, 강남_양재_구간));

        // when
        List<Station> stations = sections.getStations();

        // then
        assertThat(stations).containsExactly(강남역, 양재역, 판교역);
    }
}