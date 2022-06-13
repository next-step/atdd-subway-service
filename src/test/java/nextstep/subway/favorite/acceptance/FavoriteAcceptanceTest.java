package nextstep.subway.favorite.acceptance;

import static nextstep.subway.auth.acceptance.AuthRestAssured.로그인_되어_있음;
import static nextstep.subway.line.acceptance.LineRestAssured.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberRestAssured.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {
    StationResponse 강남역;
    StationResponse 정자역;
    LineRequest 신분당선;
    String EMAIL = "email@email.com";
    String PASSWORD = "password";
    int AGE = 20;
    String 사용자토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
        신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 정자역.getId(), 10);
        지하철_노선_등록되어_있음(신분당선);
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        사용자토큰 = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    /**
     *   Scenario: 즐겨찾기를 관리한다.
     *     When 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성됨
     *     When 즐겨찾기 목록 조회 요청
     *     Then 즐겨찾기 목록 조회됨
     *     When 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageMember() {
        // when
       //ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(사용자토큰, 강남역, 정자역);
    }
}