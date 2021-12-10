package nextstep.subway.favorite.application;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import nextstep.subway.auth.domain.*;
import nextstep.subway.common.exception.*;
import nextstep.subway.favorite.domain.*;
import nextstep.subway.favorite.dto.*;
import nextstep.subway.member.domain.*;

@Service
@Transactional
public class FavoriteService {
    private static final String FAVORITE = "즐겨찾기";

    private final FavoriteReadService favoriteReadService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteReadService favoriteReadService, FavoriteRepository favoriteRepository) {
        this.favoriteReadService = favoriteReadService;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse saveFavorite(LoginMember member, FavoriteRequest favoriteRequest) {
        if (favoriteRequest.getSource().equals(favoriteRequest.getTarget())) {
            throw new CannotAddException(FAVORITE);
        }

        Favorite favorite = favoriteRepository.save(
            Favorite.of(
                Member.of(member.getId(), member.getEmail(), member.getAge()),
                favoriteReadService.findStationById(favoriteRequest.getSource()),
                favoriteReadService.findStationById(favoriteRequest.getTarget())
            )
        );

        return FavoriteResponse.from(favorite);
    }

    public void deleteFavorite(Long favoriteId, Long loginMemberId) {
        Favorite favorite = favoriteRepository.findByIdAndMemberId(favoriteId, loginMemberId)
            .orElseThrow(() -> new NotFoundException(FAVORITE));
        favoriteRepository.delete(favorite);
    }
}
