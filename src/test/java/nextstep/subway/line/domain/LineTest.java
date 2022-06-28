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
        this.line = new Line.Builder("2호선", "GREEN")
                .section(Section.of(this.upStation, this.downStation, 10))
                .build();
    }

    @DisplayName("노선의 이름, 색상, 추가 요금으로 지하철 노선을 생성한다.")
    @ParameterizedTest
    @CsvSource(value = {"2호선,GREEN,0", "신분당선,RED,900"})
    void create_line(String name, String color, int extraFare) {
        // given & when
        Line newLine = new Line.Builder(name, color)
                .extraFare(extraFare)
                .build();

        // then
        assertAll(
                () -> assertThat(newLine.getName()).isEqualTo(name),
                () -> assertThat(newLine.getColor()).isEqualTo(color),
                () -> assertThat(newLine.getExtraFare()).isEqualTo(extraFare)
        );
    }

    @DisplayName("지하철 노선의 이름, 색상, 추가 요금을 수정한다.")
    @Test
    void update_line() {
        // given
        Line updateLine = new Line.Builder("신분당선", "RED")
                .extraFare(300)
                .build();

        // when
        this.line.update(updateLine);

        // then
        assertAll(
                () -> assertThat(this.line.getName()).isEqualTo(updateLine.getName()),
                () -> assertThat(this.line.getColor()).isEqualTo(updateLine.getColor()),
                () -> assertThat(this.line.getExtraFare()).isEqualTo(updateLine.getExtraFare())
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
        Section addedSection = Section.of(this.upStation, addedStation, 5);

        // when
        this.line.addSection(addedSection);

        // then
        assertAll(
                () -> assertThat(this.line.getSections()).hasSize(2),
                () -> assertThat(this.line.getStations()).containsExactly(this.upStation, addedStation, this.downStation)
        );
    }

    @DisplayName("지하철 노선에서 지하철역을 제거한다.")
    @Test
    void remove_station() {
        // given
        Station addedStation = Station.from("역삼역");
        Section addedSection = Section.of(this.upStation, addedStation, 5);
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