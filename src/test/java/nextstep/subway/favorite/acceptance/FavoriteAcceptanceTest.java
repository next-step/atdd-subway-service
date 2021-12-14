package nextstep.subway.favorite.acceptance;

import static nextstep.subway.common.ServiceApiFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
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
import nextstep.subway.utils.RestApiFixture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

	private StationResponse 강남역;
	private StationResponse 판교역;
	private StationResponse 광교역;

	private String 사용자;

	@BeforeEach
	public void setUp() {
		super.setUp();

		강남역 = 지하철역_등록됨("강남역");
		판교역 = 지하철역_등록됨("판교역");
		final LineResponse 신분당선 = 지하철노선_등록됨("신분당선", "red", 강남역.getId(), 판교역.getId(), 4);
		광교역 = 지하철역_등록됨("광교역");
		지하철구간_등록됨(신분당선, 판교역, 광교역, 5);

		final String email = "member@email.com";
		final String password = "<secret>";
		final int age = 20;
		회원_등록됨(email, password, age);
		사용자 = 로그인됨(email, password);
	}

	@DisplayName("즐겨찾기를 관리")
	@Test
	void manageFavorites() {
		final ExtractableResponse<Response> createResponse1 = 즐겨찾기_생성_요청(사용자, 강남역.getId(), 광교역.getId());
		즐겨찾기_생성됨(createResponse1);

		final ExtractableResponse<Response> createResponse2 = 즐겨찾기_생성_요청(사용자, 판교역.getId(), 광교역.getId());
		즐겨찾기_생성됨(createResponse2);

		final FavoriteResponse 즐겨찾기_강남광교 = createResponse1.as(FavoriteResponse.class);
		final FavoriteResponse 즐겨찾기_판교광교 = createResponse2.as(FavoriteResponse.class);

		final ExtractableResponse<Response> getResponse = 즐겨찾기_목록_조회_요청(사용자);
		즐겨찾기_목록_조회됨(getResponse, 즐겨찾기_강남광교, 즐겨찾기_판교광교);

		final ExtractableResponse<Response> deleteResponse = 즐겨찾기_생성_요청(사용자, 즐겨찾기_강남광교);
		즐겨찾기_삭제됨(deleteResponse);
	}

	public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long source, Long target) {
		final FavoriteRequest favoriteRequest = new FavoriteRequest(source, target);
		final RequestSpecification request = RestApiFixture.requestWithOAuth2(accessToken, favoriteRequest);
		return RestApiFixture.response(request.post("/favorites"));
	}

	public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
		final RequestSpecification request = RestApiFixture.requestWithOAuth2(accessToken);
		return RestApiFixture.response(request.get("/favorites"));
	}

	public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, FavoriteResponse favorite) {
		final RequestSpecification request = RestApiFixture.requestWithOAuth2(accessToken);
		return RestApiFixture.response(request.delete(String.format("/favorites/%d", favorite.getId())));
	}

	private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		final FavoriteResponse favoriteResponse = response.as(FavoriteResponse.class);
		assertThat(favoriteResponse.getId()).isNotNull();
	}

	private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, FavoriteResponse... favorites) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		final List<FavoriteResponse> favoriteResponse = response.body().jsonPath()
			.getList("", FavoriteResponse.class);
		final Set<Long> actualFavoriteIds = favoriteResponse.stream()
			.map(FavoriteResponse::getId)
			.collect(Collectors.toSet());
		final Long[] expectedFavoriteIds = Arrays.stream(favorites)
			.map(FavoriteResponse::getId)
			.toArray(Long[]::new);
		assertThat(actualFavoriteIds).containsExactlyInAnyOrder(expectedFavoriteIds);
	}

	private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	private StationResponse 지하철역_등록됨(String name) {
		return StationAcceptanceTest.지하철역_생성_요청(name).as(StationResponse.class);
	}

	private LineResponse 지하철노선_등록됨(
		String name, String color, Long upStationId, Long downStationId, int distance
	) {
		return LineAcceptanceTest.지하철_노선_생성_요청(
			lineRequest(name, color, upStationId, downStationId, distance)
		).as(LineResponse.class);
	}

	private void 지하철구간_등록됨(
		LineResponse line, StationResponse upStation, StationResponse downStation, int distance
	) {
		LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
	}

	private void 회원_등록됨(String email, String password, int age) {
		MemberAcceptanceTest.회원_생성을_요청(email, password, age);
	}

	private String 로그인됨(String email, String password) {
		return AuthAcceptanceTest.로그인_요청(email, password)
			.as(TokenResponse.class).getAccessToken();
	}
}
