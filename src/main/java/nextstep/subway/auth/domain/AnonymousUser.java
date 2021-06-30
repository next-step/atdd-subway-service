package nextstep.subway.auth.domain;

import org.springframework.stereotype.Component;

@Component
public class AnonymousUser implements UserDetails{
    private static final String EMAIL = "anonymous@email.com";
    private static final int AGE = 0;

    @Override
    public int getAge() {
        return AGE;
    }

    @Override
    public String getEmail() {
        return EMAIL;
    }

    @Override
    public UserDetails getUserDetails() {
        return new LoginMember(0L, EMAIL, AGE);
    }
}
