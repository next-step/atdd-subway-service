package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    FavoriteRepository favoriteRepository;

    @Mock
    StationRepository stationRepository;

    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("즐겨찾기 저장 테스트")
    void saveTest(){
        // 사용자 정보와 FavoriteRequest 를 통해 FavoriteResponse 를 return
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        FavoriteService favoriteService = new FavoriteService(favoriteRepository, stationRepository, memberRepository);

        when(stationRepository.findById(1L)).thenReturn(Optional.of(new Station("강남역")));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(new Station("잠실역")));
        when(favoriteRepository.save(any())).thenReturn(new Favorite(1L, new Station("강남역"), new Station("잠실역"), new Member(1L, "byunsw4@naver.com", "password", 30)));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(new Member(1L, "byunsw4@naver.com", "password123!", 30)));

        // when
        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(1L, favoriteRequest);

        // then
        assertThat(favoriteResponse).isNotNull();
        assertThat(favoriteResponse.getId()).isNotNull();
        assertThat(favoriteResponse.getSource().getName()).isEqualTo("강남역");
        assertThat(favoriteResponse.getTarget().getName()).isEqualTo("잠실역");
    }


    @Test
    @DisplayName("즐겨찾기 목록 테스트")
    void listTest(){
        // 사용자정보를 통해 해당 사용자의 즐겨찾기 목록을 조회한다.
        // given
        FavoriteService favoriteService = new FavoriteService(favoriteRepository, stationRepository, memberRepository);

        // when
        when(favoriteRepository.findAllByMember(any())).thenReturn(Arrays.asList(
                new Favorite(1L, new Station("강남역"), new Station("잠실역"), new Member("test@test.com", "password", 10)),
                new Favorite(2L, new Station("영등포역"), new Station("수원역"), new Member("test@test.com", "password", 10)),
                new Favorite(3L, new Station("시청역"), new Station("종로3가역"), new Member("test@test.com", "password", 10))
        ));
        when(memberRepository.findById(any())).thenReturn(Optional.of(new Member(1L, "test@test.com", "password", 10)));
        List<FavoriteResponse> favorites = favoriteService.findFavoriteAll(1L);

        // then
        assertThat(favorites).hasSize(3);
    }

}