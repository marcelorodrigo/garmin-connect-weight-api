# Garmin Connect Weight API Agent Guide

A Spring Boot 4.0+ microservice: `POST /weight` with body metrics JSON → Garmin `.FIT` binary file.

## Build & Test

```bash
./mvnw clean verify                     # full build + tests + coverage (JaCoCo)
./mvnw verify -Dmaven.test.skip=true    # build only
mvn -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar  # CI SonarCloud
```

## Key Architecture

- **Controller** (`gateway/controller/WeightController.java`): `@WebMvcTest`, `MockMvc`, `@MockitoBean`
- **Domain** (`domain/WeightMeasurement.java`): Java record with `@Builder`, optional fields are boxed types (`Float`, `Integer`)
- **Service** (`fit/FitWeight.java`): orchestrates GenerateFileIdMesg → GenerateWeightScaleMesg → FitFileCreator
- **FitFileCreator** (`fit/FitFileCreator.java`): `FileEncoder` writes to temp file (`Files.createTempFile`), protocol V2.0
- **Config** (`config/`): `Clock` + `ZoneId` beans (testable timestamps), `RestClient` bean (not `RestTemplate`)
- **KeepAlive** (`scheduler/KeepAliveConfiguration.java`): `@Scheduled(cron = "0 */5 6-9 * * ?", zone = "Europe/Amsterdam")` — Render.com cold-start prevention, uses `RestClient`

## Testing Conventions

- `@ExtendWith(MockitoExtension.class)` + `@InjectMocks`/`@Mock` for service/unit tests (no Spring context)
- `@WebMvcTest(WeightController.class)` + `@MockitoBean` for controller slice tests
- `@TempDir` for temp file assertions
- `FitFileCreatorTest` performs round-trip decode with `Decode`/`MesgBroadcaster` to verify FIT binary validity
- `KeepAliveConfigurationTest` verifies both HTTP errors and connection errors are caught (logged, not thrown)

## Project-Specific Conventions

- **Immutability**: Java records for domain (`WeightMeasurement`) and config (`KeepAliveProperties`)
- **Lombok**: `@Slf4j`, `@RequiredArgsConstructor`, `@Builder`
- **Logging**: `log.atInfo().log(...)` (SLF4J 2 fluent API) and `log.error(...)`
- **Virtual threads**: enabled via `spring.threads.virtual.enabled: true`
- **Lazy initialization**: `spring.main.lazy-initialization: true`
- **FIT SDK**: `com.garmin:fit:21.205.0` — `FileIdMesg`, `WeightScaleMesg`, `FileEncoder`, `File.WEIGHT`, `Manufacturer.GARMIN`
- **CI**: GitHub Actions (`build.yml`) — push/PR to master, Temurin JDK 25, Maven cache, SonarCloud
- **Dependabot**: weekly, Tue mornings CET for maven + GitHub Actions + Docker
- **Docker**: multi-stage — `eclipse-temurin:25.0.2_10-jdk-alpine` (build), `gcr.io/distroless/java25-debian13:nonroot` (runtime)

## Adding a Measurement Field

1. Add field to `WeightMeasurement` record (boxed type for optional fields)
2. Set it in `GenerateWeightScaleMesg.execute()` with a null guard
3. Update `doc/openapi.yml` and `doc/json-schema.json`
4. Sanity-check the controller test fields still match

## Doc Schema Quirks

`doc/openapi.yml` is hand-written (not auto-generated) and **lacks `timestamp`** — the field exists in code and controller test but is missing from both OpenAPI and JSON schema specs. Keep both in sync when changing fields.
