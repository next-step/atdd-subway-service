package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteAcceptanceStep {

    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(TokenResponse 사용자, StationResponse upStation, StationResponse downStation) {
        return null;
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse 사용자, ExtractableResponse<Response> createResponse) {
        return null;
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse 사용자) {
        return null;
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {

    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {

    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {

    }
}
