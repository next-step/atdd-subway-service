package nextstep.subway.auth.domain;

import nextstep.subway.common.Excetion.NotLoginException;

public class LoginMember {
    public Long id;
    public String email;
    public Integer age;
    public double discountAmount;
    public int subwayCharge;

    public Long getId() {
        if(id == null || id < 1L){
            throw new NotLoginException();
        }
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public int calculatorCharge(int charge) {
        return (int) Math.ceil((subwayCharge + charge) * discountAmount);
    }
}
