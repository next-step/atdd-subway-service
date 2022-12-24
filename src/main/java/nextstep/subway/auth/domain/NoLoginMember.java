package nextstep.subway.auth.domain;

public class NoLoginMember extends LoginMember {

    private Long id = -1L;
    private String email;
    private Integer age = -1;

    public NoLoginMember() {}

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