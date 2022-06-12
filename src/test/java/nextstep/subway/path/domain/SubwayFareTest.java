package nextstep.subway.path.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;
class SubwayFareTest {

    @Test
    void 지하철요금_음수인경우_예외발생(){
        assertThatThrownBy(() -> SubwayFare.of(-1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 지하철요금_더하기(){
        SubwayFare fare = SubwayFare.of(1500);
        SubwayFare plusFare = fare.plus(500);
        assertThat(plusFare).isNotSameAs(fare)
                .isEqualTo(SubwayFare.of(2000));
    }

    @Test
    void 지하철요금_빼기(){
        SubwayFare fare = SubwayFare.of(1500);
        SubwayFare subtractFare = fare.subtract(500);
        assertThat(subtractFare).isNotSameAs(fare)
                .isEqualTo(SubwayFare.of(1000));
    }

    @Test
    void 지하철요금_할인율_적용(){
        SubwayFare fare = SubwayFare.of(10000);
        SubwayFare discountedFare = fare.discountedByPercent(30);
        assertThat(discountedFare).isNotSameAs(fare)
                .isEqualTo(SubwayFare.of(7000));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 101})
    void 지하철요금_할인율_적용_예외(int discount){
        SubwayFare fare = SubwayFare.of(10000);
        assertThatThrownBy(() ->  fare.discountedByPercent(discount)).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource(delimiterString = ":", value ={"5:1250", "10:1250","11:1350","20:1450","50:2050","51:2150","66:2250","67:2350"} )
    void 거리에_따른_지하철요금_계산(String distance, String expectedFare){
        SubwayFare fare = SubwayFare.calculateByDistance(Integer.parseInt(distance));
        assertThat(fare).isEqualTo(SubwayFare.of(Integer.parseInt(expectedFare)));
    }

}
