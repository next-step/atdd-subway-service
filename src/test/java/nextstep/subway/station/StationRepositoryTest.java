package nextstep.subway.station;

import static nextstep.subway.station.StationFixtures.잠실;
import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class StationRepositoryTest {

    @Autowired
    StationRepository stationRepository;

    @Test
    @DisplayName("지하철 역이름 유무 체크")
    void existsByName() {
        // given
        Station persistStation = stationRepository.save(잠실);

        // when
        boolean actual = stationRepository.existsByName(persistStation.getName());

        // then
        assertThat(actual).isTrue();
    }
}
