package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.member.TestMember.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
	private StationResponse 교대역;
	private StationResponse 강남역;
	private StationResponse 역삼역;
	private StationResponse 선릉역;
	private StationResponse 남부터미널역;
	private StationResponse 양재역;
	private StationResponse 양재시민의숲역;
	private StationResponse 중앙역;
	private StationResponse 한대앞역;
	private LineResponse 신분당선;
	private LineResponse 이호선;
	private LineResponse 삼호선;
	private LineResponse 사호선;

	@BeforeEach
	public void setUp() {
		super.setUp();

		// Background
		교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
		강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
		역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);
		선릉역 = 지하철역_등록되어_있음("선릉역").as(StationResponse.class);
		남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
		양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
		양재시민의숲역 = 지하철역_등록되어_있음("양재시민의숲역").as(StationResponse.class);
		중앙역 = 지하철역_등록되어_있음("중앙역").as(StationResponse.class);
		한대앞역 = 지하철역_등록되어_있음("한대앞역").as(StationResponse.class);

		신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 2, 900);
		이호선 = 지하철_노선_등록되어_있음("2호선", "bg-green-600", 교대역, 강남역, 3);
		삼호선 = 지하철_노선_등록되어_있음("3호선", "bg-orange-600", 교대역, 남부터미널역, 5);
		사호선 = 지하철_노선_등록되어_있음("4호선", "bg-blue-600", 중앙역, 한대앞역, 2);

		지하철_노선에_지하철역_등록되어_있음(신분당선, 양재역, 양재시민의숲역, 4);
		지하철_노선에_지하철역_등록되어_있음(이호선, 강남역, 역삼역, 6);
		지하철_노선에_지하철역_등록되어_있음(이호선, 역삼역, 선릉역, 5);
		지하철_노선에_지하철역_등록되어_있음(삼호선, 남부터미널역, 양재역, 2);
	}

	@DisplayName("즐겨찾기를 관리한다.")
	@Test
	void manageFavorite() {
		// Background
		회원_등록되어_있음(윤준석);
		TokenResponse token = 로그인_되어있음(윤준석).as(TokenResponse.class);

		// Scenario: 즐겨찾기를 관리
		FavoriteRequest 즐겨찾기_생성_요청값_1 = new FavoriteRequest(교대역.getId(), 양재시민의숲역.getId());
		ExtractableResponse<Response> 즐겨찾기_생성_요청_1 = 즐겨찾기_생성_요청(token, 즐겨찾기_생성_요청값_1);
		즐겨찾기_생성됨(즐겨찾기_생성_요청_1);

		FavoriteRequest 즐겨찾기_생성_요청값_2 = new FavoriteRequest(강남역.getId(), 양재역.getId());
		ExtractableResponse<Response> 즐겨찾기_생성_요청_2 = 즐겨찾기_생성_요청(token, 즐겨찾기_생성_요청값_2);
		즐겨찾기_생성됨(즐겨찾기_생성_요청_2);

		FavoriteRequest 즐겨찾기_생성_요청값_3 = new FavoriteRequest(남부터미널역.getId(), 선릉역.getId());
		ExtractableResponse<Response> 즐겨찾기_생성_요청_3 = 즐겨찾기_생성_요청(token, 즐겨찾기_생성_요청값_3);
		즐겨찾기_생성됨(즐겨찾기_생성_요청_3);

		즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_요청(token), 3);

		즐겨찾기_삭제됨(즐겨찾기_삭제_요청(token, 즐겨찾기_생성_요청_2.as(FavoriteResponse.class)));

		즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_요청(token), 2);
	}

	private ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse token, FavoriteRequest body) {
		return post("/favorites", token.getAccessToken(), body);
	}

	private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isNotBlank();
	}

	private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse token) {
		return get("/favorites", token.getAccessToken());
	}

	private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, int expectedSize) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		List<FavoriteResponse> actualFavorites = response.jsonPath().getList(".", FavoriteResponse.class);

		assertThat(actualFavorites).hasSize(expectedSize);
	}

	private ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse token, FavoriteResponse favoriteResponse) {
		Map<String, Object> pathParams = new HashMap<>();
		pathParams.put("favoriteId", favoriteResponse.getId());
		return delete("/favorites/{favoriteId}", token.getAccessToken(), pathParams);
	}

	private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
