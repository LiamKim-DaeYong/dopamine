# 🧠 Dopamine

**Spring Boot 기반 인프라 기능을 모듈화하여 Starter 형태로 제공하는 인프라 프레임워크입니다.**  
상용 서비스에서 바로 사용할 수 있는 **품질**과 **확장성**을 목표로 하며,  
자동 설정 기반 모듈 구조를 통해 **개발자의 반복 작업을 줄여줍니다.**

> “Dopamine”은 동기부여와 몰입을 유도하는 뇌 내 신경전달물질에서 이름을 따왔습니다.  
> 반복적인 개발 업무 속에서도 **몰입할 수 있는 동력**을 제공하겠다는 의미가 담겨 있습니다.

> 번아웃 시기를 지나 다시 개발에 집중하기 위한 의지로 시작되었으며,  
> 비슷한 고민을 가진 개발자들에게도 의미 있는 도구가 되길 기대합니다.

---

## 🚀 주요 특징
- ✅ **공통 응답 포맷 자동 적용** (`response`)
- 🔍 **traceId 자동 생성 및 로그/응답 주입** (`trace`)
- 🌐 **국제화 메시지 응답 지원** (`i18n`)
- ⚠️ **예외 처리 자동화** (`exception`)
- 📖 **OpenAPI 문서 자동 구성** (`swagger`)
- 🆔 **ID 생성기 지원** (`id-generator`)
- 📁 **파일 업로드 기능** (`file`)
- 🔐 **인증/인가 모듈 개발 중** (`auth`)
- ☝️ `dopamine-starter-mvc` 하나만 의존하면 **위 기능 모두 자동 설정**됩니다.

---

## 📦 모듈 구성

| 모듈                        | 설명 |
|-----------------------------|------|
| `dopamine-starter-mvc`      | 주요 기능 통합 자동 설정 |
| `dopamine-response`         | 공통 응답 포맷 및 예외 래핑 |
| `dopamine-trace`            | traceId 생성 및 MDC 연동 |
| `dopamine-i18n`             | 다국어 메시지 처리 |
| `dopamine-swagger`          | Swagger 문서 자동 구성 |
| `dopamine-id-generator`     | Snowflake 기반 ID 생성 |
| `dopamine-file`             | 파일 업로드/썸네일 처리 |
| `dopamine-auth-common`      | 인증/인가 추상화 계층 |
| `dopamine-auth-mvc`         | JWT 기반 인증 필터 |
| `dopamine-sample-mvc`       | 샘플 애플리케이션 |
| `test-support`              | 테스트 유틸리티 |
| `starter-common`            | 공통 설정 구성 |

---

## 🧪 예시 응답 포맷

### ✅ 일반 단건 응답 예시
```json
{
  "code": "SUCCESS",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "id": 42,
    "name": "Dopamine",
    "status": "ACTIVE",
    "createdAt": "2025-07-03T23:14:53.7586458",
    "tags": ["infra", "spring-boot", "starter"],
    "details": {
      "description": "This is a detailed description of the sample.",
      "score": 87
    },
    "optionalField": "optional value"
  },
  "timestamp": "2025-07-03T23:14:53",
  "meta": {
    "traceId": "1cfc8e2b-7367-4309-9a9b-871d38c83d09"
  }
}
```
### ✅ 페이징 응답 예시 (`include-paging: true`)
```json
{
  "code": "SUCCESS",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "content": [
      {
        "id": 42,
        "name": "Dopamine",
        "status": "ACTIVE",
        "createdAt": "2025-07-03T23:38:07.7477311",
        "tags": ["infra", "spring-boot", "starter"],
        "details": {
          "description": "This is a detailed description of the sample.",
          "score": 87
        },
        "optionalField": "optional value"
      },
      {
        "id": 43,
        "name": "Dopamine",
        "status": "ACTIVE",
        "createdAt": "2025-07-03T23:38:07.7477311",
        "tags": ["infra", "spring-boot", "starter"],
        "details": {
          "description": "This is a detailed description of the sample.",
          "score": 87
        },
        "optionalField": "optional value"
      },
      {
        "id": 44,
        "name": "Dopamine",
        "status": "ACTIVE",
        "createdAt": "2025-07-03T23:38:07.7477311",
        "tags": ["infra", "spring-boot", "starter"],
        "details": {
          "description": "This is a detailed description of the sample.",
          "score": 87
        },
        "optionalField": "optional value"
      }
    ],    
  },
  "timestamp": "2025-07-03T23:38:07",
  "meta": {
    "traceId": "a5038d10-b7d5-4731-99ec-26b444e01248",
    "paging": {
      "page": 0,
      "size": 3,
      "hasNext": true,
      "hasPrevious": false,
      "totalPages": 4,
      "totalElements": 10,
      "first": true,
      "last": false
    }
  }
}
```

---

## 🛠️ 빠른 시작 (Getting Started)
### 1. Gradle 설정
```kotlin
dependencies {
    implementation("com.github.LiamKim-DaeYong:dopamine-starter-mvc:0.1.0")
}
```

### 2. application.yml 예시
```yaml
dopamine:
  response:
    enabled: true
    include-meta: true
    timestampFormat: ISO_8601
    ignore-paths:
      - /swagger-ui
      - /v3/api-docs
      - /h2-console
      - /favicon.ico
    meta-options:
      include-paging: true
    codes:
      - code: SUCCESS
        http-status: 200
        message-key: dopamine.success.200
        default-message: Request was successful.
      - code: CREATED
        http-status: 201
        message-key: dopamine.success.201
        default-message: Resource has been created.

  docs:
    enabled: true
    swagger:
      enabled: true
      title: Dopamine API
      version: v1.0.0
      description: API documentation generated by Dopamine
      additional-groups:
        - name: default
          base-packages:
            - io.dopamine

  i18n:
    base-names: classpath:/messages
    default-locale: en
    encoding: UTF-8
    fallback-to-system-locale: true

  trace:
    trace-id-header: X-Trace-ID
    trace-id-key: traceId

  file:
    enabled: true
    base-path: /dopamine/v1/files
    storages:
      - name: default
        type: LOCAL
        maxSize: 5242880
        extensionPolicy:
          mode: DENY
          patterns:
            - exe
            - bat
```

---

## 💻 기술 스택
* **Spring Boot 3.4.5**
* **Kotlin 2.1.0**
* **Gradle (Kotlin DSL) + Version Catalog**
* **Kotest**
* **Jacoco, Ktlint**
* **Build Convention: ModuleConvention, buildSrc 전략 기반**

---

## 🙋‍♂️ Maintainer

* DY (a.k.a dy.log)
* 기술 블로그: [https://velog.io/@dylog/posts](https://velog.io/@dylog/posts)

---

## 📜 라이선스

MIT License
