package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        오호선 = new Line(1L, "5호선", "보라색", 영등포구청역, 신길역, 기본_구간_거리_30);
        육호선 = new Line(2L, "6호선", "노란색", 신길역, 오목교역, 기본_구간_거리_30);
    }

    @Test
    void of_성공() {
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
}