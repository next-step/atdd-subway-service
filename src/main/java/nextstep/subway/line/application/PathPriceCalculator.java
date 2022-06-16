package nextstep.subway.line.application;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.utils.AdultDiscountHelper;
import nextstep.subway.line.utils.Between10And50PriceCalcHelper;
import nextstep.subway.line.utils.ChildDiscountHelper;
import nextstep.subway.line.utils.DiscountHelper;
import nextstep.subway.line.utils.Over50PriceCalcHelper;
import nextstep.subway.line.utils.PriceCalcHelper;
import nextstep.subway.line.utils.StudentDiscountHelper;
import nextstep.subway.line.utils.Under10PriceCalcHelper;
import nextstep.subway.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class PathPriceCalculator {

    private List<PriceCalcHelper> priceCalcHelpers;
    private List<DiscountHelper> discountHelpers;

    @PostConstruct
    private void init() {
        priceCalcHelpers = new LinkedList<>();
        priceCalcHelpers.add(new Under10PriceCalcHelper());
        priceCalcHelpers.add(new Between10And50PriceCalcHelper());
        priceCalcHelpers.add(new Over50PriceCalcHelper());

        discountHelpers = new LinkedList<>();
        discountHelpers.add(new ChildDiscountHelper());
        discountHelpers.add(new StudentDiscountHelper());
        discountHelpers.add(new AdultDiscountHelper());
    }

    public int getPrice(int distance, List<Line> lines, Member member) {
        PriceCalcHelper priceCalcHelper = priceCalcHelpers.stream()
            .filter(helper -> helper.canSupport(distance))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("알맞은 가격 측정 로직이 없습니다"));

        DiscountHelper discountHelper = discountHelpers.stream()
            .filter(helper -> helper.canSupport(member))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("알맞은 할인 정책이 없습니다"));

        int price = priceCalcHelper.calculatePrice(distance, lines);
        return discountHelper.discount(price);
    }

}
