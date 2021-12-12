package nextstep.subway.fare;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Lists;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("요금 정책")
class FarePolicyTest {

    private final int BASIC_FARE = 1250;

    private Lines lines;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void setUp() {
        // given
        강남역 = Station.of("강남역");
        양재역 = Station.of("양재역");
        교대역 = Station.of("교대역");
        남부터미널역 = Station.of("남부터미널역");

        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10, 1000);
        Line 삼호선 = new Line("삼호선", "bg-orange-600", 교대역, 양재역, 5, 200);
        Line 이호선 = new Line("이호선", "bg-green-600", 교대역, 강남역, 8, 400);

        삼호선.addSections(Section.of(삼호선, 교대역, 남부터미널역, 3));

        lines = Lines.of(Lists.newArrayList(신분당선, 삼호선, 이호선));
    }

    @DisplayName("10km 이내면 기본운임만 낸다.")
    @ParameterizedTest(name = "거리가 {0} 일 때 기본운임만 낸다")
    @ValueSource(ints = {0, 1, 5, 10})
    void 요금_정책_10km_이내면_기본료(int distance) {
        assertThat(FarePolicy.fareByDistance(distance)).isEqualTo(BASIC_FARE);
    }

    @DisplayName("10km 초과 50km 이하면 5km당 100원의 추가요금을 낸다.")
    @ParameterizedTest(name = "거리가 {0} 일 때 요금은 {1}")
    @CsvSource({
        "11, 1350",
        "15, 1350",
        "16, 1450",
        "50, 2050"
    })
    void 요금_정책_10km_초과_50km_이내면_5km_마다_추가요금(int distance, int fare) {
        assertThat(FarePolicy.fareByDistance(distance)).isEqualTo(fare);
    }

    @DisplayName("50km 초과면 8km당 100원, 10km 초과 50km 이하는 5km당 100원의 추가 요금")
    @ParameterizedTest(name = "거리가 {0} 일 때 요금은 {1}")
    @CsvSource({
        "51, 2150",
        "58, 2150",
        "59, 2250",
        "66, 2250",
        "100, 2750"
    })
    void 요금_정책_50km_초과면_8km_마다_추가요금(int distance, int fare) {
        assertThat(FarePolicy.fareByDistance(distance)).isEqualTo(fare);
    }

    @DisplayName("요금 정책에 따른 요금 테스트")
    @Test
    void 거리별_요금_노선별_추가_요금() {
        // when
        Integer fare = FarePolicy.getFare(lines.getLinesInclude(Lists.newArrayList(강남역, 양재역)), 10);

        // then
        assertThat(fare).isEqualTo(2250);
    }

    @DisplayName("노선별 추가 요금은 가장 높은 금액의 추가 요금만 적용한다")
    @Test
    void 거리별_요금_노선별_추가_요금2() {
        // when
        Integer fare = FarePolicy.getFare(lines.getLinesInclude(Lists.newArrayList(교대역, 강남역, 양재역)), 10);

        // then
        assertThat(fare).isEqualTo(2250);
    }

}