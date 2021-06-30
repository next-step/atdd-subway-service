package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

	private AuthAcceptanceTest authAcceptanceTest = new AuthAcceptanceTest();
	private MemberAcceptanceTest memberAcceptanceTest = new MemberAcceptanceTest();
	private LineAcceptanceTest lineAcceptanceTest = new LineAcceptanceTest();
	private StationAcceptanceTest stationAcceptanceTest = new StationAcceptanceTest();
	private LineSectionAcceptanceTest lineSectionAcceptanceTest = new LineSectionAcceptanceTest();
	private TokenResponse 토큰;
	private LineResponse 신분당선;
	private LineResponse 이호선;
	private LineResponse 삼호선;
	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 교대역;
	private StationResponse 남부터미널역;

	@BeforeEach
	void favoriteSetUp() {
		memberAcceptanceTest.회원_생성을_요청("taminging@kakao.com", "taminging", 20);
		토큰 = authAcceptanceTest.로그인("taminging@kakao.com", "taminging").as(TokenResponse.class);
		강남역 = stationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		양재역 = stationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		교대역 = stationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
		남부터미널역 = stationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

		신분당선 = lineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10))
			.as(LineResponse.class);
		이호선 = lineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10))
			.as(LineResponse.class);
		삼호선 = lineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5))
			.as(LineResponse.class);
		lineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 3);
	}

	@DisplayName("즐겨찾기를 관리한다.")
	@Test
	void 즐겨찾기_관리() {
		//생성
		FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId().toString(), 남부터미널역.getId().toString());
		ExtractableResponse<Response> createResponse = post(favoriteRequest, "/favorite", 토큰.getAccessToken());
		assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		assertThat(createResponse.body().jsonPath().getString("source.name")).isEqualTo(강남역.getName());
		assertThat(createResponse.body().jsonPath().getString("target.name")).isEqualTo(남부터미널역.getName());

		//조회
		ExtractableResponse<Response> selectResponse = get("/favorite", 토큰.getAccessToken());
		assertThat(selectResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(selectResponse.body().jsonPath().getString("[0].source.name")).isEqualTo(강남역.getName());
		assertThat(selectResponse.body().jsonPath().getString("[0].target.name")).isEqualTo(남부터미널역.getName());

		//삭제
		Long id = createResponse.body().jsonPath().getLong("id");
		ExtractableResponse<Response> deleteResponse = delete("/favorite/" + id, 토큰.getAccessToken());
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}