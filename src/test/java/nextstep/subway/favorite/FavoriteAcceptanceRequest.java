package nextstep.subway.favorite;

import nextstep.subway.auth.acceptance.AuthToken;
import nextstep.subway.favorite.dto.FavoriteRequest;
import org.junit.jupiter.api.function.Executable;

public class FavoriteAcceptanceRequest {

    public static Executable 즐겨찾기_등록_요청_및_등록됨(AuthToken authToken, FavoriteRequest request, Long exceptId) {
        return () -> {

        };
    }

    public static Executable 즐겨찾기_목록_요청_및_조회됨(AuthToken authToken, FavoriteRequest request) {
        return () -> {

        };
    }

    public static Executable 즐겨찾기_등록_요청_및_실패됨(AuthToken authToken, FavoriteRequest request) {
        return () -> {

        };
    }

    public static Executable 즐겨찾기_목록_요청_및_비어있음(AuthToken authToken) {
        return () -> {

        };
    }

    public static Executable 즐겨찾기_삭제_요청_및_삭제됨(AuthToken authToken, Long favoriteId) {
        return () -> {

        };
    }
}
