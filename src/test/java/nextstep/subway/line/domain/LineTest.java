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
        Section section = new Section(upStation, downStation, 10);
        Line line = new Line("신분당선", "bg-red-600");
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
        Line line = new Line("신분당선", "bg-red-600");

        // when
        line.update("분당선", "bg-blue-400");

        // then
        assertAll(
                () -> assertThat(line.getName()).isEqualTo("분당선"),
                () -> assertThat(line.getColor()).isEqualTo("bg-blue-400")
        );
    }
}
