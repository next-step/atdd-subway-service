package nextstep.subway.auth.domain;

public enum Role {
  GUEST("ROLE_GUEST"),
  USER("ROLE_USER");

  private final String role;

  Role(String role) {
    this.role = role;
  }

  public String getRole() {
    return role;
  }
}
