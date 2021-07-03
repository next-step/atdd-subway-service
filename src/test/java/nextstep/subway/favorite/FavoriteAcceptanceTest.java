package nextstep.subway.favorite;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "limdingdong@test.com";
    private static final String PASSWORD = "passowrd";
    private TokenResponse tokenResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();

        StationResponse 강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        StationResponse 정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
        StationResponse 양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        StationResponse 판교역 = 지하철역_등록되어_있음("판교역").as(StationResponse.class);

        LineResponse 신분당선 = 지하철_노선_등록되어_있음("신분당선", "red", 강남역.getId(), 정자역.getId(), 35).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 5);
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 판교역, 20);

        회원_생성을_요청(EMAIL, PASSWORD, 20);
        tokenResponse = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);
    }

    @DisplayName("즐겨찾기 관리")
    @Test
    void favorite() {

    }
}