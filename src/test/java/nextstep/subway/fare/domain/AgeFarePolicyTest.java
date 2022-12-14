package nextstep.subway.fare.domain;

import static nextstep.subway.fare.domain.AgeFarePolicy.ADULT;
import static nextstep.subway.fare.domain.AgeFarePolicy.CHILD;
import static nextstep.subway.fare.domain.AgeFarePolicy.INFANT;
import static nextstep.subway.fare.domain.AgeFarePolicy.TEENAGER;
import static nextstep.subway.fare.domain.Fare.FREE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AgeFarePolicyTest {

    @Test
    void 나이대별_생성_테스트() {
        //when
        AgeFarePolicy infant = AgeFarePolicy.from(1);
        AgeFarePolicy child = AgeFarePolicy.from(6);
        AgeFarePolicy teenager = AgeFarePolicy.from(13);
        AgeFarePolicy adult = AgeFarePolicy.from(20);

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
        assertThat(infantFare).isEqualTo(FREE);
        assertThat(childFare).isEqualTo(new Fare(450));
        assertThat(teenagerFare).isEqualTo(new Fare(720));
        assertThat(adultFare).isEqualTo(new Fare(1250));
    }
}