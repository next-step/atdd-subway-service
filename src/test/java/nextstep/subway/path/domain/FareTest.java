package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.PathAcceptanceTest;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {
    private Line 칠호선;
    private Line 이호선;

    private Section 뚝유_건대;
    private Section 건대_구의;
    private Section 구의_강변;

    private Station 뚝섬유원지역;
    private Station 건대역;
    private Station 구의역;
    private Station 강변역;

    @BeforeEach
    void init() {
        뚝섬유원지역 = new Station("뚝섬유원지역");
        건대역 = new Station("건대역");
        구의역 = new Station("구의역");
        강변역 = new Station("강변역");

        칠호선 = new Line("칠호선", "blue", 뚝섬유원지역, 건대역, 10, 500);
        이호선 = new Line("이호선", "green", 건대역, 구의역, 22, 300);
        이호선.addLineStation(구의역, 강변역, 30);

        뚝유_건대 = new Section(칠호선, 뚝섬유원지역, 건대역, 10);
        건대_구의 = new Section(이호선, 건대역, 구의역, 22);
        구의_강변 = new Section(이호선, 구의역, 강변역, 30);
    }

    @Test
    void calculate_일반요금() {
        //given
        Path path = new Path(Arrays.asList(뚝섬유원지역, 건대역, 구의역), 32);
        int fare = 1_250 + 600 + 칠호선.getSurcharge();

        //when
        Fare 요금 = new Fare(Arrays.asList(뚝유_건대, 건대_구의), path, PathAcceptanceTest.ALL_AGE);

        //then
        assertThat(요금.getFare()).isEqualTo(2350);
    }

    @Test
    void calculate_무료요금() {
        //given
        Path path = new Path(Arrays.asList(뚝섬유원지역, 건대역, 구의역), 32);
        int fare = 1_250 + 600 + 칠호선.getSurcharge();

        //when
        Fare 요금 = new Fare(Arrays.asList(뚝유_건대, 건대_구의), path, PathAcceptanceTest.FREE_AGE);

        //then
        assertThat(요금.getFare()).isZero();
    }

    @Test
    void calculate_아동요금() {
        //given
        Path path = new Path(Arrays.asList(뚝섬유원지역, 건대역, 구의역), 32);
        int fare = 1_250 + 600 + 칠호선.getSurcharge();

        //when
        Fare 요금 = new Fare(Arrays.asList(뚝유_건대, 건대_구의), path, PathAcceptanceTest.CHILDREN_AGE);

        //then
        assertThat(요금.getFare()).isEqualTo(1000);
    }

    @Test
    void calculate_청소년요금() {
        //given
        Path path = new Path(Arrays.asList(뚝섬유원지역, 건대역, 구의역), 32);
        int fare = 1_250 + 600 + 칠호선.getSurcharge();

        //when
        Fare 요금 = new Fare(Arrays.asList(뚝유_건대, 건대_구의), path, PathAcceptanceTest.TEENAGER_AGE);

        //then
        assertThat(요금.getFare()).isEqualTo(1600);
    }

    @Test
    void calculate_요금_50km이상() {
        //given
        Path path = new Path(Arrays.asList(뚝섬유원지역, 건대역, 구의역, 강변역), 62);
        int fare = 1_250 + 700 + 칠호선.getSurcharge();

        //when`
        Fare 요금 = new Fare(Arrays.asList(뚝유_건대, 건대_구의), path, PathAcceptanceTest.ALL_AGE);

        //then
        assertThat(요금.getFare()).isEqualTo(2450);
    }

    @Test
    void calculate_요금_10km이상_50km이하() {
        //given
        Path path = new Path(Arrays.asList(뚝섬유원지역, 건대역, 구의역, 강변역), 22);
        int fare = 1_250 + 400 + 칠호선.getSurcharge();

        //when
        Fare 요금 = new Fare(Arrays.asList(뚝유_건대, 건대_구의), path, PathAcceptanceTest.ALL_AGE);

        //then
        assertThat(요금.getFare()).isEqualTo(2150);
    }

    @Test
    void calculate_일반요금_10km이하() {
        //given
        Path path = new Path(Arrays.asList(뚝섬유원지역, 건대역, 구의역, 강변역), 8);
        int fare = 1_250 + 0 + 칠호선.getSurcharge();

        //when
        Fare 요금 = new Fare(Arrays.asList(뚝유_건대, 건대_구의), path, PathAcceptanceTest.ALL_AGE);

        //then
        assertThat(요금.getFare()).isEqualTo(1750);
    }
}