package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static nextstep.subway.fare.domain.FaresByDistance.BASIC_FARE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineResponseTest {
    private Station 양평역;
    private Station 영등포구청역;
    private Station 영등포시장역;
    private Station 신길역;
    private Station 오목교역;

    private Line 오호선;
    private Line 육호선;

    private Distance 기본_구간_거리_30 = new Distance(30);

    @BeforeEach
    void 구간_생성() {
        양평역 = new Station(1L, "양평역");
        영등포구청역 = new Station(2L, "영등포구청역");
        영등포시장역 = new Station(3L, "영등포시장역");
        신길역 = new Station(4L, "신길역");
        오목교역 = new Station(5L, "오목교역");

        오호선 = new Line(1L, "5호선", "보라색", 영등포구청역, 신길역, 기본_구간_거리_30, BASIC_FARE);
        육호선 = new Line(2L, "6호선", "노란색", 신길역, 오목교역, 기본_구간_거리_30, BASIC_FARE);
    }

    @Test
    void of_성공_케이스1() {
        // when
        LineResponse lineResponse = LineResponse.of(오호선);

        // then
        assertAll(
                () -> assertThat(lineResponse.getId()).isEqualTo(오호선.getId()),
                () -> assertThat(lineResponse.getName()).isEqualTo(오호선.getName()),
                () -> assertThat(lineResponse.getColor()).isEqualTo(오호선.getColor()),
                () -> assertThat(lineResponse.getStations()).isEqualTo(StationResponse.of(오호선.getStations()))
        );
    }

    @Test
    void of_성공_케이스2() {
        // when
        List<Line> lines = asList(오호선, 육호선);
        List<LineResponse> lineResponses = LineResponse.of(lines);

        // then
        for (int i = 0; i < lines.size(); i++) {
            int finalI = i;
            assertAll(
                    () -> assertThat(lineResponses.get(finalI).getId()).isEqualTo(lines.get(finalI).getId()),
                    () -> assertThat(lineResponses.get(finalI).getName()).isEqualTo(lines.get(finalI).getName()),
                    () -> assertThat(lineResponses.get(finalI).getColor()).isEqualTo(lines.get(finalI).getColor()),
                    () -> assertThat(lineResponses.get(finalI).getStations()).isEqualTo(StationResponse.of(lines.get(finalI).getStations()))
            );
        }

    }
}