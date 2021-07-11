package nextstep.subway.favorite;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineTestMethod;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
	@DisplayName("즐겨찾기 관리 시나리오")
	@Test
	void manageFavorite() {
		// Background
		// Given : 지하철역 등록되어 있음
		StationResponse 강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		StationResponse 양재역 =  StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		// And : 지하철 노선에 지하철역 등록되어 있음
		LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
		LineResponse 신분당선 = LineTestMethod.지하철_노선_등록되어_있음(lineRequest1).as(LineResponse.class);
		// And : 회원 등록되어 있음
		String EMAIL = "email@email.com";
		String PASSWORD = "password";
		int AGE = 20;
		ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
		// And : 로그인 되어 있음
		TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
		ExtractableResponse<Response> tokenResponseCandidate1 = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(tokenRequest)
			.when().post("/login/token")
			.then().log().all()
			.extract();
		TokenResponse tokenResponse = tokenResponseCandidate1.as(TokenResponse.class);
		String token = tokenResponse.getAccessToken();
		// Scenario : 즐겨찾기 관리
		// When : 즐겨찾기 생성 요청
		FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 양재역.getId());
		ExtractableResponse<Response> favoriteResponse1 = RestAssured
			.given().log().all()
			.auth().oauth2(token)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(favoriteRequest)
			.when().post("/favorites")
			.then().log().all()
			.extract();
		// Then : 즐겨찾기 생성됨
		assertThat(favoriteResponse1.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		// When : 즐겨찾기 목록 조회 요청
		ExtractableResponse<Response> favoriteResponse2 = RestAssured
			.given().log().all()
			.auth().oauth2(token)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/favorites")
			.then().log().all()
			.extract();
		// Then : 즐겨찾기 목록 조회됨
		assertThat(favoriteResponse2.statusCode()).isEqualTo(HttpStatus.OK.value());
		// When : 즐겨찾기 삭제 요청
		FavoriteResponse favoriteResponse = favoriteResponse2.as(FavoriteResponse.class);
		ExtractableResponse<Response> favoriteResponse3 = RestAssured
			.given().log().all()
			.auth().oauth2(token)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().delete("/favorites/" + favoriteResponse.getId())
			.then().log().all()
			.extract();
		// then : 즐겨찾기 삭제됨
		assertThat(favoriteResponse3.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	@DisplayName("즐겨찾기 관리 에러 시나리오")
	@Test
	void manageFavoriteError() {
		// Background
		// Given : 지하철역 / 노선 / 회원 등록 / 로그인 되어 있지 않음
		// Scenario 즐겨찾기 관리 에러
		// When : 즐겨찾기 생성 요청
		FavoriteRequest favoriteRequest1 = new FavoriteRequest(1L, 2L);
		ExtractableResponse<Response> favoriteResponse1 = RestAssured
			.given().log().all()
			.auth().oauth2(null)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(favoriteRequest1)
			.when().post("/favorites")
			.then().log().all()
			.extract();
		// Then : 즐겨찾기 생성 실패
		assertThat(favoriteResponse1.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		// Given : 지하철역, 노선 생성
		StationResponse 강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		StationResponse 양재역 =  StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
		LineResponse 신분당선 = LineTestMethod.지하철_노선_등록되어_있음(lineRequest1).as(LineResponse.class);
		// When : 즐겨찾기 생성 요청
		FavoriteRequest favoriteRequest2 = new FavoriteRequest(1L, 2L);
		ExtractableResponse<Response> favoriteResponse2 = RestAssured
			.given().log().all()
			.auth().oauth2(null)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(favoriteRequest2)
			.when().post("/favorites")
			.then().log().all()
			.extract();
		// Then : 즐겨찾기 생성 실패
		assertThat(favoriteResponse2.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		// Given : 회원 등록
		String EMAIL = "email@email.com";
		String PASSWORD = "password";
		int AGE = 20;
		ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
		// When : 즐겨찾기 생성 요청
		FavoriteRequest favoriteRequest3 = new FavoriteRequest(1L, 2L);
		ExtractableResponse<Response> favoriteResponse3 = RestAssured
			.given().log().all()
			.auth().oauth2(null)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(favoriteRequest2)
			.when().post("/favorites")
			.then().log().all()
			.extract();
		// Then : 즐겨찾기 생성 실패
		assertThat(favoriteResponse3.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		// Given : 미로그인 상태
		// When : 즐겨찾기 목록 조회 요청
		ExtractableResponse<Response> favoriteResponse4 = RestAssured
			.given().log().all()
			.auth().oauth2(null)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/favorites")
			.then().log().all()
			.extract();
		// Then : 즐겨찾기 목록 조회 실패
		assertThat(favoriteResponse4.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		// When : 즐겨찾기 삭제 요청
		ExtractableResponse<Response> favoriteResponse5 = RestAssured
			.given().log().all()
			.auth().oauth2(null)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().delete("/favorites/" + 1)
			.then().log().all()
			.extract();
		// Then : 즐겨찾기 삭제 실패
		assertThat(favoriteResponse5.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
