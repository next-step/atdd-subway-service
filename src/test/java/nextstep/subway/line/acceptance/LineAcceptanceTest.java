package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.stream.Stream;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("지하철 노선 관련 인수테스트")
public class LineAcceptanceTest extends AcceptanceTest {
    private static final int ID = 2;

    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineRequest 이호선;
    private LineRequest 신분당선;
    private LineRequest 구신분당선;
    private LineRequest 뉴신분당선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        //  given
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        이호선 = new LineRequest("이호선", "bg-green-600", 강남역.getId(), 교대역.getId(), 10);
        신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        구신분당선 = new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15);
        뉴신분당선 = new LineRequest("뉴신분당선", "bg-red-500", 강남역.getId(), 광교역.getId(), 9);
    }

    /**
     * Feature: 지하철 노선 관련 기능
     *
     *   Background 
     *     Given 지하철역 등록되어 있음
     *
     *   Scenario: 지하철 노선을 관리
     *     When n개의 노선들을 노선등록 요청하면
     *     Then 노선들이 등록된다.
     *     When 노선목록조회 요청하면
     *     Then 노선목록을 응답받는다.
     *     And  노선목록에서 n개의 노선을 확인 할 수 있다.
     *
     *     When 노선삭제 요청하면
     *     Then 등록된 노선이 삭제된다.
     *     When 노선목록조회 요청하면
     *     Then 노선목록을 응답받는다.
     *     And  노선목록에서 노선이 삭제된 것을 확인할 수 있다.
     *
     *     When 노선수정 요청하면
     *     Then 노선의 정보가 수정된다.
     *     When 노선조회 요청하면
     *     Then 노선을 응답받는다.
     *     And  노선에서 수정된 정보를 확인 할 수 있다.
     * */
    @DisplayName("지하철 노선을 관리한다.")
    @TestFactory
    Stream<DynamicTest> manageLine() {
        return Stream.of(
                dynamicTest("노선등록 요청으로 노선들을 등록하면 노선목록조회 요청시 등록한 노선들을 응답 받는다.", () -> {
                    // when
                    ExtractableResponse<Response> createResponse1 = 지하철_노선_생성_요청(이호선);
                    ExtractableResponse<Response> createResponse2 = 지하철_노선_생성_요청(신분당선);

                    // then
                    지하철_노선_생성됨(createResponse1);
                    지하철_노선_생성됨(createResponse2);

                    // when
                    ExtractableResponse<Response> getAllResponse = 지하철_노선_목록_조회_요청();

                    // then
                    지하철_노선_목록_응답됨(getAllResponse);
                    지하철_노선_목록_포함됨(getAllResponse, Arrays.asList(createResponse1, createResponse2));
                }),

                dynamicTest("노선삭제 요청으로 노선을 삭제하고 노선목록조회 요청시 삭제한 노선은 목록에 없다.", () -> {

                    //given
                    ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(구신분당선);

                    // when
                    ExtractableResponse<Response> deleteResponse = 지하철_노선_제거_요청(createResponse);

                    // then
                    지하철_노선_삭제됨(deleteResponse);

                    // when
                    ExtractableResponse<Response> getAllResponse = 지하철_노선_목록_조회_요청();

                    // then
                    지하철_노선_목록_포함되지_않음(getAllResponse, createResponse);

                }),

                dynamicTest("노선수정 요청으로 노선을 수정하면 조회시 수정여부를 확인 할 수 있다.", () -> {

                    // given
                    ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(구신분당선);

                    // when
                    ExtractableResponse<Response> updateResponse = 지하철_노선_수정_요청(createResponse, 뉴신분당선);

                    // then
                    지하철_노선_수정됨(updateResponse);

                    // when
                    ExtractableResponse<Response> getResponse = 지하철_노선_조회_요청(createResponse);

                    // then
                    지하철_노선_수정_정보_확인(getResponse, 뉴신분당선);
                })
        );

    }


    /**
     * Feature: 지하철 노선 관련 기능
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *
     *   Scenario: 지하철 노선을 관리 (실패)
     *     When 노선등록 요청하면
     *     Then 노선이 등록된다.
     *     When 등록된 노선으로 등록요청 하면
     *     Then 노선 등록에 실패한다.
     * */
    @DisplayName("지하철 노선을 관리한다.(실패)")
    @TestFactory
    void manageLine_fail(){

        // when
        ExtractableResponse<Response> createResponse1 = 지하철_노선_생성_요청(신분당선);

        // then
        지하철_노선_생성됨(createResponse1);

        // when
        ExtractableResponse<Response> createResponse2 = 지하철_노선_생성_요청(신분당선);

        // then
        지하철_노선_생성_실패됨(createResponse2);
    }


    public static ExtractableResponse<Response> 지하철_노선_등록되어_있음(LineRequest params) {
        return 지하철_노선_생성_요청(params);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all().
                        extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return 지하철_노선_목록_조회_요청("/lines");
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return 지하철_노선_목록_조회_요청(uri);
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회_요청(String uri) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(LineResponse response) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> response, LineRequest params) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(LineResponse.class)).isNotNull();
    }

    public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(createdResponse -> Long.parseLong(createdResponse.header("Location").split("/")[ID]))
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }   
    
    public static void 지하철_노선_목록_포함되지_않음(ExtractableResponse<Response> response, ExtractableResponse<Response> createResponse) {
        Long expectedLineId = Long.parseLong(createResponse.header("Location").split("/")[ID]);

        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).doesNotContain(expectedLineId);
    }

    private void 지하철_노선_수정_정보_확인(ExtractableResponse<Response> response, LineRequest request) {
        LineResponse lineResponse = response.body().as(LineResponse.class);

        assertThat(lineResponse.getName()).isEqualTo(request.getName());
        assertThat(lineResponse.getColor()).isEqualTo(request.getColor());
    }


    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
