package nextstep.subway.favorite.application;

import static nextstep.subway.line.domain.StationFixture.강남역;
import static nextstep.subway.member.MemberFixture.AGE;
import static nextstep.subway.member.MemberFixture.EMAIL;
import static nextstep.subway.member.MemberFixture.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.domain.StationFixture;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Test
    void 즐겨찾기_저장() {
        //given
        when(memberRepository.findById(any()))
            .thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));
        when(stationRepository.findById(any()))
            .thenReturn(Optional.of(new Station()));
        when(favoriteRepository.save(any()))
            .thenReturn(new Favorite(1L, null, 강남역, StationFixture.역삼역));

        FavoriteService favoriteService = new FavoriteService(favoriteRepository, memberRepository,
            stationRepository);

        //when
        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(1L, new FavoriteRequest());

        //then
        assertThat(favoriteResponse.getId()).isEqualTo(1L);
    }
}