package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LineTest {
    @Autowired
    private LineRepository lines;

    @Autowired
    private StationRepository stations;

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createEmptyLine() {
        // when
        Line line = lines.save(new Line("신분당선", "RED"));

        // then
        assertThat(line.getId()).isNotNull();
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        Station upStation = stations.save(new Station("강남역"));
        Station downStation = stations.save(new Station("광교역"));

        // when
        Line line = lines.save(new Line("신분당선", "RED", upStation, downStation, 10));

        // then
        assertThat(line.getStations()).hasSize(2);
    }
}