package nextstep.subway.member.application.exception;


import nextstep.subway.common.exception.CommonErrorCode;
import nextstep.subway.common.exception.ErrorCode;

public enum MemberErrorCode implements ErrorCode {
    MEMBER_NOTFOUND("회원을 찾을 수 없습니다."),
    ;
    private final String errorMessage;

    MemberErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}

