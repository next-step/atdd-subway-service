package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteCreateRequest;
import nextstep.subway.line.LineTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
	public static final String 신분당선_이름 = "신분당선";
	private static StationResponse 삼성역;
	private static StationResponse 선릉역;
	private static StationResponse 역삼역;
	private static StationResponse 강남역;
	public static final String 삼성역_이름 = "삼성역";
	private static final String 선릉역_이름 = "선릉역";
	private static final String 역삼역_이름 = "역삼역";
	private static final String 강남역_이름 = "강남역";
	private LineResponse 신분당선;

	@Test
	@DisplayName("즐겨찾기를 관리한다.")
	void manageFavorites() {
		ExtractableResponse<Response> loginResponse = manageFavorites_background();

		ExtractableResponse<Response> favoriteCreateResponse = 즐겨찾기_생성_요청(loginResponse, 삼성역.getId(), 선릉역.getId());
		즐겨찾기_생성됨(favoriteCreateResponse);
		ExtractableResponse<Response> favoriteReadResponse = 즐겨찾기_목록_조회_요청(loginResponse);
		즐겨찾기_목록_조회됨(favoriteReadResponse);
		즐겨찾기_목록_갯수_검증(favoriteReadResponse, 1);
		ExtractableResponse<Response> favoriteRemoveResponse = 즐겨찾기_삭제_요청(loginResponse, favoriteCreateResponse);
		즐겨찾기_삭제됨(favoriteRemoveResponse);
		ExtractableResponse<Response> favoriteReadResponseAfterDelete = 즐겨찾기_목록_조회_요청(loginResponse);
		즐겨찾기_목록_갯수_검증(favoriteReadResponseAfterDelete, 0);
	}

	private ExtractableResponse<Response> 즐겨찾기_생성_요청(ExtractableResponse<Response> loginResponse, Long sourceStationId,
		Long targetStationId) {
		FavoriteCreateRequest favoriteCreateRequest = new FavoriteCreateRequest(sourceStationId, targetStationId);
		return RestAssured.given().log().all()
			.auth().oauth2(loginResponse.jsonPath().getString("accessToken"))
			.contentType(ContentType.JSON)
			.body(favoriteCreateRequest)
			.when().log().all()
			.post("/favorites")
			.then().log().all()
			.extract();
	}

	private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(ExtractableResponse<Response> loginResponse) {
		return RestAssured.given().log().all()
			.auth().oauth2(loginResponse.jsonPath().getString("accessToken"))
			.when().log().all()
			.get("/favorites")
			.then().log().all()
			.extract();
	}

	private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 즐겨찾기_목록_갯수_검증(ExtractableResponse<Response> response, long listSize) {
		assertThat(response.jsonPath().getList(".").size()).isEqualTo(listSize);
	}

	private ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> loginResponse,
		ExtractableResponse<Response> favoriteCreateResponse) {
		String urlFavoriteCreated = favoriteCreateResponse.header("Location");
		return RestAssured.given().log().all()
			.auth().oauth2(loginResponse.jsonPath().getString("accessToken"))
			.when().log().all()
			.delete(urlFavoriteCreated)
			.then().log().all()
			.extract();
	}

	private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	private ExtractableResponse<Response> manageFavorites_background() {
		삼성역 = StationAcceptanceTest.지하철역_등록되어_있음(삼성역_이름).as(StationResponse.class);
		선릉역 = StationAcceptanceTest.지하철역_등록되어_있음(선릉역_이름).as(StationResponse.class);
		역삼역 = StationAcceptanceTest.지하철역_등록되어_있음(역삼역_이름).as(StationResponse.class);
		강남역 = StationAcceptanceTest.지하철역_등록되어_있음(강남역_이름).as(StationResponse.class);
		LineRequest 삼성_선릉_구간 = new LineRequest(신분당선_이름, LineTest.BG_RED_600, 삼성역.getId(), 선릉역.getId(), 5);

		신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(삼성_선릉_구간).as(LineResponse.class);

		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(신분당선, 선릉역, 역삼역, 5);
		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(신분당선, 역삼역, 강남역, 5);
		MemberAcceptanceTest.회원_생성되어_있음(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD,
			MemberAcceptanceTest.AGE);
		ExtractableResponse<Response> loginResponse = AuthAcceptanceTest.로그인_시도(
			MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);
		AuthAcceptanceTest.로그인_성공함(loginResponse);
		return loginResponse;
	}
}