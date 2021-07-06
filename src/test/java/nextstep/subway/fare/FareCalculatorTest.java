package nextstep.subway.fare;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.domain.User;
import nextstep.subway.fare.domain.Fare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.TestFixture.TEENAGER_AGE;
import static nextstep.subway.TestFixture.TEENAGER_EMAIL;
import static nextstep.subway.fare.domain.Fare.BASE_FARE;
import static nextstep.subway.fare.domain.Fare.DISTANCE_EXTRA_CHARGE_UNIT;
import static org.assertj.core.api.Assertions.assertThat;

public class FareCalculatorTest {

    private final int 거리 = 5;
    private final Fare 노선_추가요금 = new Fare(500);
    private final Fare 거리별_추가요금 = new Fare(DISTANCE_EXTRA_CHARGE_UNIT);
    private User 일반사용자;
    private User 청소년_사용자;

    @BeforeEach
    void setUp() {
        일반사용자 = new User();
        청소년_사용자 = new LoginMember(1L, TEENAGER_EMAIL, TEENAGER_AGE);
    }

    @DisplayName("사용자 정보와 거리가 주어졌을때 성인요금을 계산한다")
    @Test
    void 사용자정보_거리로_요금계산_성인() {
        //Given
        Fare 예상요금 = new Fare(BASE_FARE).plus(노선_추가요금);
        FareCalculator 요금계산기 = new FareCalculator(일반사용자, 거리, 노선_추가요금);

        //When
        Fare 실제요금 = 요금계산기.calculate();

        //Then
        assertThat(실제요금).isEqualTo(예상요금);
    }

    @DisplayName("사용자 정보와 거리가 주어졌을때 청소년 할인요금을 계산한다")
    @Test
    void 사용자정보_거리로_요금계산_청소년() {
        //Given
        int 요금할인_공제액 = 350;
        int 청소년_할인율 = 20;
        Fare 예상요금 = new Fare((BASE_FARE + 노선_추가요금.getFare() - 요금할인_공제액) * (100 - 청소년_할인율) / 100);
        FareCalculator 요금계산기 = new FareCalculator(청소년_사용자, 거리, 노선_추가요금);

        //When
        Fare 실제요금 = 요금계산기.calculate();

        //Then
        assertThat(실제요금).isEqualTo(예상요금);
    }

    @DisplayName("거리가 11~50km 구간일 때, 5km당 100원의 추가요금이 붙는다")
    @Test
    void 거리_11에서_50km사이_5km당_추가요금_100원() {
        //Given
        int 거리_15km = 15;
        Fare 예상요금 = new Fare(BASE_FARE)
                .plus(노선_추가요금)
                .plus(거리별_추가요금);
        FareCalculator 요금계산기 = new FareCalculator(일반사용자, 거리_15km, 노선_추가요금);

        //When
        Fare 실제요금 = 요금계산기.calculate();

        //Then
        assertThat(실제요금).isEqualTo(예상요금);
    }

    @DisplayName("거리가 51km 이상 구간일 때, 8km당 100원의 추가요금이 붙는다")
    @Test
    void 거리_51km_이상_8km당_추가요금_100원() {
        //Given
        int 거리_51km = 51;
        Fare 예상요금 = new Fare(BASE_FARE)
                .plus(노선_추가요금)
                .plus(거리별_추가요금.multiply(8));
        FareCalculator 요금계산기 = new FareCalculator(일반사용자, 거리_51km, 노선_추가요금);

        //When
        Fare 실제요금 = 요금계산기.calculate();

        //Then
        assertThat(실제요금).isEqualTo(예상요금);
    }
}
