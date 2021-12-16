package nextstep.subway.line.domain;

import nextstep.subway.exception.InputDataErrorCode;
import nextstep.subway.exception.InputDataErrorException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FareTest {

    @Test
    @DisplayName("요금이 0원 미만일 경우 테스트")
    public void feeLessthanZeroTest(){
        Station 당산역 = new Station("당산역");
        Station 합정역 = new Station("합정역");

        assertThatThrownBy(() -> {
                    new Line("이호선", "green", 당산역, 합정역, 10, -1);
                }).isInstanceOf(InputDataErrorException.class)
                .hasMessageContaining(InputDataErrorCode.THE_FEE_IS_LESS_THAN_ZERO.errorMessage());
    }

    @Test
    @DisplayName("요금 정상 저장 테스트")
    public void saveFareTest(){
        Station 당산역 = new Station("당산역");
        Station 합정역 = new Station("합정역");

        Line line = new Line("이호선", "green", 당산역, 합정역, 10, 100);
        assertThat(line.getExtraFare().getAmount()).isEqualTo(100);
    }

    @Test
    @DisplayName("요금 더하기 테스트")
    public void sumFareTest(){
        Station 당산역 = new Station("당산역");
        Station 합정역 = new Station("합정역");

        Line line = new Line("이호선", "green", 당산역, 합정역, 10, 100);
        assertThat(line.getExtraFare().sum(new Fare(200)).getAmount()).isEqualTo(300);
    }

    @Test
    @DisplayName("요금 빼기 테스트")
    public void minusFareTest(){
        Station 당산역 = new Station("당산역");
        Station 합정역 = new Station("합정역");

        Line line = new Line("이호선", "green", 당산역, 합정역, 10, 100);
        assertThat(line.getExtraFare().minus(new Fare(50)).getAmount()).isEqualTo(50);
    }

    @Test
    @DisplayName("요금 곱하기 테스트")
    public void multiplyFareTest(){
        Station 당산역 = new Station("당산역");
        Station 합정역 = new Station("합정역");

        Line line = new Line("이호선", "green", 당산역, 합정역, 10, 100);
        assertThat(line.getExtraFare().multiply(0.5f).getAmount()).isEqualTo(50);
    }

}
