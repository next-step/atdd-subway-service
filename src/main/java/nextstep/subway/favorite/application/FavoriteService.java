package nextstep.subway.favorite.application;

import static nextstep.subway.member.application.MemberService.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private StationService stationService;

    public FavoriteService(final FavoriteRepository favoriteRepository, final MemberRepository memberRepository,
        final StationService stationService
    ) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse saveFavorite(final Long memberId, final FavoriteRequest favoriteRequest) {
        final Station source = stationService.findById(favoriteRequest.getSource());
        final Station target = stationService.findById(favoriteRequest.getTarget());
        final Member member = getMemberById(memberId);

        final Favorite favorite = new Favorite(source, target, member);
        favoriteRepository.save(favorite);

        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findAllFavoritesByMemberId(final Long memberId) {
        final Member member = getMemberById(memberId);

        return member.getFavorites().stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toList());
    }

    public void deleteFavoriteById(final Long memberId, Long id) {
        final Favorite favorite = favoriteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("즐겨찾기를 찾을 수 없습니다."));
        if (!favorite.isOwnedBy(memberId)) {
            throw new BadRequestException("즐겨찾기에 대한 권한이 없습니다.");
        }

        favoriteRepository.deleteById(id);
    }

    private Member getMemberById(final Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(NOT_FOUND_ERR_MSG));
    }
}
