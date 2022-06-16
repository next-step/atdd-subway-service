package nextstep.subway.line.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedList;
import java.util.List;
import nextstep.subway.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DiscountHelperTest {

    private List<DiscountHelper> discountHelperList;

    @BeforeEach
    public void init() {
        discountHelperList = new LinkedList<>();
        discountHelperList.add(new ChildDiscountHelper());
        discountHelperList.add(new StudentDiscountHelper());
        discountHelperList.add(new AdultDiscountHelper());
    }

    @Test
    public void 어린이할인() {
        //given
        int price = 1000;
        DiscountHelper helper = discountHelperList.stream()
            .filter(
                discountHelper -> discountHelper.canSupport(new Member("email", "password", 9)))
            .findFirst()
            .get();

        //when
        int discountResult = helper.discount(price);

        //then
        assertThat(discountResult).isEqualTo(500);
    }

    @Test
    public void 청소년할인() {
        //given
        int price = 1000;
        DiscountHelper helper = discountHelperList.stream()
            .filter(
                discountHelper -> discountHelper.canSupport(new Member("email", "password", 18)))
            .findFirst()
            .get();

        //when
        int discountResult = helper.discount(price);

        //then
        assertThat(discountResult).isEqualTo(800);
    }

    @Test
    public void 할인없음() {
        //given
        int price = 1000;
        DiscountHelper helper = discountHelperList.stream()
            .filter(
                discountHelper -> discountHelper.canSupport(new Member("email", "password", 32)))
            .findFirst()
            .get();

        //when
        int discountResult = helper.discount(price);

        //then
        assertThat(discountResult).isEqualTo(1000);
    }
}