package nextstep.subway.line.domain.collections;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.StationResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("노선목록 이용 관련 단위테스트")
@DataJpaTest
class LinesTest {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    private final Station 구로 = new Station("구로");
    private final Station 독산 = new Station("독산");
    private final Station 신풍 = new Station("신풍");
    private final Station 신림 = new Station("신림");
    private final Station 신도림 = new Station("신도림");
    private final Station 남구로 = new Station("남구로");
    private final Station 가산디지털단지 = new Station("가산디지털단지");
    private final Station 구로디지털단지 = new Station("구로디지털단지");
    private final Line 일호선 = new Line("일호선", "bg-blue-100", 신도림, 구로, 5);
    private final Line 이호선 = new Line("이호선", "bg-green-100", 신도림, 신풍, 10);
    private final Line 칠호선 = new Line("칠호선", "bg-dark-green-100", 가산디지털단지, 남구로, 5);


    @BeforeEach
    void setUp() {
        일호선.addNewSection(구로, 가산디지털단지, 15);
        일호선.addNewSection(가산디지털단지, 독산, 5);
        이호선.addNewSection(신풍, 신림, 10);
        칠호선.addNewSection(남구로, 신풍, 5);

        stationRepository.saveAll(Arrays.asList(독산,구로디지털단지));
        lineRepository.saveAll(Arrays.asList(일호선,이호선,칠호선));
    }


    @DisplayName("출발역과 도착역 사이의 경유역들과 최단거리를 조회한다.")
    @Test
    void findShortestPath(){

        //when
        Lines lines = new Lines(lineRepository.findAll());
        PathResponse pathResponse = lines.findShortestPath(독산, 신림);

        //then
        List<String> pathStationNames = pathResponse.getStations().stream().map(StationResponse::getName)
                .collect(Collectors.toList());
        assertThat(pathStationNames).containsExactly("독산","가산디지털단지","남구로","신풍","신림");
        assertThat(pathResponse.getDistance()).isEqualTo(25);
    }


}
