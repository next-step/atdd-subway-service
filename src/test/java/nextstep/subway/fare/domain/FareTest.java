package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    Station 강남역;
    Station 양재역;
    Station 교대역;
    Station 남부터미널역;
    Line 신분당선;
    Line 이호선;
    Line 삼호선;

    /**
     * 교대역   --- *2호선* 10km ---   강남역
     * |                             |
     * *3호선* 3km                *신분당선* 10km
     * |                             |
     * 남부터미널역 --- *3호선* 2km ---  양재
     */
    @BeforeEach
    void init() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        신분당선 = new Line("신분당선", "빨강", 강남역, 양재역, 10, 900);
        이호선 = new Line("이호선", "초록", 교대역, 강남역, 10, 200);
        삼호선 = new Line("삼호선", "주황", 교대역, 양재역, 5, 300);
        삼호선.addSection(교대역, 남부터미널역, 3);
    }

    @DisplayName("이동 거리에 따른 추가 금액을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"10:1250", "11:1350", "15:1350", "16:1450", "49:2050", "50:2050", "51:2150", "58:2150", "59:2250"},
            delimiter = ':')
    void 거리_비례_추가_금액_계산(int distance, int totalFare) {
        // given
        Fare baseFare = new Fare(DistanceExtraFare.BASE_FARE);

        // when
        Fare result = baseFare.addExtraOf(distance);

        // then
        assertThat(result).isEqualTo(new Fare(totalFare));
    }


    @DisplayName("이동 경로에 따른 추가 금액을 계산한다.")
    @Test
    void 노선_추가_금액_계산() {
        // given
        Path path = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선))
                .getPath(강남역, 남부터미널역);

        // when
        Fare result = new Fare(0).addExtraOf(path.getSectionEdges());

        // then
        assertThat(result).isEqualTo(new Fare(신분당선.getExtraFare()));
    }
}
