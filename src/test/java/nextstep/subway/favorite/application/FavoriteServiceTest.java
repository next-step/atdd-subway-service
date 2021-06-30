package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private MemberRepository memberRepository;

    private Station 교대역;
    private Station 양재역;
    private Favorite 즐겨찾기_교대역_양재역;
    private Member 멤버;
    private LoginMember 로그인_멤버;

    @BeforeEach
    void setUp() {
        멤버 = new Member(1L, "email", "password", 99);
        로그인_멤버 = new LoginMember(멤버.getId(), 멤버.getEmail(), 멤버.getAge());

        교대역 = new Station(1L, "교대역");
        양재역 = new Station(3L, "양재역");

        즐겨찾기_교대역_양재역 = new Favorite(1L, 멤버, 교대역, 양재역);
    }

    @Test
    @DisplayName("즐겨찾기를 저장한다.")
    void saveFavorite() {
        //given
        when(memberRepository.findById(멤버.getId())).thenReturn(Optional.of(멤버));
        when(stationRepository.findById(교대역.getId())).thenReturn(Optional.of(교대역));
        when(stationRepository.findById(양재역.getId())).thenReturn(Optional.of(양재역));
        when(favoriteRepository.save(any())).thenReturn(즐겨찾기_교대역_양재역);

        FavoriteService favoriteService = new FavoriteService(favoriteRepository, stationRepository, memberRepository);
        //when
        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(로그인_멤버, new FavoriteRequest(교대역.getId(), 양재역.getId()));
        //then
        assertThat(favoriteResponse).isNotNull();
    }

    @Test
    @DisplayName("즐겨찾기 목록을 가져온다.")
    void findFavorites() {
        //given
        when(memberRepository.findById(멤버.getId())).thenReturn(Optional.of(멤버));
        when(favoriteRepository.findByMember(any(Member.class))).thenReturn(Collections.singletonList(즐겨찾기_교대역_양재역));

        FavoriteService favoriteService = new FavoriteService(favoriteRepository, stationRepository, memberRepository);
        //when
        List<FavoriteResponse> favoriteResponseListBeforeCreate = favoriteService.findFavorites(로그인_멤버);
        //then
        assertThat(favoriteResponseListBeforeCreate.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("즐겨찾기를 삭제한다.")
    void deleteFavorite() {
        when(memberRepository.findById(멤버.getId())).thenReturn(Optional.of(멤버));
        when(stationRepository.findById(교대역.getId())).thenReturn(Optional.of(교대역));
        when(stationRepository.findById(양재역.getId())).thenReturn(Optional.of(양재역));
        when(favoriteRepository.save(any())).thenReturn(즐겨찾기_교대역_양재역);
        when(favoriteRepository.findById(즐겨찾기_교대역_양재역.getId())).thenReturn(Optional.of(즐겨찾기_교대역_양재역));
        //given
        FavoriteService favoriteService = new FavoriteService(favoriteRepository, stationRepository, memberRepository);
        favoriteService.saveFavorite(로그인_멤버, new FavoriteRequest(교대역.getId(), 양재역.getId()));
        //when && then
        assertThatNoException().isThrownBy(() -> favoriteService.deleteFavorite(로그인_멤버, 즐겨찾기_교대역_양재역.getId()));
    }
}