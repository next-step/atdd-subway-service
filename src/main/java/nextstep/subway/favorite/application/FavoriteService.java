package nextstep.subway.favorite.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.favorite.exception.FavoriteDuplicatedException;
import nextstep.subway.favorite.exception.FavoriteNotFoundException;
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
        final Favorite favorite = getFavorite(request).by(findMemberById(id));
        validateDuplicate(request, id);
        favoriteRepository.save(favorite);
        return FavoriteResponse.of(favorite);
    }

    @Transactional
    public void deleteFavorite(Long favoriteId) {
        favoriteRepository.delete(
                favoriteRepository.findById(favoriteId)
                        .orElseThrow(FavoriteNotFoundException::new)
        );
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
        return FavoriteResponse.ofList(favoriteRepository.findByMemberId(member.getId()));
    }

    private void validateDuplicate(FavoriteRequest request, Long id) {
        if (favoriteRepository.findFavorite(request.getSource(), request.getTarget(), id).isPresent()) {
            throw new FavoriteDuplicatedException();
        }
    }
}
