package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PathGraphTest {

    @Autowired
    PathGraph pathGraph;

    @Autowired
    StationRepository stationRepository;

    @Test
    void path(){
        // given
        Station station1 = stationRepository.save(new Station("암사역"));
        Station station2 = stationRepository.save(new Station("천호역"));
        Station station3 = stationRepository.save(new Station("몽촌토성역"));
        Line line = new Line("8호선", "bg-color-555", station1, station2, 10, 800);
        line.addSection(station2, station3, 10);

        // when
        PathResult pathResult = pathGraph.findPath(station1, station3, line.getSections());
        List<String> list = pathResult.getStations().stream()
                .map(station -> StationResponse.of(station).getName())
                .collect(Collectors.toList());

        // then
        assertThat(list).containsExactly("암사역","천호역","몽촌토성역");
        assertThat(pathResult.getDistance()).isEqualTo(20);
        assertThat(pathResult.getLines()).contains(
                line
        );
    }
}
