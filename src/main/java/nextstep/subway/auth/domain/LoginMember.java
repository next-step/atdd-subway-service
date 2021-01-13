package nextstep.subway.auth.domain;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.path.application.fare.AgeDrawbackFarePolicy;

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

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public boolean isAuthorized() {
        return id != null;
    }

    public void validAuthorized() {
        if (id == null) {
            throw new AuthorizationException("accessToken이 유효하지 않습니다.");
        }
    }

    public int getDrawbackFare(int fare) {
        if(this.isAuthorized()) {
            AgeDrawbackFarePolicy ageOverFarePolicy = getAgeOverFarePolicy(this.age);
            return ageOverFarePolicy.calculateDrawbackFare(fare);
        }

        return 0;
    }

    private AgeDrawbackFarePolicy getAgeOverFarePolicy(int age) {
        return AgeDrawbackFarePolicy.valueOf(age);
    }
}
