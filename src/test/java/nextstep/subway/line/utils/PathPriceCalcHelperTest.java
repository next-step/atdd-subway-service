package nextstep.subway.line.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.utils.PriceCalculator.Between10And50PriceCalcHelper;
import nextstep.subway.line.utils.PriceCalculator.Over50PriceCalcHelper;
import nextstep.subway.line.utils.PriceCalculator.PriceCalcHelper;
import nextstep.subway.line.utils.PriceCalculator.Under10PriceCalcHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PathPriceCalcHelperTest {

    private List<PriceCalcHelper> priceCalcHelpers;
    private List<Line> lines;

    @BeforeEach
    public void init() {
        priceCalcHelpers = new LinkedList<>();
        lines = new LinkedList<>();

        priceCalcHelpers.add(new Under10PriceCalcHelper());
        priceCalcHelpers.add(new Between10And50PriceCalcHelper());
        priceCalcHelpers.add(new Over50PriceCalcHelper());

        lines.add(new Line("신분당선", "빨강", null, null, 10, 1_000));
        lines.add(new Line("이호선", "녹색", null, null, 10));
    }

    @Test
    public void 가격체크_10KM이하_노선요금없음() {
        //given
        int distance = 9;
        PriceCalcHelper priceHelper = priceCalcHelpers.stream()
            .filter(priceCalcHelper -> priceCalcHelper.canSupport(distance)).findFirst()
            .get();

        //when
        int price = priceHelper.calculatePrice(distance, new ArrayList<>());

        //then
        assertThat(price).isEqualTo(1250);
    }

    @Test
    public void 가격체크_10KM이하_노선요금있음() {
        //given
        int distance = 9;
        PriceCalcHelper priceHelper = priceCalcHelpers.stream()
            .filter(priceCalcHelper -> priceCalcHelper.canSupport(distance)).findFirst()
            .get();

        //when
        int price = priceHelper.calculatePrice(distance, lines);

        //then
        assertThat(price).isEqualTo(2250);
    }

    @Test
    public void 가격체크_10_50KM사이_노선요금없음() {
        //given
        int distance = 49;
        PriceCalcHelper priceHelper = priceCalcHelpers.stream()
            .filter(priceCalcHelper -> priceCalcHelper.canSupport(distance)).findFirst()
            .get();

        //when
        int price = priceHelper.calculatePrice(distance, new ArrayList<>());

        //then
        assertThat(price).isEqualTo(2050);
    }

    @Test
    public void 가격체크_10_50KM사이_노선요금있음() {
        //given
        int distance = 49;
        PriceCalcHelper priceHelper = priceCalcHelpers.stream()
            .filter(priceCalcHelper -> priceCalcHelper.canSupport(distance)).findFirst()
            .get();

        //when
        int price = priceHelper.calculatePrice(distance, lines);

        //then
        assertThat(price).isEqualTo(3050);
    }

    @Test
    public void 가격체크_50KM_이상_노선요금없음() {
        //given
        int distance = 97;
        PriceCalcHelper priceHelper = priceCalcHelpers.stream()
            .filter(priceCalcHelper -> priceCalcHelper.canSupport(distance)).findFirst()
            .get();

        //when
        int price = priceHelper.calculatePrice(distance, new ArrayList<>());

        //then
        assertThat(price).isEqualTo(2650);
    }

    @Test
    public void 가격체크_50KM_이상_노선요금있음() {
        //given
        int distance = 97;
        PriceCalcHelper priceHelper = priceCalcHelpers.stream()
            .filter(priceCalcHelper -> priceCalcHelper.canSupport(distance)).findFirst()
            .get();

        //when
        int price = priceHelper.calculatePrice(distance, lines);

        //then
        assertThat(price).isEqualTo(3650);
    }
}