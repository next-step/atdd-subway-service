package nextstep.subway.path.domain.calculator;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.DiscountRate;
import nextstep.subway.auth.domain.LoginMember;

@Service
public class AgeCalculatorService {

    public double getDiscountRate(LoginMember member) {
        Integer age = member.getAge();
        if (age == null || age == 0) {
            return 1;
        }
        return DiscountRate.findDiscountRateByAge(age).getDicountRate();
    }

}
