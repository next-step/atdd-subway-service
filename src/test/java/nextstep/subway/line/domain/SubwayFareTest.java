package nextstep.subway.line.domain;

import nextstep.subway.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
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
    private SubwayUser infantUser;
    private SubwayUser childUser;
    private SubwayUser youthUser;
    private SubwayUser adultUser;

    @BeforeEach
    void setUp() {
        final Member infant = new Member("infant@gmail.com", "11", 3);
        final Member child = new Member("child@gmail.com", "11", 6);
        final Member youth = new Member("child@gmail.com", "11", 17);
        final Member adult = new Member("child@gmail.com", "11", 20);

        infantUser = SubwayUser.of(infant.getAge());
        childUser = SubwayUser.of(child.getAge());
        youthUser = SubwayUser.of(youth.getAge());
        adultUser = SubwayUser.of(adult.getAge());
    }

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
        Money result10 = rateInquiry(distance10);
        Money result15 = rateInquiry(distance15);
        Money result20 = rateInquiry(distance20);
        Money result25 = rateInquiry(distance25);
        Money result30 = rateInquiry(distance30);
        Money result35 = rateInquiry(distance35);
        Money result40 = rateInquiry(distance40);
        Money result45 = rateInquiry(distance45);
        Money result50 = rateInquiry(distance50);
        Money result58 = rateInquiry(distance58);
        Money result66 = rateInquiry(distance66);
        Money result74 = rateInquiry(distance74);
        Money result82 = rateInquiry(distance82);
        Money result90 = rateInquiry(distance90);
        Money result98 = rateInquiry(distance98);
        Money result106 = rateInquiry(distance106);

        // then
        assertThat(result10).isEqualTo(Money.of(1_250));
        assertThat(result15).isEqualTo(Money.of(1_350));
        assertThat(result20).isEqualTo(Money.of(1_450));
        assertThat(result25).isEqualTo(Money.of(1_550));
        assertThat(result30).isEqualTo(Money.of(1_650));
        assertThat(result35).isEqualTo(Money.of(1_750));
        assertThat(result40).isEqualTo(Money.of(1_850));
        assertThat(result45).isEqualTo(Money.of(1_950));
        assertThat(result50).isEqualTo(Money.of(2_050));
        assertThat(result58).isEqualTo(Money.of(2_150));
        assertThat(result66).isEqualTo(Money.of(2_250));
        assertThat(result74).isEqualTo(Money.of(2_350));
        assertThat(result82).isEqualTo(Money.of(2_450));
        assertThat(result90).isEqualTo(Money.of(2_550));
        assertThat(result98).isEqualTo(Money.of(2_650));
        assertThat(result106).isEqualTo(Money.of(2_750));

        // 할인액 계산
        assertThat(SubwayFare.discountFare(result10, infantUser)).isEqualTo(Money.of(Money.MIN_VALUE));
        assertThat(SubwayFare.discountFare(result10, infantUser)).isEqualTo(Money.of(Money.MIN_VALUE));
        assertThat(SubwayFare.discountFare(result15, infantUser)).isEqualTo(Money.of(Money.MIN_VALUE));
        assertThat(SubwayFare.discountFare(result20, infantUser)).isEqualTo(Money.of(Money.MIN_VALUE));
        assertThat(SubwayFare.discountFare(result25, infantUser)).isEqualTo(Money.of(Money.MIN_VALUE));
        assertThat(SubwayFare.discountFare(result30, infantUser)).isEqualTo(Money.of(Money.MIN_VALUE));
        assertThat(SubwayFare.discountFare(result35, infantUser)).isEqualTo(Money.of(Money.MIN_VALUE));
        assertThat(SubwayFare.discountFare(result40, infantUser)).isEqualTo(Money.of(Money.MIN_VALUE));
        assertThat(SubwayFare.discountFare(result45, infantUser)).isEqualTo(Money.of(Money.MIN_VALUE));
        assertThat(SubwayFare.discountFare(result50, infantUser)).isEqualTo(Money.of(Money.MIN_VALUE));
        assertThat(SubwayFare.discountFare(result58, infantUser)).isEqualTo(Money.of(Money.MIN_VALUE));
        assertThat(SubwayFare.discountFare(result66, infantUser)).isEqualTo(Money.of(Money.MIN_VALUE));
        assertThat(SubwayFare.discountFare(result74, infantUser)).isEqualTo(Money.of(Money.MIN_VALUE));
        assertThat(SubwayFare.discountFare(result82, infantUser)).isEqualTo(Money.of(Money.MIN_VALUE));
        assertThat(SubwayFare.discountFare(result90, infantUser)).isEqualTo(Money.of(Money.MIN_VALUE));
        assertThat(SubwayFare.discountFare(result98, infantUser)).isEqualTo(Money.of(Money.MIN_VALUE));
        assertThat(SubwayFare.discountFare(result106, infantUser)).isEqualTo(Money.of(Money.MIN_VALUE));

        assertThat(SubwayFare.discountFare(result10, childUser)).isEqualTo(Money.of(450));
        assertThat(SubwayFare.discountFare(result15, childUser)).isEqualTo(Money.of(500));
        assertThat(SubwayFare.discountFare(result20, childUser)).isEqualTo(Money.of(550));
        assertThat(SubwayFare.discountFare(result25, childUser)).isEqualTo(Money.of(600));
        assertThat(SubwayFare.discountFare(result30, childUser)).isEqualTo(Money.of(650));
        assertThat(SubwayFare.discountFare(result35, childUser)).isEqualTo(Money.of(700));
        assertThat(SubwayFare.discountFare(result40, childUser)).isEqualTo(Money.of(750));
        assertThat(SubwayFare.discountFare(result45, childUser)).isEqualTo(Money.of(800));
        assertThat(SubwayFare.discountFare(result50, childUser)).isEqualTo(Money.of(850));
        assertThat(SubwayFare.discountFare(result58, childUser)).isEqualTo(Money.of(900));
        assertThat(SubwayFare.discountFare(result66, childUser)).isEqualTo(Money.of(950));
        assertThat(SubwayFare.discountFare(result74, childUser)).isEqualTo(Money.of(1000));
        assertThat(SubwayFare.discountFare(result82, childUser)).isEqualTo(Money.of(1050));
        assertThat(SubwayFare.discountFare(result90, childUser)).isEqualTo(Money.of(1100));
        assertThat(SubwayFare.discountFare(result98, childUser)).isEqualTo(Money.of(1150));
        assertThat(SubwayFare.discountFare(result106, childUser)).isEqualTo(Money.of(1200));

        assertThat(SubwayFare.discountFare(result10, youthUser)).isEqualTo(Money.of(720));
        assertThat(SubwayFare.discountFare(result15, youthUser)).isEqualTo(Money.of(800));
        assertThat(SubwayFare.discountFare(result20, youthUser)).isEqualTo(Money.of(880));
        assertThat(SubwayFare.discountFare(result25, youthUser)).isEqualTo(Money.of(960));
        assertThat(SubwayFare.discountFare(result30, youthUser)).isEqualTo(Money.of(1040));
        assertThat(SubwayFare.discountFare(result35, youthUser)).isEqualTo(Money.of(1120));
        assertThat(SubwayFare.discountFare(result40, youthUser)).isEqualTo(Money.of(1200));
        assertThat(SubwayFare.discountFare(result45, youthUser)).isEqualTo(Money.of(1280));
        assertThat(SubwayFare.discountFare(result50, youthUser)).isEqualTo(Money.of(1360));
        assertThat(SubwayFare.discountFare(result58, youthUser)).isEqualTo(Money.of(1440));
        assertThat(SubwayFare.discountFare(result66, youthUser)).isEqualTo(Money.of(1520));
        assertThat(SubwayFare.discountFare(result74, youthUser)).isEqualTo(Money.of(1600));
        assertThat(SubwayFare.discountFare(result82, youthUser)).isEqualTo(Money.of(1680));
        assertThat(SubwayFare.discountFare(result90, youthUser)).isEqualTo(Money.of(1760));
        assertThat(SubwayFare.discountFare(result98, youthUser)).isEqualTo(Money.of(1840));
        assertThat(SubwayFare.discountFare(result106, youthUser)).isEqualTo(Money.of(1920));

        assertThat(SubwayFare.discountFare(result10, adultUser)).isEqualTo(result10);
        assertThat(SubwayFare.discountFare(result15, adultUser)).isEqualTo(result15);
        assertThat(SubwayFare.discountFare(result20, adultUser)).isEqualTo(result20);
        assertThat(SubwayFare.discountFare(result25, adultUser)).isEqualTo(result25);
        assertThat(SubwayFare.discountFare(result30, adultUser)).isEqualTo(result30);
        assertThat(SubwayFare.discountFare(result35, adultUser)).isEqualTo(result35);
        assertThat(SubwayFare.discountFare(result40, adultUser)).isEqualTo(result40);
        assertThat(SubwayFare.discountFare(result45, adultUser)).isEqualTo(result45);
        assertThat(SubwayFare.discountFare(result50, adultUser)).isEqualTo(result50);
        assertThat(SubwayFare.discountFare(result58, adultUser)).isEqualTo(result58);
        assertThat(SubwayFare.discountFare(result66, adultUser)).isEqualTo(result66);
        assertThat(SubwayFare.discountFare(result74, adultUser)).isEqualTo(result74);
        assertThat(SubwayFare.discountFare(result82, adultUser)).isEqualTo(result82);
        assertThat(SubwayFare.discountFare(result90, adultUser)).isEqualTo(result90);
        assertThat(SubwayFare.discountFare(result98, adultUser)).isEqualTo(result98);
        assertThat(SubwayFare.discountFare(result106, adultUser)).isEqualTo(result106);
    }
}
