package nextstep.subway.fare.domain.age;

import java.util.function.Predicate;

public interface AgePredicate extends Predicate<Integer> {
    default boolean includeAge(int age) {
        return test(age);
    }
}
