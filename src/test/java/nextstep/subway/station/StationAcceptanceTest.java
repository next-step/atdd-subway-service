package nextstep.subway.station;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.RestAssuredUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역);

        // then
        지하철역_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        //given
        지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역);

        // then
        지하철역_생성_실패됨(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        ExtractableResponse<Response> createResponse1 = 지하철역_등록되어_있음(강남역);
        ExtractableResponse<Response> createResponse2 = 지하철역_등록되어_있음(역삼역);

        // when
        ExtractableResponse<Response> response = 지하철역_목록_조회_요청();

        // then
        지하철역_목록_응답됨(response);
        지하철역_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = 지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(createResponse);

        // then
        지하철역_삭제됨(response);
    }

    public static ExtractableResponse<Response> 지하철역_등록되어_있음(String name) {
        return 지하철역_생성_요청(name);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        StationRequest stationRequest = new StationRequest(name);
        return RestAssuredUtil.jsonPost(stationRequest, "/stations");
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return RestAssuredUtil.jsonGet("/stations");
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssuredUtil.delete(uri);
    }

    public static void 지하철역_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철역_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철역_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철역_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철역_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }
}
