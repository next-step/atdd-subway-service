package nextstep.subway.auth.domain;

public class LoginMember {
    public static final GuestMember GUEST_MEMBER = new GuestMember();

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

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
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
