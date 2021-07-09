package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
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

import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
	private StationResponse 강남역;
	private StationResponse 광교역;
	private LineResponse 신분당선;

	private String accessToken;

	@Override
	@BeforeEach
	public void setUp() {
		super.setUp();

		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
		신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

		MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, 20);
		accessToken = AuthAcceptanceTest.로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
	}

	@Test
	@DisplayName("즐겨찾기를 관리한다.")
	void favoriteAcceptanceTest() {
		ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청();
		즐겨찾기_생성됨(createResponse);

		//ExtractableResponse<Response> getResponse = 즐겨찾기_목록_조회_요청();
		//즐겨찾기_목록_조회됨(getResponse);

		//ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(1L);
		//즐겨찾기_삭제됨(deleteResponse);
	}

	private void 즐겨찾기_삭제됨(ExtractableResponse<Response> deleteResponse) {
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	private ExtractableResponse<Response> 즐겨찾기_삭제_요청(long favoriteId) {
		return RestAssured.given().log().all()
				.auth()
				.oauth2(accessToken)
				.when()
				.delete("/favorites/" + favoriteId)
				.then().log().all()
				.extract();
	}

	private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> getResponse) {
		assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
		return RestAssured.given().log().all()
				.auth()
				.oauth2(accessToken)
				.when()
				.get("/favorites")
				.then().log().all()
				.extract();
	}

	private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	private ExtractableResponse<Response> 즐겨찾기_생성_요청() {
		return RestAssured.given().log().all()
				.auth()
				.oauth2(accessToken)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(new FavoriteRequest(강남역.getId(), 광교역.getId()))
				.when()
				.post("/favorites")
				.then().log().all()
				.extract();
	}
}
