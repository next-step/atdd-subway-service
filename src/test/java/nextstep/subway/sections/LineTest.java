package nextstep.subway.sections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.sections.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LineTest {

    private Line 일호선;
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
        일호선 = new Line("1호선", "red", new Section(강남역, 판교역, 10));
    }

    @DisplayName("구간의 모든 역을 가져온다")
    @Test
    public void getAllStation() {
        //given
        일호선.updateSection(판교역, 광교역, 10);
        //when
        List<Station> stations = 일호선.orderedStations();
        //then
        assertThat(stations).containsExactly(강남역, 판교역, 광교역);
    }

    @DisplayName("노선의 사이에 새로운 역 구간을 등록한다.")
    @Test
    public void addSection() {
        //given
        Line line = new Line("1호선", "red", new Section(강남역, 판교역, 10));
        //when
        line.updateSection(강남역, 양재역, 5);
        List<Station> stations = line.orderedStations();
        //then
        assertThat(stations).containsExactly(강남역, 양재역, 판교역);
    }

    @DisplayName("노선의 상행종점으로 새로운 역구간을 등록한다.")
    @Test
    public void addSectionAtFirst() {
        //given
        Line line = new Line("1호선", "red", new Section(양재역, 판교역, 10));
        //when
        line.updateSection(강남역, 양재역, 5);
        List<Station> stations = line.orderedStations();
        //then
        assertThat(stations).containsExactly(강남역, 양재역, 판교역);
    }

    @DisplayName("노선의 하행종점으로 새로운 역구간을 등록한다.")
    @Test
    public void addSectionAtLast() {
        //given
        Line line = new Line("1호선", "red", new Section(강남역, 양재역, 10));
        //when
        line.updateSection(양재역, 광교역, 5);
        List<Station> stations = line.orderedStations();
        //then
        assertThat(stations).containsExactly(강남역, 양재역, 광교역);
    }

    @DisplayName("지하철 노선의 중간 역을 삭제한다.")
    @Test
    public void removeSection() {
        //given
        Line line = new Line("1호선", "red", new Section(강남역, 양재역, 10));
        line.updateSection(양재역, 광교역, 5);
        //when
        line.removeSectionByStation(양재역);
        List<Station> stations = line.orderedStations();
        //then
        assertThat(stations).containsExactly(강남역, 광교역);
    }

    @DisplayName("구간이 하나인 노선에서 역을 삭제할 수 없다.")
    @Test
    public void removeOnlySection() {
        //given
        Line line = new Line("1호선", "red", new Section(강남역, 양재역, 10));
        //when
        //then
        assertThatThrownBy(() -> line.removeSectionByStation(양재역)).isInstanceOf(
            RuntimeException.class).hasMessageContaining("구간이 하나뿐일 때는 삭제할 수 없습니다.");
    }
}
