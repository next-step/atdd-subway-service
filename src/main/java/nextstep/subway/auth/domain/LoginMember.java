package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.Age;

public class LoginMember {
    public static final GuestMember GUEST_MEMBER = new GuestMember();

    private Long id;
    private String email;
    private Age age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = Age.of(age);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Age getAge() {
        return age;
    }

    public boolean isGuestUser() {
        return false;
    }

    private static class GuestMember extends LoginMember {
        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
