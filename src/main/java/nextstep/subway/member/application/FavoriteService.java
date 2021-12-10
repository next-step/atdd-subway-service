package nextstep.subway.member.application;

import java.util.List;
import nextstep.subway.member.domain.Favorite;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberFavoriteCollection;
import nextstep.subway.member.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.infrastructure.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteService {

    private final StationRepository stationRepository;
    private final MemberService memberService;

    public FavoriteService(StationRepository stationRepository, MemberService memberService) {
        this.stationRepository = stationRepository;
        this.memberService = memberService;
    }

    public void saveFavorite(Long memberId, Long sourceId, Long targetId) {
        Member member = memberService.findMemberById(memberId);

        member.addFavorite(Favorite.of(sourceId, targetId));
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        Member member = memberService.findMemberById(memberId);

        member.removeFavoriteById(favoriteId);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> getFavorite(Long memberId) {
        Member member = memberService.findMemberById(memberId);

        List<Station> stations = this.stationRepository.findAllById(member.getFavoriteStationIds());

        MemberFavoriteCollection favoriteCollection = MemberFavoriteCollection.of(member, stations);

        return favoriteCollection.toFavoriteResponses();
    }

}
