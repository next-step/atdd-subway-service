package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.Excetion.MemberNotFoundException;
import nextstep.subway.common.Excetion.StationNotFoundException;
import nextstep.subway.favorite.domain.FavoriteSection;
import nextstep.subway.favorite.domain.FavoriteSectionRepository;
import nextstep.subway.favorite.domain.FavoriteSections;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {
    private FavoriteSectionRepository favoriteSectionRepository;
    private StationRepository stationRepository;
    private MemberRepository memberRepository;

    public FavoriteService(FavoriteSectionRepository favoriteSectionRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteSectionRepository = favoriteSectionRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public List<FavoriteResponse> findFavoriteSection(Long meId) {
        Member member = memberRepository.findById(meId).orElseThrow(() -> new MemberNotFoundException());
        FavoriteSections favoriteSections = member.getFavoriteSections();
        return favoriteSections.getFavoriteSections().stream()
                .map(favoriteSection -> FavoriteResponse.of(favoriteSection))
                .collect(Collectors.toList());
    }

    public void addFavoriteSection(Long meId, FavoriteRequest favoriteRequest) {
        Station sourceStation = findStationById(favoriteRequest.getSourceId());
        Station targetStation = findStationById(favoriteRequest.getTargetId());
        Member member = memberRepository.findById(meId).orElseThrow(() -> new MemberNotFoundException());
        member.addFavoriteSection(sourceStation, targetStation);
    }

    public void deleteFavoriteSection(Long meId, Long deleteFavoriteSectionId) {
        Member member = memberRepository.findById(meId).orElseThrow(() -> new MemberNotFoundException());
        FavoriteSection favoriteSection = favoriteSectionRepository.findByIdAndMemberId(deleteFavoriteSectionId, member.getId()).orElseThrow(RuntimeException::new);
        member.removeFavoriteSection(favoriteSection);
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new StationNotFoundException());
    }
}
