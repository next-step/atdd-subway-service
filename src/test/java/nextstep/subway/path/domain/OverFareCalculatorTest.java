package nextstep.subway.path.domain;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OverFareCalculatorTest {

    @ParameterizedTest
    @CsvSource(delimiterString = ":", value ={"5:0", "10:0","11:100","20:200","50:800","51:900","66:1000","67:1100"} )
    void 거리별_추가요금_계산_테스트(String distance, String expectedOverFare){
        PathFindResult pathFindResult = new PathFindResult(emptyList(), emptySet(),Integer.parseInt(distance));
        int overFare = OverFareCalculator.calculateOverFareByDistance(pathFindResult);
        assertThat(overFare).isEqualTo(Integer.parseInt(expectedOverFare));
    }

    @Test
    void 노선별_추가요금_계산_테스트(){

    }

    @Test
    void 연령별_추가요금_계산_테스트(){

    }
}
