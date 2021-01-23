package nextstep.subway.auth.domain;

import org.apache.commons.lang3.Range;

public class LoginMember {
    public static final int YOUTH_MAX_AGE = 18;
    public static final int YOUTH_MIN_AGE = 13;
    public static final int CHILD_MAX_AGE = 12;
    public static final int CHILD_MIN_AGE = 6;
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

    public boolean adult() {
        return age > YOUTH_MAX_AGE;
    }

    public boolean youth() {
        Range<Integer> range = Range.between(YOUTH_MIN_AGE, YOUTH_MAX_AGE);
        return range.contains(age);
    }

    public boolean child() {
        Range<Integer> range = Range.between(CHILD_MIN_AGE, CHILD_MAX_AGE);
        return range.contains(age);
    }

    public boolean empty() {
        return id == null && email == null && age == null;
    }
}
