package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LineResponseTest {

    @DisplayName("LineResponse 생성 테스트")
    @Test
    void createLineResponse() {
        List<Station> stations = Arrays.asList(
                new Station("강남역"),
                new Station("광교역")
        );

        LineResponse lineResponse = LineResponse.of(new Line("신분당선", "빨간색", new Station("강남역"), new Station("광교역"), 10));

        List<String> stationNames = lineResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        List<String> expectedStationNames = stations.stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        assertThat(stationNames).containsExactlyElementsOf(expectedStationNames);
    }
}
