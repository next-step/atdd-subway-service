package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.*;

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
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

	@DisplayName("즐겨찾기를 관리한다.")
	@Test
	void manageFavorite() {
		// given
		StationResponse 강남역 = StationAcceptanceTest.지하철역_생성_요청("강남역").as(StationResponse.class);
		StationResponse 양재역 = StationAcceptanceTest.지하철역_생성_요청("양재역").as(StationResponse.class);
		StationResponse 판교역 = StationAcceptanceTest.지하철역_생성_요청("판교역").as(StationResponse.class);
		StationResponse 광교역 = StationAcceptanceTest.지하철역_생성_요청("광교역").as(StationResponse.class);

		LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 30);
		LineResponse 신분당선 = LineAcceptanceTest.지하철_노선_생성_요청(lineRequest).as(LineResponse.class);

		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 5);
		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 판교역, 10);

		MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD,
			MemberAcceptanceTest.AGE);

		String accessToken = AuthAcceptanceTest.로그인을_시도한다(MemberAcceptanceTest.EMAIL,
			MemberAcceptanceTest.PASSWORD).as(TokenResponse.class).getAccessToken();

		// when
		ExtractableResponse<Response> createdResponse = 즐겨찾기_생성_요청(accessToken, 강남역.getId(), 판교역.getId());

		// then
		즐겨찾기_생성됨(createdResponse);

	}

	private void 즐겨찾기_생성됨(final ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	public static ExtractableResponse<Response> 즐겨찾기_생성_요청(final String accessToken,
		final Long sourceId, final Long targetId) {
		FavoriteRequest request = new FavoriteRequest(sourceId, targetId);
		return RestAssured
			.given().log().all()
			.auth().oauth2(accessToken)
			.body(request)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/favorites")
			.then().log().all()
			.extract();
	}

}
