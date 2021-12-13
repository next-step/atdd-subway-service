package nextstep.subway.auth.application;

public enum AuthorizationErrorCode {

    THERE_IS_WORNG_MEMBER("[ERROR]잘못된 멤버입니다.");

    private String errorMessage;

    AuthorizationErrorCode(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String errorMessage(){
        return this.errorMessage;
    }
}
