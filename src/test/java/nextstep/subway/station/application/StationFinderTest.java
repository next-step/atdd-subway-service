package nextstep.subway.station.application;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.infrastructure.InMemoryStationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StationFinderTest {
    private StationFinder stationFinder;

    @BeforeEach
    void setUp() {
        StationRepository stationRepository = new InMemoryStationRepository();
        stationFinder = new StationFinder(stationRepository);
    }

    @Test
    void 모든_지하철_역을_조회한다() {
        // when
        List<StationResponse> result = stationFinder.findAllStations();

        // then
        assertThat(result).hasSize(6);
    }

    @Test
    void 지하철역을_조회한다() {
        // when
        Station result = stationFinder.findStationById(1L);

        // then
        assertThat(result.getId()).isEqualTo(1L);
    }
}
