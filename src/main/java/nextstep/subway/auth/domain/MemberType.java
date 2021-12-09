package nextstep.subway.auth.domain;

public enum MemberType {
    GUEST, MEMBER;

    public boolean isGuest() {
        return this.equals(GUEST);
    }
}
