package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthRestHelper;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineRestHelper;
import nextstep.subway.line.acceptance.LineSectionRestHelper;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberRestHelper;
import nextstep.subway.station.StationRestHelper;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 광교역;

    private String accessToken;

    @BeforeEach
    void setup() {
        강남역 = StationRestHelper.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationRestHelper.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        광교역 = StationRestHelper.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = LineRestHelper.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        LineSectionRestHelper.지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 5);

        String email = "test@test.com";
        String password = "p@ssw0rd";
        MemberRestHelper.회원_생성을_요청(email, password, 20);
        accessToken = AuthRestHelper.토큰_구하기(email, password);
    }


    @DisplayName("즐겨찾기 생성 테스트")
    @Test
    void favoriteCreateTest() {
        ExtractableResponse<Response> createResponse = FavoriteRestHelper.즐겨찾기_생성을_요청(강남역.getId(), 양재역.getId(), accessToken);
        FavoriteResponse createResponseBody = createResponse.as(FavoriteResponse.class);

        assertAll(
                () -> assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(createResponseBody.getId()).isNotNull(),
                () -> assertThat(createResponseBody.getSource().getId()).isEqualTo(강남역.getId()),
                () -> assertThat(createResponseBody.getSource().getName()).isEqualTo(강남역.getName()),
                () -> assertThat(createResponseBody.getTarget().getId()).isEqualTo(양재역.getId()),
                () -> assertThat(createResponseBody.getTarget().getName()).isEqualTo(양재역.getName())
        );
    }

    @DisplayName("즐겨찾기 조회 테스트")
    @Test
    void favoriteSelectTest() {
        FavoriteRestHelper.즐겨찾기_생성을_요청(강남역.getId(), 양재역.getId(), accessToken);
        FavoriteRestHelper.즐겨찾기_생성을_요청(양재역.getId(), 광교역.getId(), accessToken);

        ExtractableResponse<Response> findResponse = FavoriteRestHelper.즐겨찾기_목록_조회_요청(accessToken);

        List<FavoriteResponse> responsesBody = findResponse.body().jsonPath().getList(".", FavoriteResponse.class);
        assertAll(
                () -> assertThat(responsesBody.get(0).getSource().getName()).isEqualTo("강남역"),
                () -> assertThat(responsesBody.get(0).getTarget().getName()).isEqualTo("양재역"),
                () -> assertThat(responsesBody.get(1).getSource().getName()).isEqualTo("양재역"),
                () -> assertThat(responsesBody.get(1).getTarget().getName()).isEqualTo("광교역")
        );
    }


    @DisplayName("즐겨찾기 삭제 테스트")
    @Test
    void favoriteDeleteTest() {
        String url = FavoriteRestHelper.즐겨찾기_생성을_요청(강남역.getId(), 양재역.getId(), accessToken)
                .header("Location");

        ExtractableResponse<Response> deleteResponse = FavoriteRestHelper.즐겨찾기_목록_삭제_요청(url, accessToken);
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> findResponse = FavoriteRestHelper.즐겨찾기_목록_조회_요청(accessToken);
        List<FavoriteResponse> responsesBody = findResponse.body().jsonPath().getList(".", FavoriteResponse.class);
        assertThat(responsesBody).isEmpty();
    }

}
