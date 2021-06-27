package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static nextstep.subway.auth.application.AuthServiceTest.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    private FavoriteService favoriteService;

    @BeforeEach
    void setup() {
        favoriteService = new FavoriteService(memberRepository, stationRepository, favoriteRepository);
    }

    @DisplayName("사용자가 존재하지 않으면 즐겨찾기 저장, 목록 조회, 삭제시 예외가 발생한다.")
    @Test
    void findMemberById() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertAll(
                () -> assertThatThrownBy(() -> favoriteService.saveFavorite(1L, new FavoriteRequest(1L, 2L)))
                        .isInstanceOf(EntityNotFoundException.class),
                () -> assertThatThrownBy(() -> favoriteService.findAllFavorites(1L))
                        .isInstanceOf(EntityNotFoundException.class),
                () -> assertThatThrownBy(() -> favoriteService.deleteFavorite(1L, 1L))
                        .isInstanceOf(EntityNotFoundException.class)
        );
    }

    @DisplayName("역이 존재하지 않으면 즐겨찾기 저장, 삭제시 에외가 발생한다.")
    @Test
    void findStationById() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));
        when(stationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertAll(
                () -> assertThatThrownBy(() -> favoriteService.saveFavorite(1L, new FavoriteRequest(1L, 2L)))
                        .isInstanceOf(EntityNotFoundException.class),
                () -> assertThatThrownBy(() -> favoriteService.deleteFavorite(1L, 1L))
                        .isInstanceOf(EntityNotFoundException.class)
        );
    }
}
