package nextstep.subway.sections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import nextstep.subway.sections.domain.Section;
import nextstep.subway.sections.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionsTest {

    private Station 판교역;
    private Station 강남역;
    private Station 양재역;
    private Station 광교역;

    @BeforeEach
    public void setUp() {
        판교역 = new Station("판교역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        광교역 = new Station("광교역");
    }

    @DisplayName("잘못된 구간 값을 추가하면 실패한다.")
    @Test
    public void addSectionWithNull() {
        //given
        Sections sections = new Sections();
        //when
        //then
        assertThatThrownBy(() -> sections.add(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("구간의 모든 역을 가져온다")
    @Test
    public void getAllStation() {
        //given
        Sections sections = new Sections(new Section(강남역, 판교역, 10));
        Section section = new Section(판교역, 광교역, 10);
        sections.updateSection(section);
        //when
        List<Station> stations = sections.orderedStations();
        //then
        assertThat(stations).containsExactly(강남역, 판교역, 광교역);
    }

    @DisplayName("노선의 사이에 새로운 역 구간을 등록한다.")
    @Test
    public void updateSectionInMiddle() {
        //given
        Sections sections = new Sections(new Section(강남역, 판교역, 10));
        Section section = new Section(강남역, 양재역, 3);
        sections.updateSection(section);
        //when
        List<Station> stations = sections.orderedStations();
        //then
        assertThat(stations).containsExactly(강남역, 양재역, 판교역);
    }

    @DisplayName("노선의 상행종점으로 새로운 역구간을 등록한다.")
    @Test
    public void addSectionAtFirst() {
        //given
        Sections sections = new Sections(new Section(양재역, 판교역, 10));
        Section section = new Section(강남역, 양재역, 3);
        sections.updateSection(section);
        //when
        List<Station> stations = sections.orderedStations();
        //then
        assertThat(stations).containsExactly(강남역, 양재역, 판교역);
    }

    @DisplayName("노선의 하행종점으로 새로운 역구간을 등록한다.")
    @Test
    public void addSectionAtLast() {
        //given
        Sections sections = new Sections(new Section(양재역, 판교역, 10));
        Section section = new Section(판교역, 광교역, 3);
        sections.updateSection(section);
        //when
        List<Station> stations = sections.orderedStations();
        //then
        assertThat(stations).containsExactly(양재역, 판교역, 광교역);
    }

    @DisplayName("역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없음.")
    @Test
    public void addSectionWithLongerDistance() {
        //given
        Sections sections = new Sections(new Section(강남역, 판교역, 10));
        Section section = new Section(강남역, 양재역, 20);
        //when
        //then
        assertThatThrownBy(() -> sections.updateSection(section))
            .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("지하철 노선의 중간 역을 삭제한다.")
    @Test
    public void deleteSection() {
        //given
        Sections sections = new Sections(new Section(강남역, 판교역, 10));
        sections.updateSection(new Section(판교역, 양재역, 3));
        //when
        sections.delete(판교역);
        List<Station> stations = sections.orderedStations();
        //then
        assertThat(stations).containsExactly(강남역, 양재역);
    }

    @DisplayName("구간이 하나인 노선에서 마지막 역을 삭제한다.")
    @Test
    public void deleteOnlySection() {
        //given
        Sections sections = new Sections(new Section(강남역, 판교역, 10));
        //when
        //then
        assertThatThrownBy(() -> sections.delete(판교역)).isInstanceOf(
            RuntimeException.class).hasMessageContaining("구간이 하나뿐일 때는 삭제할 수 없습니다.");
    }
}
