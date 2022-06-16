package nextstep.subway.favorite.application;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(memberService, stationService, favoriteRepository);
    }

    @Test
    @DisplayName("즐겨찾기를 등록한다.")
    void createFavorite() {
        //given
        Long memberId = 1L;
        Member member = new Member(EMAIL, PASSWORD, AGE);
        when(memberService.findMemberById(memberId)).thenReturn(member);

        Long sourceId = 1L;
        Long targetId = 2L;
        Station source = new Station("합정역");
        Station target = new Station("당산역");
        when(stationService.findById(sourceId)).thenReturn(source);
        when(stationService.findById(targetId)).thenReturn(target);

        Favorite favorite = new Favorite(member, source, target);
        when(favoriteRepository.save(any())).thenReturn(favorite);

        //when
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceId, targetId);
        FavoriteResponse actual = favoriteService.saveFavorite(memberId, favoriteRequest);

        assertThat(actual).isNotNull();
    }
}