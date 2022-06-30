package nextstep.subway.auth.domain;

import org.apache.commons.lang3.StringUtils;
import org.jcp.xml.dsig.internal.dom.Utils;
import org.springframework.util.ObjectUtils;

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

    public static LoginMember guest() {
        return new LoginMember();
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

    public boolean isGuest() {
        return ObjectUtils.isEmpty(id) && StringUtils.isEmpty(email) && ObjectUtils.isEmpty(age);
    }
}
