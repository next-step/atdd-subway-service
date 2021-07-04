package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
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
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

	@DisplayName("즐겨찾기를 관리")
	@Test
	void 즐겨찾기를_관리() {
		// given
		//지하철역 등록되어 있음
		StationResponse 서울역 = StationAcceptanceTest.지하철역_등록되어_있음("서울역").as(StationResponse.class);
		StationResponse 광명역 = StationAcceptanceTest.지하철역_등록되어_있음("광명역").as(StationResponse.class);
		StationResponse 대전역 = StationAcceptanceTest.지하철역_등록되어_있음("대전역").as(StationResponse.class);

		//지하철 노선 등록되어 있음
		LineResponse 경부고속선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
			new LineRequest("경부고속선", "blue", 서울역.getId(), 대전역.getId(), 15)).as(
			LineResponse.class);

		//지하철 노선에 지하철역 등록되어 있음
		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(경부고속선, 서울역, 광명역, 6);

		//회원 등록되어 있음
		final String email = "gtgt";
		final String password = "password";
		final Integer age = 29;
		MemberAcceptanceTest.회원_생성되어_있음(email, password, age);

		//로그인 되어있음
		TokenResponse tokenResponse = AuthAcceptanceTest.토큰_발행(email, password);

		// when
		ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(tokenResponse.getAccessToken(), 서울역.getId(), 대전역.getId());
		// then
		즐겨찾기_생성됨(createResponse);

		/*즐겨찾기 목록 조회 요청();
		즐겨찾기 목록 조회됨();
		즐겨찾기 삭제 요청();
		즐겨찾기 삭제됨();*/
	}



	private ExtractableResponse<Response> 즐겨찾기_생성을_요청(final String accessToken, final Long source, final Long target) {
		// when
		FavoriteRequest favoriteRequest = new FavoriteRequest(source, target);
		return RestAssured
			.given().log().all()
			.auth().oauth2(accessToken)
			.body(favoriteRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/favorites")
			.then().log().all().extract();
	}

	private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
		Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}
}
