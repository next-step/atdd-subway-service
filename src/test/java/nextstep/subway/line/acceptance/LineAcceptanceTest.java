package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.utils.LineAcceptanceHelper.지하철_노선_등록되어_있음;
import static nextstep.subway.utils.LineAcceptanceHelper.지하철_노선_목록_응답됨;
import static nextstep.subway.utils.LineAcceptanceHelper.지하철_노선_목록_조회_요청;
import static nextstep.subway.utils.LineAcceptanceHelper.지하철_노선_목록_포함됨;
import static nextstep.subway.utils.LineAcceptanceHelper.지하철_노선_삭제됨;
import static nextstep.subway.utils.LineAcceptanceHelper.지하철_노선_생성_실패됨;
import static nextstep.subway.utils.LineAcceptanceHelper.지하철_노선_생성_요청;
import static nextstep.subway.utils.LineAcceptanceHelper.지하철_노선_생성됨;
import static nextstep.subway.utils.LineAcceptanceHelper.지하철_노선_수정_요청;
import static nextstep.subway.utils.LineAcceptanceHelper.지하철_노선_수정됨;
import static nextstep.subway.utils.LineAcceptanceHelper.지하철_노선_응답됨;
import static nextstep.subway.utils.LineAcceptanceHelper.지하철_노선_제거_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15);
    }

/**
 * Feature: 지하철 노선 생성기능
  *  Background
      *  Given 강남역, 광교역 정거장 및 두 역을 포함한 신분당선, 구신분당선 노선 생성 request 선언
  *   when 신분당선 생성 요청을 보내면
  *   then 정상 생성된다 (201 상태코드와 /lines/{신분당선ID} Location이 Header에 설정된다)
*/
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1);

        // then
        지하철_노선_생성됨(response);
    }

/**
 * Feature: 지하철 노선 중복 생성 불가 기
 *  Background
     *  Given 강남역, 광교역 정거장 및 두 역을 포함한 신분당선, 구신분당선 노선 생성 request 선언
 *   given 신분당선 생성 요청
 *   when 다시 신분당선 생성 요청을 보낼시
 *   then 생성이 실패한다(400 상태코드가 반환된다)
 */
    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1);

        // then
        지하철_노선_생성_실패됨(response);
    }

/**
 * Feature: 지하철 노선 목록 조회 기능
 *  Background
    *  Given 강남역, 광교역 정거장 및 두 역을 포함한 신분당선, 구신분당선 노선 생성 request 선언
 *   given 신분당선, 구신분당선 생성 요청
 *   when 지하철 노선 목록 조회 요청시
 *   then 정상적으로 조회가 된다.(상태코드가 200이 반환되고, 각 Line의 정보가 반환된다)
 */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = 지하철_노선_등록되어_있음(lineRequest1);
        ExtractableResponse<Response> createResponse2 = 지하철_노선_등록되어_있음(lineRequest2);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

/**
 * Feature: 지하철 노선 조회 기능
 *  Background
    *  Given 강남역, 광교역 정거장 및 두 역을 포함한 신분당선, 구신분당선 노선 생성 request 선언
 *   given 신분당선 생성 요청
 *   when 지하철 노선 목록 조회 요청시
 *   then 정상적으로 조회가 된다.(상태코드가 200이 반환되고, 해당 라인의 정보가 반환된다.)
 */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청(createResponse);

        // then
        지하철_노선_응답됨(response, createResponse);
    }

/**
 * Feature: 지하철 노선 수정 기능
 *  Background
    *  Given 강남역, 광교역 정거장 및 두 역을 포함한 신분당선, 구신분당선 노선 생성 request 선언
 *   given 신분당선 생성 요청
 *   when 신분당선을 구신분당선으로 변경시
 *   then 정상적으로 수정된다. (상태코드가 200이 반환된다)
 */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        String name = "신분당선";
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(createResponse, lineRequest2);

        // then
        지하철_노선_수정됨(response);
    }

/**
 * Feature: 지하철 노선 삭제 기능
 *  Background
    *  Given 강남역, 광교역 정거장 및 두 역을 포함한 신분당선, 구신분당선 노선 생성 request 선언
 *   given 신분당선 생성 요청
 *   when 신분당선 삭제 요청시
 *   then 정상적으로 삭제가 된다.(상태코드가 204가 반환된다.)
 */
    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(createResponse);

        // then
        지하철_노선_삭제됨(response);
    }
}
