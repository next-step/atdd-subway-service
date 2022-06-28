package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.AgeGrade;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;
    private AgeGrade ageGrade;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
        this.ageGrade = AgeGrade.getGrade(age);
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

    public AgeGrade getAgeGrade() {
        return ageGrade;
    }
}
