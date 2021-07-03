package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.PrivateRestAssuredTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.PageController.URIMapping.FAVORITES;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기를 관리한다.")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private PrivateRestAssuredTemplate restAssuredTemplate;

    private Long upStationId;
    private Long downStationId;

    @BeforeEach
    public void setup() {
        지하철_노선_등록됨();
        회원_로그인함();
    }

    @DisplayName("즐겨찾기 생성을 요청하고, 조회를 요청한다. 마지막으로 해당 즐겨찾기를 삭제요청한다.")
    @Test
    public void step1() {
        /** step1 즐겨찾기를 생성 요청 */
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(upStationId, downStationId);

        // when
        ExtractableResponse<Response> createResponse = requestCreateFavorite(favoriteRequest);
        FavoriteResponse favoriteResponse = createResponse.as(FavoriteResponse.class);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        /** step2 즐겨찾기를 조회 요청 */
        // when
        ExtractableResponse<Response> findResponse = requestFindFavorites();

        // then
        assertAll(
            () -> assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> {
                FavoriteResponse firstFavoriteResponse = findResponse.jsonPath().getList(".", FavoriteResponse.class).get(0);
                assertThat(firstFavoriteResponse.getId()).isEqualTo(favoriteResponse.getId());
            }
        );

        /** step3 즐겨찾기를 삭제 요청 */
        // when
        ExtractableResponse<Response> deleteResponse = requestDeleteFavorite(favoriteResponse.getId());

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 회원_로그인함() {
        //회원 등록되어 있음
        MemberAcceptanceTest.회원_생성됨(MemberAcceptanceTest.requestCreateMember(EMAIL, PASSWORD, AGE));

        //로그인 되어있음
        String token = AuthAcceptanceTest.login(new TokenRequest(EMAIL, PASSWORD))
                .as(TokenResponse.class)
                .getAccessToken();

        restAssuredTemplate = new PrivateRestAssuredTemplate(token, FAVORITES);
    }

    private void 지하철_노선_등록됨() {
        //지하철역 등록되어 있음
        upStationId = StationAcceptanceTest.requestCreateStation("강남역").as(StationResponse.class).getId();
        downStationId = StationAcceptanceTest.requestCreateStation("정자역").as(StationResponse.class).getId();
        LineRequest lineRequest = LineRequest.builder()
                .name("신분당선").upStationId(upStationId).downStationId(downStationId).distance(6)
                .build();

        //지하철 노선 등록되어 있음
        LineAcceptanceTest.requestCreatedLine(lineRequest);
    }

    /**
     * @see nextstep.subway.favorite.ui.FavoriteController#createFavorite
     */
    private ExtractableResponse<Response> requestCreateFavorite(FavoriteRequest favoriteRequest) {
        return restAssuredTemplate.post(favoriteRequest);
    }

    /**
     * @see nextstep.subway.favorite.ui.FavoriteController#findFavorites
     */
    private ExtractableResponse<Response> requestFindFavorites() {
        return restAssuredTemplate.get();
    }

    /**
     * @see nextstep.subway.favorite.ui.FavoriteController#deleteFavorite
     */
    private ExtractableResponse<Response> requestDeleteFavorite(Long id) {
        return restAssuredTemplate.delete(id);
    }

}