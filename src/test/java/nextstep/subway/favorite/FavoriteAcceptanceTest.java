package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static nextstep.subway.utils.AuthRestAssuredTestUtils.*;
import static nextstep.subway.utils.LineSectionRestAssuredUtils.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.utils.LineSectionRestAssuredUtils.지하철_노선에_지하철역_등록됨;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 사당역;
    private StationResponse 교대역;

    private LineResponse 이호선;

    private String 토큰;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        // given
        사당역 = 지하철역_등록되어_있음("사당역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);

        이호선 = 지하철_노선_등록되어_있음("이호선", "green", 교대역, 강남역, 5).as(LineResponse.class);
        지하철_노선에_지하철역_등록됨(지하철_노선에_지하철역_등록_요청(이호선, 사당역, 교대역, 3));

        회원_등록됨(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);
        토큰 = 로그인_됨(loginResponse);
    }

    @DisplayName("즐겨찾기를 관리한다")
    @Test
    void manageFavorites() {
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(토큰, 사당역, 강남역);
        즐겨찾기_생성됨(createResponse);

        ExtractableResponse<Response> searchResponse = 즐겨찾기_목록_조회_요청(토큰);
        즐겨찾기_목록_조회됨(searchResponse, 사당역, 강남역);

        ExtractableResponse<Response> deleteResponse = 즐겨찾기_목록_삭제_요청(토큰, 1l);
        즐겨찾기_삭제됨(deleteResponse);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청(String token, StationResponse source, StationResponse target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(token)
            .body(favoriteRequest)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(token)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, StationResponse source, StationResponse target) {
        List<StationResponse> sources = response.jsonPath().getList(".", FavoriteResponse.class).stream()
            .map(FavoriteResponse::getSource)
            .collect(Collectors.toList());

        List<StationResponse> targets = response.jsonPath().getList(".", FavoriteResponse.class).stream()
            .map(FavoriteResponse::getTarget)
            .collect(Collectors.toList());

        assertThat(sources.get(0)).isEqualTo(source);
        assertThat(targets.get(0)).isEqualTo(target);
    }

    private void 즐겨찾기_목록_조회되지_않음(ExtractableResponse<Response> response, StationResponse source, StationResponse target) {
        List<StationResponse> sources = response.jsonPath().getList(".", FavoriteResponse.class).stream()
            .map(FavoriteResponse::getSource)
            .collect(Collectors.toList());

        List<StationResponse> targets = response.jsonPath().getList(".", FavoriteResponse.class).stream()
            .map(FavoriteResponse::getTarget)
            .collect(Collectors.toList());

        assertThat(sources.get(0)).isEqualTo(source);
        assertThat(targets.get(0)).isEqualTo(target);
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_삭제_요청(String token, long favoriteId) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(token)
            .when().delete("/favorites/{id}", favoriteId)
            .then().log().all()
            .extract();
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
