package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;


@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationService stationService;

    @Mock
    private MemberService memberService;

    private FavoriteService favoriteService;

    private Station 강남역;
    private Station 양재역;
    private Member 회원;
    private LoginMember 로그인유저;
    private Favorite 강남_양재_즐겨찾기;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        양재역 = new Station(2L, "양재역");
        회원 = new Member(1L, "ehdgml3206@gmail.com", "1234", 31);
        로그인유저 = new LoginMember(회원.getId(), 회원.getEmail(), 회원.getAge());
        강남_양재_즐겨찾기 = Favorite.of(1L, 회원, 강남역, 양재역);

        favoriteService = new FavoriteService(favoriteRepository, memberService, stationService);
    }

    @Test
    @DisplayName("즐겨찾기 생성")
    void createFavorite() {
        // when
        when(memberService.findById(회원.getId())).thenReturn(회원);
        when(stationService.findById(강남역.getId())).thenReturn(강남역);
        when(stationService.findById(양재역.getId())).thenReturn(양재역);
        when(favoriteRepository.save(any())).thenReturn(강남_양재_즐겨찾기);
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(로그인유저, new FavoriteRequest(강남역.getId(), 양재역.getId()));

        // then
        assertThat(favoriteResponse).isNotNull();
        assertThat(favoriteResponse.getId()).isEqualTo(강남_양재_즐겨찾기.getId());
    }

    @Test
    @DisplayName("즐겨찾기 목록 조회")
    void findFavorites() {
        // when
        when(favoriteRepository.findAllByMemberId(로그인유저.getId())).thenReturn(Arrays.asList(강남_양재_즐겨찾기));
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(로그인유저);

        // then
        assertThat(favoriteResponses).isNotNull();
        assertThat(favoriteResponses.size()).isEqualTo(1);
        assertThat(favoriteResponses.get(0).getId()).isEqualTo(강남_양재_즐겨찾기.getId());
    }

    @Test
    @DisplayName("즐겨찾기 삭제")
    void deleteFavorite() {
        // when
        when(favoriteRepository.findById(강남_양재_즐겨찾기.getId())).thenReturn(java.util.Optional.ofNullable(강남_양재_즐겨찾기));
        favoriteService.deleteFavorite(로그인유저, 강남_양재_즐겨찾기.getId());

        // then
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(로그인유저);
        assertThat(favoriteResponses.size()).isEqualTo(0);
    }
}

