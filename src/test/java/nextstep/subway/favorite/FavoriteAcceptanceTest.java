package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private LineResponse 이호선;
    private StationResponse 강남역;
    private StationResponse 삼성역;
    private StationResponse 교대역;
    private MemberRequest 회원;
    private TokenResponse 토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        삼성역 = StationAcceptanceTest.지하철역_등록되어_있음("삼성역").as(StationResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 5)).as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 강남역, 삼성역, 15);

        회원 = new MemberRequest("jdragon@woo.com", "12345", 20);
        AuthAcceptanceTest.회원등록됨(회원);
        토큰 = AuthAcceptanceTest.로그인_되어있음(회원);
    }

    @DisplayName("즐겨찾기 관리")
    @Test
    void favorites() {
        //    When 즐겨찾기 생성을 요청
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(토큰, 교대역.getId(), 삼성역.getId());
        //    Then 즐겨찾기 생성됨
        즐겨찾기_생성_확인(createResponse);

        //    When 즐겨찾기 목록 조회 요청
        ExtractableResponse<Response> findResponse = 즐겨찾기_조회_요청(토큰);
        //    Then 즐겨찾기 목록 조회됨
        즐겨찾기_조회_확인(findResponse, 교대역, 삼성역);

        //    When 즐겨찾기 삭제 요청
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(토큰, 1);
        //    Then 즐겨찾기 삭제됨
        즐겨찾기_삭제_확인(deleteResponse);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse tokenResponse, Long source, Long target) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new FavoriteRequest(source, target))
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성_확인(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청(TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse tokenResponse, int favoriteId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete("/favorites/{id}", favoriteId)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_조회_확인(ExtractableResponse<Response> findResponse, StationResponse 교대역, StationResponse 삼성역) {
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<FavoriteResponse> list = findResponse.jsonPath().getList("", FavoriteResponse.class);
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getSource().getName()).isEqualTo(교대역.getName());
        assertThat(list.get(0).getTarget().getName()).isEqualTo(삼성역.getName());
    }

    private void 즐겨찾기_삭제_확인(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
