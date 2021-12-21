package nextstep.subway.favorite.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
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

import com.google.common.collect.Lists;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
	private static StationResponse 두정역;
	private static StationResponse 천안역;
	private static StationResponse 봉명역;

	private static TokenResponse 사용자;
	private static TokenResponse 다른_사용자;
	
	private static final String EMAIL = "wooahan@naver.com";
	private static final String PASSWORD = "password";
	private static final Integer AGE = 33;
	private static final String OTHER_EMAIL = "oter@naver.com";
	private static final String OTHER_PASSWORD = "other";
	private static final Integer OTHER_AGE = 11;

	@BeforeEach
	public void setUp() {
		super.setUp();
		// Given 지하철역 등록되어 있음
		두정역 = 지하철역_등록_되어있음("두정역");
		천안역 = 지하철역_등록_되어있음("천안역");
		봉명역 = 지하철역_등록_되어있음("봉명역");

		// And 지하철 노선 등록되어 있음
		LineResponse 일호선 = 지하철_노선_등록_되어있음(new LineRequest("일호선", "blue", 두정역.getId(), 봉명역.getId(), 30, 900));

		// And 지하철 노선에 지하철역 등록되어 있음
		지하철_노선에_지하철역_등록_되어있음(일호선);

		// And 회원 등록되어 있음
		회원_등록_되어있음(EMAIL, PASSWORD, AGE);

		// And 로그인 되어있음
		사용자 = 로그인_되어있음(EMAIL, PASSWORD);
	}

	@Test
	@DisplayName("즐겨찾기 생성, 조회. 삭제 성공 통합테스트")
	public void manageFavoriteSuccessTest() {
		//when 즐겨찾기 생성
		ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(사용자, new FavoriteRequest(두정역.getId(), 봉명역.getId()));

		//then
		즐겨찾기_생성_성공(createResponse);

		//when
		ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(사용자);

		//then
		즐겨찾기_목록_조회_성공(findResponse);
		즐겨찾기_목록_조회_데이터_검증(findResponse);

		//when
		String uri = createResponse.header("Location");
		ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(사용자, uri);

		//then
		즐겨찾기_삭제_성공(deleteResponse);
	}

	@Test
	@DisplayName("즐겨찾기 삭제권한 없어서 실패 테스트")
	public void deleteFavoriteFailTest() {
		//given 다른 사용자 생성
		회원_등록_되어있음(OTHER_EMAIL, OTHER_PASSWORD, OTHER_AGE);
		다른_사용자 = 로그인_되어있음(OTHER_EMAIL, OTHER_PASSWORD);

		//when
		ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(사용자, new FavoriteRequest(두정역.getId(), 천안역.getId()));

		//then
		즐겨찾기_생성_성공(createResponse);

		//when
		String uri = createResponse.header("Location");
		ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(다른_사용자, uri);

		//then
		즐겨찾기_삭제_실패(deleteResponse);
	}

	private void 즐겨찾기_삭제_실패(ExtractableResponse<Response> deleteResponse) {
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	private void 즐겨찾기_삭제_성공(ExtractableResponse<Response> deleteResponse) {
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	private ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse tokenResponse, String uri) {
		return RestAssured.given().log().all()
			.auth().oauth2(tokenResponse.getAccessToken())
			.when().delete(uri)
			.then().log().all()
			.extract();
	}

	private void 즐겨찾기_목록_조회_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 즐겨찾기_목록_조회_데이터_검증(ExtractableResponse<Response> response) {
		List<FavoriteResponse> favoriteResponses = response.jsonPath().getList(".", FavoriteResponse.class);
		favoriteResponses.containsAll(Lists.newArrayList(두정역, 봉명역));
	}

	private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse tokenResponse) {
		return RestAssured.given().log().all()
			.auth().oauth2(tokenResponse.getAccessToken())
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/favorites")
			.then().log().all()
			.extract();
	}

	private void 즐겨찾기_생성_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	private ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse tokenResponse, FavoriteRequest favoriteRequest) {
		return RestAssured.given().log().all()
			.auth().oauth2(tokenResponse.getAccessToken())
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(favoriteRequest)
			.when().post("/favorites")
			.then().log().all()
			.extract();
	}

	private StationResponse 지하철역_등록_되어있음(String stationName) {
		return StationAcceptanceTest.지하철역_생성_요청(stationName).as(StationResponse.class);
	}

	private ExtractableResponse<Response> 지하철_노선에_지하철역_등록_되어있음(LineResponse 일호선) {
		return LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(일호선, 두정역, 천안역, 13);
	}

	private LineResponse 지하철_노선_등록_되어있음(LineRequest lineRequest) {
		return LineAcceptanceTest.지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
	}

	private ExtractableResponse<Response> 회원_등록_되어있음(String email, String password, Integer age) {
		return MemberAcceptanceTest.회원_생성을_요청(email, password, age);
	}

	private TokenResponse 로그인_되어있음(String email, String password) {
		return AuthAcceptanceTest.로그인_요청(new TokenRequest(email, password)).as(TokenResponse.class);
	}

}
