package nextstep.subway.member.dto;

import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.Member;

public class MemberResponse {
    private Long id;
    private String email;
    private Age age;

    public MemberResponse() {
    }

    public MemberResponse(Long id, String email, Age age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getAge());
    }

    public Member toMember() {
        return new Member(id, email, "", age);
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
}
