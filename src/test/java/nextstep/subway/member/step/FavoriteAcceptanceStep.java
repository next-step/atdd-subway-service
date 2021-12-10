package nextstep.subway.member.step;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class FavoriteAcceptanceStep {

    public static ExtractableResponse<Response> 로그인_회원_즐겨찾기_삭제(
        ExtractableResponse<Response> response,
        String token) {
        Integer deleteId = response.jsonPath().get("[0].id");
        return RestAssured
            .given().log().all()
            .auth()
            .oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/favorites/" + deleteId)
            .then().log().all()
            .extract();
    }


    public static ExtractableResponse<Response> 로그인_회원_즐겨찾기_조회(String token) {
        return RestAssured
            .given().log().all()
            .auth()
            .oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }


    public static ExtractableResponse<Response> 즐겨찾기_생성_됨(String token, Integer sourceId,
        Integer targetId) {
        Map<String, Integer> params = new HashMap<>();
        params.put("source", sourceId);
        params.put("target", targetId);

        return RestAssured
            .given().log().all()
            .auth()
            .oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_조회_정상(ExtractableResponse<Response> response, Integer expectedSourceId,
        Integer expectedTargetId) {
        List<Integer> sourceIds = response.jsonPath().getList("source.id");
        List<Integer> targetIds = response.jsonPath().getList("target.id");

        assertThat(sourceIds).contains(expectedSourceId);
        assertThat(targetIds).contains(expectedTargetId);
    }

    public static void 즐거찾기_추가_정상(ExtractableResponse<Response> 즐겨찾기_추가_응답) {
        assertThat(즐겨찾기_추가_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 즐겨찾기_삭제_정상(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
