package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthStaticAcceptance.*;
import static nextstep.subway.favorite.FavoriteStaticAcceptance.*;
import static nextstep.subway.line.acceptance.LineSectionStaticAcceptance.*;
import static nextstep.subway.line.acceptance.LineStaticAcceptance.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.member.MemberStaticAcceptance.*;
import static nextstep.subway.station.StationsStaticAcceptance.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

	private StationResponse 강남역;
	private StationResponse 광교역;
	private StationResponse 양재역;
	private StationResponse 판교역;
	private TokenResponse 로그인_토큰;

	@BeforeEach
	void setup() {
		super.setUp();

		강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
		광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
		양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
		판교역 = 지하철역_등록되어_있음("판교역").as(StationResponse.class);
		LineRequest 신분당선_생성_요청값 = 지하철_노선_생성_요청값("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
		LineResponse 신분당선 = 지하철_노선_등록되어_있음(신분당선_생성_요청값).as(LineResponse.class);
		지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);
		지하철_노선에_지하철역_등록_요청(신분당선, 판교역, 광교역, 5);

		회원_생성을_요청(EMAIL, PASSWORD, AGE);
		로그인_토큰 = 로그인_요청(로그인_요청값_생성(EMAIL, PASSWORD)).as(TokenResponse.class);
	}

	@DisplayName("즐겨찾기를 생성한다.")
	@Test
	void createFavorite() {
		// given
		FavoriteRequest 즐겨찾기_생성_요청값 = 즐겨찾기_요청값_생성(강남역.getId(), 광교역.getId());

		// when
		ExtractableResponse<Response> response = 즐겨찾기_생성_요청(로그인_토큰.getAccessToken(), 즐겨찾기_생성_요청값);

		// then
		즐겨찾기_생성됨(response);
	}

	@DisplayName("즐겨찾기 목록을 조회한다.")
	@Test
	void getFavoriteList() {
		// given
		FavoriteRequest 즐겨찾기_생성_요청값_1 = 즐겨찾기_요청값_생성(강남역.getId(), 광교역.getId());
		FavoriteRequest 즐겨찾기_생성_요청값_2 = 즐겨찾기_요청값_생성(양재역.getId(), 판교역.getId());
		즐겨찾기_생성_요청(로그인_토큰.getAccessToken(), 즐겨찾기_생성_요청값_1);
		즐겨찾기_생성_요청(로그인_토큰.getAccessToken(), 즐겨찾기_생성_요청값_2);

		// when
		ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(로그인_토큰.getAccessToken());

		// then
		즐겨찾기_목록_조회됨(response, 2);
	}

	@DisplayName("즐겨찾기를 삭제한다.")
	@Test
	void deleteFavorite() {
		// given
		FavoriteRequest 즐겨찾기_생성_요청값_1 = 즐겨찾기_요청값_생성(강남역.getId(), 광교역.getId());
		FavoriteRequest 즐겨찾기_생성_요청값_2 = 즐겨찾기_요청값_생성(양재역.getId(), 판교역.getId());
		FavoriteResponse 즐겨찾기 = 즐겨찾기_생성_요청(로그인_토큰.getAccessToken(), 즐겨찾기_생성_요청값_1).as(FavoriteResponse.class);
		즐겨찾기_생성_요청(로그인_토큰.getAccessToken(), 즐겨찾기_생성_요청값_2).as(FavoriteResponse.class);

		// when
		ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(로그인_토큰.getAccessToken(), 즐겨찾기.getId());

		// then
		즐겨찾기_삭제됨(response);
	}
}