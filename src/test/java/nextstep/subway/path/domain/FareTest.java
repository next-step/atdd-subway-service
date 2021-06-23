package nextstep.subway.path.domain;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.stream.IntStream;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Fare 단위 테스트")
class FareTest {

    private BelongLines noSurcharge;
    private BelongLines oneSurcharge;
    private BelongLines twoSurcharge;

    private DiscountStrategy noDiscountStrategy;
    private DiscountStrategy childDiscountStrategy;
    private DiscountStrategy teenagerDiscountStrategy;

    @BeforeEach
    void setUp() {

        List<Station> stations = ImmutableList.of(
            new Station("name1"), new Station("name2"), new Station("name3"),
            new Station("name4"), new Station("name5"), new Station("name6"));

        Line line0 = new Line("name1", "color", 0, stations.get(0), stations.get(1), 1);
        Line line500 = new Line("name1", "color", 500, stations.get(2), stations.get(3), 1);
        Line line900 = new Line("name1", "color", 900, stations.get(4), stations.get(5), 1);

        noSurcharge = new BelongLines(ImmutableList.of(line0), stations);
        oneSurcharge = new BelongLines(ImmutableList.of(line500), stations);
        twoSurcharge = new BelongLines(ImmutableList.of(line500, line900), stations);

        noDiscountStrategy = new NoDiscountStrategy();
        childDiscountStrategy = new ChildDiscountStrategy();
        teenagerDiscountStrategy = new TeenagerDiscountStrategy();
    }

    @DisplayName("10km 이하는 1250원 고정 운임")
    @MethodSource("lessThen10")
    @ParameterizedTest
    void lessThen10Test01(int distance) {
        Fare fare = new Fare(new Distance(distance), noSurcharge);
        assertThat(fare.getTotalFare(noDiscountStrategy)).isEqualTo(1250);
    }

    @DisplayName("10km 이하 고정 운임 + 노선 추가 요금 500원")
    @MethodSource("lessThen10")
    @ParameterizedTest
    void lessThen10Test02(int distance) {
        Fare fare = new Fare(new Distance(distance), oneSurcharge);
        assertThat(fare.getTotalFare(noDiscountStrategy)).isEqualTo(1250 + 500);
    }

    @DisplayName("10km 이하 고정 운임, 추가 요금이 있는 노선을 2개 탄 경우 최대 추가 요금 적용(900원)")
    @MethodSource("lessThen10")
    @ParameterizedTest
    void lessThen10Test03(int distance) {
        Fare fare = new Fare(new Distance(distance), twoSurcharge);
        assertThat(fare.getTotalFare(noDiscountStrategy)).isEqualTo(1250 + 900);
    }

    @DisplayName("10km 이하 고정 운임, 어린이 할인 적용")
    @MethodSource("lessThen10")
    @ParameterizedTest
    void lessThen10Test04(int distance) {
        Fare fare = new Fare(new Distance(distance), noSurcharge);
        assertThat(fare.getTotalFare(childDiscountStrategy)).isEqualTo((1250 - 350) / 2);
    }

    @DisplayName("10km 이하 고정 운임, 청소년 할인 적용")
    @MethodSource("lessThen10")
    @ParameterizedTest
    void lessThen10Test05(int distance) {
        Fare fare = new Fare(new Distance(distance), noSurcharge);
        assertThat(fare.getTotalFare(teenagerDiscountStrategy)).isEqualTo((1250 - 350) / 5 * 4);
    }

    @DisplayName("10km 초과 50km 미만인 경우, 5km 마다 100원씩 추가(1 ~ 5km 100원, 6 ~ 10km 200원, ...)")
    @CsvSource(value = {"10,0", "11,100", "14,100", "15,100", "16,200", "45,700", "47,800"})
    @ParameterizedTest
    void additionalFareTest01(int distance, int additionalFare) {
        Fare fare = new Fare(new Distance(distance), noSurcharge);
        assertThat(fare.getTotalFare(noDiscountStrategy)).isEqualTo(1250 + additionalFare);
    }

    @DisplayName("50km 이상인 경우, 50km 초과분은 8km 마다 100원씩 추가(1 ~ 8km 100원, 9 ~ 16km 200원, ...)")
    @CsvSource(value = {"50,800", "51,900", "55,900", "58,900", "60,1000", "70,1100"})
    @ParameterizedTest
    void additionalFareTest02(int distance, int additionalFare) {
        Fare fare = new Fare(new Distance(distance), noSurcharge);
        assertThat(fare.getTotalFare(noDiscountStrategy)).isEqualTo(1250 + additionalFare);
    }

    @SuppressWarnings("unused")
    private static IntStream lessThen10() {
        return IntStream.range(1, 10);
    }
}
