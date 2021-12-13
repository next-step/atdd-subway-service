package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 서초역;
    private StationResponse 교대역;
    private StationResponse 강남역;
    private String 토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();
        
        서초역 = StationAcceptanceTest.지하철역_등록되어_있음("서초역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("이호선", "bg-green-600", 서초역.getId(), 교대역.getId(), 30);
        LineResponse 이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 강남역, 50);

        MemberAcceptanceTest.회원_생성을_요청("jennie267@email.com", "pwjennie", 30);

        토큰 = AuthAcceptanceTest.토큰_조회(AuthAcceptanceTest.로그인_요청("jennie267@email.com", "pwjennie"));
    }

    @Test
    @DisplayName("즐겨찾기 관리 시나리오 통합 테스트")
    public void 즐겨찾기_관리_통합_테스트() throws Exception {
        // when
        ExtractableResponse<Response> 즐겨찾기_등록_응답 = 즐겨찾기_등록_요청(토큰, 서초역, 교대역);

        // then
        즐겨찾기_등록됨(즐겨찾기_등록_응답);

        // when
        ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청(토큰);

        // then
        즐겨찾기_조회됨(즐겨찾기_조회_응답);

        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(토큰, 즐겨찾기_등록_응답);

        // then
        즐겨찾기_삭제됨(즐겨찾기_삭제_응답);
    }
    
    private ExtractableResponse<Response> 즐겨찾기_등록_요청(String token, StationResponse sourceStation, StationResponse targetStation) {
        FavoriteRequest favoriteRequest = FavoriteRequest.of(sourceStation.getId(), targetStation.getId());

        return RestAssured
                .given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(favoriteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }
    
    private void 즐겨찾기_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청(String token) {
        return RestAssured
                .given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }
    
    private void 즐겨찾기_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, ExtractableResponse<Response> response) {
        FavoriteResponse favoriteResponse = response.as(FavoriteResponse.class);
        return RestAssured
                .given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/favorites/{id}", favoriteResponse.getId())
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}