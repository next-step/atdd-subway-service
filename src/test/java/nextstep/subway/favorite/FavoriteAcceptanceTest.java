package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.application.FavoriteService;
import nextstep.subway.member.domain.Favorite;
import nextstep.subway.member.dto.FavoriteResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestSupport.로그인_되어_있음;
import static nextstep.subway.favorite.FavoriteAcceptanceTestSupport.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static String EMAIL = "email@email.com";
    private static String PASSWORD = "password";
    private static int AGE = 20;

    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private String 사용자토큰;

    @BeforeEach
    void setup() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10))
                .as(LineResponse.class);

        사용자토큰 = 로그인_되어_있음();
    }

    @Test
    @DisplayName("즐겨찾기를 관리한다")
    void manageFavorite() {
        // when
        // 즐겨찾기 생성을 요청
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(사용자토큰, 강남역.getId(), 양재역.getId());

        // then
        // 즐겨찾기 생성됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.LOCATION)).isNotBlank();

        // when
        // 즐겨찾기 목록 조회 요청
        ExtractableResponse<Response> responseFindFavorite = 즐겨찾기_조회_요청(사용자토큰);
        FavoriteResponse favoriteResponse = responseFindFavorite.as(FavoriteResponse.class);

        // then
        // 즐겨찾기 목록 조회됨
        assertThat(responseFindFavorite.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(favoriteResponse.getFavorites()).hasSize(1);

        // when
        // 즐겨찾기 삭제 요청
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(사용자토큰, response.header(HttpHeaders.LOCATION));

        // then
        // 즐겨찾기 삭제됨
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}