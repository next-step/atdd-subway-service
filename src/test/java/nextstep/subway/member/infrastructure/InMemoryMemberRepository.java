package nextstep.subway.member.infrastructure;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static nextstep.subway.member.MemberAcceptanceTest.*;

public class InMemoryMemberRepository implements MemberRepository {
    private final Map<Long, Member> elements = new HashMap<>();
    private long memberId = 0L;

    public InMemoryMemberRepository() {
        save(new Member(EMAIL, PASSWORD, AGE));
        save(new Member(NEW_EMAIL, NEW_PASSWORD, NEW_AGE));
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return elements.values()
                .stream()
                .filter(member -> StringUtils.equals(member.getEmail(), email))
                .findFirst();
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(elements.get(id));
    }

    @Override
    public Member save(Member member) {
        ReflectionTestUtils.setField(member, "id", ++memberId);
        elements.put(memberId, member);
        return member;
    }

    @Override
    public void deleteById(Long id) {
        elements.remove(id);
    }
}
