package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class LineTest {
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    Station gangnam;
    Station gwanggyo;
    Station seoul;
    Station sillim;
    Station sadang;

    @BeforeEach
    void beforeEach() {
        gangnam = stationRepository.save(new Station("강남역"));
        gwanggyo = stationRepository.save(new Station("광교역"));
        seoul = stationRepository.save(new Station("서울역"));
        sillim = stationRepository.save(new Station("신림역"));
        sadang = stationRepository.save(new Station("사당역"));
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createEmptyLine() {
        // when
        Line line = lineRepository.save(new Line("신분당선", "RED"));

        // then
        assertThat(line.getId()).isNotNull();
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        Line line = lineRepository.save(new Line("신분당선", "RED", gangnam, gwanggyo, 10));

        // then
        assertThat(line.getStations()).hasSize(2);
    }

    @DisplayName("지하철 구간을 추가한다.")
    @Test
    void addStationAtBeginning() {
        // given
        Line line = lineRepository.save(new Line("신분당선", "RED", gangnam, gwanggyo, 10));

        // when
        line.addLineStation(new Section(line, sillim, gangnam, 10));

        // then
        assertThat(line.getStations()).containsExactly(sillim, gangnam, gwanggyo);
    }

    @DisplayName("지하철 구간을 추가한다.")
    @Test
    void addStationBetweenStations() {
        // given
        Line line = lineRepository.save(new Line("신분당선", "RED", gangnam, gwanggyo, 10));

        // when
        line.addLineStation(new Section(line, gangnam, sillim, 5));

        // then
        assertThat(line.getStations()).containsExactly(gangnam, sillim, gwanggyo);
    }

    @DisplayName("지하철 구간을 추가한다.")
    @Test
    void addStationAtEnd() {
        // given
        Line line = lineRepository.save(new Line("신분당선", "RED", gangnam, gwanggyo, 10));

        // when
        line.addLineStation(new Section(line, gwanggyo, sillim, 10));

        // then
        assertThat(line.getStations()).containsExactly(gangnam, gwanggyo, sillim);
    }

    @DisplayName("지하철 노선을 2개 생성한다.")
    @Test
    void createMultiLine() {
        // given
        Line 신분당선 = lineRepository.save(new Line("신분당선", "RED", gangnam, gwanggyo, 10));
        Line 남부선 = lineRepository.save(new Line("남부선", "BLUE", sillim, gwanggyo, 10));

        // when
        신분당선.addLineStation(new Section(신분당선, gwanggyo, seoul, 10));
        남부선.addLineStation(new Section(남부선, sillim, sadang, 1));
        남부선.addLineStation(new Section(남부선, sadang, seoul, 1));

        // then
        assertThat(신분당선.getStations()).containsExactly(gangnam, gwanggyo, seoul);
        assertThat(남부선.getStations()).containsExactly(sillim, sadang, seoul, gwanggyo);
    }

    @DisplayName("이미 노선에 등록된 역으로는 구간을 추가할 수 없다.")
    @Test
    void addDuplicatedStation() {
        // when
        Line line = lineRepository.save(new Line("신분당선", "RED", gangnam, gwanggyo, 10));
        line.addLineStation(new Section(line, gwanggyo, sillim, 10));

        // then
        assertThatThrownBy(() -> line.addLineStation(new Section(line, gangnam, sillim, 10)))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("기존 역 사이 길이보다 크거나 같은 구간은 추가할 수 없다.")
    @Test
    void addStation() {
        // when
        Line line = lineRepository.save(new Line("신분당선", "RED", gangnam, gwanggyo, 10));

        // then
        assertThatThrownBy(() -> line.addLineStation(new Section(line, gangnam, sillim, 10)))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("시작역을 삭제한다.")
    @Test
    void deleteUpStation() {
        // given
        Line line = lineRepository.save(new Line("신분당선", "RED", gangnam, gwanggyo, 10));
        line.addLineStation(new Section(line, gwanggyo, sillim, 10));

        // when
        line.removeLineStation(gangnam);

        // then
        assertThat(line.getStations()).containsExactly(gwanggyo, sillim);

    }

    @DisplayName("도착역을 삭제한다.")
    @Test
    void deleteDownStation() {
        // given
        Line line = lineRepository.save(new Line("신분당선", "RED", gangnam, gwanggyo, 10));
        line.addLineStation(new Section(line, gwanggyo, sillim, 10));

        // when
        line.removeLineStation(sillim);

        // then
        assertThat(line.getStations()).containsExactly(gangnam, gwanggyo);
    }

    @DisplayName("중간역을 삭제한다.")
    @Test
    void deleteStationBetweenStations() {
        // given
        Line line = lineRepository.save(new Line("신분당선", "RED", gangnam, gwanggyo, 10));
        line.addLineStation(new Section(line, gwanggyo, sillim, 10));

        // when
        line.removeLineStation(gwanggyo);

        // then
        assertThat(line.getStations()).containsExactly(gangnam, sillim);
    }

    @DisplayName("역을 삭제할 수 없다.")
    @Test
    void cannotDeleteAnyStations() {
        // given
        Line line = lineRepository.save(new Line("신분당선", "RED", gangnam, gwanggyo, 10));

        // then
        assertAll(
                () -> assertThatThrownBy(() -> line.removeLineStation(gangnam)).isInstanceOf(RuntimeException.class),
                () -> assertThatThrownBy(() -> line.removeLineStation(gwanggyo)).isInstanceOf(RuntimeException.class),
                () -> assertThatThrownBy(() -> line.removeLineStation(sillim)).isInstanceOf(RuntimeException.class)
        );
    }
}
