package nextstep.subway.favorite.application;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.FavoriteSection;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @InjectMocks
    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private MemberRepository memberRepository;

    private final Station sourceStation = new Station("source");
    private final Station targetStation = new Station("target");
    private final LoginMember loginMember = new LoginMember(1L, "email", 20);
    private final Member member = new Member();

    @DisplayName("저장 기능 테스트")
    @Test
    void create() {
        // given
        final FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        final FavoriteSection favoriteSection = new FavoriteSection(member, sourceStation, targetStation);
        when(memberRepository.findById(loginMember.getId())).thenReturn(Optional.of(member));
        when(stationRepository.findById(favoriteRequest.getSourceId())).thenReturn(Optional.of(sourceStation));
        when(stationRepository.findById(favoriteRequest.getTargetId())).thenReturn(Optional.of(targetStation));
        when(favoriteRepository.save(any(FavoriteSection.class))).thenReturn(favoriteSection);

        // when
        favoriteService.createFavoriteSection(loginMember, favoriteRequest);

        // then
        verify(favoriteRepository).save(any(FavoriteSection.class));
    }

    @DisplayName("목록 조회 기능 테스트")
    @Test
    void findAllByMemberId() {
        // given
        when(memberRepository.findById(loginMember.getId())).thenReturn(Optional.of(member));
        when(favoriteRepository.findAllByMemberId(loginMember.getId())).thenReturn(anyList());

        // when
        favoriteService.findFavoriteSectionsByMember(loginMember);

        // then
        verify(favoriteRepository).findAllByMemberId(loginMember.getId());
    }

    @DisplayName("삭제 기능 테스트")
    @Test
    void delete() {
        // given
        final Long id = 1L;
        final FavoriteSection favoriteSection = new FavoriteSection(member, sourceStation, targetStation);
        when(memberRepository.findById(loginMember.getId())).thenReturn(Optional.of(member));
        when(favoriteRepository.findById(id)).thenReturn(Optional.of(favoriteSection));

        // when
        favoriteService.deleteFavoriteSection(loginMember, id);

        // then
        verify(favoriteRepository).deleteById(id);
    }
}
