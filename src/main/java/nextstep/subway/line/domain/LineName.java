package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LineName {
    @Column(name = "name", unique = true)
    private String value;

    protected LineName() {
    }

    public LineName(String value) {
        validate(value);
        this.value = value;
    }

    public static LineName from(String name) {
        return new LineName(name);
    }

    private void validate(String name) {
        validateNonNull(name);
        validateEmptyString(name);
    }

    private void validateNonNull(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("노선 이름은 필수값 입니다.");
        }
    }

    private void validateEmptyString(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("노선 이름이 빈 문자열입니다.");
        }
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineName lineName = (LineName) o;
        return Objects.equals(value, lineName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
