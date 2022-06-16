package nextstep.subway.favorite.application;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class FavoriteServiceTest {
    @MockBean
    MemberService memberService;

    @MockBean
    FavoriteRepository favoriteRepository;

    @MockBean
    StationService stationService;

    private FavoriteService favoriteService;

    private Long memberId;
    private Member member;
    private Long sourceId;
    private Long targetId;
    private Long favoriteId;
    private Station source;
    private Station target;
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(memberService, stationService, favoriteRepository);
        memberId = 1L;
        member = new Member(EMAIL, PASSWORD, AGE);
        sourceId = 1L;
        targetId = 2L;
        source = new Station("합정역");
        target = new Station("당산역");
        favoriteId = 1L;
        favorite = new Favorite(member, source, target);
    }

    @Test
    @DisplayName("즐겨찾기를 등록한다.")
    void createFavorite() {
        //given
        when(memberService.findMemberById(memberId)).thenReturn(member);
        when(stationService.findById(sourceId)).thenReturn(source);
        when(stationService.findById(targetId)).thenReturn(target);
        when(favoriteRepository.save(any())).thenReturn(favorite);

        //when
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceId, targetId);
        FavoriteResponse actual = favoriteService.saveFavorite(memberId, favoriteRequest);

        //then
        assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("즐겨찾기 목록을 조회한다.")
    void findAllFavorites() {
        //given
        Favorite otherFavorite = new Favorite(member, new Station("공덕역"), new Station("마포역"));
        List<Favorite> favorites = Arrays.asList(favorite, otherFavorite);
        when(favoriteRepository.findAllByMemberId(memberId)).thenReturn(favorites);

        //when
        List<FavoriteResponse> actual = favoriteService.findFavorites(memberId);

        //then
        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("즐겨찾기를 삭제한다.")
    void deleteFavorite() {
        //when
        favoriteService.deleteFavorite(memberId, favoriteId);

        //then
        verify(favoriteRepository, times(1)).deleteByMemberIdAndId(memberId, favoriteId);
    }
}