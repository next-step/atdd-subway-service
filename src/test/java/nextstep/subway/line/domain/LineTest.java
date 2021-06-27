package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("노선 entity 테스트")
public class LineTest {

    private Line 신분당선;
    private Station 강남역;
    private Station 광교역;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        광교역 = new Station(2L, "광교역");
        신분당선 = new Line("신분당선", "red", 강남역, 광교역, 10);
    }

    @Test
    void 지하철역_리스트_반환() {
        List<Station> stations = 신분당선.getStations();
        assertThat(stations).containsExactly(강남역, 광교역);
    }

    @Test
    void 노선_이름_색상_업데이트() {
        Line expected = new Line("구분당선", "green");
        신분당선.update(expected);
        assertThat(신분당선.getName()).isEqualTo(expected.getName());
        assertThat(신분당선.getColor()).isEqualTo(expected.getColor());
    }

    @Test
    void 노선에_구간_추가_middle() {
        Station 양재역 = new Station(3L, "양재역");
        Section section = new Section(신분당선, 강남역, 양재역, 3);
        신분당선.addLineStation(section);
        assertThat(신분당선.getStations()).containsExactly(강남역, 양재역, 광교역);
    }

    @Test
    void 노선에_구간_추가_up() {
        Station 양재역 = new Station(3L, "양재역");
        Section section = new Section(신분당선, 양재역, 강남역, 3);
        신분당선.addLineStation(section);
        assertThat(신분당선.getStations()).containsExactly(양재역, 강남역, 광교역);
    }

    @Test
    void 노선에_구간_추가_down() {
        Station 정자역 = new Station(4L, "정자역");
        Section section = new Section(신분당선, 광교역, 정자역, 3);
        신분당선.addLineStation(section);
        assertThat(신분당선.getStations()).containsExactly(강남역, 광교역, 정자역);
    }

    @Test
    void 이미_추가된_구간_노선에_추가_시_에러_발생() {
        Section section = new Section(신분당선, 강남역, 광교역, 10);
        assertThatThrownBy(() -> 신분당선.addLineStation(section))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("강남역, 광교역 구간은 이미 등록된 구간 입니다.");
    }

    @Test
    void 등록할_수_없는_구간_노선에_추가_시_에러_발생() {
        Station 양재역 = new Station(3L, "양재역");
        Station 정자역 = new Station(4L, "정자역");
        Section section = new Section(신분당선, 양재역, 정자역, 3);
        assertThatThrownBy(() -> 신분당선.addLineStation(section))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("등록할 수 없는 구간 입니다.");
    }

    @Test
    void 노선_제거_middle() {
        Station 양재역 = new Station(3L, "양재역");
        Section section = new Section(신분당선, 강남역, 양재역, 3);
        신분당선.addLineStation(section);
        신분당선.removeSection(양재역);
        assertThat(신분당선.getStations()).containsExactly(강남역, 광교역);
    }

    @Test
    void 노선_제거_up() {
        Station 양재역 = new Station(3L, "양재역");
        Section section = new Section(신분당선, 강남역, 양재역, 3);
        신분당선.addLineStation(section);
        신분당선.removeSection(강남역);
        assertThat(신분당선.getStations()).containsExactly(양재역, 광교역);
    }

    @Test
    void 노선_제거_down() {
        Station 양재역 = new Station(3L, "양재역");
        Section section = new Section(신분당선, 강남역, 양재역, 3);
        신분당선.addLineStation(section);
        신분당선.removeSection(광교역);
        assertThat(신분당선.getStations()).containsExactly(강남역, 양재역);
    }

    @Test
    void 한개의_구간일때_구간_삭제_실행하면_에러_발생() {
        assertThatThrownBy(() -> 신분당선.removeSection(강남역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간이 하나만 존재하는 경우 삭제할 수 없습니다.");
    }
}
