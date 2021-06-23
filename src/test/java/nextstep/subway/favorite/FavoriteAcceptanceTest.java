package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.dto.StationResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

	private TokenResponse 사용자;
	private	StationResponse 강남역;
	private StationResponse 광교역;

	@BeforeEach
	public void setUp() {
		super.setUp();

		강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
		광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

		LineRequest lineRequest = new LineRequest("신분당선", "red", 강남역.getId(), 광교역.getId(), 10);
		지하철_노선_등록되어_있음(lineRequest);

		회원_등록되어_있음(EMAIL, PASSWORD, AGE);
		사용자 = 로그인_되어있음(EMAIL, PASSWORD).as(TokenResponse.class);
	}

	@DisplayName("즐겨찾기를 관리한다")
	@Test
	void manageFavorite() {
		// when
		ExtractableResponse<Response> response = 즐겨찾기_생성을_요청(사용자, 강남역, 광교역);
		// then
		즐겨찾기_생성됨(response);

		// when
		ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(사용자);
		// then
		즐겨찾기_목록_조회됨(findResponse);

		// when
		ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(response, 사용자);
		//  then
		즐겨찾기_삭제됨(deleteResponse);
	}

	public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(TokenResponse token, StationResponse source, StationResponse target) {
		FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());

		return RestAssured
		        .given().log().all()
				.auth().oauth2(token.getAccessToken())
		        .body(favoriteRequest)
		        .contentType(MediaType.APPLICATION_JSON_VALUE)
		        .when().post("/favorites")
		        .then().log().all().extract();
	}

	public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse token) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(token.getAccessToken())
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/favorites")
			.then().log().all().extract();
	}

	public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		List<FavoriteResponse> favoriteResponses = response.jsonPath().getList(".", FavoriteResponse.class);
		List<Long> expected = favoriteResponses.stream().map(FavoriteResponse::getId).collect(Collectors.toList());
		List<Long> actual = response.jsonPath().getList("id", Long.class);
		assertThat(actual).containsExactlyElementsOf(expected);
	}

	public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> response, TokenResponse token) {
		String location = response.header("Location");

		return RestAssured
			.given().log().all()
			.auth().oauth2(token.getAccessToken())
			.when().delete(location)
			.then().log().all().extract();
	}

	public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
