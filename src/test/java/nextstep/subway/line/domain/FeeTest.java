package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FeeTest {

    @DisplayName("요금 생성")
    @Test
    void create() {
        //given
        Fee fee = new Fee();
        //when
        Fee defaultFee = new Fee(Fee.DEFAULT_FEE_AMOUNT);
        //then
        assertThat(fee).isEqualTo(defaultFee);
    }

    @DisplayName("요금 생성 - 값 지정")
    @Test
    void createFeeAmount() {
        //given
        Fee fee = new Fee(100);
        //when
        Fee specifyFee = new Fee(100);
        //then
        assertThat(fee).isEqualTo(specifyFee);
    }
}