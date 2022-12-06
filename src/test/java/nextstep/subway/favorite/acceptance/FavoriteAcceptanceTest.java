package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.favorite.acceptance.FavoriteSteps.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_생성_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_생성_요청;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "member@email.com";
    public static final String PASSWORD = "password";
    private Long stationA;
    private Long stationB;
    private Long stationC;
    private Long stationD;
    private Long lineA;
    private Long lineB;
    private Long lineC;
    private Long lineD;

    /**
     * stationA    --- *lineD* ---   stationD
     * |                                |
     * *lineA*                     *lineC*
     * |                              |
     * stationB  --- *lineB* ---   stationC
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        stationA = 지하철역_생성_요청(memberA, "A").jsonPath().getLong("id");
        stationB = 지하철역_생성_요청(memberA, "B").jsonPath().getLong("id");
        stationC = 지하철역_생성_요청(memberA, "C").jsonPath().getLong("id");
        stationD = 지하철역_생성_요청(memberA, "D").jsonPath().getLong("id");

        lineA = 지하철_노선_생성_요청(new LineRequest("A", "green", stationA, stationB, 10)).jsonPath().getLong("id");
        lineB = 지하철_노선_생성_요청(new LineRequest("B", "red", stationB, stationC, 10)).jsonPath().getLong("id");
        lineC = 지하철_노선_생성_요청(new LineRequest("C", "orange", stationC, stationD, 2)).jsonPath().getLong("id");
        lineD = 지하철_노선_생성_요청(new LineRequest("D", "yellow", stationA, stationD, 2)).jsonPath().getLong("id");
    }

    /*
    Feature: 즐겨찾기를 관리한다.

      Background
        Given 지하철역 등록되어 있음
        And 지하철 노선 등록되어 있음
        And 지하철 노선에 지하철역 등록되어 있음
        And 회원 등록되어 있음
        And 로그인 되어있음

      Scenario: 즐겨찾기를 관리
        When 즐겨찾기 생성을 요청
        Then 즐겨찾기 생성됨
        When 즐겨찾기 목록 조회 요청
        Then 즐겨찾기 목록 조회됨
        When 즐겨찾기 삭제 요청
        Then 즐겨찾기 삭제됨
     */

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageMember() {

        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(memberA, stationA, stationB);
        즐겨찾기_생성됨(createResponse);

        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(memberA);
        즐겨찾기_목록_조회됨(findResponse);

        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(memberA, createResponse);
        즐겨찾기_삭제됨(deleteResponse);
    }
}
