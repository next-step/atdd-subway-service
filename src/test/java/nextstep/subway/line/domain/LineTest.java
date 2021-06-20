package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {

    Line 신분당선;
    private Station 강남;
    private Station 광교;
    private Station 정자;
    private int disatnce;
    private Station 미금;

    @BeforeEach
    void setUp() {
        강남 = new Station("강남");
        광교 = new Station("광교");
        정자 = new Station("정자");
        신분당선 = new Line("신분당선", "abc", 강남, 광교, 10);
        disatnce = 10;
    }

    @Test
    void updateLine() {
        Line update = new Line("신분당선-임시", "aaa");
        신분당선.update(update);

        assertThat(신분당선.getName()).isEqualTo("신분당선-임시");
        assertThat(신분당선.getColor()).isEqualTo("aaa");
    }

    @Test
    void addSection_equals_upStation() {
        Section newSection = new Section(신분당선, 강남, 정자, 2);
        신분당선.addSection(newSection);

        assertThat(신분당선.getSections())
                .contains(newSection,
                        new Section(신분당선, 정자, 광교, 8));
    }

    @Test
    void addSection_equals_downStation() {
        Section newSection = new Section(신분당선, 정자, 광교, 2);
        신분당선.addSection(newSection);

        assertThat(신분당선.getSections())
                .contains(new Section(신분당선, 강남, 정자, 8),
                        newSection);
    }

    @Test
    void addSection_none_of_match() {
        Station 구리 = new Station("구리");
        Station 도농 = new Station("도농");
        Section newSection = new Section(신분당선, 구리, 도농, 2);
        assertThatThrownBy(() -> 신분당선.addSection(newSection))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("등록할 수 없는 구간 입니다.");
    }

    @Test
    void addSection_both_station_already_registered() {
        Section newSection = new Section(신분당선, 강남, 광교, 2);
        assertThatThrownBy(() -> 신분당선.addSection(newSection))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("이미 등록된 구간 입니다.");
    }

    @Test
    void getStation() {
        Section newSection = new Section(신분당선, 강남, 정자, 2);
        미금 = new Station("미금");

        신분당선.addSection(newSection);
        assertThat(신분당선.getStations()).containsExactly(강남, 정자, 광교);

        Section newSection2 = new Section(신분당선, 정자, 미금, 2);
        신분당선.addSection(newSection2);

        assertThat(신분당선.getStations()).containsExactly(강남, 정자, 미금, 광교);
    }

    @Test
    void removeStation() {
        Section newSection = new Section(신분당선, 강남, 정자, 2);
        신분당선.addSection(newSection);
        assertThat(신분당선.getStations()).containsExactly(강남, 정자, 광교);

        신분당선.removeStation(정자);
        assertThat(신분당선.getStations()).containsExactly(강남, 광교);
    }

    @Test
    void removeStation_error_when_final_upStation() {
        Section newSection = new Section(신분당선, 강남, 정자, 2);
        신분당선.addSection(newSection);
        assertThat(신분당선.getStations()).containsExactly(강남, 정자, 광교);

        assertThatThrownBy(() -> 신분당선.removeStation(강남)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void removeStation_error_when_final_downStation() {
        Section newSection = new Section(신분당선, 강남, 정자, 2);
        신분당선.addSection(newSection);
        assertThat(신분당선.getStations()).containsExactly(강남, 정자, 광교);

        assertThatThrownBy(() -> 신분당선.removeStation(광교)).isInstanceOf(RuntimeException.class);
    }
}
