package nextstep.subway.auth.domain;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.constants.ErrorMessages;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public boolean isEmpty() {
        return (id == null);
    }

    public void checkValidLoginMember() {
        if (isEmpty()) {
            throw new AuthorizationException(ErrorMessages.UNAUTHORIZED_MEMBER_REQUESTED_FAVORITE_CREATION);
        }
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}
