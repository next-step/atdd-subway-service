package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LoginMemberValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Validator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginMember loginMember = (LoginMember) target;

        if(this.validateAge(loginMember)) {
            errors.rejectValue("age", "bad", "사용자의 나이가 비었거나 올바르지 않습니다.");
        }
    }

    private boolean validateAge(LoginMember loginMember) {
        return loginMember.getAge() == null || loginMember.getAge() < 0;
    }
}
