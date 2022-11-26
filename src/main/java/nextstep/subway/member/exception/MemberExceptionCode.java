package nextstep.subway.member.exception;

public enum MemberExceptionCode {

    REQUIRED_EMAIL("The email is a required field."),
    REQUIRED_PASSWORD("The password is a required field."),
    NOT_EMAIL_FORMAT("The input string is not in email format."),
    NOT_PASSWORD_FORMAT("The input string is not in password format."),
    PASSWORD_NOT_MATCH("The password entered does not match the member's password.");

    private String title = "[ERROR] ";
    private String message;

    MemberExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return title + message;
    }
}
