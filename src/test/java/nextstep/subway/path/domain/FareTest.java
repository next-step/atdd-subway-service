package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class FareTest {
    public static final int 기본운임비 = 1250;

    Station 교대역;
    Station 강남역;
    Station 양재역;
    Station 판교역;
    Line 삼호선;
    Line 이호선;
    Line 신분당선;
    List<Section> sections;

    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        판교역 = new Station("판교역");
        삼호선 = new Line("삼호선", "ORANGE", 교대역, 양재역, 2);
        이호선 = new Line("이호선", "ORANGE", 강남역, 양재역, 2, 600);
        신분당선 = new Line("신분당선", "RED", 양재역, 판교역, 2, 900);
        sections = Arrays.asList(new Section(삼호선, 교대역, 강남역, 2),
                new Section(이호선, 강남역, 양재역, 2),
                new Section(신분당선, 양재역, 판교역, 2));
    }

    @DisplayName("거리 정책에 따른 요금 산정")
    @Test
    void calculateChargeByDistance() {
        assertAll(() -> {
            assertThat(Fare.of(5).getFare()).isEqualTo(기본운임비);
            assertThat(Fare.of(10).getFare()).isEqualTo(기본운임비);
            assertThat(Fare.of(25).getFare()).isEqualTo(기본운임비 + 300);
            assertThat(Fare.of(27).getFare()).isEqualTo(기본운임비 + 300);
            assertThat(Fare.of(57).getFare()).isEqualTo(기본운임비 + 800);
            assertThat(Fare.of(58).getFare()).isEqualTo(기본운임비 + 900);
        });
    }

    @DisplayName("노선 정책에 따른 요금 산정. 노선 추가 요금 중 최대 추가 요금으로 적용")
    @Test
    void getMaxSurchargeByLine() {
        assertThat(Fare.of(8, sections).getFare()).isEqualTo(기본운임비 + 900);
    }

    @DisplayName("어린이 요금 할인 성공")
    @Test
    void calculateByAge() {
        //when
        int result = Fare.of(8, sections).calculateByAge(8).getFare();

        //then
        assertThat(result).isEqualTo(900);
    }

    @DisplayName("청소년 요금 할인 성공")
    @Test
    void calculateByAge2() {
        //when
        int result = Fare.of(8, sections).calculateByAge(15).getFare();

        //then
        assertThat(result).isEqualTo(1440);
    }
}
