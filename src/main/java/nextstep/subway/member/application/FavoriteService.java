package nextstep.subway.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.member.domain.Favorite;
import nextstep.subway.member.domain.FavoriteRepository;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.FavoriteRequest;
import nextstep.subway.member.dto.FavoriteResponse;
import nextstep.subway.member.exception.MemberNotFoundException;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.Path;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteService {
    private final PathService pathService;
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public FavoriteResponse addFavorite(Long id, FavoriteRequest request) {
        Member member = findMemberById(id);
        member.addFavorite(getFavorite(request));
        memberRepository.save(member);
        final Favorite persistFavorite = favoriteRepository.findFavorite(request.getSource(), request.getTarget(), id);
        return FavoriteResponse.of(persistFavorite);
    }

    @Transactional
    public void deleteFavorite(Long memberId, Long favoriteId) {
        Member member = findMemberById(memberId);
        member.removeFavorite(favoriteId);
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Favorite getFavorite(FavoriteRequest request) {
        Path path = pathService.getShortestPath(request);
        return Favorite.of(path);
    }

    public List<FavoriteResponse> findFavorites(Long id) {
        Member member = findMemberById(id);
        return FavoriteResponse.ofList(member.getFavorites());
    }
}
