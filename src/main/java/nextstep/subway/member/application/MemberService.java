package nextstep.subway.member.application;

import nextstep.subway.common.NotFoundException;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    private final FavoriteRepository favoriteRepository;

    public MemberService(MemberRepository memberRepository,
                         FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse findMember(Long id) {
        Member member = findMemberById(id);
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(Long id, MemberRequest param) {
        Member member = findMemberById(id);
        member.update(param.toMember());
    }

    @Transactional
    public void deleteMember(Long id) {
        favoriteRepository.deleteByMember(findMemberById(id));
        memberRepository.deleteById(id);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(email));
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }
}
