# SonarQube Fix Result

Branch: `fix/sonarqube-warnings`

## SonarQube

- Project: `folksflow-backend`
- Initial open issues read from SonarQube: `76`
- Final open issues after reanalysis: `0`
- Final Compute Engine task status: `SUCCESS`
- Final dashboard: `http://localhost:9000/dashboard?id=folksflow-backend`

## Changes Made

- Renamed DTO package from `model.DTO` to `model.dto`.
- Replaced generic thrown `RuntimeException` usages with `BusinessException`.
- Added `BusinessException` as the application-specific runtime exception.
- Replaced `Stream.collect(Collectors.toList())` with `Stream.toList()`.
- Removed direct `System.out` logging and used Lombok `Slf4j`.
- Removed hard-coded seeded admin password; it now uses `app.seed.admin.password` or a generated UUID fallback.
- Added private constructors to utility specification classes.
- Removed unused imports and commented-out repository code.
- Replaced duplicated not-found string literals with constants.
- Changed field injection in `SecurityConfig` to method parameter injection.
- Added assertions to context smoke tests.
- Replaced boxed `Boolean` direct usage in refresh-token validation with `Boolean.TRUE.equals(...)`.

## Verification

- `.\mvnw.cmd -DskipTests compile test-compile`: `BUILD SUCCESS`
- Sonar analysis command:
  - `.\mvnw.cmd -DskipTests sonar:sonar '-Dsonar.host.url=http://localhost:9000' '-Dsonar.projectKey=folksflow-backend' '-Dsonar.login=***'`
  - Result: `BUILD SUCCESS`
- Sonar API confirmation:
  - `issues_total=0`

## Notes

- Full `clean test` previously timed out while running Spring tests in this local environment, so compile and test-compile were used for verification.
- The repository already had pre-existing local changes in `docker-compose.yml`, `pom.xml`, and an untracked `hs_err_pid22144.log`; they were not intentionally reverted.
