package nextstep.subway.line.acceptance;

import static nextstep.subway.utils.LineAcceptanceHelper.지하철_노선_등록되어_있음;
import static nextstep.subway.utils.LineAcceptanceHelper.지하철_노선_조회_요청;
import static nextstep.subway.utils.LineSectionApiHelper.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.utils.LineSectionApiHelper.지하철_노선에_지하철역_제외_요청;
import static nextstep.subway.utils.LineSectionAssertionHelper.지하철_노선에_지하철역_등록_실패됨;
import static nextstep.subway.utils.LineSectionAssertionHelper.지하철_노선에_지하철역_등록됨;
import static nextstep.subway.utils.LineSectionAssertionHelper.지하철_노선에_지하철역_순서_정렬됨;
import static nextstep.subway.utils.LineSectionAssertionHelper.지하철_노선에_지하철역_제외_실패됨;
import static nextstep.subway.utils.LineSectionAssertionHelper.지하철_노선에_지하철역_제외됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 구간 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {

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

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(),
            10);
        신분당선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    /**
     * Feature: 지하철 구간 등록 기능 Background Given 강남역, 양재역, 정자역, 광교역 지하철역 등록 Given 강남역과 광교역을 시작, 종료 지점으로
     * 한 신분당선 노선을 등록 when 신분당선 에 강남역 ~ 양재역 구간 등록시 then 정상적으로 등록이 된다.(노선 조회 상태코드가 200이 반환되고, 노선 조회시
     * 지하철역이 순서대로 반환된다)
     */
    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void addLineSection() {
        // when
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_등록됨(response);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 양재역, 광교역));
    }

    /**
     * Feature: 지하철 구간 반복등록 기능 Background Given 강남역, 양재역, 정자역, 광교역 지하철역 등록 Given 강남역과 광교역을 시작, 종료
     * 지점으로 한 신분당선 노선을 등록 when 신분당선에 강남역 ~ 양재역 구간 등록후 when 신분당선에 정자역 ~ 강남역 구간 등록시 then 정상적으로 등록이
     * 된다.(노선 조회 상태코드가 200이 반환되고, 노선 조회시 지하철역이 순서대로 반환된다)
     */
    @DisplayName("지하철 노선에 여러개의 역을 순서 상관 없이 등록한다.")
    @Test
    void addLineSection2() {
        // when
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);
        지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 강남역, 5);

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_등록됨(response);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(정자역, 강남역, 양재역, 광교역));
    }

    /**
     * Feature: 지하철 구간 중복 등록 실패 기능 Background Given 강남역, 양재역, 정자역, 광교역 지하철역 등록 Given 강남역과 광교역을 시작,
     * 종료 지점으로 한 신분당선 노선을 등록 when 신분당선에 강남역 ~ 광교역 구간 등록시 then 중복 구간 등록으로 인해 등록이 실패한다.(상태코드 500 반환)
     */
    @DisplayName("지하철 노선에 이미 등록되어있는 역을 등록한다.")
    @Test
    void addLineSectionWithSameStation() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 광교역, 3);

        // then
        지하철_노선에_지하철역_등록_실패됨(response);
    }

    /**
     * Feature: 지하철 저장되지 않는 역에대한 구간 등록 실패 기능 Background Given 강남역, 양재역, 정자역, 광교역 지하철역 등록 Given 강남역과
     * 광교역을 시작, 종료 지점으로 한 신분당선 노선을 등록 when 신분당선에 정자역 ~ 양재역 등록시 then 저장되지 않은 지하철 정거장에 대한 구간 등록이
     * 실패한다.(상태코드 500 반환)
     */
    @DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
    @Test
    void addLineSectionWithNoStation() {
        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 양재역, 3);

        // then
        지하철_노선에_지하철역_등록_실패됨(response);
    }

    /**
     * Feature: 지하철역 삭제 기능 Background Given 강남역, 양재역, 정자역, 광교역 지하철역 등록 Given 강남역과 광교역을 시작, 종료 지점으로 한
     * 신분당선 노선을 등록 given 신분당선에 강남역 ~ 양재역 구간 등록과 given 신분당선에 양재역 ~ 정자역 구간 등록후 when 신분당선에서 양재역을 제외할시
     * then 정상적으로 삭제 결과가 조회된다.(삭제 결과 상태코드 200반환, 양제역이 제외된 지하철역들 순서대로 조회가능 )
     */
    @DisplayName("지하철 노선에 등록된 지하철역을 제외한다.")
    @Test
    void removeLineSection1() {
        // given
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 정자역, 2);

        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);

        // then
        지하철_노선에_지하철역_제외됨(removeResponse);
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 정자역, 광교역));
    }

    /**
     * Feature: 지하철 단일구간(지하철역 2개)일때 지하철역 삭제 실패 기능 Background Given 강남역, 양재역, 정자역, 광교역 지하철역 등록 Given
     * 강남역과 광교역을 시작, 종료 지점으로 한 신분당선 노선을 등록 when 신분당선에서 강남역을 제외할시 then 삭제가 실패한다.(삭제 결과 상태코드 500반환)
     */
    @DisplayName("지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다.")
    @Test
    void removeLineSection2() {
        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);

        // then
        지하철_노선에_지하철역_제외_실패됨(removeResponse);
    }


}
