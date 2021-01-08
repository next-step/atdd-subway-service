package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 * Feature: 즐겨찾기를 관리한다.
 * <p>
 * Background Given 지하철역 등록되어 있음 And 지하철 노선 등록되어 있음 And 지하철 노선에 지하철역 등록되어 있음 And 회원 등록되어 있음 And 로그인
 * 되어있음
 * <p>
 * Scenario: 즐겨찾기를 관리 When 즐겨찾기 생성을 요청 Then 즐겨찾기 생성됨 When 즐겨찾기 목록 조회 요청 Then 즐겨찾기 목록 조회됨 When 즐겨찾기
 * 삭제 요청 Then 즐겨찾기 삭제됨
 */
@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

	private String accessToken;

	@Override
	@BeforeEach
	public void setUp() {
		super.setUp();
		//로그인_되이_있음
		this.accessToken = 등록된_회원이_로그인_되어있음("test@dev.com", "1234", 10);
	}

	@DisplayName("즐겨찾기를 관리한다")
	@Test
	void manageFavorite() {
		//지하철역이 등록되어 있음
		Long sourceId1 = 지하철역이_등록되어_있음("강남역");
		Long targetId1 = 지하철역이_등록되어_있음("역삼역");
		Long sourceId2 = 지하철역이_등록되어_있음("삼성역");
		Long targetId2 = 지하철역이_등록되어_있음("미금역");

		//즐겨찾기 생성을 요청
		ExtractableResponse<Response> createResponse1 = 즐겨찾기_생성을_요청한다(sourceId1, targetId1);
		ExtractableResponse<Response> createResponse2 = 즐겨찾기_생성을_요청한다(sourceId2, targetId2);

		//즐겨찾기가 생성됨
		즐겨찾기가_생성됨(createResponse1);
		즐겨찾기가_생성됨(createResponse2);

		//즐겨찾기 목록 조회
		ExtractableResponse<Response> selectResponse = 즐겨찾기_목록을_조회한다();
		//즐겨찾기목록이 조회됨
		즐겨찾기_목록이_조회됨(selectResponse, 2);

		//즐겨찾기 삭제
		ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제를_요청한다(sourceId1, targetId1);
		//즐겨찾기가 삭제됨
		즐겨찾기가_삭제됨(deleteResponse);
		//즐겨찾기 목록 조회
		ExtractableResponse<Response> selectResponse2 = 즐겨찾기_목록을_조회한다();
		//즐겨찾기목록이 조회됨
		즐겨찾기_목록이_조회됨(selectResponse2, 1);
	}

	@DisplayName("비어있는 즐겨찾기 목록 조회")
	@Test
	void findAllWithEmptyFavorites() {
		//즐겨찾기 목록 조회
		ExtractableResponse<Response> selectResponse = 즐겨찾기_목록을_조회한다();
		//즐겨찾기목록이 조회됨
		즐겨찾기_목록이_조회됨(selectResponse, 0);
	}

	@DisplayName("출발역과 도착역이 같은 경우 즐겨찾기에 추가할 수 없다.")
	@Test
	void createWithSameSourceTarget() {
		//지하철역이 등록되어 있음
		Long stationId = 지하철역이_등록되어_있음("강남역");

		//즐겨찾기 생성을 요청
		ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청한다(stationId, stationId);
		//즐겨찾기 생성 실패
		요청_실패(createResponse);
	}

	@DisplayName("등록되지 않은 역은 즐겨찾기에 추가할 수 없다.")
	@Test
	void createWithNotExistStation() {
		//즐겨찾기 생성을 요청
		ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청한다(1L, 2L);
		//즐겨찾기 생성 실
		요청_실패(createResponse);
	}

	@DisplayName("등록되지 않은 즐겨찾기는 삭제할 수 없다.")
	@Test
	void deleteNotExistFavorite() {
		//지하철역이 등록되어 있음
		Long sourceId1 = 지하철역이_등록되어_있음("강남역");
		Long targetId1 = 지하철역이_등록되어_있음("역삼역");

		//즐겨찾기 삭제
		ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제를_요청한다(sourceId1, targetId1);
		요청_실패(deleteResponse);
	}

	private Long 지하철역이_등록되어_있음(String name) {
		ExtractableResponse<Response> response = StationAcceptanceTest.지하철역_등록되어_있음(name);
		return response.body().as(StationResponse.class).getId();
	}

	private String 등록된_회원이_로그인_되어있음(String email, String password, int age) {
		MemberAcceptanceTest.회원_생성을_요청(email, password, age);
		return AuthAcceptanceTest.접근권한_토큰값을_가져온다(email, password);
	}

	private ExtractableResponse<Response> 즐겨찾기_생성을_요청한다(Long sourceId,
		  Long targetId) {
		ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
			  .auth().oauth2(accessToken)
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .body(new FavoriteRequest(sourceId, targetId))
			  .when().post("/favorites")
			  .then().log().all()
			  .extract();
		return createResponse;
	}

	private ExtractableResponse<Response> 즐겨찾기_목록을_조회한다() {
		ExtractableResponse<Response> response = RestAssured.given().log().all()
			  .auth().oauth2(accessToken)
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .when().get("/favorites")
			  .then().log().all()
			  .extract();
		return response;
	}

	private ExtractableResponse<Response> 즐겨찾기_삭제를_요청한다(Long sourceId1,
		  Long targetId1) {
		ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
			  .auth().oauth2(accessToken)
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .body(new FavoriteRequest(sourceId1, targetId1))
			  .with().delete("/favorites")
			  .then().log().all()
			  .extract();
		return deleteResponse;
	}

	private void 즐겨찾기가_생성됨(ExtractableResponse<Response> createResponse) {
		assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(createResponse.header("Location")).isNotNull();
	}

	private void 즐겨찾기_목록이_조회됨(ExtractableResponse<Response> response, int contentSize) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getList(".", FavoriteResponse.class)).hasSize(contentSize);
	}

	private void 즐겨찾기가_삭제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	private void 요청_실패(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
