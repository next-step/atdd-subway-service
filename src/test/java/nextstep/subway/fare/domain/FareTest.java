package nextstep.subway.fare.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FareTest {

    @Test
    @DisplayName("기본요금")
    public void 기본요금() {
       Fare defaultFare =  Fare.of(10, 0 , 30);
        assertEquals(defaultFare.getValue(), 1250);
    }

    @Test
    @DisplayName("이용 거리초과 시 추가운임 부과 10km 초과∼50km 까지(5km마다 100원)")
    public void 초과금액_50km_이내() {
        Fare defaultFare =  Fare.of(43, 0, 30);
        assertEquals(defaultFare.getValue(), 1850);
    }

    @Test
    @DisplayName("이용 거리초과 시 추가운임 부과 50km 초과 시 (8km마다 100원)")
    public void 초과금액_50km_초과() {
        Fare defaultFare =  Fare.of(71, 0 , 30);
        assertEquals(defaultFare.getValue(), 2250);
    }

    @Test
    @DisplayName("어린이 할인: 6세 이상~ 13세 미만")
    public void 어린이_할인() {
        Fare defaultFare =  Fare.of(71, 0, 7);
        assertEquals(defaultFare.getValue(), 950);
    }

    @Test
    @DisplayName("청소년 할인: 13세 이상~19세 미만")
    public void 청소년_할인() {
        Fare defaultFare =  Fare.of(71,0, 15);
        assertEquals(defaultFare.getValue(), 1520);
    }


}
