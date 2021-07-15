package nextstep.subway.favorite.acceptance;

import static nextstep.subway.auth.acceptance.AuthTestMethod.*;
import static nextstep.subway.favorite.acceptance.FavoriteTestMethod.*;
import static nextstep.subway.line.acceptance.LineTestMethod.*;
import static nextstep.subway.member.MemberTestMethod.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
	@DisplayName("즐겨찾기 관리 시나리오")
	@Test
	void manageFavorite() {
		// Background
		// Given
		StationResponse 강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		StationResponse 양재역 =  StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		// And
		LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
		LineResponse 신분당선 = 지하철_노선_등록되어_있음(lineRequest1).as(LineResponse.class);
		// And
		String EMAIL = "email@email.com";
		String PASSWORD = "password";
		int AGE = 20;
		ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
		// And
		String token = getToken(login(EMAIL, PASSWORD));
		// Scenario : 즐겨찾기 관리
		// When
		ExtractableResponse<Response> favoriteResponse1 = createFavorite(token, 강남역.getId(), 양재역.getId());
		// Then
		assertThat(favoriteResponse1.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		// When
		ExtractableResponse<Response> favoriteResponse2 = findFavorite(token);
		// Then
		assertThat(favoriteResponse2.statusCode()).isEqualTo(HttpStatus.OK.value());
		// When
		List<FavoriteResponse> favoriteResponses = favoriteResponse2.jsonPath().getList(".", FavoriteResponse.class);
		ExtractableResponse<Response> favoriteResponse3 = deleteFavorite(token, favoriteResponses.get(0).getId());
		// then
		assertThat(favoriteResponse3.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	@DisplayName("즐겨찾기 관리 에러 시나리오")
	@Test
	void manageFavoriteError() {
		// Background
		// Given : 지하철역 / 노선 등록 되어 있지 않음
		// Given : 회원 가입 / 로그인 되어 있음
		String EMAIL = "email@email.com";
		String PASSWORD = "password";
		int AGE = 20;
		ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
		String token = getToken(login(EMAIL, PASSWORD));
		// Scenario 즐겨찾기 관리 에러
		// When
		ExtractableResponse<Response> favoriteResponse1 = createFavorite(token, 1L, 2L);
		// Then
		assertThat(favoriteResponse1.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		// When
		ExtractableResponse<Response> favoriteResponse2 = findFavorite(token);
		// Then
		assertThat(favoriteResponse2.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
		// When
		ExtractableResponse<Response> favoriteResponse3 = deleteFavorite(token, 1L);
		// Then
		assertThat(favoriteResponse3.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
