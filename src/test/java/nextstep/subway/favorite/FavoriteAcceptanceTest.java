package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.step.AuthAcceptanceStep.로그인_되어_있음;
import static nextstep.subway.favorite.step.FavoriteAcceptanceStep.즐겨찾기_목록_조회_요청;
import static nextstep.subway.favorite.step.FavoriteAcceptanceStep.즐겨찾기_목록_조회됨;
import static nextstep.subway.favorite.step.FavoriteAcceptanceStep.즐겨찾기_삭제_요청;
import static nextstep.subway.favorite.step.FavoriteAcceptanceStep.즐겨찾기_삭제됨;
import static nextstep.subway.favorite.step.FavoriteAcceptanceStep.즐겨찾기_생성됨;
import static nextstep.subway.favorite.step.FavoriteAcceptanceStep.즐겨찾기_생성을_요청;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.member.step.MemberAcceptanceStep.회원_등록_되어_있음;
import static nextstep.subway.station.step.StationAcceptanceStep.지하철역_등록되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    private String 사용자;
    private StationResponse 강남역;
    private StationResponse 광교역;

    @BeforeEach
    void setUp() {
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
        지하철_노선_등록되어_있음(new LineCreateRequest("신분당선", "bg-red-600",
            new SectionRequest(강남역.getId(), 광교역.getId(), 10)));

        String email = "email@email.com";
        String password = "password";
        회원_등록_되어_있음(email, password, 1);
        사용자 = 로그인_되어_있음(email, password);
    }

    @Test
    @DisplayName("즐겨찾기를 관리")
    void manageFavorite() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(사용자, 강남역.getId(), 광교역.getId());

        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(사용자);

        // then
        즐겨찾기_목록_조회됨(findResponse, 강남역, 광교역);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(사용자, createResponse);

        // then
        즐겨찾기_삭제됨(deleteResponse);
    }
}
