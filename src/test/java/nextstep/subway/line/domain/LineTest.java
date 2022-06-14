package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class LineTest {
    @Autowired
    private LineRepository lines;

    @Autowired
    private StationRepository stations;

    Station upStation;
    Station downStation;
    Station addStation;

    @BeforeEach
    void beforeEach() {
        upStation = stations.save(new Station("강남역"));
        downStation = stations.save(new Station("광교역"));
        addStation = stations.save(new Station("신림역"));
    }

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
        // when
        Line line = lines.save(new Line("신분당선", "RED", upStation, downStation, 10));

        // then
        assertThat(line.getStations()).hasSize(2);
    }

    @DisplayName("지하철 구간을 추가한다.")
    @Test
    void addStationAtBeginning() {
        Line line = lines.save(new Line("신분당선", "RED", upStation, downStation, 10));

        // when
        line.addLineStation(new Section(line, addStation, upStation, 10));

        // then
        List<Long> ids = line.getStations().stream().map(Station::getId).collect(Collectors.toList());
        assertThat(ids).containsExactly(addStation.getId(), upStation.getId(), downStation.getId());
    }

    @DisplayName("지하철 구간을 추가한다.")
    @Test
    void addStationBetweenStations() {
        // given
        Line line = lines.save(new Line("신분당선", "RED", upStation, downStation, 10));

        // when
        line.addLineStation(new Section(line, upStation, addStation, 5));

        // then
        List<Long> ids = line.getStations().stream().map(Station::getId).collect(Collectors.toList());
        assertThat(ids).containsExactly(upStation.getId(), addStation.getId(), downStation.getId());
    }

    @DisplayName("지하철 구간을 추가한다.")
    @Test
    void addStationAtEnd() {
        // given
        Line line = lines.save(new Line("신분당선", "RED", upStation, downStation, 10));

        // when
        line.addLineStation(new Section(line, downStation, addStation, 10));

        // then
        List<Long> ids = line.getStations().stream().map(Station::getId).collect(Collectors.toList());
        assertThat(ids).containsExactly(upStation.getId(), downStation.getId(), addStation.getId());
    }

    @DisplayName("이미 노선에 등록된 역으로는 구간을 추가할 수 없다.")
    @Test
    void addDuplicatedStation() {
        // given
        Line line = lines.save(new Line("신분당선", "RED", upStation, downStation, 10));
        line.addLineStation(new Section(line, downStation, addStation, 10));

        // then
        assertThatThrownBy(() -> line.addLineStation(new Section(line, upStation, addStation,10)))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("기존 역 사이 길이보다 크거나 같은 구간은 추가할 수 없다.")
    @Test
    void addStation() {
        // given
        Line line = lines.save(new Line("신분당선", "RED", upStation, downStation, 10));

        // then
        assertThatThrownBy(() -> line.addLineStation(new Section(line, upStation, addStation,10)))
                .isInstanceOf(RuntimeException.class);
    }
}