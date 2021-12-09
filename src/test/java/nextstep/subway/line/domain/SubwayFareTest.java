package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.domain.SubwayFare.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : nextstep.subway.line.domain
 * fileName : SubwayFareTest
 * author : haedoang
 * date : 2021-12-09
 * description :
 */
public class SubwayFareTest {
    @Test
    @DisplayName("요금 구간조회")
    public void findFare() {
        // given
        Distance distance10 = Distance.of(10);
        Distance distance50 = Distance.of(50);
        Distance distance55 = Distance.of(55);

        // when
        SubwayFare result10 = of(distance10);
        SubwayFare result50 = of(distance50);
        SubwayFare result55 = of(distance55);

        // then
        assertThat(result10.isUntil10Km()).isTrue();
        assertThat(result50.isUntil50Km()).isTrue();
        assertThat(result55.isOver50Km()).isTrue();
    }

    @Test
    @DisplayName("부가 요금 거리 계산하기")
    public void chargeDistance() {
        // given
        Distance distance10 = Distance.of(10);
        Distance distance50 = Distance.of(50);
        Distance distance55 = Distance.of(55);
        Distance distance60 = Distance.of(60);

        // then
        assertThat(UNTIL_10KM.toChargeDistance(distance10)).isEqualTo(0);
        assertThat(UNTIL_10KM.toChargeDistance(distance50)).isEqualTo(0);
        assertThat(UNTIL_10KM.toChargeDistance(distance55)).isEqualTo(0);
        assertThat(UNTIL_10KM.toChargeDistance(distance60)).isEqualTo(0);

        assertThat(UNTIL_50KM.toChargeDistance(distance10)).isEqualTo(0);
        assertThat(UNTIL_50KM.toChargeDistance(distance50)).isEqualTo(39);
        assertThat(UNTIL_50KM.toChargeDistance(distance55)).isEqualTo(39);
        assertThat(UNTIL_50KM.toChargeDistance(distance60)).isEqualTo(39);

        assertThat(OVER_50KM.toChargeDistance(distance10)).isEqualTo(0);
        assertThat(OVER_50KM.toChargeDistance(distance50)).isEqualTo(0);
        assertThat(OVER_50KM.toChargeDistance(distance55)).isEqualTo(4);
        assertThat(OVER_50KM.toChargeDistance(distance60)).isEqualTo(9);
    }


    @Test
    @DisplayName("지하철 요금 계산하기 ")
    public void subwayFareCalculate() {
        // given
        Distance distance10 = Distance.of(10);
        Distance distance15 = Distance.of(15);
        Distance distance20 = Distance.of(20);
        Distance distance25 = Distance.of(25);
        Distance distance30 = Distance.of(30);
        Distance distance35 = Distance.of(35);
        Distance distance40 = Distance.of(40);
        Distance distance45 = Distance.of(45);
        Distance distance50 = Distance.of(50);
        Distance distance58 = Distance.of(58);
        Distance distance66 = Distance.of(66);
        Distance distance74 = Distance.of(74);
        Distance distance82 = Distance.of(82);
        Distance distance90 = Distance.of(90);
        Distance distance98 = Distance.of(98);
        Distance distance106 = Distance.of(106);

        // when
        int result10 = rateInquiry(distance10);
        int result15 = rateInquiry(distance15);
        int result20 = rateInquiry(distance20);
        int result25 = rateInquiry(distance25);
        int result30 = rateInquiry(distance30);
        int result35 = rateInquiry(distance35);
        int result40 = rateInquiry(distance40);
        int result45 = rateInquiry(distance45);
        int result50 = rateInquiry(distance50);
        int result58 = rateInquiry(distance58);
        int result66 = rateInquiry(distance66);
        int result74 = rateInquiry(distance74);
        int result82 = rateInquiry(distance82);
        int result90 = rateInquiry(distance90);
        int result98 = rateInquiry(distance98);
        int result106 = rateInquiry(distance106);

        // then
        assertThat(result10).isEqualTo(1_250);
        assertThat(result15).isEqualTo(1_350);
        assertThat(result20).isEqualTo(1_450);
        assertThat(result25).isEqualTo(1_550);
        assertThat(result30).isEqualTo(1_650);
        assertThat(result35).isEqualTo(1_750);
        assertThat(result40).isEqualTo(1_850);
        assertThat(result45).isEqualTo(1_950);
        assertThat(result50).isEqualTo(2_050);
        assertThat(result58).isEqualTo(2_150);
        assertThat(result66).isEqualTo(2_250);
        assertThat(result74).isEqualTo(2_350);
        assertThat(result82).isEqualTo(2_450);
        assertThat(result90).isEqualTo(2_550);
        assertThat(result98).isEqualTo(2_650);
        assertThat(result106).isEqualTo(2_750);
    }
}
