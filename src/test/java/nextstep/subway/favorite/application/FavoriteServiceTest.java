package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("즐겨찾기 서비스")
@SpringBootTest
class FavoriteServiceTest {
    @Autowired
    FavoriteService favoriteService;
    @MockBean
    FavoriteRepository favoriteRepository;
    @MockBean
    StationRepository stationRepository;

    Station source = new Station("강남역");
    Station target = new Station("교대역");
    FavoriteRequest request = new FavoriteRequest(1L, 2L);
    LoginMember loginMember = new LoginMember(1L, "test@gmail.com", 20);
    Favorite favorite = new Favorite(loginMember.getId(), source, target);

    @BeforeEach
    void setUp() {
        when(favoriteRepository.findByMemberId(loginMember.getId())).thenReturn(Arrays.asList(new Favorite(loginMember.getId(), source, target)));
        when(favoriteRepository.save(any())).thenReturn(favorite);
        when(favoriteRepository.findById(any())).thenReturn(Optional.of(favorite));
        when(stationRepository.findById(request.getSource())).thenReturn(Optional.of(source));
        when(stationRepository.findById(request.getTarget())).thenReturn(Optional.of(target));
    }

    @DisplayName("내 즐겨찾기 목록을 조회한다")
    @Test
    void findAllBy() {
        List<FavoriteResponse> favorites = favoriteService.findAllBy(loginMember);
        assertThat(favorites.size()).isEqualTo(1);
        assertThat(favorites.get(0).getSource().getName()).isEqualTo(source.getName());
        assertThat(favorites.get(0).getTarget().getName()).isEqualTo(target.getName());
    }

    @DisplayName("내 즐겨찾기를 추가한다")
    @Test
    void createFavorite() {
        FavoriteResponse response = favoriteService.createFavorite(loginMember, request);
        assertThat(response.getSource().getName()).isEqualTo(source.getName());
        assertThat(response.getTarget().getName()).isEqualTo(target.getName());
    }

    @DisplayName("내 즐겨찾기를 삭제한다")
    @Test
    void deleteById() {
        favoriteService.deleteById(loginMember, 1L);
        verify(favoriteRepository).delete(favorite);
    }
}
