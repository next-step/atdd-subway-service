package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.LoginMember;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
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

        this.rejectEmptyValue(errors);

        if(this.validateNotAge(loginMember)) {
            errors.rejectValue("age", "bad", "사용자의 나이가 비었거나 올바르지 않습니다.");
        }
    }

    /**
     * 사용자의 각 값이 비어있는 경우 오류를 추가합니다.
     * @param errors
     */
    private void rejectEmptyValue(Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "notempty"
                , "사용자 아이디는 필수 값 입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "notempty"
                , "사용자 이메일은 필수 값 입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "age", "notempty"
                , "사용자 나이는 필수 값 입니다.");
    }

    /**
     * 나이가 0보다 작은지 검증합니다.
     * @param loginMember
     * @return
     */
    private boolean validateNotAge(LoginMember loginMember) {
        return loginMember.getAge() == null || loginMember.getAge() < 0;
    }
}
