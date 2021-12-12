package nextstep.subway.auth.domain;

import nextstep.subway.auth.application.AuthorizationException;

/**
 * packageName : nextstep.subway.auth.domain
 * fileName : Stranger
 * author : haedoang
 * date : 2021-12-10
 * description :
 */
public class Stranger implements User {
    private static Stranger instance;

    public static Stranger getInstance() {
        if (instance == null) {
            instance = new Stranger();
        }
        return instance;
    }

    @Override
    public Long getId() {
        throw new AuthorizationException();
    }

    @Override
    public Integer getAge() {
        throw new AuthorizationException();
    }

    @Override
    public boolean isStranger() {
        return true;
    }
}
