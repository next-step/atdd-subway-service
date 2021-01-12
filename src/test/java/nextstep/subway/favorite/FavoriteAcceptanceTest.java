package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

	public static final String EMAIL = "email@email.com";
	public static final String PASSWORD = "password";

	private StationResponse upStation;
	private StationResponse middleStation;
	private StationResponse downStation;
	private LineResponse line;
	private TokenResponse tokenResponse;

	@BeforeEach
	public void setUp() {
		super.setUp();
		upStation = StationAcceptanceTest.지하철역_등록되어_있음("문래역").as(StationResponse.class);
		middleStation = StationAcceptanceTest.지하철역_등록되어_있음("사당역").as(StationResponse.class);
		downStation = StationAcceptanceTest.지하철역_등록되어_있음("잠실역").as(StationResponse.class);
		line = LineAcceptanceTest.지하철_노선_등록되어_있음("2호선", "green", upStation, downStation, 10)
			.as(LineResponse.class);
		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(line, middleStation, downStation, 4);
		MemberAcceptanceTest.회원_등록_되어있음(EMAIL, PASSWORD, 20);
		tokenResponse = AuthAcceptanceTest.로그인_되어있음(EMAIL, PASSWORD).as(TokenResponse.class);
	}

	@Test
	@DisplayName("즐겨찾기를 관리한다.")
	void favorite() {
		//when
		ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(upStation, downStation, tokenResponse);
		//then
		즐겨찾기_생성됨(createResponse);

		//when
		ExtractableResponse<Response> findAllResponse = 즐겨찾기_목록_조회_요청(tokenResponse);
		//then
		즐겨찾기_목록_응답됨(findAllResponse);
		즐겨찾기_목록_포함됨(findAllResponse, Arrays.asList(createResponse));

		//when
		ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(createResponse, tokenResponse);
		//then
		즐겨찾기_삭제됨(deleteResponse);
	}

	public static ExtractableResponse<Response> 즐겨찾기_생성_요청(StationResponse source, StationResponse target, TokenResponse tokenResponse) {
		FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());

		return RestAssured.given().log().all()
			.auth().oauth2(tokenResponse.getAccessToken())
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(favoriteRequest)
			.when().post("/favorites")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse tokenResponse) {
		return RestAssured.given().log().all()
			.auth().oauth2(tokenResponse.getAccessToken())
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/favorites")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> response, TokenResponse tokenResponse) {
		String uri = response.header("Location");

		return RestAssured.given().log().all()
			.auth().oauth2(tokenResponse.getAccessToken())
			.when().delete(uri)
			.then().log().all()
			.extract();
	}

	public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	public static void 즐겨찾기_목록_응답됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static void 즐겨찾기_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
		List<Long> expectedFavoriteIds = createdResponses.stream()
			.map(it -> Long.parseLong(it.header("Location").split("/")[2]))
			.collect(Collectors.toList());

		List<Long> resultFavoriteIds = response.jsonPath().getList(".", FavoriteResponse.class).stream()
			.map(FavoriteResponse::getId)
			.collect(Collectors.toList());

		assertThat(resultFavoriteIds).containsAll(expectedFavoriteIds);
	}

	public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
