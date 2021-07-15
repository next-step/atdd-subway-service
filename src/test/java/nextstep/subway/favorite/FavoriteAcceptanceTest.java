package nextstep.subway.favorite;

import static nextstep.subway.auth.infrastructure.AuthorizationExtractor.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

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
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

	private static final String EMAIL = "test@eamil.com";
	private static final String PASSWORD = "password";
	private static final int AGE = 21;

	private LineResponse 신분당선;
	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 정자역;
	private StationResponse 광교역;
	private String token;

	@BeforeEach
	public void setup() {
		super.setUp();

		강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
		광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
		신분당선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

		회원_생성_되어있음(EMAIL, PASSWORD, AGE);
		TokenResponse response = 로그인_되어있음(EMAIL, PASSWORD).as(TokenResponse.class);
		token = response.getAccessToken();
	}

	@DisplayName("즐겨찾기를 관리")
	@Test
	void 즐겨찾기를_관리() {
		// When 즐겨찾기 생성을 요청
		ExtractableResponse<Response> response = 즐겨찾기_생성_요청(token, new FavoriteRequest(강남역.getId(), 광교역.getId()));
		// Then 즐겨찾기 생성됨
		즐겨찾기_생성됨(response);

		// When 즐겨찾기 목록 조회 요청
		ExtractableResponse<Response> listResponse = 즐겨찾기_목록_조회_요청(token);
		// Then 즐겨찾기 목록 조회됨
		즐겨찾기_조회됨(listResponse);
		// When 즐겨찾기 삭제 요청
		ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(token, response.header("Location"));
		// Then 즐겨찾기 삭제됨
		즐겨찾기_삭제됨(deleteResponse);
	}

	public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, FavoriteRequest params) {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(HttpHeaders.AUTHORIZATION, makeAccessToken(token))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when().post("/favorites")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(HttpHeaders.AUTHORIZATION, makeAccessToken(token))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/favorites")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, String uri) {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(HttpHeaders.AUTHORIZATION, makeAccessToken(token))
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().delete(uri)
			.then().log().all()
			.extract();
	}

	public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	public static void 즐겨찾기_조회됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	private static String makeAccessToken(String token) {
		return BEARER_TYPE + " " + token;
	}

	private ExtractableResponse<Response> 회원_생성_되어있음(String email, String password, int age) {
		ExtractableResponse<Response> createResponse = 회원_생성을_요청(email, password, age);
		// then
		회원_생성됨(createResponse);
		return createResponse;
	}

	private ExtractableResponse<Response> 로그인_되어있음(String email, String password) {
		return 로그인_요청(new TokenRequest(email, password));
	}
}
