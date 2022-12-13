package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.rest.AuthRestAssured;
import nextstep.subway.favorite.dto.FavoriteCreateRequest;
import nextstep.subway.favorite.rest.FavoriteRestAssured;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.line.rest.LineRestAssured;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.rest.MemberRestAssured;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.rest.StationRestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 출발역;
    private StationResponse 도착역;
    private String 인증_토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();

        출발역 = StationRestAssured.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        도착역 = StationRestAssured.지하철역_등록되어_있음("삼성역").as(StationResponse.class);
        LineRestAssured.지하철_노선_등록되어_있음(new LineCreateRequest("이호선", "color", 출발역.getId(), 도착역.getId(), 5, 0));

        String 회원_아이디 = "test@test.com";
        String 회원_패스워드 = "test";
        int 회원_나이 = 31;

        MemberRequest memberRequest = new MemberRequest(회원_아이디, 회원_패스워드, 회원_나이);
        MemberRestAssured.회원_가입_요청(memberRequest);

        TokenRequest tokenRequest = new TokenRequest(회원_아이디, 회원_패스워드);
        인증_토큰 = AuthRestAssured.로그인_요청(tokenRequest)
                .as(TokenResponse.class)
                .getAccessToken();
    }

    /**
     * Feature: 즐겨찾기를 관리한다.
     *
     * Background
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     * And 회원 등록되어 있음
     * And 로그인 되어있음
     *
     * Scenario: 즐겨찾기를 관리
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨찾기 목록 조회됨
     * When 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제됨
     */
    @DisplayName("로그인 된 회원이 시작역와 도착역을 선택하여 즐겨찾기 생성 요청시 요청에 성공한다")
    @Test
    void create_favorite() {
        // when
        ExtractableResponse<Response> 즐겨찾기_생성_요청_결과 = FavoriteRestAssured.즐겨찾기_생성_요청(인증_토큰, new FavoriteCreateRequest(출발역.getId(), 도착역.getId()));
        // then
        즐겨찾기_생성됨(즐겨찾기_생성_요청_결과);
    }

    @DisplayName("로그인 된 회원이 등록한 즐겨찾기 목록 요청시 요청에 성공한다")
    @Test
    void find_my_favorites() {
        // given
        FavoriteRestAssured.즐겨찾기_생성_요청(인증_토큰, new FavoriteCreateRequest(출발역.getId(), 도착역.getId()));
        // when
        ExtractableResponse<Response> 즐겨찾기_목록_요청_결과 = FavoriteRestAssured.즐겨찾기_목록_요청(인증_토큰);
        // then
        즐겨찾기_목록_조회됨(즐겨찾기_목록_요청_결과);
    }

    @DisplayName("로그인 된 회원이 등록한 즐겨찾기 삭제 요청시 요청에 성공한다")
    @Test
    void delete_favorite() {
        // given
        ExtractableResponse<Response> 즐겨찾기_생성_요청_결과 = FavoriteRestAssured.즐겨찾기_생성_요청(인증_토큰, new FavoriteCreateRequest(출발역.getId(), 도착역.getId()));
        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_요청_결과 = FavoriteRestAssured.즐겨찾기_삭제_요청(인증_토큰, 즐겨찾기_생성_요청_결과);
        // then
        즐겨찾기_삭제됨(즐겨찾기_삭제_요청_결과);
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        List<String> sourceNames = response.jsonPath().getList("source.name", String.class);
        List<String> targetNames = response.jsonPath().getList("target.name", String.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(sourceNames).containsExactly(출발역.getName()),
                () -> assertThat(targetNames).containsExactly(도착역.getName())
        );
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
