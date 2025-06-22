# 🚀 Dopamine

**Dopamine**은 Spring Boot 기반 서비스 개발에서 반복되는 인프라 작업을 줄이고,  
**자동 구성되는 모듈화된 공통 인프라 프레임워크**를 지향합니다.

번아웃 시기를 지나 다시 개발에 집중하기 위한 의지로 시작되었으며,  
비슷한 고민을 가진 개발자들에게도 의미 있는 도구가 되길 기대합니다.

---

## 🎯 목표

- 실무에서 자주 쓰이는 기능을 모듈화하여 **재사용 가능한 프레임워크** 제공
- Spring Boot Starter 구조에 기반한 **자동 설정 및 비활성화 옵션 지원**
- 설정 파일만으로 다양한 기능을 **선택적으로 활성화**할 수 있도록 설계

---

## 🧩 주요 Features

✔️ 공통 응답 포맷 자동 래핑 (`DopamineResponse<T>`)  
✔️ traceId 자동 생성 및 응답/로그 포함  
✔️ 예외 포맷 자동 처리 (비즈니스 / 시스템 오류 구분)  
✔️ 국제화(i18n) 메시지 바인딩 및 커스텀 코드 지원  
✔️ Kotlin + Gradle(KTS) + Kotest 기반 테스트 환경  
✔️ 조건부 기능 비활성화 (`dopamine.xxx.enabled=false`)

---

## 🧱 모듈 구성

| 모듈                      | 역할 설명 |
|---------------------------|-----------|
| `trace-common`            | traceId 생성/전파 추상화 |
| `trace-mvc`               | Servlet(MDC) 기반 trace 필터 |
| `response-common`         | 공통 응답 포맷 및 meta 정의 |
| `response-mvc`            | 응답/예외 자동 래핑 처리 |
| `i18n`                    | 국제화 메시지 바인딩 및 코드 처리 |
| `auth-common`             | 인증 추상화 (`UserPrincipal`, `TokenProvider`) |
| `auth-mvc`                | JWT 기반 인증 필터 및 보안 설정 |
| `docs`                    | Swagger + Markdown 문서 자동화 |
| `file-common`             | 파일 업로드 메타 및 응답 포맷 정의 |
| `file-mvc`                | 파일 업로드, S3 연동 기능 구성 |
| `starter-common`          | 공통 설정 구성 |
| `starter-mvc`             | 위의 주요 기능 통합 자동 설정 |
| `test-support`            | 공통 테스트 유틸 및 인프라 도우미 |
| `sample`                  | `starter-mvc` 종속 샘플 애플리케이션 |

---

## 📂 샘플 실행 방법

```bash
./gradlew :dopamine-starter-mvc-sample:bootRun
```

다음 엔드포인트로 샘플 응답 포맷을 확인할 수 있습니다:

```
GET http://localhost:8080/sample/dto
```

예상 응답 예시 (일부 축약):

```json
{
  "code": "SUCCESS_200",
  "message": "Request was successful.",
  "data": {
    "id": 42,
    "name": "Dopamine",
    "status": "ACTIVE",
    "createdAt": "2025-06-01T12:13:53.9964288"
  },
  "timestamp": "2025-06-01T12:13:54",
  "meta": {
    "traceId": "b99b6bc6-c05d-4c9d-ae20-e5d27f58a49a"
  }
}
```

## 🛠️ 향후 로드맵
- ✅ 인증/인가: JWT, OAuth2, 세션 기반 인증 지원
- ✅ 파일 관리: 로컬 + S3 업로드, 응답 포맷 통일
- ✅ Swagger + Markdown 문서 자동화
- ✅ admin UI 기반 설정/운영 기능
- ⏳ 마스킹 애노테이션 기반 개인정보 보호 기능
- ⏳ 알림(SMS/Email), 배치, 캐시 기능 모듈화
- ⏳ 테스트 유틸, coverage/report 자동화
- ⏳ persistence 공통 유틸: auditing, soft delete, repository DSL

---

## ⚙️ 기술 스택
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
