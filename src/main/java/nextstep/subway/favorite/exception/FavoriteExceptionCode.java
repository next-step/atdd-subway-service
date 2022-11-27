package nextstep.subway.favorite.exception;

public enum FavoriteExceptionCode {

    REQUIRED_SOURCE("The source is a required field."),
    REQUIRED_TARGET("The target is a required field."),
    REQUIRED_MEMBER("The member is a required field.");

    private String title = "[ERROR] ";
    private String message;

    FavoriteExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return title + message;
    }
}
