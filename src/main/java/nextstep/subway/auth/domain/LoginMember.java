package nextstep.subway.auth.domain;

import java.util.function.Consumer;

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

    public boolean isPresent() {
        return id != null;
    }

    public void ifPresent(Consumer<LoginMember> consumer) {
        if (id != null)
            consumer.accept(this);
    }

}
