package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.infrastructure.InMemoryFavoriteRepository;
import nextstep.subway.member.application.MemberFinder;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.infrastructure.InMemoryMemberRepository;
import nextstep.subway.station.application.StationFinder;
import nextstep.subway.station.infrastructure.InMemoryStationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteServiceTest {
    private FavoriteService favoriteService;
    private Member member;
    private Member member2;
    private Long source;
    private Long target;

    @BeforeEach
    void setUp() {
        source = 1L;
        target = 2L;

        InMemoryMemberRepository memberRepository = new InMemoryMemberRepository();
        MemberFinder memberFinder = new MemberFinder(memberRepository);
        member = memberFinder.findById(1L);
        member2 = memberFinder.findById(2L);

        StationFinder stationFinder = new StationFinder(new InMemoryStationRepository());
        FavoriteRepository favoriteRepository = new InMemoryFavoriteRepository();
        favoriteService = new FavoriteService(memberFinder, stationFinder, favoriteRepository);
    }

    @Test
    void 즐겨찾기를_생성한다() {
        // given
        FavoriteRequest request = new FavoriteRequest(source, target);

        // when
        Long result = favoriteService.createFavorite(member.getId(), request);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    void 즐겨찾기_목록을_조회한다() {
        // when
        List<FavoriteResponse> result = favoriteService.findFavorites(member.getId());

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void 즐겨찾기를_삭제한다() {
        // when
        favoriteService.deleteFavorite(member.getId(), 1L);

        // then
        assertThatThrownBy(() ->
                favoriteService.findById(member.getId())
        ).isInstanceOf(NoSuchElementException.class)
                .hasMessage("즐겨찾기를 찾을 수 없습니다.");
    }

    @Test
    void 다른사람의_즐겨찾기는_삭제할_수_없다() {
        // when & then
        assertThatThrownBy(() ->
                favoriteService.deleteFavorite(member2.getId(), 1L)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("내 즐겨찾기가 아닙니다.");
    }
}
