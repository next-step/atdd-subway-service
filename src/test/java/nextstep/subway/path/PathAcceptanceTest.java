package nextstep.subway.path;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private Long 왕십리;
    private Long 신당;
    private Long 행당;
    private Long 청구;
    private Long DDP;

    private Long LINE_2;
    private Long LINE_5;

    /**
     * Background
     * Given 지하철 역과 노선 구역이 등록되어 있음
     * 2호선: 왕십리 - 신당 - DDP
     * 5호선: 왕십리 - 행당 - 청구 - DDP
     */
    @BeforeEach
    void setup() {
        super.setUp();

        // given
        왕십리 = 지하철역_ID_추출(지하철역_등록되어_있음("왕십리"));
        신당 = 지하철역_ID_추출(지하철역_등록되어_있음("신당"));
        행당 = 지하철역_ID_추출(지하철역_등록되어_있음("행당"));
        청구 = 지하철역_ID_추출(지하철역_등록되어_있음("청구"));
        DDP = 지하철역_ID_추출(지하철역_등록되어_있음("DDP"));

        LINE_2 = 지하철_노선과_구역_등록되어_있음_2호선();
        LINE_5 = 지하철_노선과_구역_등록되어_있음_5호선();
    }

    /**
     * Given 지하철 역과 노선과 구간이 등록되어 있음
     * When 출발역과 도착역을 똑같이 최단 경로 조회
     * Then 최단 경로 조회 실패
     */
    @DisplayName("출발역과 도착역이 같은 경우 최단 경로 조회 실패")
    @Test
    void 최단_경로_조회_출발역_도착역_같음_실패() {
        // when
        ExtractableResponse<Response> 최단_경로_조회_응답 = 최단_경로_조회_요청(왕십리, 왕십리);

        // then
        최단_경로_조회_실패(최단_경로_조회_응답);
    }

    /**
     * Given 지하철 역과 노선과 구간이 등록되어 있음
     * And 기존 노선과 연결되어 있지 않는 노선 등록되어 있음
     * When 기존역에 연결되어 있지 않는 노선의 역을 출발역으로 최단 경로 조회
     * Then 최단 경로 조회 실패
     * When 기존역에 연결되어 있지 않는 노선의 역을 도착역으로 최단 경로 조회
     * Then 최단 경로 조회 실패
     * When 기존역에 연결되어 있지 않는 노선의 역들만 출발역 도착역으로 지정 최단 경로 조회
     * Then 최단 경로 조회 성공
     */
    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 최단 경로 조회 실패")
    @Test
    void 최단_경로_조회_출발역_도착역_미연결_실패() {
        // given
        Long 동대구 = 지하철역_등록되어_있음("동대구").jsonPath().getLong("id");
        Long 서대구 = 지하철역_등록되어_있음("서대구").jsonPath().getLong("id");
        int distance = 10;
        지하철_노선_등록되어_있음_대구(서대구, 동대구, distance);

        // when
        ExtractableResponse<Response> 미연결_출발역_최단_경로_조회_응답 = 최단_경로_조회_요청(동대구, 왕십리);

        // then
        최단_경로_조회_실패(미연결_출발역_최단_경로_조회_응답);

        // when
        ExtractableResponse<Response> 미연결_도착역_최단_경로_조회_응답 = 최단_경로_조회_요청(왕십리, 동대구);

        // then
        최단_경로_조회_실패(미연결_도착역_최단_경로_조회_응답);

        // when
        ExtractableResponse<Response> 미연결_역간_최단_경로_조회_응답 = 최단_경로_조회_요청(서대구, 동대구);

        // then
        최단_경로_조회_성공(미연결_역간_최단_경로_조회_응답, distance);
    }

    /**
     * Given 지하철 역과 노선과 구간이 등록되어 있음
     * And 존재하지 않는 역 ID -1
     * When 존재하지 않는 역을 출발역으로 최단 경로 조회
     * Then 최단 경로 조회 실패
     * When 존재하지 않는 역을 도착역으로 최단 경로 조회
     * Then 최단 경로 조회 실패
     */
    @DisplayName("존재하지 않는 역을 출발역이나 도착역으로 조회할 경우 최단 경로 조회 실패")
    @Test
    void 최단_경로_조회_출발역_도착역_미존재_실패() {
        // given
        Long 미존재 = -1L;

        // when
        ExtractableResponse<Response> 미존재_출발역_최단_경로_조회_응답 = 최단_경로_조회_요청(미존재, 왕십리);

        // then
        최단_경로_조회_실패(미존재_출발역_최단_경로_조회_응답);

        // when
        ExtractableResponse<Response> 미존재_도착역_최단_경로_조회_응답 = 최단_경로_조회_요청(왕십리, 미존재);

        // then
        최단_경로_조회_실패(미존재_도착역_최단_경로_조회_응답);
    }

    /**
     * Given 지하철 역과 노선과 구간이 등록되어 있음
     * When 노선 겹치는 역(환승역)간 최단 경로 조회
     * Then 최단 경로 조회 성공
     * When 환승역 아닌 역간 최단 경로 조회
     * Then 최단 경로 조회 성공
     * Given 지름길 호선 개통
     * When 지름길 호선 연결 역간 최단 경로 조회
     * Then 최단 경로 조회 성공
     */
    @DisplayName("최단 경로가 존재하는 경우 최단 경로 조회 성공")
    @Test
    void 최단_경로_존재_성공() {
        // when
        ExtractableResponse<Response> 환승역간_최단_경로_조회 = 최단_경로_조회_요청(왕십리, DDP);

        // then
        최단_경로_조회_성공(환승역간_최단_경로_조회, 20);

        // when
        ExtractableResponse<Response> 비환승역간_최단_경로_조회 = 최단_경로_조회_요청(청구, 신당);

        // then
        최단_경로_조회_성공(비환승역간_최단_경로_조회, 20);

        // Given
        지름길_노선_개통(왕십리, DDP, 1);

        // when
        ExtractableResponse<Response> 지름길_노선_최단_경로_조회 = 최단_경로_조회_요청(왕십리, DDP);

        // then
        최단_경로_조회_성공(지름길_노선_최단_경로_조회, 1);

    }

    public static void 최단_경로_조회_성공(ExtractableResponse<Response> response, int expectedDistance) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(expectedDistance)
        );
    }

    public static void 최단_경로_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 최단_경로_조회_요청(Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source.toString());
        params.put("target", target.toString());

        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParams(params)
            .when().get("/paths")
            .then().log().all()
            .extract();
    }

    private LineResponse 지하철_노선_등록되어_있음_대구(Long start, Long end, int distance) {
        Map<String, String> params = 지하철_노선_생성_요청_파라미터(
            "대구",
            "bg-blue-600",
            start,
            end,
            distance
        );
        return 지하철_노선_등록되어_있음(params).as(LineResponse.class);
    }

    private Long 지하철_노선과_구역_등록되어_있음_2호선() {
        Map<String, String> params = 지하철_노선_생성_요청_파라미터(
            "2호선",
            "bg-green-600",
            왕십리,
            DDP,
            20
        );
        Long LINE_2 = 지하철_노선_ID_추출(지하철_노선_등록되어_있음(params));
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(LINE_2, 왕십리, 신당, 10);
        return LINE_2;
    }

    private Long 지하철_노선과_구역_등록되어_있음_5호선() {
        Map<String, String> params = 지하철_노선_생성_요청_파라미터(
            "5호선",
            "bg-pupple-600",
            왕십리,
            DDP,
            30
        );
        Long LINE_5 = 지하철_노선_ID_추출(지하철_노선_등록되어_있음(params));
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(LINE_5, 왕십리, 행당, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(LINE_5, 행당, 청구, 10);
        return LINE_5;
    }

    private LineResponse 지름길_노선_개통(Long start, Long end, int distance) {
        Map<String, String> params = 지하철_노선_생성_요청_파라미터(
            "지름길노선",
            "bg-red-600",
            start,
            end,
            distance
        );
        return 지하철_노선_등록되어_있음(params).as(LineResponse.class);
    }
}
