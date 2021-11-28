package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.station.dto.StationResponse;

import static nextstep.subway.AcceptanceTest.*;

public class FavoriteSteps {

    public static final String FAVORITE_URI = "/favorites";

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, StationResponse source, StationResponse target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());
        return postWithAuth(FAVORITE_URI, favoriteRequest, accessToken);
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return getWithAuth(FAVORITE_URI, accessToken);
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return deleteWithAuth(uri, accessToken);
    }
}
