package nextstep.subway.favorite.exception;

public enum FavoriteExceptionCode {

    REQUIRED_SOURCE("The source is a required field."),
    REQUIRED_TARGET("The target is a required field."),
    REQUIRED_MEMBER("The member is a required field."),
    CANNOT_EQUALS_SOURCE_TARGET("The source and target cannot be the same."),
    NOT_FOUND_BY_ID("The favorite not found by id."),
    MEMBER_NOT_MATCH("The logged in member is different from the member who registered this favorite.");

    private String title = "[ERROR] ";
    private String message;

    FavoriteExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return title + message;
    }
}
