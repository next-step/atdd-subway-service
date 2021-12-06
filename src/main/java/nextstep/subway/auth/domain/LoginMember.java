package nextstep.subway.auth.domain;

import nextstep.subway.auth.enumerate.AuthenticationType;

public class LoginMember {
    private Long id;
    private String email;
    private int age;
    private AuthenticationType authenticationType;

    public LoginMember() {
        authenticationType = AuthenticationType.GUEST;
    }

    public LoginMember(Long id, String email, int age) {
        this.id = id;
        this.email = email;
        this.age = age;
        authenticationType = AuthenticationType.MEMBER;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public AuthenticationType getAuthenticationType() {
        return this.authenticationType;
    }

}
