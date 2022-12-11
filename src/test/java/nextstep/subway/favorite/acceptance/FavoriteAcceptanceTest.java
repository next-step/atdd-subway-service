package nextstep.subway.favorite.acceptance;

import static nextstep.subway.member.acceptance.MemberAcceptanceTest.*;
import static nextstep.subway.utils.AuthAcceptanceUtils.*;
import static nextstep.subway.utils.FavoriteAcceptanceUtils.*;
import static nextstep.subway.utils.LineAcceptanceUtils.*;
import static nextstep.subway.utils.MemberAcceptanceUtils.*;
import static nextstep.subway.utils.StationAcceptanceUtils.지하철역_등록되어_있음;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

	private StationResponse 강남역;
	private StationResponse 역삼역;
	private String 사용자;

	/**
	 * Given 지하철역 등록되어 있음
	 * And 지하철 노선 등록되어 있음
	 * And 지하철 노선에 지하철역 등록되어 있음
	 * And 회원 등록되어 있음
	 * And 로그인 되어있음
	 */
	@BeforeEach
	public void setUp() {
		super.setUp();
		강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
		역삼역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
		지하철_노선_등록되어_있음(new LineCreateRequest("이호선", "green", 강남역.getId(), 역삼역.getId(), 10));

		회원_등록되어_있음(EMAIL, PASSWORD, AGE);
		사용자 = 로그인_완료되어_토큰_발급(EMAIL, PASSWORD);
	}

	@DisplayName("즐겨찾기 관리")
	@Test
	void manageFavorite() {
		// when : 즐겨찾기 생성 요청
		ExtractableResponse<Response> 즐겨찾기_생성_요청 = 즐겨찾기_생성_요청(사용자, 강남역.getId(), 역삼역.getId());
		// then : 즐겨찾기 생성됨
		즐겨찾기_생성됨(즐겨찾기_생성_요청);

		// when : 즐겨찾기 목록 조회 요청
		ExtractableResponse<Response> 즐겨찾기_목록_조회_요청 = 즐겨찾기_목록_조회_요청(사용자);
		// then : 즐겨찾기 목록 조회됨
		즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_요청);

		// when : 즐겨찾기 삭제 요청
		ExtractableResponse<Response> 즐겨찾기_삭제_요청 = 즐겨찾기_삭제_요청(사용자, 즐겨찾기_생성_요청);
		// then : 즐겨찾기 삭제됨
		즐겨찾기_삭제됨(즐겨찾기_삭제_요청);
	}

}