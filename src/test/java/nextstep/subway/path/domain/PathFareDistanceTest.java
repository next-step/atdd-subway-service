package nextstep.subway.path.domain;

import nextstep.subway.line.domain.*;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathFareDistanceTest {

    private List<Section> 이호선구간목록 = new ArrayList<>();
    private List<Section> 삼호선구간목록 = new ArrayList<>();
    private Line 이호선 = null;
    private Line 삼호선 = null;
    private static Section 첫번째구간 = null;
    private static Section 두번째구간 = null;
    private static Section 세번째구간 = null;
    private static Station 잠실역 = null;
    private static Station 잠실새내역 = null;
    private static Station 강변역 = null;
    private static Station 몽촌토성역 = null;
    private static Distance 거리5 = null;

    @BeforeEach
    void setUp() {
        이호선 = new Line(1L, "2호선", "green", Sections.from(이호선구간목록), Fare.from(400));
        삼호선 = new Line(2L, "3호선", "green", Sections.from(삼호선구간목록), Fare.from(500));
        거리5 = Distance.of(5);
        거리5 = Distance.of(10);
        잠실역 = Station.from("잠실역");
        잠실새내역 = Station.from("잠실새내역");
        강변역 = Station.from("강변역");
        몽촌토성역 = Station.from("몽촌토성역");
        첫번째구간 = new Section(1L, 이호선, 잠실역, 잠실새내역, 거리5);
        두번째구간 = new Section(2L, 이호선, 잠실새내역, 강변역, 거리5);
        세번째구간 = new Section(3L, 삼호선, 강변역, 몽촌토성역, 거리5);
        이호선구간목록.add(첫번째구간);
        이호선구간목록.add(두번째구간);
        삼호선구간목록.add(세번째구간);
    }

    @DisplayName("로그인하지 않은 사용자의 경로 조회 시 거리 요금 계산 및 추가요금(500)을 더해서 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "10:1750",
            "14:1750",
            "15:1850",
            "20:1950",
            "25:2050",
            "30:2150",
            "35:2250",
            "40:2350",
            "45:2450",
            "49:2450",
            "50:2550",
            "58:2650",
            "63:2650"}, delimiter = ':')
    void calculatorFare(final int weight, final String expected) {
        final Path path = Path.of(weight, Arrays.asList(첫번째구간, 두번째구간, 세번째구간), Arrays.asList(잠실역, 몽촌토성역));
        final Fare fare = PathFareDistance.of(path);
        assertThat(fare.plus(Fare.from(new BigDecimal("1250"))).value()).isEqualTo(new BigDecimal(expected));
    }
}
