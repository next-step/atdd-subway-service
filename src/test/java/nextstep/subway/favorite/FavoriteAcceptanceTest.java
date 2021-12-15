package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
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

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
	private static StationResponse 두정역;
	private static StationResponse 천안역;
	private static StationResponse 봉명역;

	private static final String EMAIL = "wooahan@naver.com";
	private static final String PASSWORD = "password";
	private static final Integer AGE = 33;

	private static TokenResponse tokenResponse;

	@BeforeEach
	public void setUp() {
		super.setUp();
		// Given 지하철역 등록되어 있음
		두정역 = 지하철역_등록_되어있음("두정역");
		천안역 = 지하철역_등록_되어있음("천안역");
		봉명역 = 지하철역_등록_되어있음("봉명역");

		// And 지하철 노선 등록되어 있음
		LineResponse 일호선 = 지하철_노선_등록_되어있음(new LineRequest("일호선", "blue", 두정역.getId(), 봉명역.getId(), 30));

		// And 지하철 노선에 지하철역 등록되어 있음
		지하철_노선에_지하철역_등록_되어있음(일호선);

		// And 회원 등록되어 있음
		회원_등록_되어있음(EMAIL, PASSWORD, AGE);

		// And 로그인 되어있음
		tokenResponse = 로그인_되어있음(EMAIL, PASSWORD);
	}

	@Test
	@DisplayName("즐겨찾기 생성, 조회. 삭제 성공 통합테스트")
	public void manageFavoriteSuccessTest() {
		//when 즐겨찾기 생성
		FavoriteRequest favoriteRequest = new FavoriteRequest(두정역.getId(), 봉명역.getId());
		ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
			.auth().oauth2(tokenResponse.getAccessToken())
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(favoriteRequest)
			.when().post("/favorites")
			.then().log().all()
			.extract();

		//then
		assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		//when

		ExtractableResponse<Response> findResponse = RestAssured.given().log().all()
			.auth().oauth2(tokenResponse.getAccessToken())
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/favorites")
			.then().log().all()
			.extract();

		//then
		assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

		//when
		String uri = createResponse.header("Location");
		ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
			.auth().oauth2(tokenResponse.getAccessToken())
			.when().delete(uri)
			.then().log().all()
			.extract();

		//then
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
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
