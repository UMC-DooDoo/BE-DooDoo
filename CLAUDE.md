# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# Build
./gradlew build

# Run
./gradlew bootRun

# Run tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.umc.doodoo.DoodooApplicationTests"
```

**Required environment variables** (set in `.env` or IDE run config):
- `DB_URL` — JDBC URL for MySQL
- `DB_USERNAME`
- `DB_PASSWORD`

## Tech Stack

- Java 17, Spring Boot 4.1.0
- Spring Data JPA + MySQL (`ddl-auto: update`)
- springdoc-openapi 3.0.3 (Swagger UI at `/swagger-ui/index.html`)
- Lombok

## Architecture

The project follows a domain-driven package structure under `com.umc.doodoo`:

```
domain/
  <domain>/
    controller/   — REST controllers, return ApiResponse<T> directly
    service/      — business logic, @Transactional
    repository/   — Spring Data JPA interfaces
    entity/       — JPA entities extending BaseEntity
    dto/
      request/    — record-based request DTOs
      response/   — record-based response DTOs with static from() factory
    exception/    — domain-specific error codes (enum implementing BaseErrorCode)
global/
  config/         — Spring configuration classes
  entity/         — BaseEntity (createdAt, updatedAt via JPA Auditing)
  exception/      — BaseErrorCode interface, CustomException, GeneralErrorCode, GlobalExceptionHandler
  response/       — ApiResponse<T>, BaseSuccessCode interface, GeneralSuccessCode
```

## Key Patterns

**Error handling** — Throw `CustomException(SomeDomainErrorCode.SOME_ERROR)` anywhere; `GlobalExceptionHandler` converts it to `ApiResponse.onFailure(errorCode)`. Every domain owns an error code enum implementing `BaseErrorCode` (httpStatus + code string + message).

**Adding a new error code:**
1. Add a value to the domain's `*ErrorCode` enum (or create one implementing `BaseErrorCode`)
2. Throw via `new CustomException(MyErrorCode.FOO)`

**Success responses** — Controllers return `ApiResponse.onSuccess(result)` (uses `GeneralSuccessCode.OK`) or `ApiResponse.onSuccess(MySuccessCode.CREATED, result)` for custom codes. Domain-specific success codes implement `BaseSuccessCode`.

**Entities** — Extend `BaseEntity` for `createdAt`/`updatedAt`. Use `@NoArgsConstructor(access = PROTECTED)` + `@Builder`. Add mutation methods directly on the entity (e.g., `todo.updateTitle(...)`, `todo.toggleComplete()`).

**Enum DB mapping** — Use a `*Converter` (AttributeConverter) to store enum as an integer/string column (see `PriorityConverter`). Add a static `fromValue()` factory on the enum that throws `CustomException` on invalid input.

## PR Convention

PR title format: `[타입] 간단한 변경 내용 요약`
- Types: `feat`, `fix`, `refactor`, `chore`, `docs`, `test`
- Target base branch: `develop`
