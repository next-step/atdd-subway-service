package nextstep.subway.line.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionsTest {
    private Line 신분당선 = new Line("신분당선", "빨간색");
    private Station 강남역= new Station("강남역");

    private Station 정자역 = new Station("정자역");
    private Station 미금역 = new Station("미금역");
    private Station 광교역 = new Station("광교역");

    @DisplayName("노선에 최초의 구간을 추가할 수 있다.")
    @Test
    void addSection_new_test() {
        // given
        Sections 구간_목록 = new Sections();
        Section 구간 = new Section(신분당선, 강남역, 광교역, 10);

        // when
        구간_목록.add(구간);

        // then
        assertThat(구간_목록.getAll()).contains(구간);

    }

    @DisplayName("노선에 상행종점 구간을 추가할 수 있다.")
    @Test
    void addSection_firstSection_test() {
        // given
        Sections 구간_목록 = new Sections();
        구간_목록.add(new Section(신분당선, 미금역, 광교역, 10));
        Section 상행_종점_구간 = new Section(신분당선, 강남역, 미금역, 10);

        // when
        구간_목록.add(상행_종점_구간);

        // then
        assertThat(구간_목록.getAll()).contains(상행_종점_구간);
    }

    @DisplayName("노선에 하행종점 구간을 추가할 수 있다.")
    @Test
    void addSection_lastSection_test() {
        // given
        Sections 구간_목록 = new Sections();
        구간_목록.add(new Section(신분당선, 강남역, 미금역, 10));
        Section 하행_종점_구간 = new Section(신분당선, 미금역, 광교역, 10);

        // when
        구간_목록.add(하행_종점_구간);

        // then
        assertThat(구간_목록.getAll()).contains(하행_종점_구간);
    }


    @DisplayName("노선에 중간 구간을 추가할 수 있다.")
    @Test
    void addSection_middleSection_test() {
        // given
        Sections 구간_목록 = new Sections();
        구간_목록.add(new Section(신분당선, 강남역, 광교역, 10));
        Section 하행역_기준_추가_구간 = new Section(신분당선, 미금역, 광교역, 10);
        Section 상행역_기준_추가_구간 = new Section(신분당선, 강남역, 정자역, 10);

        // when
        구간_목록.add(하행역_기준_추가_구간);
        구간_목록.add(상행역_기준_추가_구간);

        // then
        assertThat(구간_목록.getAll()).containsAll(Arrays.asList(하행역_기준_추가_구간, 상행역_기준_추가_구간));
    }


    @DisplayName("구간을 추가할때 상행역, 하행역이 전부 노선에 있으면 IllegalArgumentException 발생한다.")
    @Test
    void addSection_duplicated_exception() {
        // when
        Sections 구간_목록 = new Sections();
        Section 구간 = new Section(신분당선, 강남역, 광교역, 10);
        구간_목록.add(구간);

        // then
        assertThatThrownBy(() -> 구간_목록.add(구간)).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("구간을 추가할때 상행역, 하행역이 전부 노선에 없으면 IllegalArgumentException 발생한다.")
    @Test
    void addSection_notExists_exception() {
        // when
        Sections 구간_목록 = new Sections();
        Section 구간 = new Section(신분당선, 강남역, 광교역, 10);
        구간_목록.add(구간);

        Section 신규_구간 = new Section(신분당선, 정자역, 미금역, 10);

        // then
        assertThatThrownBy(() -> 구간_목록.add(신규_구간)).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("구간의 역들을 순차적으로 조회할 수 있다.")
    @Test
    void findOrderedStation_test() {
        // when
        Sections 구간_목록 = new Sections();
        구간_목록.add(new Section(신분당선, 강남역, 미금역, 10));
        구간_목록.add(new Section(신분당선, 미금역, 광교역, 10));
        // then
        assertThat(구간_목록.getOrderedStations()).containsExactly(강남역, 미금역, 광교역);
    }

    @DisplayName("노선의 상행종점을 삭제할 수 있다.")
    @Test
    void deleteStation_firstStation_test() {
        // given
        Sections 구간_목록 = new Sections();
        구간_목록.add(new Section(신분당선, 강남역, 미금역, 10));
        구간_목록.add(new Section(신분당선, 미금역, 광교역, 10));
        Station 상행_종점 = 상행_종점_조회(구간_목록);
        // when

        구간_목록.deleteStation(상행_종점);

        // then
        assertFalse(역이_존재한다(구간_목록, 상행_종점));

    }

    @DisplayName("노선의 중간역을 삭제하면 해당 역을 상행역으로 갖는 구간이 삭제된다.")
    @Test
    void deleteStation_middleStation_test() {
        // given
        Sections 구간_목록 = new Sections();
        구간_목록.add(new Section(신분당선, 강남역, 미금역, 10));
        Section 추가_구간 = new Section(신분당선, 미금역, 광교역, 10);
        구간_목록.add(추가_구간);
        // when
        구간_목록.deleteStation(미금역);
        // then
        assertThat(구간_목록.getAll()).doesNotContain(추가_구간);
    }

    @DisplayName("노선의 역을 삭제할때, 현재 구간이 한개라면 IllegalStateException 발생한다.")
    @Test
    void deleteStation_onlyOneSection_exception() {
        // given
        Sections 구간_목록 = new Sections();
        구간_목록.add(new Section(신분당선, 강남역, 미금역, 10));

        // then
        assertThatThrownBy(() -> 구간_목록.deleteStation(미금역)).isInstanceOf(IllegalStateException.class);
    }

    private Station 상행_종점_조회(Sections 구간_목록){
        return 구간_목록.getOrderedStations().get(0);
    }

    private boolean 역이_존재한다(Sections 구간_목록, Station 역){
        return 구간_목록.getOrderedStations().contains(역);
    }

}
