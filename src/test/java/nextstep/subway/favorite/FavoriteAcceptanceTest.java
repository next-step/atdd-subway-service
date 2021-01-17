package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.PathAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
	private LineResponse 신분당선;
	private LineResponse 이호선;
	private LineResponse 삼호선;
	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 교대역;
	private StationResponse 남부터미널역;
	private TokenResponse tokenResponse;
	private String email = "manageFavorite@email.com";

	@BeforeEach
	public void setUp() {
		super.setUp();
//		Given 지하철역 등록되어 있음
		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
		남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
//		And 지하철 노선 등록되어 있음
//		And 지하철 노선에 지하철역 등록되어 있음
		신분당선 = PathAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10));
		이호선 = PathAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-400", 교대역.getId(), 강남역.getId(), 10));
		삼호선 = PathAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-yellow-600", 교대역.getId(), 양재역.getId(), 5));
		SectionRequest sectionRequest = new SectionRequest(교대역.getId(), 남부터미널역.getId(), 3);
		ExtractableResponse<Response> responseExtractableResponse = PathAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(삼호선.getId(), sectionRequest);
//		And 회원 등록되어 있음
//		And 로그인 되어있음
		//given 회원 등록되어있음

		회원_생성을_요청(email, MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);
		//when 로그인 요청
		ExtractableResponse<Response> response = AuthAcceptanceTest.로그인_요청(email, MemberAcceptanceTest.PASSWORD);
		tokenResponse = response.as(TokenResponse.class);
	}

	@DisplayName("즐겨찾기 관리")
	@Test
	void manageFavorite() {
//		When 즐겨찾기 생성을 요청
		FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 교대역.getId());
		ExtractableResponse<Response> response = 즐겨찾기_생성_요청(tokenResponse.getAccessToken(), favoriteRequest);
		FavoriteResponse postResponse= response.as(FavoriteResponse.class);
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

//		Then 즐겨찾기 생성됨
//		When 즐겨찾기 목록 조회 요청
//		Then 즐겨찾기 목록 조회됨
//		When 즐겨찾기 삭제 요청
//		Then 즐겨찾기 삭제됨

	}

	public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, FavoriteRequest favoriteRequest) {
		ExtractableResponse<Response> responseExtractableResponse = RestAssured
				.given().log().all()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(ContentType.JSON)
				.auth().oauth2(accessToken)
				.body(favoriteRequest)
				.when().post("/favorites")
				.then().log().all()
				.extract();
		return responseExtractableResponse;
	}

}