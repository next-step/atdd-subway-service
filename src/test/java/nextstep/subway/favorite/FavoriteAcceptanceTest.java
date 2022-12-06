package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.auth.infrastructure.AuthorizationExtractor.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private String email = "byunsw4@naver.com";
    private String password = "password123!";
    private String token;
    StationResponse 강남역;
    StationResponse 역삼역;
    StationResponse 삼성역;
    StationResponse 잠실역;
    @BeforeEach
    void beforeEach(){
        // 지하철역 등록
        강남역 = 지하철역_생성_요청("강남역").as(StationResponse.class);
        역삼역 = 지하철역_생성_요청("역삼역").as(StationResponse.class);
        삼성역 = 지하철역_생성_요청("삼성역").as(StationResponse.class);
        잠실역 = 지하철역_생성_요청("잠실역").as(StationResponse.class);

        // 노선 등록
        LineResponse 이호선 = 지하철_노선_생성_요청(new LineRequest("2호선", "green", 강남역.getId(), 역삼역.getId(), 10)).as(LineResponse.class);

        // 구간 등록
        지하철_노선에_지하철역_등록_요청(이호선, 역삼역, 삼성역, 15);
        지하철_노선에_지하철역_등록_요청(이호선, 삼성역, 잠실역, 20);

        // 회원등록
        회원_생성을_요청(email, password,30);

        // 로그인
        TokenResponse tokenResponse = 로그인_요청(email, password).as(TokenResponse.class);
        token = tokenResponse.getAccessToken();
    }

    /**
     * Feature: 즐겨찾기를 생성한다.
     *  Background
     *      given: 지하철역 등록되어 있음
     *      And: 지하철 노선 등록되어 있음
     *      And: 지하철 노선에 지하철역 등록되어 있음
     *      And: 회원 등록 되어있음
     *      And: 로그인 되어있음
     *
     *  Scenario: 즐겨찾기 생성
     *      when: 즐겨찾기 생성을 요청
     *      then: 즐겨찾기가 생성됨
     */
    @Test
    @DisplayName("즐겨찾기 생성 테스트")
    void addFavorite(){
        // given

        // when
        ExtractableResponse<Response> 즐겨찾기_생성_요청_결과 = 즐겨찾기_생성_요청(강남역.getId(), 잠실역.getId());

        // then
        즐겨찾기_생성됨(즐겨찾기_생성_요청_결과);
    }

    /**
     * Feature: 즐겨찾기 목록을 조회한다.
     *  Background
     *      given: 지하철역 등록되어 있음
     *      And: 지하철 노선 등록되어 있음
     *      And: 지하철 노선에 지하철역 등록되어 있음
     *      And: 회원 등록 되어있음
     *      And: 로그인 되어있음
     *
     *  Scenario: 즐겨찾기 목록 조회
     *      given: 즐겨찾기 추가
     *      when: 즐겨찾기 목록 조회를 요청
     *      then: 즐겨찾기 목록이 조회됨
     */
    @Test
    @DisplayName("즐겨찾기 목록 조회 테스트")
    void listFavorite(){
        // given
        즐겨찾기_생성_요청(강남역.getId(), 잠실역.getId());
        즐겨찾기_생성_요청(역삼역.getId(), 잠실역.getId());

        // when
        ExtractableResponse<Response> 즐겨찾기_목록_조회_요청_결과 = 즐겨찾기_목록_조회_요청();

        // then
        즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_요청_결과);
    }

    /**
     * Feature: 즐겨찾기를 삭제한다.
     *  Background
     *      given: 지하철역 등록되어 있음
     *      And: 지하철 노선 등록되어 있음
     *      And: 지하철 노선에 지하철역 등록되어 있음
     *      And: 회원 등록 되어있음
     *      And: 로그인 되어있음
     *
     *  Scenario: 즐겨찾기 삭제
     *      given: 즐겨찾기 추가
     *      when: 즐겨찾기 삭제를 요청
     *      then: 즐겨찾기가 삭제됨
     */
    @Test
    @DisplayName("즐겨찾기 삭제 테스트")
    void deleteFavorite(){
        // given
        즐겨찾기_생성_요청(강남역.getId(), 잠실역.getId());
        ExtractableResponse<Response> 즐겨찾기_생성_요청_결과 = 즐겨찾기_생성_요청(역삼역.getId(), 잠실역.getId());

        // when: 즐겨찾기 삭제 요청
        ExtractableResponse<Response> 즐겨찾기_삭제_요청_결과 = 즐겨찾기_삭제_요청(즐겨찾기_생성_요청_결과);

        // then: 즐겨찾기가 삭제됨
        즐겨찾기_삭제됨(즐겨찾기_삭제_요청_결과);
    }

    /**
     * Feature: 즐겨찾기를 관리한다.
     *  Background
     *      given: 지하철역 등록되어 있음
     *      And: 지하철 노선 등록되어 있음
     *      And: 지하철 노선에 지하철역 등록되어 있음
     *      And: 회원 등록 되어있음
     *      And: 로그인 되어있음
     *
     *  Scenario: 즐겨찾기 관리
     *      when: 즐겨찾기 생성을 요청
     *      then: 즐겨찾기가 생성됨
     *      when: 즐겨찾기 목록 조회 요청
     *      then: 즐겨찾기가 조회됨
     *      when: 즐겨찾기 삭제 요청
     *      then: 즐겨찾기가 삭제됨
     */
    @Test
    @DisplayName("즐겨찾기 관리 기능")
    void favoriteTest() {
        // given

        // when: 즐겨찾기 생성을 요청
        ExtractableResponse<Response> 즐겨찾기_생성_요청_결과 = 즐겨찾기_생성_요청(강남역.getId(), 잠실역.getId());

        // then: 즐겨찾기가 생성됨
        즐겨찾기_생성됨(즐겨찾기_생성_요청_결과);

        // when: 즐겨찾기 목록 조회 요청
        ExtractableResponse<Response> 즐겨찾기_목록_조회_요청_결과 = 즐겨찾기_목록_조회_요청();

        // then: 즐겨찾기가 조회됨
        즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_요청_결과);

        // when: 즐겨찾기 삭제 요청
        ExtractableResponse<Response> 즐겨찾기_삭제_요청_결과 = 즐겨찾기_삭제_요청(즐겨찾기_생성_요청_결과);

        // then: 즐겨찾기가 삭제됨
        즐겨찾기_삭제됨(즐겨찾기_삭제_요청_결과);
    }

    /**
     * Feature: 유효하지 않는 인증정보로 즐겨찾기 추가/삭제/조회 할 수 없다.
     *  Background:
     *      given: 유효하지 않은 인증정보
     *
     *  Scenario: 유효하지 않는 인증정보를 통한 즐쳐찾기 요청
     *      when: 즐겨찾기 생성을 요청하면
     *      then: 인증 실패결과에 따라 즐겨찾기가 생성되지 않고,
     *      when: 즐겨찾기 삭제를 요청하면
     *      then: 인증 실패결과에 따라 즐겨찾기가 삭제되지 않고,
     *      when: 즐겨찾기 목록 조회를 요청하면
     *      then: 인증 실패결과에 따라 즐겨찾기가 조회되지 않는다.
     */
    @Test
    @DisplayName("인증 토큰 유효성 예외 테스트")
    void tokenExpireException(){
        // given
        String unAuthorizedToken = "un-authorized-token";

        // when
        ExtractableResponse<Response> 즐겨찾기_생성_요청_결과 = 즐겨찾기_생성_요청(강남역.getId(), 잠실역.getId(), unAuthorizedToken);

        // then
        요청_결과가_인증_오류로_인해_실패됨(즐겨찾기_생성_요청_결과);

        // when
        ExtractableResponse<Response> 즐겨찾기_목록_조회_요청_결과 = 즐겨찾기_목록_조회_요청(unAuthorizedToken);

        // then
        요청_결과가_인증_오류로_인해_실패됨(즐겨찾기_목록_조회_요청_결과);

        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_요청_결과 = 즐겨찾기_삭제_요청(즐겨찾기_생성_요청(강남역.getId(), 잠실역.getId()), unAuthorizedToken);

        // then
        요청_결과가_인증_오류로_인해_실패됨(즐겨찾기_삭제_요청_결과);
    }

    private void 요청_결과가_인증_오류로_인해_실패됨(ExtractableResponse<Response> 즐겨찾기_생성_요청_결과) {
        assertThat(즐겨찾기_생성_요청_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(Long sourceId, Long targetId) {
        return 즐겨찾기_생성_요청(sourceId, targetId, token);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(Long sourceId, Long targetId, String authToken) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", sourceId);
        params.put("target", targetId);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, BEARER_TYPE + " "+ authToken)
                .body(params)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return 즐겨찾기_목록_조회_요청(token);
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String authToken) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, BEARER_TYPE + " "+ authToken)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> response) {
        return 즐겨찾기_삭제_요청(response, token);
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> response, String authToken) {
        FavoriteResponse favoriteResponse = response.as(FavoriteResponse.class);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, BEARER_TYPE + " "+ authToken)
                .when().delete("/favorites/{id}", favoriteResponse.getId())
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}