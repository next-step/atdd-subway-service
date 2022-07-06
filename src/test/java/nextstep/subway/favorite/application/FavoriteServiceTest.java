package nextstep.subway.favorite.application;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.IllegalArgumentException;
import nextstep.subway.exception.NoSuchElementFoundException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private MemberRepository memberRepository;

    private Station 강남역;
    private Station 남부터미널역;

    private Member member;
    private LoginMember loginMember;

    @BeforeEach
    void init() {
        favoriteService = new FavoriteService(favoriteRepository, stationRepository, memberRepository);

        강남역 = new Station("강남역");
        남부터미널역 = new Station("남부터미널역");

        member = new Member(EMAIL, PASSWORD, AGE);

        loginMember = new LoginMember(1L, EMAIL, AGE);
    }

    @Test
    void 즐겨찾기_생성() {
        //given
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(member));
        when(stationRepository.findById(1L)).thenReturn(Optional.ofNullable(강남역));
        when(stationRepository.findById(4L)).thenReturn(Optional.ofNullable(남부터미널역));
        Favorite 강남_남부_즐겨찾기 = new Favorite(member, 강남역, 남부터미널역);
        when(favoriteRepository.save(any())).thenReturn(강남_남부_즐겨찾기);

        //when
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(loginMember, new FavoriteRequest(1L, 4L));

        //then
        assertThat(favoriteResponse.getSource().getName()).isEqualTo("강남역");
        assertThat(favoriteResponse.getTarget().getName()).isEqualTo("남부터미널역");
    }

    @Test
    void 출발역과_도착역이_같은_경우_즐겨찾기_생성시_에러() {
        //given
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(member));
        when(stationRepository.findById(1L)).thenReturn(Optional.ofNullable(강남역));

        //when then
        assertThrows(IllegalArgumentException.class, () -> {
           favoriteService.createFavorite(loginMember, new FavoriteRequest(1L, 1L));
        });
    }

    @Test
    void 존재하지_않은_역_즐겨찾기_생성시_에러() {
        assertThrows(NoSuchElementFoundException.class, () -> {
            favoriteService.createFavorite(loginMember, new FavoriteRequest(1L, 5L));
        });
    }

    @Test
    void 즐겨찾기_목록_조회() {
        //given
        Favorite 강남_남부_즐겨찾기 = new Favorite(member, 강남역, 남부터미널역);
        given(favoriteRepository.findByMember(any())).willReturn(Arrays.asList(강남_남부_즐겨찾기));

        //when
        List<FavoriteResponse> responses = favoriteService.findFavorites(loginMember);

        assertThat(responses.size()).isEqualTo(1);
    }
}
