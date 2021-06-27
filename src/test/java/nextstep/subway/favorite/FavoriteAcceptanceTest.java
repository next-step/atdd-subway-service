package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceStep.로그인_되어_있음;
import static nextstep.subway.favorite.FavoriteAcceptanceStep.*;
import static nextstep.subway.line.acceptance.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceStep.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceStep.회원_등록되어_있음;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private StationResponse 잠실역;
    private StationResponse 삼성역;
    private StationResponse 강남역;
    private TokenResponse 사용자;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // Given 지하철역 등록되어 있음
        잠실역 = 지하철역_등록되어_있음("잠실역").as(StationResponse.class);
        삼성역 = 지하철역_등록되어_있음("삼성역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        // And 지하철 노선 등록되어 있음
        LineResponse lineResponse = 지하철_노선_등록되어_있음("이호선", "green", 잠실역, 삼성역, 10);
        // And 지하철 노선에 지하철역 등록되어 있음
        지하철_노선에_지하철역_등록되어_있음(lineResponse, 삼성역, 강남역, 3);
        // And 회원 등록되어 있음
        회원_등록되어_있음(EMAIL, PASSWORD, 20);
        // And 로그인 되어있음
        사용자 = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void favorite() {
        // When 즐겨찾기 생성을 요청
        ExtractableResponse<Response> 즐겨찾기_생성을_요청_결과 = 즐겨찾기_생성을_요청(사용자, 잠실역, 강남역);
        // Then 즐겨찾기 생성됨
        즐겨찾기_생성됨(즐겨찾기_생성을_요청_결과);

        // When 즐겨찾기 목록 조회 요청
        ExtractableResponse<Response> 즐겨찾기_목록_조회_요청_결과 = 즐겨찾기_목록_조회_요청(사용자);
        // Then 즐겨찾기 목록 조회됨
        즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_요청_결과);

        // When 즐겨찾기 삭제 요청
        ExtractableResponse<Response> 즐겨찾기_삭제_요청_결과 = 즐겨찾기_삭제_요청(사용자, 즐겨찾기_생성을_요청_결과);
        // Then 즐겨찾기 삭제됨
        즐겨찾기_삭제됨(즐겨찾기_삭제_요청_결과);
    }
}
