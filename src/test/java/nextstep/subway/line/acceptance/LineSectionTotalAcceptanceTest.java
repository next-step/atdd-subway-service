package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

@DisplayName("지하철 구간 관련 통합 테스트")
public class LineSectionTotalAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    @TestFactory
    Collection<DynamicTest> 지하철_구간_통합_테스트() {
        return Arrays.asList(
                DynamicTest.dynamicTest("지하철 구간 등록 테스트", () -> {
                    // when 지하철 구간 등록 요청
                    LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);

                    //then 지하철 구간 등록됨
                    ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
                    LineSectionAcceptanceTest.지하철_노선에_지하철역_등록됨(response);
                }),
                DynamicTest.dynamicTest("등록된 구간 목록 조회 테스트", () -> {
                    //when 지하철 노선에 등록된 역 목록 조회
                    ExtractableResponse<Response> linesResponse = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);

                    //then 등록한 지하철 구간이 반영된 역 목록이 조회됨
                    LineSectionAcceptanceTest.지하철_노선에_지하철역_순서_정렬됨(linesResponse, Arrays.asList(강남역, 양재역, 광교역));
                }),
                DynamicTest.dynamicTest("지하철 구간 삭제 테스트", () -> {
                    //when 지하철 구간 삭제 요청
                    ExtractableResponse<Response> removeResponse = LineSectionAcceptanceTest.지하철_노선에_지하철역_제외_요청(신분당선, 양재역);

                    //then 지하철 구간 삭제됨
                    LineSectionAcceptanceTest.지하철_노선에_지하철역_제외됨(removeResponse);
                }),
                DynamicTest.dynamicTest("삭제된 구간 목록 조회 테스트", () -> {
                    //when 지하철 노선에 등록된 역 목록 조회 요청
                    ExtractableResponse<Response> linesResponseForRemoved = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);

                    //then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
                    LineSectionAcceptanceTest.지하철_노선에_지하철역_순서_정렬됨(linesResponseForRemoved, Arrays.asList(강남역, 광교역));
                })

        );
    }
}
