package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SectionGroupTest {
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private StationRepository stationRepository;

    @DisplayName("역목록은 상행역 부터 하행역 순으로 정렬 되어야 한다.")
    @Test
    void getStations() {
        // given
        final Station firstStation = stationRepository.save(Station.of("1번"));
        final Station secondStation = stationRepository.save(Station.of("2번"));
        final Station thirdStation = stationRepository.save(Station.of("3번"));
        final Station forthStation = stationRepository.save(Station.of("4번"));
        final Station fifthStation = stationRepository.save(Station.of("5번"));
        final Line line = lineRepository.save(Line.of("노선이름", "색상", Arrays.asList(
                Section.of(thirdStation, forthStation, 10),
                Section.of(forthStation, fifthStation, 10),
                Section.of(firstStation, secondStation, 10),
                Section.of(secondStation, thirdStation, 10)
        )));
        // when
        final List<Station> stationsOrderByUptoDown = line.getStations();
        // then
        assertThat(stationsOrderByUptoDown).containsExactlyElementsOf(Arrays.asList(firstStation, secondStation, thirdStation, forthStation, fifthStation));
    }
}
