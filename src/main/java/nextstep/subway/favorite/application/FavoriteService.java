package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;

import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {

    private static final String CAN_NOT_DELETE_EXCEPTION = "사용자가 달라 삭제할 수 없습니다.";

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public FavoriteResponse addFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Station source = findStationById(favoriteRequest.getSourceId());
        Station target = findStationById(favoriteRequest.getTargetId());
        Member member = findMemberById(memberId);

        Favorite favorite = new Favorite(member, source.getId(), target.getId());
        favorite.validateDuplicate(favoriteRepository.findAll());
        favoriteRepository.save(favorite);

        return new FavoriteResponse(favorite.getId(), source, target);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> retrieveFavorites(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(memberId);

        return favorites.stream()
                .map(favorite -> new FavoriteResponse(favorite.getId(), findStationById(favorite.getSourceId()), findStationById(favorite.getTargetId())))
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        Member member = findMemberById(memberId);
        Favorite favorite = favoriteRepository.findById(favoriteId).get();

        if (!favorite.getMember().equals(member)) {
            throw new InvalidDataException(CAN_NOT_DELETE_EXCEPTION);
        }
        favoriteRepository.delete(favorite);
    }

    private Station findStationById(Long stationId) {
        return stationService.findById(stationId);
    }

    private Member findMemberById(Long memberId) {
        return memberService.findMemberById(memberId);
    }
}
