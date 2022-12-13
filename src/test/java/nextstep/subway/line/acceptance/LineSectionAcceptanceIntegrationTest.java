package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 구간 관련 통합 테스트")
public class LineSectionAcceptanceIntegrationTest extends LineSectionAcceptanceTestFixture {

    /**
     * Feature: 지하철 구간 관련 기능
     *
     * 테스트에서 사용한 역, 노선 정보
     * 논현역 - 신논현역 - 강남역 - 양재역 - ..... - 정자역 ..... - 광교역
     *
     * Background
     *   Given 지하철역 등록되어 있음 (논현역, 신논현역, 강남역, 양재역, 정자역, 광교역, 광교옆_신설역)
     *   And 지하철 노선 등록되어 있음 (신분당선)
     *   And 지하철 노선에 지하철역 등록되어 있음 (강남역, 광교역)
     *
     * Scenario: 지하철 구간을 관리
     */

    private StationResponse 논현역;
    private StationResponse 신논현역;
    private StationResponse 광교옆_신설역;

    @BeforeEach
    private void setup() {
        /**
        * Background
        *   Given 지하철역 등록되어 있음 (논현역, 신논현역, 강남역, 양재역, 정자역, 광교역, 광교옆_신설역)
        *   And 지하철 노선 등록되어 있음 (신분당선)
        *   And 지하철 노선에 지하철역 등록되어 있음 (강남역, 광교역)
        */
        super.setUp();
        논현역 = StationAcceptanceTest.지하철역_등록되어_있음("논현역").as(StationResponse.class);
        신논현역 = StationAcceptanceTest.지하철역_등록되어_있음("신논현역").as(StationResponse.class);
        광교옆_신설역 = StationAcceptanceTest.지하철역_등록되어_있음("광교옆_신설역").as(StationResponse.class);
    }

    @DisplayName("지하철 구간 관리 통합 인수테스트")
    @Test
    void integrationTest() {
        /**
         *   When 지하철 구간 등록 요청 (상행종점)(논현역-강남역)
         *   Then 지하철 구간 등록됨
         *   When 지하철 노선에 등록된 역 목록 조회 요청
         *   Then 등록한 지하철 구간이 반영된 역 목록이 조회됨 (논현역-강남역-광교역)
         */
        // when
        int 논현_강남_구간_길이 = 8;
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 논현역, 강남역, 논현_강남_구간_길이);
        // then
        지하철_노선에_지하철역_등록됨(response);
        // when
        response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        // then
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(논현역, 강남역, 광교역));

        /**
         *   When 지하철 노선에 이미 등록되어있는 역을 등록 요청 (강남역-광교역)
         *   Then 지하철 구간 등록되지 않음
         */
        // when
        response = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 광교역, 3);
        // then
        지하철_노선에_지하철역_등록_실패됨(response);

        /**
         *   When 지하철 노선에 등록되어있지 않은 두 역으로 이루어진 구간 등록 요청 (양재역-정자역)
         *   Then 지하철 구간 등록되지 않음
         */
        // when
        response = 지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 정자역, 3);
        // then
        지하철_노선에_지하철역_등록_실패됨(response);

        /**
         *   When 지하철 노선에 등록되어있는 두 역 사이에 기존 구간 길이와 같은 구간길이의 구간 등록 요청 (논현역-신논현역)(기존 구간:논현역-강남역)
         *   Then 지하철 구간 등록되지 않음
         */
        // when
        response = 지하철_노선에_지하철역_등록_요청(신분당선, 논현역, 신논현역, 논현_강남_구간_길이);
        // then
        지하철_노선에_지하철역_등록_실패됨(response);

        /**
         *   When 지하철 노선에 등록되어있는 두 역 사이에 기존 구간 길이보다 큰 구간길이의 구간 등록 요청 (논현역-신논현역)(기존 구간:논현역-강남역)
         *   Then 지하철 구간 등록되지 않음
         */
        // when
        response = 지하철_노선에_지하철역_등록_요청(신분당선, 논현역, 신논현역, 논현_강남_구간_길이 + 1);
        // then
        지하철_노선에_지하철역_등록_실패됨(response);

        /**
         *   When 지하철 노선에 등록되어있는 두 역 사이에 역 등록 요청 (상행역 중첩)(논현역-신논현역)
         *   Then 지하철 구간 등록됨
         *   When 지하철 노선에 등록된 역 목록 조회 요청
         *   Then 등록한 지하철 구간이 반영된 역 목록이 조회됨 (논현역-신논현역-강남역-광교역)
         */
        response = 지하철_노선에_지하철역_등록_요청(신분당선, 논현역, 신논현역, 논현_강남_구간_길이 - 5);
        // then
        지하철_노선에_지하철역_등록됨(response);
        // when
        response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        // then
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(논현역, 신논현역, 강남역, 광교역));

        /**
         *   When 지하철 노선에 등록되어있는 두 역 사이에 역 등록 요청 (하행역 중첩)(정자역-광교역)
         *   Then 지하철 구간 등록됨
         *   When 지하철 노선에 등록된 역 목록 조회 요청
         *   Then 등록한 지하철 구간이 반영된 역 목록이 조회됨 (논현역-신논현역-강남역-정자역-광교역)
         */
        response = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 광교역, 3);
        // then
        지하철_노선에_지하철역_등록됨(response);
        // when
        response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        // then
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(논현역, 신논현역, 강남역, 정자역, 광교역));

        /**
         *   When 지하철 구간 등록 요청 (하행종점)(광교역-광교옆_신설역)
         *   Then 지하철 구간 등록됨
         *   When 지하철 노선에 등록된 역 목록 조회 요청
         *   Then 등록한 지하철 구간이 반영된 역 목록이 조회됨 (논현역-신논현역-강남역-정자역-광교역-광교옆_신설역)
         */
        response = 지하철_노선에_지하철역_등록_요청(신분당선, 광교역, 광교옆_신설역, 2);
        // then
        지하철_노선에_지하철역_등록됨(response);
        // when
        response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        // then
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(논현역, 신논현역, 강남역, 정자역, 광교역, 광교옆_신설역));

        /**
         *   When 지하철 하행종점 삭제 요청 (광교옆_신설역)
         *   Then 지하철 구간 삭제됨
         *   When 지하철 노선에 등록된 역 목록 조회 요청
         *   Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨 (논현역-신논현역-강남역-정자역-광교역)
         */
        // when
        response = 지하철_노선에_지하철역_제외_요청(신분당선, 광교옆_신설역);
        // then
        지하철_노선에_지하철역_제외됨(response);
        // when
        response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        // then
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(논현역, 신논현역, 강남역, 정자역, 광교역));

        /**
         *   When 지하철 상행종점 삭제 요청 (논현역)
         *   Then 지하철 구간 삭제됨
         *   When 지하철 노선에 등록된 역 목록 조회 요청
         *   Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨 (신논현역-강남역-정자역-광교역)
         */
        // when
        response = 지하철_노선에_지하철역_제외_요청(신분당선, 논현역);
        // then
        지하철_노선에_지하철역_제외됨(response);
        // when
        response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        // then
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(신논현역, 강남역, 정자역, 광교역));

        /**
         *   When 지하철 노선에 등록되어있는 두 역 사이의 역 삭제 요청 (강남역)(신논현역과 정자역 사이)
         *   Then 지하철 구간 삭제됨
         *   When 지하철 노선에 등록된 역 목록 조회 요청
         *   Then 등록한 지하철 구간이 반영된 역 목록이 조회됨 (신논현역-정자역-광교역)
         */
        // when
        response = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);
        // then
        지하철_노선에_지하철역_제외됨(response);
        // when
        response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        // then
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(신논현역, 정자역, 광교역));

        /**
         *   When 지하철 노선에 등록되어있지 않은 역 삭제 요청 (강남역)(바로 이전 단계에서 삭제한 역)
         *   Then 지하철 구간 삭제되지 않음
         */
        // when
        response = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);
        // then
        지하철_노선에_지하철역_제외_실패됨(response);

        /**
         *   When 지하철 노선에 하나의 구간만 남기고 삭제 (신논현역 삭제)
         *   And 마지막 남은 구간의 역 삭제 요청 (정자역)
         *   Then 지하철 구간 삭제되지 않음
         *   When 마지막 남은 구간의 역 삭제 요청 (광교역)
         *   Then 지하철 구간 삭제되지 않음
         */
        // when
        response = 지하철_노선에_지하철역_제외_요청(신분당선, 신논현역);
        지하철_노선에_지하철역_제외됨(response);
        response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(정자역, 광교역));

        // when
        response = 지하철_노선에_지하철역_제외_요청(신분당선, 정자역);
        // then
        지하철_노선에_지하철역_제외_실패됨(response);

        // when
        response = 지하철_노선에_지하철역_제외_요청(신분당선, 광교역);
        // then
        지하철_노선에_지하철역_제외_실패됨(response);
    }
}
