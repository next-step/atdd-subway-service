package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceSupport;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceSupport;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

	private StationResponse 잠실역;
	private StationResponse 선릉역;
	private StationResponse 건대역;
	private LineResponse 이호선;
	private String accessToken;
	private String otherAccessToken;

	@Override
	@BeforeEach
	public void setUp() {
		super.setUp();
		잠실역 = StationAcceptanceTest.지하철역_등록되어_있음("잠실역").as(StationResponse.class);
		선릉역 = StationAcceptanceTest.지하철역_등록되어_있음("선릉역").as(StationResponse.class);
		건대역 = StationAcceptanceTest.지하철역_등록되어_있음("건대역").as(StationResponse.class);

		이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "green", 잠실역.getId(), 선릉역.getId(), 10))
				.as(LineResponse.class);
		LineSectionAcceptanceSupport.지하철_노선에_지하철역_등록_요청(이호선, 건대역, 잠실역, 5);

		MemberAcceptanceSupport.회원_생성을_요청("any", "any", 50);
		accessToken = MemberAcceptanceSupport.회원_로그인_요청("any", "any");

		MemberAcceptanceSupport.회원_생성을_요청("any2", "any2", 50);
		otherAccessToken = MemberAcceptanceSupport.회원_로그인_요청("any2", "any2");
	}

	@DisplayName("즐겨찾기를 관리한다.")
	@Test
	void manage() {
		// 즐겨찾기 생성
		ExtractableResponse<Response> 즐겨찾기_잠실역_생성_결과 = FavoriteAcceptanceSupport.즐겨찾기_생성_요청(accessToken, 잠실역, 선릉역);
		FavoriteAcceptanceSupport.즐겨찾기_생성됨(즐겨찾기_잠실역_생성_결과);
		ExtractableResponse<Response> 즐겨찾기_건대역_생성_결과 = FavoriteAcceptanceSupport.즐겨찾기_생성_요청(accessToken, 건대역, 선릉역);
		FavoriteAcceptanceSupport.즐겨찾기_생성됨(즐겨찾기_건대역_생성_결과);

		// 즐겨찾기 조회
		ExtractableResponse<Response> 즐겨찾기_목록_조회_결과 = FavoriteAcceptanceSupport.즐겨찾기_목록_조회_요청(accessToken);
		FavoriteAcceptanceSupport.즐겨찾기_목록_검사(즐겨찾기_목록_조회_결과, 2, Arrays.asList("잠실역", "건대역"));

		// 즐겨찾기 삭제
		ExtractableResponse<Response> 즐겨찾기_삭제_결과 = FavoriteAcceptanceSupport.즐겨찾기_삭제_요청(accessToken, 즐겨찾기_잠실역_생성_결과);
		FavoriteAcceptanceSupport.즐겨찾기_삭제됨(즐겨찾기_삭제_결과);
		FavoriteAcceptanceSupport.즐겨찾기_목록_검사(FavoriteAcceptanceSupport.즐겨찾기_목록_조회_요청(accessToken), 1, Arrays.asList("건대역"));
	}

	@DisplayName("다른사람의 즐겨찾기는 공개가 되지 않아 삭제할 수 없다.")
	@Test
	void deleteByOther() {
		// 즐겨찾기 생성
		ExtractableResponse<Response> 즐겨찾기_잠실역_생성_결과 = FavoriteAcceptanceSupport.즐겨찾기_생성_요청(otherAccessToken, 잠실역, 선릉역);
		FavoriteAcceptanceSupport.즐겨찾기_생성됨(즐겨찾기_잠실역_생성_결과);

		// 즐겨찾기 삭제
		ExtractableResponse<Response> 즐겨찾기_삭제_결과 = FavoriteAcceptanceSupport.즐겨찾기_삭제_요청(accessToken, 즐겨찾기_잠실역_생성_결과);
		FavoriteAcceptanceSupport.즐겨찾기_삭제_실패함(즐겨찾기_삭제_결과);
	}
}
