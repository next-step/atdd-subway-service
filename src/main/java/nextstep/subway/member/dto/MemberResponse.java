package nextstep.subway.member.dto;

import nextstep.subway.member.domain.Member;

import java.util.Objects;

public class MemberResponse {
    private final Long id;
    private final String email;
    private final Integer age;

    public MemberResponse(final Long id, final String email, final Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static MemberResponse of(final Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getAge());
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

    @Override
    public String toString() {
        return "MemberResponse{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MemberResponse that = (MemberResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email) && Objects.equals(age, that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, age);
    }
}
