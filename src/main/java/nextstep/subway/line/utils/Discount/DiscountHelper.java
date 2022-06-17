package nextstep.subway.line.utils.Discount;

import nextstep.subway.member.domain.Member;

public interface DiscountHelper {

    boolean canSupport(Member member);

    int discount(int price);
}
