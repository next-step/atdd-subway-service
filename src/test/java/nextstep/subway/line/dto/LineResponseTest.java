package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 반환 객체 테스트")
class LineResponseTest {

    private Line 신분당선;
    private Station 강남역;
    private Station 광교역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        신분당선 = new Line("신분당선", "red", 강남역, 광교역, 10);
    }

    @Test
    void 노선_객체를_이용하여_노선_반환_객체_생성() {
        LineResponse lineResponse = LineResponse.of(신분당선);
        assertThat(lineResponse.getName()).isEqualTo("신분당선");
        assertThat(lineResponse.getColor()).isEqualTo("red");
        List<StationResponse> stationResponses = Arrays.asList(강남역, 광교역).stream().map(StationResponse::of).collect(Collectors.toList());
        assertThat(lineResponse.getStations()).containsExactlyElementsOf(stationResponses);
    }
}
