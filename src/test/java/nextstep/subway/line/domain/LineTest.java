package nextstep.subway.line.domain;

import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @DisplayName("구간 응답 객체 생성")
    @Test
    void findLineResponse() {
        Station A역 = new Station(1L, "A역");
        Station B역 = new Station(1L, "B역");
        Line line = new Line("2호선", "green", A역, B역, 10);

        LineResponse lineResponse = line.findLineResponse();

        assertThat(lineResponse.getName()).isEqualTo("2호선");
        assertThat(lineResponse.getColor()).isEqualTo("green");
        assertThat(lineResponse.getStations()).containsExactly(
                new StationResponse(A역.getId(), A역.getName()),
                new StationResponse(B역.getId(), B역.getName()));
    }
}
