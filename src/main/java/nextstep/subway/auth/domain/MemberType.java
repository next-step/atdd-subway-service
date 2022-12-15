package nextstep.subway.auth.domain;

public enum MemberType {
    LOGIN, NOT_LOGIN;


    public boolean isNotLogin() {
        return this == NOT_LOGIN;
    }
}
