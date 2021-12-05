package nextstep.subway.member.dto;

import java.util.Objects;

import nextstep.subway.member.domain.Member;

public class MemberResponse {
    private Long id;
    private String email;
    private int age;

    public MemberResponse() {
    }

    public MemberResponse(Long id, String email, int age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public MemberResponse(String email, int age) {
        this.email = email;
        this.age = age;
    }

    public static MemberResponse of(Member member) {
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
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MemberResponse)) {
            return false;
        }
        MemberResponse memberResponse = (MemberResponse) o;
        return Objects.equals(id, memberResponse.id) && Objects.equals(email, memberResponse.email) && Objects.equals(age, memberResponse.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, age);
    }
}
