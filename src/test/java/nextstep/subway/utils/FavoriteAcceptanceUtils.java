package nextstep.subway.utils;

import static nextstep.subway.utils.RestAssuredUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.Favorite.dto.FavoriteRequest;
import nextstep.subway.Favorite.dto.FavoritesResponse;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteAcceptanceUtils {

	private static final String FAVORITES_API_URL = "/favorites";

	public static void 지하철역_등록되어_있음() {
		StationAcceptanceUtils.지하철역_등록되어_있음("강남역");
	}

	public static ExtractableResponse<Response> 즐겨찾기_생성_요청(final String accessToken, final Long target,
		final Long source) {
		return post(FAVORITES_API_URL, FavoriteRequest.of(target, source), accessToken).extract();
	}

	public static void 즐겨찾기_생성됨(final ExtractableResponse<Response> response) {
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(response.header("Location")).isNotBlank()
		);
	}

	public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(final String accessToken) {
		return get(FAVORITES_API_URL, accessToken).extract();
	}

	public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, final StationResponse... stations
	) {
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> 즐겨찾기_목록_포함됨(response, stations)
		);
	}

	private static void 즐겨찾기_목록_포함됨(ExtractableResponse<Response> response, StationResponse... stations) {
		List<Long> expectedIds = Arrays.stream(stations)
			.map(StationResponse::getId)
			.collect(Collectors.toList());

		List<Long> resultIds = response.jsonPath().getList(".", FavoritesResponse.class).stream()
			.map(it -> Arrays.asList(it.getSource().getId(), it.getTarget().getId()))
			.flatMap(Collection::stream)
			.collect(Collectors.toList());

		assertThat(resultIds).containsExactlyElementsOf(expectedIds);
	}

	public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(
		final String accessToken,
		ExtractableResponse<Response> response
	) {
		String location = response.header("Location");
		return delete(location, accessToken).extract();
	}

	public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> deleteResponse) {
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
