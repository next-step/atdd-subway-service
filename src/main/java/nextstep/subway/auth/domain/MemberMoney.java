package nextstep.subway.auth.domain;

import java.util.Objects;

public class MemberMoney {

    private Money money;

    private MemberMoney(Money money) {
        this.money = money;
    }

    public static MemberMoney from(Money money) {
        return new MemberMoney(money);
    }

    public MemberMoney minus(MemberMoney memberMoney) {
        return MemberMoney.from(this.money.minus(memberMoney.money));
    }

    public MemberMoney divideByDecimalPoint(double rate) {
        return MemberMoney.from(this.money.divideByDecimalPoint(rate));
    }

    public Money getCharge() {
        return this.money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberMoney memberMoney = (MemberMoney) o;
        return Objects.equals(money, memberMoney.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(money);
    }
}
