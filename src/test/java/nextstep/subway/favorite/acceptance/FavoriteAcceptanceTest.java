package nextstep.subway.favorite.acceptance;

import static org.assertj.core.api.Assertions.*;

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
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
	private LineResponse 신분당선;
	private LineResponse 이호선;
	private LineResponse 삼호선;
	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 교대역;
	private StationResponse 남부터미널역;
	private String accessToken;

	@Override
	@BeforeEach
	public void setUp() {
		super.setUp();

		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
		남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

		신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10).as(LineResponse.class);
		이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10).as(LineResponse.class);
		삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5).as(LineResponse.class);

		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);

		MemberAcceptanceTest.회원_등록되어_있음("test@gmail.com", "test123", 20);

		accessToken = AuthAcceptanceTest.로그인_되어_있음("test@gmail.com", "test123");
	}

	@DisplayName("즐겨찾기를 관리한다.")
	@Test
	void manageMyFavorite() {
		// when
		ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 강남역, 교대역);
		// then
		즐겨찾기_생성됨(createResponse);

		// when
		ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(accessToken);
		// then
		즐겨찾기_목록_조회됨(findResponse);

		// when
		ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, createResponse);
		// then
		즐겨찾기_삭제됨(deleteResponse);
	}

	public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, StationResponse source,
		StationResponse target) {

		FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());

		return RestAssured
			.given().log().all()
			.auth().oauth2(accessToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(favoriteRequest)
			.when().post("/favorites")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/favorites")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> response) {
		String uri = response.header("Location");
		return RestAssured
			.given().log().all()
			.auth().oauth2(accessToken)
			.when().delete(uri)
			.then().log().all()
			.extract();
	}

	public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isNotBlank();
	}

	public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
