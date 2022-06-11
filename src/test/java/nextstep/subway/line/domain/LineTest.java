package nextstep.subway.line.domain;

import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {
    private Station A역;
    private Station B역;
    private Station C역;
    private Station D역;
    private Station E역;
    private Station F역;

    @BeforeEach
    void setUp() {
        A역 = new Station(1L, "A역");
        B역 = new Station(2L, "B역");
        C역 = new Station(3L, "C역");
        D역 = new Station(4L, "D역");
        E역 = new Station(5L, "E역");
        F역 = new Station(6L, "F역");
    }

    @DisplayName("구간 지하철 역 응답 객체 생성")
    @Test
    void findStationResponses() {
        // given
        Line line = new Line("2호선", "green", A역, B역, 10);

        // when
        List<StationResponse> responses = line.findStationResponses();

        // then
        assertThat(responses).containsExactly(
                new StationResponse(A역.getId(), A역.getName()),
                new StationResponse(B역.getId(), B역.getName()));
    }

    @DisplayName("구간 응답 객체 생성")
    @Test
    void findLineResponse() {
        // given
        Line line = new Line("2호선", "green", A역, B역, 10);

        // when
        LineResponse lineResponse = line.findLineResponse();

        // then
        assertThat(lineResponse.getName()).isEqualTo("2호선");
        assertThat(lineResponse.getColor()).isEqualTo("green");
        assertThat(lineResponse.getStations()).containsExactly(
                new StationResponse(A역.getId(), A역.getName()),
                new StationResponse(B역.getId(), B역.getName()));
    }

    @DisplayName("구간 추가 기능 테스트")
    @Test
    void addSection() {
        // given
        Line line = new Line("2호선", "green", B역, E역, 10);

        // when
        line.addSection(new Section(line, A역, B역, 10));
        line.addSection(new Section(line, B역, C역, 3));
        line.addSection(new Section(line, D역, E역, 3));
        line.addSection(new Section(line, E역, F역, 10));

        // then
        List<StationResponse> responses = line.findStationResponses();
        assertThat(responses).containsExactly(
                new StationResponse(A역.getId(), A역.getName()),
                new StationResponse(B역.getId(), B역.getName()),
                new StationResponse(C역.getId(), C역.getName()),
                new StationResponse(D역.getId(), D역.getName()),
                new StationResponse(E역.getId(), E역.getName()),
                new StationResponse(F역.getId(), F역.getName()));
    }
}
