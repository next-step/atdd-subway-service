package nextstep.subway.line.domain;

import nextstep.subway.path.domain.KmPerFeePolicy;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LinesTest {

    private final Station stationA = Station.from("수원역");
    private final Station stationB = Station.from("성균관대역");
    private final Station stationC = Station.from("역삼역");
    private final Station stationD = Station.from("선흥역");
    private final Line 일호선 = Line.of("1호선", "blue", stationA, stationB, Distance.from(10), 500);
    private final Line 이호선 = Line.of("이호선", "green", stationC, stationD, Distance.from(10), 1000);


    @Test
    @DisplayName("최대 요금을 부담하는 구간의 요금을 구한다")
    void getMaxExtraFee() {
        List<Station> stations = Arrays.asList(stationA, stationB, stationC, stationD);
        Integer result = Lines.from(Arrays.asList(일호선, 이호선)).getPaidFee(stations, 100, new KmPerFeePolicy());

        assertThat(result).isEqualTo(1000);
    }
}
