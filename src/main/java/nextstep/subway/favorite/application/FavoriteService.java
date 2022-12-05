package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteCreatedRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private MemberRepository memberRepository;
    private StationRepository stationRepository;
    private FavoriteRepository favoriteRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository, FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse create(Long memberId, FavoriteCreatedRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        Station source = stationRepository.findById(request.getSourceId()).orElseThrow(EntityNotFoundException::new);
        Station target = stationRepository.findById(request.getTargetId()).orElseThrow(EntityNotFoundException::new);
        Favorite favorite = favoriteRepository.save(new Favorite(member, source, target));
        return new FavoriteResponse(favorite);
    }

    public List<FavoriteResponse> findAll() {
        return favoriteRepository.findAll().stream()
                .map(FavoriteResponse::new)
                .collect(Collectors.toList());
    }
}
