package nextstep.subway.favorite.application;

import static nextstep.subway.station.domain.StationFixtures.광화문역;
import static nextstep.subway.station.domain.StationFixtures.서대문역;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("즐겨찾기 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private MemberService memberService;
    @Mock
    private StationService stationService;
    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    private static Member member = new Member(1L, "jhh992000@gmail.com", "1234", 39);
    private static Favorite favorite = new Favorite(1L, member, 서대문역, 광화문역);


    @DisplayName("즐겨찾기 생성")
    @Test
    void createFavorite() {

        // given
        given(memberService.findById(any())).willReturn(member);

        Map<Long, Station> stationMap = new HashMap<>();
        stationMap.put(서대문역.getId(), 서대문역);
        stationMap.put(광화문역.getId(), 광화문역);
        given(stationService.findMapByIds(서대문역.getId(), 광화문역.getId())).willReturn(stationMap);

        given(favoriteRepository.save(any())).willReturn(favorite);

        // when
        LoginMember loginMember = new LoginMember(member.getId(), member.getEmail(), member.getAge());
        FavoriteRequest favoriteRequest = new FavoriteRequest(서대문역.getId(), 광화문역.getId());
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(loginMember, favoriteRequest);

        // then
        assertThat(favoriteResponse.getId()).isNotNull();
        assertThat(favoriteResponse.getSource().getId()).isEqualTo(서대문역.getId());
        assertThat(favoriteResponse.getTarget().getId()).isEqualTo(광화문역.getId());
    }

    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void findByMemberId() {
        // given
        List<Favorite> favorites = new ArrayList<>(Collections.singletonList(new Favorite(member, 서대문역, 광화문역)));
        given(favoriteRepository.findByMemberId(any())).willReturn(favorites);

        // when
        List<FavoriteResponse> memberFavorites = favoriteService.findByMemberId(member.getId());

        // then
        assertThat(memberFavorites).hasSize(1);
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        // given
        given(favoriteRepository.findById(any())).willReturn(Optional.of(favorite));

        // when
        favoriteService.deleteFavorite(member.getId(), favorite.getId());

        // then
        assertThat(favoriteService.findByMemberId(member.getId())).isEmpty();
    }

}
