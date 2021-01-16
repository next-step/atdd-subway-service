package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.dto.FavoriteRequest;
import nextstep.subway.member.dto.MemberRequest;
import org.springframework.http.MediaType;

public class FavoriteAcceptanceTestSupport extends AcceptanceTest {

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken
            , Long sourceStationId, Long targetStationId) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceStationId, targetStationId);

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, String deletePath) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(deletePath)
                .then().log().all()
                .extract();
    }
}
