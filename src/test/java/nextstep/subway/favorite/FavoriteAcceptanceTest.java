package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    StationResponse 왕십리역;
    StationResponse 서울숲역;
    StationResponse 강남구청역;
    StationResponse 선릉역;

    LineResponse 분당선;

    String 사용자;

    @BeforeEach
    void init() {
        // given
        왕십리역 = StationAcceptanceTest.지하철역_등록되어_있음("왕십리역").as(StationResponse.class);
        서울숲역 = StationAcceptanceTest.지하철역_등록되어_있음("서울숲역").as(StationResponse.class);
        강남구청역 = StationAcceptanceTest.지하철역_등록되어_있음("강남구청역").as(StationResponse.class);
        선릉역 = StationAcceptanceTest.지하철역_등록되어_있음("선릉역").as(StationResponse.class);
        // and
        분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("분당선", "노랑", 왕십리역.getId(), 선릉역.getId(), 7)).as(LineResponse.class);
        // and
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(분당선, 왕십리역, 서울숲역, 3);
        // and
        MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);
        // and
        사용자 = AuthAcceptanceTest.로그인되어_있음(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);
    }

    /**
     * Feature: 즐겨찾기를 관리한다.
     * <p>
     * Background
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     * And 회원 등록되어 있음
     * And 로그인 되어있음
     * <p>
     * Scenario: 즐겨찾기를 관리
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨찾기 목록 조회됨
     * When 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(사용자, 서울숲역.getId(), 선릉역.getId());
        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> updateResponse = 즐겨찾기_목록_조회_요청(사용자);
        // then
        즐겨찾기_목록_조회됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(사용자, createResponse);
        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Long source, Long target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source, target);

        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
