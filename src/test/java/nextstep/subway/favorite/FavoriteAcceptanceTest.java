package nextstep.subway.favorite;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTestRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceRequest.로그인_요청_및_전체_검증;
import static nextstep.subway.auth.acceptance.AuthRequestFixture.등록된_계정_토큰_요청;
import static nextstep.subway.favorite.FavoriteAcceptanceRequest.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTestRequest.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTestRequest.회원_생성을_요청;
import static nextstep.subway.member.MemberFixture.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_생성_요청_및_검증;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private Long 강남역_양재역_즐겨찾기_ID = 1L;
    private FavoriteRequest 강남역_양재역_즐겨찾기;

    private AuthToken 인증_정보;
    private AuthToken 비로그인_정보;

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        회원_생성됨(회원_생성을_요청(EMAIL, PASSWORD, AGE));

        인증_정보 = new AuthToken(로그인_요청_및_전체_검증(등록된_계정_토큰_요청).getAccessToken());
        비로그인_정보 = new AuthToken();

        강남역 = 지하철역_생성_요청_및_검증("강남역").as(StationResponse.class);
        양재역 = 지하철역_생성_요청_및_검증("양재역").as(StationResponse.class);
        광교역 = 지하철역_생성_요청_및_검증("광교역").as(StationResponse.class);

        LineAcceptanceTestRequest.지하철_노선_생성_요청_및_검증(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10));

        강남역_양재역_즐겨찾기 = new FavoriteRequest(강남역.getId(), 양재역.getId());
    }

    @TestFactory
    @DisplayName("라인에 등록된 역을 즐겨찾기 한다.")
    Stream<DynamicTest> 라인에_등록된_역을_즐겨찾기_한다() {
        return Stream.of(
                dynamicTest("즐겨찾기 등록된 강남역 - 양재역 추가", 즐겨찾기_등록_요청_및_등록됨(인증_정보, 강남역_양재역_즐겨찾기, 강남역_양재역_즐겨찾기_ID))
        );
    }

    @TestFactory
    @DisplayName("라인에 등록되지 않은 역을 즐겨찾기 한다")
    Stream<DynamicTest> 라인에_등록되지_않은_역을_즐겨찾기_한다() {
        FavoriteRequest 강남역_광교역_즐겨찾기 = new FavoriteRequest(강남역.getId(), 광교역.getId());

        return Stream.of(
                dynamicTest("즐겨찾기 등록되지 않은 양재역 - 광교역 추가", 즐겨찾기_등록_요청_및_실패됨(인증_정보, 강남역_광교역_즐겨찾기))
        );
    }

    @TestFactory
    @DisplayName("등록되지 않은 역을 즐겨찾기 한다")
    Stream<DynamicTest> 등록되지_않은_역을_즐겨찾기_한다() {
        Long 정자역_ID = 10000L;
        Long 판교역_ID = 10001L;

        FavoriteRequest 정자역_판교역_즐겨찾기 = new FavoriteRequest(정자역_ID, 판교역_ID);
        return Stream.of(
                dynamicTest("즐겨찾기 등록되지 않은 양재역 - 광교역 추가", 즐겨찾기_등록_요청_및_실패됨(인증_정보, 정자역_판교역_즐겨찾기))
        );
    }

    @TestFactory
    @DisplayName("목록을 조회한다")
    Stream<DynamicTest> 목록을_조회한다() {
        FavoriteResponse 강남역_양재역_목록 = new FavoriteResponse(강남역_양재역_즐겨찾기_ID, 강남역, 양재역);
        return Stream.of(
                dynamicTest("즐겨찾기 강남역 - 양재역 추가", 즐겨찾기_등록_요청_및_등록됨(인증_정보, 강남역_양재역_즐겨찾기, 강남역_양재역_즐겨찾기_ID)),
                dynamicTest("즐겨찾기 목록을 조회한다", 즐겨찾기_목록_요청_및_조회됨(인증_정보, 강남역_양재역_목록))
        );
    }
    
    @TestFactory
    @DisplayName("등록된 즐겨찾기를 삭제한다")
    Stream<DynamicTest> 등록된_즐겨찾기를_삭제한다() {
        return Stream.of(
                dynamicTest("즐겨찾기 강남역 - 양재역 추가", 즐겨찾기_등록_요청_및_등록됨(인증_정보, 강남역_양재역_즐겨찾기, 강남역_양재역_즐겨찾기_ID)),
                dynamicTest("즐겨찾기 강남역 - 양재역을 삭제한다.", 즐겨찾기_삭제_요청_및_삭제됨(인증_정보, 강남역_양재역_즐겨찾기_ID)),
                dynamicTest("즐겨찾기 목록을 조회시 비어있다.", 즐겨찾기_목록_요청_및_비어있음(인증_정보))
        );
    }

    @TestFactory
    @DisplayName("비로그인 추가시 실패한다")
    Stream<DynamicTest> 비로그인_추가시_실패한다() {
        return Stream.of(
                dynamicTest("즐겨찾기 강남역 - 양재역 추가", 비로그인시_즐겨찾기_등록_요청_및_실패됨(비로그인_정보, 강남역_양재역_즐겨찾기))
        );
    }

    @TestFactory
    @DisplayName("비로그인 목록조회시 실패한다")
    Stream<DynamicTest> 비로그인_목록조회시_실패한다() {
        return Stream.of(
                dynamicTest("즐겨찾기 목록을 조회한다", 비로그인시_즐겨찾기_목록_요청_및_실패됨(비로그인_정보))
        );
    }

    @TestFactory
    @DisplayName("비로그인 삭제요청시 실패한다")
    Stream<DynamicTest> 비로그인_삭제요청시_실패한다() {
        return Stream.of(
                dynamicTest("즐겨찾기 강남역 - 양재역 추가", 즐겨찾기_등록_요청_및_등록됨(인증_정보, 강남역_양재역_즐겨찾기, 강남역_양재역_즐겨찾기_ID)),
                dynamicTest("즐겨찾기 강남역 - 양재역을 삭제한다", 비로그인시_즐겨찾기_삭제_요청_및_실패됨(비로그인_정보, 강남역_양재역_즐겨찾기_ID))
        );
    }

    @TestFactory
    @DisplayName("다른계정으로 삭제시 실패한다")
    Stream<DynamicTest> 다른계정으로_삭제시_실패한다() {
        회원_생성됨(회원_생성을_요청(NEW_EMAIL, NEW_PASSWORD, NEW_AGE));
        AuthToken 새로운_유저 = new AuthToken(로그인_요청_및_전체_검증(new TokenRequest(NEW_EMAIL, NEW_PASSWORD)).getAccessToken());
        return Stream.of(
                dynamicTest("즐겨찾기 강남역 - 양재역 추가", 즐겨찾기_등록_요청_및_등록됨(인증_정보, 강남역_양재역_즐겨찾기, 강남역_양재역_즐겨찾기_ID)),
                dynamicTest("즐겨찾기 강남역 - 양재역을 삭제한다", 즐겨찾기_삭제_요청_및_거절됨(새로운_유저, 강남역_양재역_즐겨찾기_ID))
        );
    }

    @TestFactory
    @DisplayName("다른계정으로 목록 조회시 조회가 안된다")
    Stream<DynamicTest> 다른계정으로_목록_조회시_조회가_안된다() {
        회원_생성됨(회원_생성을_요청(NEW_EMAIL, NEW_PASSWORD, NEW_AGE));
        AuthToken 새로운_유저 = new AuthToken(로그인_요청_및_전체_검증(new TokenRequest(NEW_EMAIL, NEW_PASSWORD)).getAccessToken());
        return Stream.of(
                dynamicTest("즐겨찾기 강남역 - 양재역 추가", 즐겨찾기_등록_요청_및_등록됨(인증_정보, 강남역_양재역_즐겨찾기, 강남역_양재역_즐겨찾기_ID)),
                dynamicTest("즐겨찾기 목록을 조회시 비어있다.", 즐겨찾기_목록_요청_및_비어있음(새로운_유저))
        );
    }
}