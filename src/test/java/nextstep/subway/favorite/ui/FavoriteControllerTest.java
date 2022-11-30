package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("즐겾찾기 관련 컨트롤러 테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteControllerTest {

    @InjectMocks
    private FavoriteController favoriteController;

    @Mock
    private FavoriteService favoriteService;

    @DisplayName("즐겨찾기 생성 작업에 성공한다")
    @Test
    void createFavorite() {
        LoginMember loginMember = new LoginMember(1L, "email", 30);
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        FavoriteResponse favoriteResponse = new FavoriteResponse(1L, null, null);
        when(favoriteService.saveFavorite(any(), any())).thenReturn(favoriteResponse);

        ResponseEntity<Void> responseEntity = favoriteController.createFavorite(loginMember, favoriteRequest);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @DisplayName("즐겨찾기 목록 조회에 성공한다")
    @Test
    void findAllFavorites() {
        ResponseEntity<List<FavoriteResponse>> allFavorites = favoriteController.findAllFavorites(new LoginMember());

        assertThat(allFavorites.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("즐겨찾기 삭제에 성공한다")
    @Test
    void deleteFavorite() {
        favoriteService.deleteFavoriteById(1L, 1L);
    }
}
