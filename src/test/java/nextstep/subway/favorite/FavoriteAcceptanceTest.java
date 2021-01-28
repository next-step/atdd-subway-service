package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 판교역;
	private StationResponse 광교역;
	private LineResponse 신분당선;

	@BeforeEach
	public void setup() {
		// given
		강남역 = StationAcceptanceTest.지하철역_생성_요청("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_생성_요청("양재역").as(StationResponse.class);
		판교역 = StationAcceptanceTest.지하철역_생성_요청("판교역").as(StationResponse.class);
		광교역 = StationAcceptanceTest.지하철역_생성_요청("광교역").as(StationResponse.class);

		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 30);
		신분당선 = LineAcceptanceTest.지하철_노선_생성_요청(lineRequest).as(LineResponse.class);

		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선,강남역,양재역,5);
		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선,양재역,판교역,10);

		MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD,
			MemberAcceptanceTest.AGE);
	}

	@DisplayName("즐겨찾기를 관리한다.")
	@Test
	void manageFavorite() {
		String accessToken = AuthAcceptanceTest.로그인을_시도한다(MemberAcceptanceTest.EMAIL,
			MemberAcceptanceTest.PASSWORD).as(TokenResponse.class).getAccessToken();

		// when
		ExtractableResponse<Response> createdResponse = 즐겨찾기_생성_요청(accessToken, 강남역.getId(), 판교역.getId());

		// then
		즐겨찾기_생성됨(createdResponse);

		// when
		ExtractableResponse<Response> listResponse = 즐겨찾기_목록_조회_요청(accessToken);

		// then
		즐겨찾기_목록_조회됨(listResponse);

		// given
		List<FavoriteResponse> favoriteResponses = listResponse.jsonPath()
			.getList(".", FavoriteResponse.class);
		Long favoriteId = favoriteResponses.get(0).getId();

		// when
		ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, favoriteId);
		listResponse = 즐겨찾기_목록_조회_요청(accessToken);

		// then
		즐겨찾기_삭제됨(deleteResponse, listResponse, favoriteId);

	}

	@DisplayName("다른 사용자가 즐겨찾기 삭제 처리시 예외")
	@Test
	void throwExceptionWhenOthersTryDeleteFavorite() {
		// 기존 회원의 즐겨찾기 생성
		String accessToken = AuthAcceptanceTest.로그인을_시도한다(MemberAcceptanceTest.EMAIL,
			MemberAcceptanceTest.PASSWORD).as(TokenResponse.class).getAccessToken();
		ExtractableResponse<Response> createdResponse = 즐겨찾기_생성_요청(accessToken, 강남역.getId(), 판교역.getId());
		즐겨찾기_생성됨(createdResponse);

		// 기존 회원 즐겨 찾기 목록 조회
		ExtractableResponse<Response> listResponse = 즐겨찾기_목록_조회_요청(accessToken);
		즐겨찾기_목록_조회됨(listResponse);
		List<FavoriteResponse> favoriteResponses = listResponse.jsonPath()
			.getList(".", FavoriteResponse.class);
		Long favoriteId = favoriteResponses.get(0).getId();

		// 신규 회원 생성 및 로그인
		MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.NEW_EMAIL, MemberAcceptanceTest.NEW_PASSWORD,
			MemberAcceptanceTest.NEW_AGE);
		String newAccessToken = AuthAcceptanceTest.로그인을_시도한다(MemberAcceptanceTest.NEW_EMAIL,
			MemberAcceptanceTest.NEW_PASSWORD).as(TokenResponse.class).getAccessToken();

		// when 신규 회원 토큰 정보로 기존 회원이 가진 즐겨찾기 삭제 요청
		ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(newAccessToken, favoriteId);

		// then
		즐겨찾기_삭제_권한_없음_예외(deleteResponse);

	}

	public static ExtractableResponse<Response> 즐겨찾기_생성_요청(final String accessToken,
		final Long sourceId, final Long targetId) {
		FavoriteRequest request = new FavoriteRequest(sourceId, targetId);
		return RestAssured
			.given().log().all()
			.auth().oauth2(accessToken)
			.body(request)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/favorites")
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(final String accessToken) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.ALL_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/favorites")
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> 즐겨찾기_삭제_요청(final String accessToken,
		final Long id) {

		return RestAssured
			.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.ALL_VALUE)
			.when().delete("/favorites/" + id)
			.then().log().all()
			.extract();
	}

	private void 즐겨찾기_생성됨(final ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	private void 즐겨찾기_목록_조회됨(final ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 즐겨찾기_삭제됨(final ExtractableResponse<Response> response,
		final ExtractableResponse<Response> listResponse, final Long favoriteId) {

		List<FavoriteResponse> favoriteResponses = listResponse.jsonPath()
			.getList(".", FavoriteResponse.class);
		List<Long> ids = favoriteResponses.stream().map(FavoriteResponse::getId)
			.collect(Collectors.toList());

		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
			() -> assertThat(ids).doesNotContain(favoriteId)
		);
	}

	private void 즐겨찾기_삭제_권한_없음_예외(final ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

}
