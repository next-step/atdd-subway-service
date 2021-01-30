package nextstep.subway.member;

import nextstep.subway.member.domain.ChildDiscountStrategy;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.NoDiscountStrategy;
import nextstep.subway.member.domain.TeenagerDiscountStrategy;
import nextstep.subway.member.dto.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class MemberTest {

    private static final String ID = "eversong";
    private static final String PW = "1234";

    @DisplayName("할인 정책 테스트")
    @Test
    public void discountStrategyTest() {
        Member child = new Member(ID, PW, 6);
        assertThat(child.getDiscountStrategy()).isInstanceOf(ChildDiscountStrategy.class);

        Member teenager = new Member(ID, PW, 18);
        assertThat(teenager.getDiscountStrategy()).isInstanceOf(TeenagerDiscountStrategy.class);

        Member adult = new Member(ID, PW, 19);
        assertThat(adult.getDiscountStrategy()).isInstanceOf(NoDiscountStrategy.class);
    }

}
