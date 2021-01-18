package nextstep.subway.favorite;

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
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.PathAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
	private String 사용자;
	private LineResponse 신분당선;
	private LineResponse 이호선;
	private LineResponse 삼호선;
	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 교대역;
	private StationResponse 남부터미널역;

	@BeforeEach
	public void setUp() {
		super.setUp();
		//given
		지하철역_등록되어_있음();
		지하철_노선_등록되어_있음();
		지하철_노선에_지하철역_등록되어_있음();
		회원_등록되어_있음();
		로그인_되어있음();
	}

	@DisplayName("즐겨찾기를 관리한다.")
	@Test
	void manageMember() {
		// when
		ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(사용자, 강남역.getId(), 양재역.getId());
		// then
		즐겨찾기_생성됨(createResponse);

		//when
		ExtractableResponse<Response> findResponse = 즐겨찾기_조회_요청(사용자);
		//then
		즐겨찾기_조회됨(findResponse,2);

	}

	private ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/favorites")
			.then().log().all()
			.extract();
	}

	private void 즐겨찾기_조회됨(ExtractableResponse<Response> response, int size) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getList(".",FavoriteResponse.class)).hasSize(1);
	}

	private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	private ExtractableResponse<Response> 즐겨찾기_생성을_요청(String accessToken, Long source,
		Long target) {
		return RestAssured
			.given().log().all()
			.auth().oauth2(accessToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new FavoriteRequest(source, target))
			.when().post("/favorites")
			.then().log().all().extract();
	}

	public void 로그인_되어있음() {
		ExtractableResponse<Response> response = AuthAcceptanceTest.로그인_요청(MemberAcceptanceTest.EMAIL,
			MemberAcceptanceTest.PASSWORD);
		TokenResponse tokenResponse = response.as(TokenResponse.class);
		사용자 = tokenResponse.getAccessToken();
	}

	public void 회원_등록되어_있음() {
		MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD,
			MemberAcceptanceTest.AGE);
	}

	public void 지하철_노선에_지하철역_등록되어_있음() {
		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
	}

	public void 지하철_노선_등록되어_있음() {
		신분당선 = PathAcceptanceTest.지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
		이호선 = PathAcceptanceTest.지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
		삼호선 = PathAcceptanceTest.지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);
	}

	public void 지하철역_등록되어_있음() {
		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
		남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
	}

}