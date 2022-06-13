package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_되어있음;
import static nextstep.subway.behaviors.MemberBehaviors.회원_생성을_요청;
import static nextstep.subway.behaviors.SubwayBehaviors.즐겨찾기_목록_조회_요청;
import static nextstep.subway.behaviors.SubwayBehaviors.즐겨찾기_목록_조회됨;
import static nextstep.subway.behaviors.SubwayBehaviors.즐겨찾기_삭제_요청;
import static nextstep.subway.behaviors.SubwayBehaviors.즐겨찾기_삭제됨;
import static nextstep.subway.behaviors.SubwayBehaviors.즐겨찾기_생성_요청;
import static nextstep.subway.behaviors.SubwayBehaviors.즐겨찾기_생성됨;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선_등록되어_있음;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.behaviors.SubwayBehaviors.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private String 사용자토큰;
    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 잠실역;
    private LineResponse 이호선;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        잠실역 = 지하철역_등록되어_있음("잠실역").as(StationResponse.class);
        이호선 = 지하철_노선_등록되어_있음("이호선", "green", 강남역, 잠실역, 10);
        지하철_노선에_지하철역_등록_요청(이호선, 강남역, 역삼역, 5);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        사용자토큰 = 로그인_되어있음(EMAIL, PASSWORD).getAccessToken();
    }

    /*
    <인수조건>
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
    @DisplayName("즐겨찾기 관리 시나리오 테스트")
    @Test
    void favoriteTest() {
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(사용자토큰, String.valueOf(강남역.getId()),
                String.valueOf(역삼역.getId()));
        즐겨찾기_생성됨(createResponse);

        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(사용자토큰);
        즐겨찾기_목록_조회됨(response);

        List<FavoriteResponse> favorites = response.jsonPath().getList("$", FavoriteResponse.class);
        assertThat(favorites).hasSize(1);

        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(사용자토큰, favorites.get(0));
        즐겨찾기_삭제됨(deleteResponse);

        favorites = 즐겨찾기_목록_조회_요청(사용자토큰).jsonPath().getList("$", FavoriteResponse.class);
        assertThat(favorites).isEmpty();
    }
}
