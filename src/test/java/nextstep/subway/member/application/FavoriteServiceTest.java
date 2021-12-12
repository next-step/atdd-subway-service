package nextstep.subway.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import nextstep.subway.member.domain.Favorite;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("FavoriteServiceTest 단위 테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private StationService stationService;

    @Mock
    private MemberService memberService;

    private FavoriteService favoriteService;

    private Member member;
    private final Long MEMBER_ID = 1L;
    private final Long SOURCE_ID = 1L;
    private final Long TARGET_ID = 2L;

    @BeforeEach
    void setUp() {
        // given
        favoriteService = new FavoriteService(stationService, memberService);

        member = new Member("email@email.com", "password", 10);

        when(memberService.findMemberById(any())).thenReturn(member);
    }

    @Test
    @DisplayName("좋아요 저장 후 좋아요 검증")
    void getFavorite() {
        // when
        favoriteService.saveFavorite(MEMBER_ID, SOURCE_ID, TARGET_ID);
        List<Favorite> favorites = member.getFavorites();

        // then
        assertThat(favorites).extracting("stationSourceId").containsExactly(SOURCE_ID);
        assertThat(favorites).extracting("stationTargetId").containsExactly(TARGET_ID);
    }

    @Test
    @DisplayName("좋아요 삭제 검증")
    void removeFavorite() {
        // when
        favoriteService.saveFavorite(MEMBER_ID, SOURCE_ID, TARGET_ID);
        List<Favorite> favorites = member.getFavorites();
        // then
        assertThat(favorites).hasSize(1);

        // when
        favoriteService.deleteFavorite(MEMBER_ID, favorites.get(0).getId());
        // then
        assertThat(favorites).isEmpty();
    }
}
