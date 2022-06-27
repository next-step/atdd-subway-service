package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
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

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private FavoriteService favoriteService;


    private Station 주안역;
    private Station 인천역;
    private Station 화곡역;
    private Member 유저;


    @BeforeEach
    public void setUp() {
        주안역 = new Station(1L,"주안역");
        인천역 = new Station(2L,"인천역");
        화곡역 = new Station(3L,"화곡역");
        유저 = new Member(1L,"email@gmail.com", "password", 20);

    }

    @Test
    @DisplayName("즐겨찾기 저장")
    void saveFavorite() {

        //when
        when(memberRepository.getById(any())).thenReturn(유저);
        when(stationRepository.getById(1L)).thenReturn(주안역);
        when(stationRepository.getById(2L)).thenReturn(인천역);
        when(favoriteRepository.save(any())).thenReturn(new Favorite(유저, 인천역, 주안역));

        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(주안역.getId(),
                new FavoriteRequest(인천역.getId(), 주안역.getId()));

        assertAll(
                () -> assertThat(favoriteResponse.getSource().getName()).isEqualTo(인천역.getName()),
                () -> assertThat(favoriteResponse.getTarget().getName()).isEqualTo(주안역.getName())
        );
    }

    @Test
    @DisplayName("역이 없는 경우 저장이 되지 않는다.")
    void saveStationIsNull() {
        when(memberRepository.getById(any())).thenReturn(유저);

        assertThatIllegalArgumentException().isThrownBy(() ->
            favoriteService.saveFavorite(주안역.getId(), new FavoriteRequest(인천역.getId(), 주안역.getId()))
        );
    }

    @Test
    @DisplayName("유저가 없는 경우 저장이 되지 않는다.")
    void saveMemberIsNull() {
        when(stationRepository.getById(1L)).thenReturn(주안역);
        when(stationRepository.getById(2L)).thenReturn(인천역);

        assertThatIllegalArgumentException().isThrownBy(() ->
                favoriteService.saveFavorite(주안역.getId(), new FavoriteRequest(인천역.getId(), 주안역.getId()))
        );
    }

    @Test
    @DisplayName("내 즐겨찾기 목록이 조회된다")
    void findMyFavorites() {
        //given
        final Favorite favorite1 = new Favorite(유저, 주안역, 인천역);
        final Favorite favorite2 = new Favorite(유저, 인천역, 화곡역);

        when(favoriteRepository.findAllByMemberId(유저.getId())).thenReturn(Arrays.asList(favorite1, favorite2));

        //when
        List<FavoriteResponse> favoriteResponses = favoriteService.searchMemberFavorite(유저.getId());

        //then
        assertAll(
                () -> assertThat(favoriteResponses).hasSize(2),
                () -> assertThat(favoriteResponses).extracting("source.name").contains("주안역", "인천역"),
                () -> assertThat(favoriteResponses).extracting("target.name").contains("인천역", "화곡역")
        );

    }

    @Test
    @DisplayName("즐겨찾기가 조회된다")
    void favorites() {
        //given
        final Favorite favorite = new Favorite(유저, 주안역, 인천역);

        when(favoriteRepository.findById(any())).thenReturn(Optional.of(favorite));

        //when
        FavoriteResponse favoriteResponses = favoriteService.searchFavorite(1L);

        //then
        assertAll(
                () -> assertThat(favoriteResponses.getSource().getName()).isEqualTo(주안역.getName()),
                () -> assertThat(favoriteResponses.getTarget().getName()).isEqualTo(인천역.getName())
        );
    }

    @Test
    @DisplayName("즐겨찾기가 조회가 되지 않음")
    void noSearchFavorite() {
        //given
        when(favoriteRepository.findById(any())).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> favoriteService.searchFavorite(1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("즐겨찾기 항목이 없을경우 삭제가 되지 않는다.")
    void deleteFavorites() {
        when(favoriteRepository.findByMemberIdAndId(유저.getId(),1L)).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> favoriteService.deleteFavorite(유저.getId(), 1L))
                .isInstanceOf(NoSuchElementException.class);
    }
    
}