package nextstep.subway.fare.domain;

import static nextstep.subway.fare.domain.FareAge.ADULT;
import static nextstep.subway.fare.domain.FareAge.CHILD;
import static nextstep.subway.fare.domain.FareAge.INFANT;
import static nextstep.subway.fare.domain.FareAge.TEENAGER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FareAgeTest {

    @Test
    void 나이대별_생성_테스트() {
        //when
        FareAge infant = FareAge.from(1);
        FareAge child = FareAge.from(6);
        FareAge teenager = FareAge.from(13);
        FareAge adult = FareAge.from(20);

        //then
        assertThat(infant).isEqualTo(INFANT);
        assertThat(child).isEqualTo(CHILD);
        assertThat(teenager).isEqualTo(TEENAGER);
        assertThat(adult).isEqualTo(ADULT);
    }

    @Test
    void 나이대별_할인후_금액_조회() {
        //when
        Fare infantFare = INFANT.discount(new Fare(1250));
        Fare childFare = CHILD.discount(new Fare(1250));
        Fare teenagerFare = TEENAGER.discount(new Fare(1250));
        Fare adultFare = ADULT.discount(new Fare(1250));

        //then
        assertThat(infantFare).isEqualTo(new Fare(0));
        assertThat(childFare).isEqualTo(new Fare(450));
        assertThat(teenagerFare).isEqualTo(new Fare(720));
        assertThat(adultFare).isEqualTo(new Fare(1250));
    }
}