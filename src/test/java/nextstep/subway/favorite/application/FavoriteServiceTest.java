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
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Mock Extention 이용 테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    @Mock
    FavoriteRepository favoritRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    StationRepository stationRepository;

    @Test
    @DisplayName("즐겨찾기 추가")
    void createFavorite() {
        // 1.생성된 즐겨찾기 정보를 이용해 favoriteResponse를 응답한다.
        // 2.favoriteRequest로 넘어온 정보를 가지고 디비에 즐겨찾기를 생성한다.

        //given
        FavoriteService favoriteService = new FavoriteService(favoritRepository, memberRepository, stationRepository);
        LoginMember user = new LoginMember(1L, "test@test.com", 40);
        FavoriteRequest favoriteRequest = new FavoriteRequest(user, 1L, 4L);
        // FavoriteRepository favoritRepository = mock(FavoriteRepository.class);
        when(memberRepository.findById(any())).thenReturn(java.util.Optional.of(new Member(1L, "test@test.com", "1111", 40)));
        when(stationRepository.findById(favoriteRequest.getSource())).thenReturn(java.util.Optional.of(new Station(1L, "강남역")));
        when(stationRepository.findById(favoriteRequest.getTarget())).thenReturn(java.util.Optional.of(new Station(4L, "남부터미널역")));
        when(favoritRepository.save(any())).thenReturn(new Favorite(new Member(1L, "test@test.com", "1111", 40), new Station("강남역"), new Station("남부터미널역")));

        //when
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(favoriteRequest);

        //then
        assertThat(favoriteResponse).isNotNull();
        assertThat(favoriteResponse.getId()).isNotNull();
    }

    @Test
    @DisplayName("즐겨찾기 목록 조회")
    void findFavorite() {
        //생성된 즐겨찾기를 조회한다.

        //given
        FavoriteService favoriteService = new FavoriteService(favoritRepository, memberRepository, stationRepository);
        when(favoritRepository.findAll())
                .thenReturn(Lists.newArrayList(new Favorite(new Member(2L, "test@test.com", "1111", 40),
                        new Station(1L, "강남역"),
                        new Station(4L, "남부터미널역"))));

        //when
        List<Favorite> favorite = favoriteService.findFavorite();

        assertThat(favorite.size()).isEqualTo(1);

    }
}