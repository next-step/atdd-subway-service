package nextstep.subway.member.dto;

import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.Member;

public class MemberResponse {
    private Long id;
    private String email;
    private Integer age;

    private MemberResponse() {}

    public MemberResponse(Long id, String email, Age age) {
        this.id = id;
        this.email = email;
        this.age = age.value();
    }

    public static MemberResponse from(Member member) {
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
}
