package nextstep.subway.auth.enumerate;

public enum AuthenticationType {
    GUEST(1),
    MEMBER(2);
    
    private Integer value;

    AuthenticationType(Integer value) {
        this.value = value;
    }
}
