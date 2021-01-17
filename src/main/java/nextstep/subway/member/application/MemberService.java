package nextstep.subway.member.application;

import nextstep.subway.advice.exception.MemberBadRequestException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService {

    private StationService stationService;
    private MemberRepository memberRepository;

    public MemberService(final StationService stationService, final MemberRepository memberRepository) {
        this.stationService = stationService;
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = getMember(id);
        return MemberResponse.of(member);
    }

    public List<Favorite> findFavorites(Long id) {
        Member member = getMember(id);
        return member.getFavorites();
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = getMember(id);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public FavoriteResponse addFavorite(Long id, FavoriteRequest request) {
        Member member = getMember(id);
        Station sourceStation = stationService.findById(request.getSourceStationId());
        Station targetStation = stationService.findById(request.getTargetStationId());
        Favorite favorite = new Favorite(member, sourceStation, targetStation);
        member.addFavorite(favorite);
        return FavoriteResponse.of(favorite);
    }

    public void deleteFavorite(Long memberId, Favorite favorite) {
        Member member = getMember(memberId);
        member.removeFavorite(favorite);
    }

    private Member getMember(Long id) {
        return memberRepository.findById(id).orElseThrow(()-> new MemberBadRequestException("존재하지 않는 회원입니다", id));
    }

}
