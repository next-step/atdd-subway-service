package nextstep.subway.line.acceptance;

import static nextstep.subway.line.acceptance.LineAcceptanceMethods.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceMethods.*;
import static nextstep.subway.station.StationAcceptanceMethods.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 구간 관련 기능")
class LineSectionAcceptanceTest extends AcceptanceTest {
    private static final int DISTANCE_2 = 2;
    private static final int DISTANCE_3 = 3;
    private static final int DISTANCE_5 = 5;
    private static final int DISTANCE_10 = 10;
    private static final int FARE_1000 = 1000;

    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음(StationRequest.of("강남역")).as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음(StationRequest.of("양재역")).as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음(StationRequest.of("정자역")).as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음(StationRequest.of("광교역")).as(StationResponse.class);

        LineRequest lineRequest = LineRequest.of("신분당선", "bg-red-600", FARE_1000, 강남역.getId(), 광교역.getId(), DISTANCE_10);
        신분당선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void addLineSection() {
        // given
        Long 신분당선_Id = 신분당선.getId();
        SectionRequest 강남_양재_구간_Request = SectionRequest.of(강남역.getId(), 양재역.getId(), DISTANCE_3);

        // when
        지하철_노선에_지하철역_등록_요청(신분당선_Id, 강남_양재_구간_Request);

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선_Id);
        지하철_노선에_지하철역_등록됨(response);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 양재역, 광교역));
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서 상관 없이 등록한다.")
    @Test
    void addLineSection2() {
        // given
        Long 신분당선_Id = 신분당선.getId();
        SectionRequest 강남_양재_구간_Request = SectionRequest.of(강남역.getId(), 양재역.getId(), DISTANCE_2);
        SectionRequest 정자_강남_구간_Request = SectionRequest.of(정자역.getId(), 강남역.getId(), DISTANCE_5);

        // when
        지하철_노선에_지하철역_등록_요청(신분당선_Id, 강남_양재_구간_Request);
        지하철_노선에_지하철역_등록_요청(신분당선_Id, 정자_강남_구간_Request);

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선_Id);
        지하철_노선에_지하철역_등록됨(response);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(정자역, 강남역, 양재역, 광교역));
    }

    @DisplayName("지하철 노선에 이미 등록되어있는 역을 등록한다.")
    @Test
    void addLineSectionWithSameStation() {
        // given
        Long 신분당선_Id = 신분당선.getId();
        SectionRequest 강남_광교_구간_Request = SectionRequest.of(강남역.getId(), 광교역.getId(), DISTANCE_3);

        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선_Id, 강남_광교_구간_Request);

        // then
        지하철_노선에_지하철역_등록_실패됨(response);
    }

    @DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
    @Test
    void addLineSectionWithNoStation() {
        // given
        Long 신분당선_Id = 신분당선.getId();
        SectionRequest 정자_양재_구간_Request = SectionRequest.of(정자역.getId(), 양재역.getId(), DISTANCE_3);

        // when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선_Id, 정자_양재_구간_Request);

        // then
        지하철_노선에_지하철역_등록_실패됨(response);
    }

    @DisplayName("지하철 노선에 등록된 지하철역을 제외(삭제)한다.")
    @Test
    void removeLineSection1() {
        // given
        Long 신분당선_Id = 신분당선.getId();
        SectionRequest 강남_양재_구간_Request = SectionRequest.of(강남역.getId(), 양재역.getId(), DISTANCE_2);
        SectionRequest 양재_정자_구간_Request = SectionRequest.of(양재역.getId(), 정자역.getId(), DISTANCE_2);

        지하철_노선에_지하철역_등록_요청(신분당선_Id, 강남_양재_구간_Request);
        지하철_노선에_지하철역_등록_요청(신분당선_Id, 양재_정자_구간_Request);

        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선_Id, 양재역.getId());

        // then
        지하철_노선에_지하철역_제외됨(removeResponse);
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선_Id);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(강남역, 정자역, 광교역));
    }

    @DisplayName("지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다.")
    @Test
    void removeLineSection2() {
        // given
        Long 신분당선_Id = 신분당선.getId();

        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선_Id, 강남역.getId());

        // then
        지하철_노선에_지하철역_제외_실패됨(removeResponse);
    }
}
