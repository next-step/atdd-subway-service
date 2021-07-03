package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static nextstep.subway.path.domain.FarePolicyByLineTest.getTestLineByExtraFare;
import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @ParameterizedTest
    @MethodSource("provideParameterForFareCalculate")
    void 회원별_거리별_요금계산(int age, List<Line> transferLines, double distance, double expectedFare) {
        //given
        List<FarePolicy> farePolicies = new ArrayList<>();
        //라인별 정책
        farePolicies.add(new FarePolicyByLine(transferLines));
        //거리별 정책
        farePolicies.add(new FarePolicyByDistance(distance));
        //나이별 정책
        farePolicies.add(new FarePolicyByAge(age));

        //when
        Fare fare = Fare.of(farePolicies);
        //then
        assertThat(fare.getFare()).isEqualTo(expectedFare);
    }

    /**
     * 성인회원이  900원 추가 요금이 있는 노선 8km 이용 시 2,150원
     * 성인회원이  900원 추가 요금이 있는 노선 12km 이용 시 2,250원
     * 청소년회원이 900원 추가 요금이 있는 노선 8km 이용 시 1440원
     * 청소년회원이 900원 추가 요금이 있는 노선 12km 이용 시 1520원
     * 어린이회원이 900원 추가 요금이 있는 노선 8km 이용 시 900원
     * 어린이회원이 900원 추가 요금이 있는 노선 12km 이용 시 950원
     */
    private static Stream<Arguments> provideParameterForFareCalculate() {
        Line 추가요금_900원_노선 = getTestLineByExtraFare(900);
        Line 추가요금_500원_노선 = getTestLineByExtraFare(500);
        Line 추가요금_0원_노선 = getTestLineByExtraFare(0);
        return Stream.of(
                Arguments.of(20, Arrays.asList(추가요금_900원_노선, 추가요금_500원_노선, 추가요금_0원_노선), 8, 2150),
                Arguments.of(20, Arrays.asList(추가요금_900원_노선, 추가요금_500원_노선, 추가요금_0원_노선), 12, 2250),
                Arguments.of(13, Arrays.asList(추가요금_900원_노선, 추가요금_500원_노선, 추가요금_0원_노선), 8, 1440),
                Arguments.of(13, Arrays.asList(추가요금_900원_노선, 추가요금_500원_노선, 추가요금_0원_노선), 12, 1520),
                Arguments.of(6, Arrays.asList(추가요금_900원_노선, 추가요금_500원_노선, 추가요금_0원_노선), 8, 900),
                Arguments.of(6, Arrays.asList(추가요금_900원_노선, 추가요금_500원_노선, 추가요금_0원_노선), 12, 950)
        );
    }
}