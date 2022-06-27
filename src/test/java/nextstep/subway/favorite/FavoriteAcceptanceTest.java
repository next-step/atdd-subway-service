package nextstep.subway.favorite;

import static nextstep.subway.utils.AuthAcceptanceMethods.*;
import static nextstep.subway.utils.FavoriteAcceptanceMethods.*;
import static nextstep.subway.utils.LineAcceptanceMethods.*;
import static nextstep.subway.utils.StationAcceptanceMethods.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String 강남역 = "강남역";
    private static final String 건대역 = "건대역";
    private static final String 이호선 = "2호선";
    private static final String 노선색상 = "green";
    private static final int 구간거리 = 10;

    private static final String 계정_이메일 = "aajisafj@naver.com";
    private static final String 계정_패스워드 = "abbddffl";
    private static final int 계정_연령 = 27;

    private Long sourceStationId;
    private Long targetStationId;
    private String accessToken;

    /**
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *     And 회원 등록되어 있음
     *     And 로그인 되어있음
     */
    @BeforeEach
    void beforeProcess() {
        ExtractableResponse<Response> upStationResponse = 지하철역_등록되어_있음(강남역);
        ExtractableResponse<Response> downStationResponse = 지하철역_등록되어_있음(건대역);

        sourceStationId = upStationResponse.jsonPath().getLong("id");
        targetStationId = downStationResponse.jsonPath().getLong("id");

        지하철_노선_등록되어_있음(new LineRequest(이호선, 노선색상, sourceStationId, targetStationId, 구간거리));
        회원_등록되어_있음(계정_이메일, 계정_패스워드, 계정_연령);

        ExtractableResponse<Response> loginResponse = 로그인을_요청한다(계정_이메일, 계정_패스워드);
        accessToken = extractAccessToken(loginResponse);
    }

    /**
     *   Scenario: 즐겨찾기를 관리
     *     When 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성됨
     *     When 즐겨찾기 목록 조회 요청
     *     Then 즐겨찾기 목록 조회됨
     *     When 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기를 관리")
    @Test
    void manageMember() {
        //when
        ExtractableResponse<Response> createdResponse = 즐겨찾기_생성을_요청(accessToken,
                new FavoriteRequest(sourceStationId, targetStationId));
        //then
        Long favoriteId = createdResponse.jsonPath().getLong("id");
        즐겨찾기_생성_요청_성공함(createdResponse);

        //when
        ExtractableResponse<Response> searchResponse = 즐겨찾기_목록_조회를_요청(accessToken);
        //then
        즐겨찾기_목록_조회_요청_성공함(searchResponse);

        //when
        ExtractableResponse<Response> deletedResponse = 즐겨찾기_삭제를_요청(accessToken, createdResponse);
        //then
        즐겨찾기_삭제_요청_성공함(deletedResponse);
    }
}
