package nextstep.subway.line.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class LineServiceTest {

    @Autowired
    LineService lineService;

    @Autowired
    StationService stationService;

    @Test
    @DisplayName("최단 경로 탐색")
    void path(){
        // given
        StationResponse station1 = stationService.saveStation(new StationRequest("영등포구청역"));
        StationResponse station2 = stationService.saveStation(new StationRequest("여의도역"));
        StationResponse station3 = stationService.saveStation(new StationRequest("마포역"));
        StationResponse station4 = stationService.saveStation(new StationRequest("공덕역"));
        LineResponse line = lineService.saveLine(
                new LineRequest("5호선", "red", station1.getId(), station2.getId(), 2));

        lineService.addLineStation(line.getId(), new SectionRequest(station2.getId(), station3.getId(), 5));
        lineService.addLineStation(line.getId(), new SectionRequest(station3.getId(), station4.getId(), 4));

        // when
        PathResponse path = lineService.path(station1.getId(), station4.getId());

        // then
        List<String> stationNames = path.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertThat(path.getDistance()).isEqualTo(11);
        assertThat(stationNames).containsExactly(
                "영등포구청역","여의도역","마포역","공덕역"
        );

    }
}
