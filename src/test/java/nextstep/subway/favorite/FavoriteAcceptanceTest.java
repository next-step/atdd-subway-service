package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favortie.dto.FavoriteRequest;
import nextstep.subway.favortie.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

	private LineResponse 신분당선;
	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 교대역;
	private StationResponse 남부터미널역;
	private TokenResponse 토큰;
	private TokenResponse 다른_유저_토큰;

	@BeforeEach
	public void setup() {
		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);

		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
		신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 교대역, 3);

		String email = "chaeyun@github.com";
		String password = "chaeyun123";
		MemberAcceptanceTest.회원_등록됨(email, password);
		토큰 = AuthAcceptanceTest.로그인됨(email, password);

		String email2 = "chaeyun2@github.com";
		String password2 = "chaeyun123";
		MemberAcceptanceTest.회원_등록됨(email2, password2);
		다른_유저_토큰 = AuthAcceptanceTest.로그인됨(email2, password2);
	}

	@DisplayName("즐겨찾기를 관리")
	@Test
	void manageFavorite() {
		// When
		ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(토큰, 양재역, 교대역);
		// Then
		즐겨찾기_생성됨(createResponse);

		// When
		ExtractableResponse<Response> foundResponse = 즐겨찾기_목록_조회_요청(토큰);
		// Then
		즐겨찾기_목록_조회됨(foundResponse, Collections.singletonList(createResponse));

		// When
		ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(토큰, createResponse);
		// Then
		즐겨찾기_삭제됨(deleteResponse);

		// Given
		ExtractableResponse<Response> createResponse2 = 즐거찾기_등록됨(토큰, 양재역, 교대역);
		// When
		ExtractableResponse<Response> deleteResponse2 = 즐겨찾기_삭제_요청(다른_유저_토큰, createResponse2);
		// Then
		즐겨찾기_삭제_실패함(deleteResponse2);
	}

	private static ExtractableResponse<Response> 즐거찾기_등록됨(TokenResponse token, StationResponse source,
		StationResponse target) {
		return 즐겨찾기_생성_요청(token, source, target);
	}

	private static void 즐겨찾기_삭제_실패함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	private void 즐겨찾기_삭제됨(ExtractableResponse<Response> deleteResponse) {
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	private static ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse token,
		ExtractableResponse<Response> createResponse) {

		String uri = createResponse.header("Location");

		return RestAssured.given().log().all().
			auth().oauth2(token.getAccessToken())
			.when().delete(uri)
			.then().log().all().
			extract();
	}

	private static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response,
		List<ExtractableResponse<Response>> createdResponses) {
		List<Long> expectedLineIds = createdResponses.stream()
			.map(it -> Long.parseLong(it.header("Location").split("/")[2]))
			.collect(Collectors.toList());

		List<Long> resultLineIds = response.jsonPath()
			.getList(".", FavoriteResponse.class).stream()
			.map(FavoriteResponse::getId)
			.collect(Collectors.toList());

		assertThat(resultLineIds).containsAll(expectedLineIds);
	}

	private static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse token) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(token.getAccessToken())
			.when().get("/favorites")
			.then().log().all()
			.extract();
	}

	private static ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse token, StationResponse source,
		StationResponse target) {
		FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());
		return RestAssured
			.given().log().all()
			.auth().oauth2(token.getAccessToken())
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(favoriteRequest)
			.when().post("/favorites")
			.then().log().all()
			.extract();
	}

	private static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

}
