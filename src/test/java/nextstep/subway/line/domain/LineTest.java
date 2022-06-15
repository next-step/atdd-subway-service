package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class LineTest {

    private Line line;
    private Station upStation;
    private Station downStation;

    @BeforeEach
    void setUp() {
        // given
        this.upStation = Station.from("강남역");
        this.downStation = Station.from("선릉역");
        this.line = Line.of("2호선", "GREEN", this.upStation, this.downStation, 10);
    }

    @DisplayName("노선의 이름과 색상으로 지하철 노선을 생성한다.")
    @ParameterizedTest
    @CsvSource(value = {"2호선,GREEN", "신분당선,RED"})
    void create_line(String name, String color) {
        // given & when
        Line newLine = Line.of(name, color);

        // then
        assertAll(
                () -> assertThat(newLine.getName()).isEqualTo(name),
                () -> assertThat(newLine.getColor()).isEqualTo(color)
        );
    }

    @DisplayName("지하철 노선의 이름과 색상을 수정한다.")
    @Test
    void update_line() {
        // given
        Line updateLine = Line.of("신분당선", "RED");

        // when
        this.line.update(updateLine);

        // then
        assertAll(
                () -> assertThat(this.line.getName()).isEqualTo(updateLine.getName()),
                () -> assertThat(this.line.getColor()).isEqualTo(updateLine.getColor())
        );
    }

    @DisplayName("지하철 노선에 포함된 지하철역 목록을 조회한다.")
    @Test
    void get_stations() {
        // when
        List<Station> stations = this.line.getStations();

        // then
        assertAll(
                () -> assertThat(stations).hasSize(2),
                () -> assertThat(stations).containsExactly(this.upStation, this.downStation)
        );
    }

    @DisplayName("지하철 노선에 포함된 지하철 구간 목록을 조회한다.")
    @Test
    void get_sections() {
        // when
        List<Section> sections = line.getSections();

        // then
        assertThat(sections).hasSize(1);
    }

    @DisplayName("지하철 노선에 지하철 구간을 추가한다.")
    @Test
    void add_section() {
        // given
        Station addedStation = Station.from("역삼역");
        Section addedSection = Section.of(this.line, this.upStation, addedStation, 5);

        // when
        this.line.addSection(addedSection);

        // then
        assertAll(
                () -> assertThat(this.line.getSections()).hasSize(2),
                () -> assertThat(this.line.getStations()).containsExactly(addedStation)
        );
    }

    @DisplayName("지하철 노선에서 지하철역을 제거한다.")
    @Test
    void remove_station() {
        // given
        Station addedStation = Station.from("역삼역");
        Section addedSection = Section.of(this.line, this.upStation, addedStation, 5);
        this.line.addSection(addedSection);

        // when
        this.line.removeStation(addedStation);

        // then
        assertAll(
                () -> assertThat(this.line.getSections()).hasSize(1),
                () -> assertThat(this.line.getStations()).doesNotContain(addedStation)
        );
    }
}