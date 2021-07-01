package nextstep.subway.auth.domain;

public interface UserDetails {
    int getAge();

    String getEmail();

    UserDetails getUserDetails();
}
