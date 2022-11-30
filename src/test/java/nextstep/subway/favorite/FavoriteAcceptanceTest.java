package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestFixture.로그인_됨;
import static nextstep.subway.favorite.FavoriteAcceptanceTestFixture.즐겨찾기_목록_조회_요청;
import static nextstep.subway.favorite.FavoriteAcceptanceTestFixture.즐겨찾기_목록_조회됨;
import static nextstep.subway.favorite.FavoriteAcceptanceTestFixture.즐겨찾기_삭제_요청;
import static nextstep.subway.favorite.FavoriteAcceptanceTestFixture.즐겨찾기_삭제됨;
import static nextstep.subway.favorite.FavoriteAcceptanceTestFixture.즐겨찾기_생성됨;
import static nextstep.subway.favorite.FavoriteAcceptanceTestFixture.즐겨찾기_생성을_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTestFixture.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTestFixture.지하철역_등록되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 서초역;
    private StationResponse 교대역;
    private StationResponse 강남역;
    private String accessToken;

    @BeforeEach
    void favoriteSetUp() {
        서초역 = 지하철역_등록되어_있음("서초역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("이호선", "green", 서초역.getId(), 강남역.getId(), 10, 100);
        LineResponse 이호선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(이호선, 서초역, 교대역, 5);

        회원_생성을_요청("valid@email.com", "valid_password", 26);

        accessToken = 로그인_됨("valid@email.com", "valid_password");
    }

    /**
     * Feature: 즐겨찾기를 관리한다.
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *     And 회원 등록되어 있음
     *     And 로그인 되어있음
     *
     *   Scenario: 즐겨찾기를 관리
     *     When 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성됨
     *     When 즐겨찾기 목록 조회 요청
     *     Then 즐겨찾기 목록 조회됨
     *     When 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(accessToken, 서초역, 강남역);

        즐겨찾기_생성됨(createResponse);

        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(accessToken);

        즐겨찾기_목록_조회됨(findResponse, 서초역, 강남역);

        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, createResponse);

        즐겨찾기_삭제됨(deleteResponse);
    }
}
