package nextstep.subway.fare;

import static nextstep.subway.fare.domain.Fare.BASIC_FARE;
import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.fare.domain.Fare;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FareTest {
    private final int 청소년_나이 = 13;
    private final int 노선_추가_요금 = 900;
    private final int NON_MEMBER_AGE = -1;

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 10})
    void 비회원_기본_요금_생성(int distance) {
        Fare fare = Fare.calculateFare(distance, 0, NON_MEMBER_AGE);
        assertThat(fare.getFare()).isEqualTo(BASIC_FARE);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 10})
    void 회원_성인_기본_요금_생성(int distance) {
        Fare fare = Fare.calculateFare(distance, 0, 30);
        assertThat(fare.getFare()).isEqualTo(BASIC_FARE);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 10})
    void 회원_청소년_기본_요금_생성(int distance) {
        Fare fare = Fare.calculateFare(distance, 0, 청소년_나이);
        int 할인_금액 = (int) Math.ceil((BASIC_FARE - 350) * 0.2);
        assertThat(fare.getFare()).isEqualTo(BASIC_FARE - 할인_금액);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 10})
    void 회원_어린이_기본_요금_생성(int distance) {
        Fare fare = Fare.calculateFare(distance, 0, 7);
        int 할인_금액 = (int) Math.ceil((BASIC_FARE - 350) * 0.5);
        assertThat(fare.getFare()).isEqualTo(BASIC_FARE - 할인_금액);
    }

    @Test
    void 비회원_기본_요금과_노선_추가_요금_생성() {
        Fare fare = Fare.calculateFare(10, 노선_추가_요금, NON_MEMBER_AGE);
        assertThat(fare.getFare()).isEqualTo(BASIC_FARE + 노선_추가_요금);
    }

    @ParameterizedTest
    @ValueSource(ints = {11, 13, 15, 20, 30})
    void 비회원_기본_요금과_10km_초과_거리_추가_요금_생성(int distance) {
        Fare fare = Fare.calculateFare(distance, 0, NON_MEMBER_AGE);
        int 초과_거리_추가_요금 = (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
        assertThat(fare.getFare()).isEqualTo(BASIC_FARE + 초과_거리_추가_요금);
    }

    @ParameterizedTest
    @ValueSource(ints = {51, 55, 60, 70, 80})
    void 비회원_기본_요금과_50km_초과_거리_추가_요금_생성(int distance) {
        Fare fare = Fare.calculateFare(distance, 0, NON_MEMBER_AGE);
        int 초과_거리_추가_요금 = (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
        assertThat(fare.getFare()).isEqualTo(BASIC_FARE + 초과_거리_추가_요금);
    }
}
