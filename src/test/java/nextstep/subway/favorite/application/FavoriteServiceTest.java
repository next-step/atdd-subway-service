package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
}
