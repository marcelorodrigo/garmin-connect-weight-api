# AI Agent Guidelines for Garmin Connect Weight API

## Project Overview
A **Spring Boot 4.0+ microservice** that generates Garmin `.FIT` files from body measurement data. The API accepts weight and health metrics (body fat, hydration, muscle mass, etc.) and returns a downloadable binary FIT file compatible with Garmin Connect.

**Key characteristics:**
- Single endpoint: `POST /weight` with JSON request body → FIT binary file response
- Java 25 with records, virtual threads (lightweight concurrency)
- Minimal dependencies: Spring Boot WebMVC, Garmin FIT SDK, Lombok, SpringDoc OpenAPI
- Deployed to Render.com with keep-alive scheduler to prevent cold starts

## Architecture & Data Flow

### Component Layers
1. **Controller** (`WeightController.java:38`): REST endpoint, request validation, file attachment response
2. **Domain** (`WeightMeasurement.java`): Record (immutable data structure) with optional body metrics
3. **Service** (`FitWeight.java`): Orchestrates FIT file generation pipeline
4. **FIT Generation**:
   - `GenerateFileIdMesg`: Creates FIT file header with timestamp
   - `GenerateWeightScaleMesg`: Converts body metrics to FIT weight scale message
   - `FitFileCreator`: Writes messages to binary FIT file using Garmin SDK

### Key Design Patterns
- **Records over classes**: Immutable data structures (JDK 14+) for `WeightMeasurement` and `KeepAliveProperties`
- **Dependency Injection**: Spring components (`@Component`, `@Service`, `@RequiredArgsConstructor`)
- **Clock abstraction**: Injectable `Clock` bean for testable timestamp handling
- **Temporary file cleanup**: Files created in system temp directory; relies on OS cleanup

## Critical Developer Workflows

### Build & Test
```bash
./mvnw clean verify               # Full build with tests and code coverage
./mvnw verify -Dmaven.test.skip=true  # Build without tests
./mvnw org.sonarsource.scanner.maven:sonar-maven-plugin:sonar  # SonarCloud analysis
```

**Test conventions:**
- `@WebMvcTest(WeightController.class)` for controller isolation
- Mock `FitWeight` and `Clock` beans; use `@TempDir` for temp files
- See `WeightControllerTest.java:25` for testing patterns

### Docker Build
Uses multi-stage build (lines 1-20 in Dockerfile):
1. Downloads Maven dependencies first (caching optimization)
2. Copies source and builds JAR
3. Alpine base with distroless runtime (nonroot user, Java 25)

### Development Environment Setup
- Java 25 required (see `.sdkmanrc`)
- Maven via provided wrapper (`mvnw`)
- Project structure: `src/main/java/com/marcelorodrigo/fit/{domain,fit,gateway,config,scheduler}`

## Project-Specific Conventions

### Configuration Management
- **YAML-based** (`application.yml`): Server port, app name, thread settings
- **ConfigurationProperties** for typed config: `KeepAliveProperties` (prefix `keep-alive`)
- **Environment overrides**: `${ENV_VAR:default}` syntax (e.g., `RENDER_EXTERNAL_URL`, `PORT`)

### Scheduling & Background Tasks
- **Cron trigger** (`KeepAliveConfiguration.java:19`): `0 */5 6-9 * * ?` → every 5 min, 6-9 AM CET
- Prevents Render.com free tier cold starts; catches and logs errors
- Uses injected `RestTemplate` and config properties

### Logging & Monitoring
- **SLF4J via Lombok** (`@Slf4j`): `log.atInfo().log()` and `log.error()` patterns
- **OpenAPI/Swagger**: Auto-generated docs via SpringDoc (`springdoc-openapi-starter-webmvc-ui`)
- **Code quality**: SonarCloud integration on push/PRs to master

### Response Handling
- FIT files returned as binary attachments: `Content-Disposition: attachment; filename=weight-{timestamp}.fit`
- Content-Type: `application/octet-stream`
- Status codes: 200 (success), 400 (validation errors via `@Valid`)

## Integration Points & External Dependencies

### Garmin FIT SDK (`com.garmin:fit:21.176.0`)
- **FileIdMesg**: Binary message for file identity (timestamp, manufacturer, type)
- **WeightScaleMesg**: Measurement message (weight, body composition metrics)
- **FileEncoder**: Writes messages to FIT binary format (Protocol V2.0)
- See `FitFileCreator.java:16` for usage pattern

### Spring Boot 4.0 Specifics
- Virtual threads enabled (`spring.threads.virtual.enabled: true`) for scalability
- Lazy initialization to reduce startup time
- Lombok annotation processing via Maven plugin

### External Services
- **Render.com**: Deployed REST service; environment vars: `RENDER_EXTERNAL_URL`, `PORT`
- **SonarCloud**: Code quality gate on CI/CD (Maven plugin)

## Testing & Quality Standards

**Coverage expectations:** Tests run via `mvnw verify`; coverage tracked by JaCoCo plugin, reported to SonarCloud.

**Patterns:**
- Controller tests use MockMvc (not integration tests)
- Mock external services (FitWeight, Clock)
- Use `@TempDir` for temporary file artifacts
- Request/response assertions on headers and status codes

**JSON Schema & OpenAPI:** Validated specs in `doc/` directory; update when adding fields to `WeightMeasurement`.

## Common Tasks & Pitfalls

### Adding a New Measurement Field
1. Add field to `WeightMeasurement` record (e.g., `Float newMetric`)
2. Add validation annotations if needed
3. Update `GenerateWeightScaleMesg` to set value in FIT message
4. Update `doc/openapi.yml` and `doc/json-schema.json`
5. Add test case to `WeightControllerTest`

### Modifying FIT File Format
- Garmin SDK handles message encoding; only modify via `GenerateFileIdMesg` and `GenerateWeightScaleMesg`
- FIT protocol version fixed to V2.0; changing requires validation with Garmin Connect

### Deployment
- Docker image built with Maven wrapper; ensure `pom.xml` and `mvnw` in sync
- Cold start prevention: keep-alive scheduler runs during business hours (6-9 AM CET)
- Render logs available at https://dashboard.render.com
