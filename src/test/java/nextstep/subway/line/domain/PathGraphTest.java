package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class PathGraphTest {

    @Autowired
    LineService lineService;

    @Autowired
    StationService stationService;

    @Autowired
    LineRepository lineRepository;

    StationResponse station1;
    StationResponse station2;
    LineResponse line;
    @BeforeEach
    void beforeEach(){
        station1 = stationService.saveStation(new StationRequest("암사역"));
        station2 = stationService.saveStation(new StationRequest("천호역"));
        line = lineService.saveLine(new LineRequest("8호선", "bg-color-333", station1.getId(), station2.getId(), 10));
    }

    @Test
    void path(){
        // given

        // when
        GraphPath path = PathGraph.findPath(station1.getId(), station2.getId(), lineRepository.findAll());

        // then
        assertThat(path.getVertexList()).containsExactly(
                station1.getId().toString(), station2.getId().toString()
        );
        assertThat((int)path.getWeight()).isEqualTo(10);

    }
}
