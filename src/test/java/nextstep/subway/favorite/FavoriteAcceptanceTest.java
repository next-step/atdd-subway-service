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
		memberAcceptanceTest.회원_생성을_요청("taminging@kakao.com", "taminging", 20);
		토큰 = authAcceptanceTest.로그인("taminging@kakao.com", "taminging").as(TokenResponse.class);
	}

	@DisplayName("즐겨찾기를 관리한다.")
	@Test
	void 즐겨찾기_관리() {
		//생성
		ExtractableResponse<Response> response = 즐겨찾기_생성_요청(강남역.getId(), 남부터미널역.getId());
		Long id = 즐겨찾기_생성됨(response, 강남역, 남부터미널역);

		//조회
		ExtractableResponse<Response> selectResponse = 즐겨찾기_조회_요청();
		즐겨찾기_조회_확인(selectResponse);

		//삭제
		ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(id);
		즐겨찾기_삭제_확인(deleteResponse);
	}

	@DisplayName("즐겨찾기 생성")
	@Test
	void 즐겨찾기_생성하기() {
		//when
		ExtractableResponse<Response> response = 즐겨찾기_생성_요청(강남역.getId(), 남부터미널역.getId());
		//then
		즐겨찾기_생성됨(response, 강남역, 남부터미널역);
	}

	@DisplayName("즐겨찾기 모두 조회")
	@Test
	void 즐겨찾기_모두_조회() {
		//given
		ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(강남역.getId(), 남부터미널역.getId());
		즐겨찾기_생성됨(createResponse, 강남역, 남부터미널역);
		//when
		ExtractableResponse<Response> selectResponse = 즐겨찾기_조회_요청();
		//then
		즐겨찾기_조회_확인(selectResponse);
	}

	@DisplayName("즐겨찾기 삭제")
	@Test
	void 즐겨찾기_삭제() {
		//given
		ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(강남역.getId(), 남부터미널역.getId());
		Long id = 즐겨찾기_생성됨(createResponse, 강남역, 남부터미널역);
		//when
		ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(id);
		//then
		즐겨찾기_삭제_확인(deleteResponse);
	}

	ExtractableResponse<Response> 즐겨찾기_생성_요청(Long sourceId, Long targetId) {
		FavoriteRequest favoriteRequest = new FavoriteRequest(sourceId, targetId);
		return post(favoriteRequest, "/favorite", 토큰.getAccessToken());
	}

	Long 즐겨찾기_생성됨(ExtractableResponse<Response> response, StationResponse source, StationResponse target) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.body().jsonPath().getString("source.name")).isEqualTo(source.getName());
		assertThat(response.body().jsonPath().getString("target.name")).isEqualTo(target.getName());
		return response.body().jsonPath().getLong("id");
	}

	ExtractableResponse<Response> 즐겨찾기_조회_요청() {
		return get("/favorite", 토큰.getAccessToken());
	}

	void 즐겨찾기_조회_확인(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}


	ExtractableResponse<Response> 즐겨찾기_삭제_요청(Long id) {
		return delete("/favorite/" + id, 토큰.getAccessToken());
	}

	void 즐겨찾기_삭제_확인(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}



}