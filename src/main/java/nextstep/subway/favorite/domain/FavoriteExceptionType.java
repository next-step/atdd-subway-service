package nextstep.subway.favorite.domain;

public enum FavoriteExceptionType {
    FAVORITE_FILED_IS_NOT_NULL("즐겨찾기를 구성하는 항목은 null이 될 수 없습니다.");

    private String message;

    FavoriteExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
