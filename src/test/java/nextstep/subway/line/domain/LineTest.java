package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineTest {
    @DisplayName("노선에 포함된 지하철 역 정보를 가져올 수 있다")
    @Test
    void stations() {
        // given
        Station upStation = new Station("판교역");
        Station downStation = new Station("강남역");
        Section section = new Section(upStation, downStation, new Distance(10));
        Line line = new Line("신분당선", "bg-red-600", new ExtraCharge(0));
        line.addSection(section);

        // when
        List<Station> results = line.getStations();

        // then
        assertThat(results).containsOnly(upStation, downStation);
    }

    @DisplayName("노선의 이름과 색깔을 수정할 수 있다")
    @Test
    void update() {
        // given
        Line line = new Line("신분당선", "bg-red-600", new ExtraCharge(0));

        // when
        line.update("분당선", "bg-blue-400");

        // then
        assertAll(
                () -> assertThat(line.getName()).isEqualTo("분당선"),
                () -> assertThat(line.getColor()).isEqualTo("bg-blue-400")
        );
    }

    @DisplayName("노선에 구간을 추가할 수 있다")
    @Test
    void addSection() {
        // given
        Line line = new Line("신분당선", "bg-red-600", new ExtraCharge(0));
        Station upStation = new Station("판교역");
        Station downStation = new Station("강남역");
        Section section = new Section(upStation, downStation, new Distance(10));

        // when
        line.addSection(section);

        // then
        assertThat(line.getStations()).hasSize(2);
    }

    @DisplayName("노선의 구간을 삭제할 수 있다")
    @Test
    void deleteSection() {
        // given
        Station upStation = new Station("판교역");
        Station downStation = new Station("강남역");
        Station newStation = new Station("양재역");

        Section section1 = new Section(upStation, downStation, new Distance(10));
        Section section2 = new Section(downStation, newStation, new Distance(10));

        Line line = new Line("신분당선", "bg-red-600", new ExtraCharge(0));
        line.addSection(section1);
        line.addSection(section2);

        // when
        line.deleteSection(newStation);

        // then
        assertThat(line.getStations()).hasSize(2);
    }
}
