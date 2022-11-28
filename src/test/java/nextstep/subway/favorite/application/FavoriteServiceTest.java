package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    private Member member;
    private Station source;
    private Station target;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        member = new Member("valid@email.com", "valid_password", 26);
        source = new Station("출발역");
        target = new Station("도착역");
    }


    @DisplayName("즐겨찾기를 생성할 수 있다.")
    @Test
    void createFavorite() {
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        Favorite favorite = new Favorite(member, source, target);
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(stationRepository.findById(1L)).willReturn(Optional.of(source));
        given(stationRepository.findById(2L)).willReturn(Optional.of(target));
        given(favoriteRepository.save(any())).willReturn(favorite);

        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(1L, favoriteRequest);

        assertAll(
                () -> assertThat(favoriteResponse.getSource().getName()).isEqualTo(source.getName()),
                () -> assertThat(favoriteResponse.getTarget().getName()).isEqualTo(target.getName())
        );
    }

    @DisplayName("출발역과 도착역이 동일한 경우 즐겨찾기 등록 시 에외가 발생한다.")
    @Test
    void createFavoriteWithSameSourceAndTarget() {
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 1L);
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(stationRepository.findById(1L)).willReturn(Optional.of(source));

        assertThatThrownBy(() -> favoriteService.saveFavorite(1L, favoriteRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발역과 도착역이 동일한 경우 즐겨찾기로 등록할 수 없습니다.");
    }

    @DisplayName("즐겨찾기 목록을 조회할 수 있다.")
    @Test
    void findFavorites() {
        Favorite first = new Favorite(member, source, target);
        Favorite second = new Favorite(member, new Station("역곡역"), new Station("신도림역"));
        List<Favorite> favorites = Arrays.asList(new Favorite(member, source, target), second);
        given(favoriteRepository.findAllByMemberId(1L)).willReturn(favorites);

        List<FavoriteResponse> findFavorites = favoriteService.findFavorites(1L);

        assertThat(findFavorites).hasSize(2);
    }

    @DisplayName("즐겨찾기를 삭제할 수 있다.")
    @Test
    void deleteFavorite() {
        Favorite favorite = new Favorite(member, source, target);
        given(favoriteRepository.findById(1L)).willReturn(Optional.of(favorite));
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));

        favoriteService.deleteFavorite(1L, 1L);

        verify(favoriteRepository).delete(favorite);
    }

    @DisplayName("본인의 즐겨찾기가 아닌 경우 즐겨찾기 삭제 시 예외가 발생한다.")
    @Test
    void deleteFavoriteWithAnotherMember() {
        Favorite favorite = new Favorite(member, source, target);
        Member anotherMember = new Member("another@email.com", "another_password", 25);
        given(favoriteRepository.findById(1L)).willReturn(Optional.of(favorite));
        given(memberRepository.findById(1L)).willReturn(Optional.of(anotherMember));

        assertThatThrownBy(() -> favoriteService.deleteFavorite(1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("본인의 즐겨찾기가 아닌 경우 작업을 진행할 수 없습니다.");
    }
}
