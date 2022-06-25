package nextstep.subway.member.dto;

import nextstep.subway.member.domain.Member;

import java.util.Objects;

public class MemberRequest {
    private final String email;
    private final String password;
    private final Integer age;

    public MemberRequest(final String email, final String password, final Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }

    public Member toMember() {
        return Member.of(email, password, age);
    }

    @Override
    public String toString() {
        return "MemberRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MemberRequest that = (MemberRequest) o;
        return Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(age, that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, age);
    }
}
