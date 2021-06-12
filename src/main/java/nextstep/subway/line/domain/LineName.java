package nextstep.subway.line.domain;

public class LineName {
    private String name;

    public LineName(String name) {
        validate(name);

        this.name = name;
    }

    private void validate(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("이름은 공백이거나 공백이면 안됩니다");
        }
    }
}
