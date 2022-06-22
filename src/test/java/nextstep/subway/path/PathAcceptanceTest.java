package nextstep.subway.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.auth.factory.AuthAcceptanceFactory.로그인_되어_있음;
import static nextstep.subway.member.factory.MemberAcceptanceFactory.회원_생성을_요청;
import static nextstep.subway.path.factory.PathAcceptanceFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    private String 이메일 = "14km@github.com";
    private String 패스워드 = "a1s2d3f4";
    private int 나이 = 20;

    private String 사용자_토큰;

    /**
     * 교대역    --- *2호선* ---   강남역 --- 역삼역 --- 선릉역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_생성을_요청(이메일, 패스워드, 나이);
        사용자_토큰 = 로그인_되어_있음(이메일, 패스워드);

        강남역 = 지하철역_등록되어_있음("강남역");
        양재역 = 지하철역_등록되어_있음("양재역");
        교대역 = 지하철역_등록되어_있음("교대역");
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 0);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-green-600", 교대역, 강남역, 10, 300);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-orange-600", 교대역, 양재역, 12, 0);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 8);
    }

    @Test
    @DisplayName("성인 최단거리 조회 테스트")
    void findShortestDistance() {
        PathRequest pathRequest = PathRequest.of(강남역.getId(), 교대역.getId());
        List<Station> 역_목록_조회_결과 = Arrays.asList(new Station("강남역"), new Station("교대역"));
        int 요금_조회_결과 = 1_550;

        ExtractableResponse<Response> 최단_거리_조회 = 최단_거리_조회(사용자_토큰, pathRequest);

        최단_거리_조회_됨(최단_거리_조회, 역_목록_조회_결과, 요금_조회_결과);
    }

    @Test
    @DisplayName("성인 최단거리 조회 추가 요금 테스트")
    void findShortestDistance2() {
        PathRequest pathRequest = PathRequest.of(교대역.getId(), 양재역.getId());
        List<Station> 역_목록_조회_결과 = Arrays.asList(new Station("교대역"), new Station("남부터미널역"), new Station("양재역"));
        int 요금_조회_결과 = 1_350;

        ExtractableResponse<Response> 최단_거리_조회 = 최단_거리_조회(사용자_토큰, pathRequest);

        최단_거리_조회_됨(최단_거리_조회, 역_목록_조회_결과, 요금_조회_결과);
    }


    @Test
    @DisplayName("어린이 사용자 최단거리 조회 테스트")
    void findShortestDistanceByChild() {
        String 어린이_사용자_이메일 = "15km@github.com";
        String 어린이_사용자_패스워드 = "15";
        회원_생성을_요청(어린이_사용자_이메일, 어린이_사용자_패스워드, 7);
        String 어린이_사용자_토큰 = 로그인_되어_있음(어린이_사용자_이메일, 어린이_사용자_패스워드);

        PathRequest pathRequest = PathRequest.of(강남역.getId(), 양재역.getId());
        List<Station> 역_목록_조회_결과 = Arrays.asList(new Station("강남역"), new Station("양재역"));
        int 요금_조회_결과 = 450;

        ExtractableResponse<Response> 최단_거리_조회 = 최단_거리_조회(어린이_사용자_토큰, pathRequest);

        최단_거리_조회_됨(최단_거리_조회, 역_목록_조회_결과, 요금_조회_결과);
    }

    @Test
    @DisplayName("청소년 사용자 최단거리 조회 테스트")
    void findShortestDistanceByYouth() {
        String 청소년_사용자_이메일 = "16km@github.com";
        String 청소년_사용자_패스워드 = "16";
        회원_생성을_요청(청소년_사용자_이메일, 청소년_사용자_패스워드, 15);
        String 청소년_사용자_토큰 = 로그인_되어_있음(청소년_사용자_이메일, 청소년_사용자_패스워드);

        PathRequest pathRequest = PathRequest.of(강남역.getId(), 교대역.getId());
        List<Station> 역_목록_조회_결과 = Arrays.asList(new Station("강남역"), new Station("교대역"));
        int 요금_조회_결과 = 960;

        ExtractableResponse<Response> 최단_거리_조회 = 최단_거리_조회(청소년_사용자_토큰, pathRequest);

        최단_거리_조회_됨(최단_거리_조회, 역_목록_조회_결과, 요금_조회_결과);
    }

    @Test
    @DisplayName("최단거리 조회시 동일한 역정보 실패 테스트")
    void findShortestDistanceSameStation() {
        PathRequest pathRequest = PathRequest.of(강남역.getId(), 강남역.getId());

        ExtractableResponse<Response> 최단_거리_조회 = 최단_거리_조회(사용자_토큰, pathRequest);

        최단_거리_조회_역정보_조회_실패_됨(최단_거리_조회);
    }

    @Test
    @DisplayName("최단거리 조회시 존재하지 않는 역정보 실패 테스트")
    void findShortestDistanceNotFindStation() {
        Long 방배역_id = 999L;
        PathRequest pathRequest = PathRequest.of(강남역.getId(), 방배역_id);

        ExtractableResponse<Response> 최단_거리_조회 = 최단_거리_조회(사용자_토큰, pathRequest);

        최단_거리_조회_미존재_역정보_조회_실패_됨(최단_거리_조회);
    }

    @Test
    @DisplayName("최단거리 조회시 출발역과 도착역이 연결이 되어 있지 않은 경우 실패 테스트")
    void findShortestDistanceNotConnected() {
        StationResponse 사당역 = 지하철역_등록되어_있음("사당역");
        StationResponse 이수역 = 지하철역_등록되어_있음("이수역");
        LineResponse 사호선 = 지하철_노선_등록되어_있음("사호선", "bg-blue-600", 사당역, 이수역, 5, 0);

        PathRequest pathRequest = PathRequest.of(강남역.getId(), 사당역.getId());

        ExtractableResponse<Response> 최단_거리_조회 = 최단_거리_조회(사용자_토큰, pathRequest);

        최단_거리_조회_역정보_조회_실패_됨(최단_거리_조회);
    }

    void 최단_거리_조회_역정보_조회_실패_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    void 최단_거리_조회_미존재_역정보_조회_실패_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    void 최단_거리_조회_됨(ExtractableResponse<Response> response, List<Station> stations, int fare) {
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(PathResponse.class).getStations()).isEqualTo(stations),
                () -> assertThat(response.as(PathResponse.class).getFare()).isEqualTo(fare)
        );
    }
}
