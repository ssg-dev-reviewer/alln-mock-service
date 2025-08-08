# SSG 제휴사 Mock 서비스

이 프로젝트는 SSG 제휴 중계 서비스 과제를 위한 제휴사 Mock API 서버입니다.  
3개의 제휴사(MA0001, MA0002, MA0003)에 대한 Mock API를 제공하며, 각 제휴사별로 다른 요청/응답 포맷과 랜덤 예외 발생 기능을 포함합니다.

**제휴사1(MA0001),제휴사2(MA0002)는 REST JSON 방식의 API 제공하고, 제휴사3(MA0003)은 REST XML 방식의 API를 제공합니다.**

## 프로젝트 구조

```
alln-mock-servcie/
├── build.gradle                    # Gradle 빌드 설정
├── settings.gradle                 # Gradle 프로젝트 설정
├── gradlew                         # Gradle Wrapper (Linux/Mac)
├── gradlew.bat                     # Gradle Wrapper (Windows)
├── README.md                       # 프로젝트 설명서
├── 제휴사별요청응답_포맷.md           # API 상세 명세서
└── src/
    ├── main/
    │   ├── java/com/ssg/api/ssgallnmock/
    │   │   ├── controller/         # 제휴사별 API 컨트롤러
    │   │   │   ├── Partner1Controller.java    # 제휴사1 (MA0001) API - REST JSON
    │   │   │   ├── Partner2Controller.java    # 제휴사2 (MA0002) API - REST JSON
    │   │   │   └── Partner3Controller.java    # 제휴사3 (MA0003) API - REST XML
    │   │   ├── exception/          # 예외 처리
    │   │   │   └── GlobalExceptionHandler.java    # 전역 예외 처리
    │   │   ├── util/               # 유틸리티
    │   │   │   ├── RandomExceptionGenerator.java  # 랜덤 예외 및 timeout 발생
    │   │   └── SsgAllnMockApplication.java        # 메인 애플리케이션
    │   └── resources/
    │       └── application.yml     # 애플리케이션 설정
```

## 구현 방향

### 1. 제휴사별 다른 API 포맷
- **제휴사1, 2**: REST API (JSON)
- **제휴사3**: REST API (XML)

각 제휴사는 서로 다른 요청/응답 포맷을 사용하여 실제 제휴사 환경을 시뮬레이션합니다.

자세한 사항은 제휴사별요청응답_포맷.md를 참고하세요.

### 2. String 기반 통신
모든 API는 객체 대신 String으로 요청과 응답을 처리합니다:
- **요청**: String 형태의 JSON/XML 데이터
- **응답**: String 형태의 JSON/XML 데이터

### 3. 랜덤 예외 발생
각 API 호출 시 10% 확률로 다양한 예외 상황을 발생시켜 실제 운영 환경의 불안정성을 시뮬레이션합니다.

#### 발생 가능한 예외 종류
- 서버 일시적 오류
- API 인증 실패
- 시스템 점검 중

### 4. 랜덤 Timeout 발생
Partner2 API 호출 시 10% 확률로 5-10초 사이의 랜덤한 시간만큼 응답이 지연되어 실제 운영 환경의 네트워크 지연이나 서버 부하 상황을 시뮬레이션합니다.

- **Timeout 발생 확률**: 10%
- **지연 시간**: 5-10초 (랜덤)
- **동작 방식**: 예외 발생과 독립적으로 작동

### 5. 주문 취소 API
각 제휴사별로 주문 취소 API를 제공합니다:
- **Partner1**: `POST /api/partner1/orders/{orderNumber}/cancel`
- **Partner2**: `POST /api/partner2/orders/{orderId}/cancel`
- **Partner3**: `POST /api/partner3/orders/{transactionNumber}/cancel`

### 6. 각 제휴사별 고유 필드
각 제휴사는 자체적인 추가 필드를 가지고 있어 실제 제휴사 API의 다양성을 반영합니다.

## 빌드 및 실행 방법

### 1. 프로젝트 빌드
```bash
# 전체 빌드 (테스트 포함)
./gradlew build
```

### 2. 애플리케이션 실행
```bash
# Gradle을 통한 실행
./gradlew bootRun

# 또는 JAR 파일로 실행
java -jar build/libs/ssg-alln-mock-0.0.1-SNAPSHOT.jar
```

### 3. 서버 접속 및 확인
- **서버 URL**: `http://localhost:9710`

## API 명세
자세한 사항은 **제휴사별요청응답_포맷.md**를 참고하세요.

## 테스트 방법

### CURL을 이용한 테스트

#### 제휴사1 테스트 (REST JSON)
**주문 생성:**
```bash
curl -X POST http://localhost:9710/api/partner1/orders \
  -H "Content-Type: application/json" \
  -d '{
  "orderNumber": "ORDER001",
  "orderDate": "2024-01-15T10:30:00",
  "items": [
    {
      "itemCode": "P001",
      "itemName": "상품1",
      "orderQty": 2,
      "price": 15000.0
    },
    {
      "itemCode": "P002",
      "itemName": "상품2",
      "orderQty": 1,
      "price": 25000.0
    }
  ]
}'
```

**주문 상태 조회:**
```bash
curl -X GET 'http://localhost:9710/api/partner1/orders/ORDER001/status?itemCode=P001'
```

**주문 취소:**
```bash
curl -X POST http://localhost:9710/api/partner1/orders/ORDER001/cancel \
  -H "Content-Type: application/json" \
  -d '{
    "orderNumber": "ORDER001",
    "cancelTime": "2024-01-15 15:30:00",
    "cancelReason": "SIMPLE_CANCEL",
    "itemCode": "P001"
  }'
```

#### 제휴사2 테스트 (REST JSON)
**주문 생성:**
```bash
curl -X POST http://localhost:9710/api/partner2/orders \
  -H "Content-Type: application/json" \
  -d '{
  "orderId": "ORDER002",
  "orderTimestamp": "2024-01-15T10:30:00",
  "productList": [
    {
      "sku": "ITEM001",
      "name": "상품1",
      "qty": 3,
      "unitCost": 25000.0
    },
    {
      "sku": "ITEM002",
      "name": "상품2",
      "qty": 1,
      "unitCost": 15000.0
    }
  ]
}'
```

**주문 상태 조회:**
```bash
curl -X GET 'http://localhost:9710/api/partner2/orders/ORDER002/status?sku=ITEM001'
```

**주문 취소:**
```bash
curl -X POST http://localhost:9710/api/partner2/orders/ORDER002/cancel \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "ORDER002",
    "cancelTime": "2024-01-15 15:30:00",
    "reason": "20",
    "sku": "ITEM001"
  }'
```

#### 제휴사3 테스트 (REST XML)
**주문 생성:**
```bash
curl -X POST http://localhost:9710/api/partner3/orders \
  -H "Content-Type: application/xml" \
  -d '<?xml version="1.0" encoding="UTF-8"?>
      <orderRequest>
        <transactionNumber>TXN001</transactionNumber>
        <createdAt>2024-01-15T10:30:00</createdAt>
        <orderDetails>
          <orderItem>
            <productId>PROD001</productId>
            <productTitle>상품1</productTitle>
            <amount>5</amount>
            <cost>12000.0</cost>
          </orderItem>
          <orderItem>
            <productId>PROD002</productId>
            <productTitle>상품2</productTitle>
            <amount>2</amount>
            <cost>8000.0</cost>
          </orderItem>
        </orderDetails>
      </orderRequest>'
```

**주문 상태 조회:**
```bash
curl -X GET 'http://localhost:9710/api/partner3/orders/TXN001/status?productId=PROD001' \
  -H "Accept: application/xml"
```

**주문 취소:**
```bash
curl -X POST http://localhost:9710/api/partner3/orders/TXN001/cancel \
  -H "Content-Type: application/xml" \
  -d '<?xml version="1.0" encoding="UTF-8"?>
      <cancelRequest>
        <transactionNumber>TXN001</transactionNumber>
        <cancelTime>2024-01-15 15:30:00</cancelTime>
        <cancelReason>D01</cancelReason>
        <productId>PROD001</productId>
      </cancelRequest>'
```

## API 엔드포인트 요약

| 제휴사 | 프로토콜 | 데이터 형식 | 주문 생성 | 주문 상태 조회 | 주문 취소 |
|--------|----------|-------------|-----------|----------------|-----------|
| Partner1 | REST | JSON | `POST /api/partner1/orders` | `GET /api/partner1/orders/{orderNumber}/status?itemCode={itemCode}` | `POST /api/partner1/orders/{orderNumber}/cancel` |
| Partner2 | REST | JSON | `POST /api/partner2/orders` | `GET /api/partner2/orders/{orderId}/status?sku={sku}` | `POST /api/partner2/orders/{orderId}/cancel` |
| Partner3 | REST | XML | `POST /api/partner3/orders` | `GET /api/partner3/orders/{transactionNumber}/status?productId={productId}` | `POST /api/partner3/orders/{transactionNumber}/cancel` |

## 예외 처리

### 랜덤 예외 발생
각 API 호출 시 10% 확률로 다양한 예외 상황이 발생합니다.

#### 예외 종류
1. **서버 일시적 오류**: "제휴사 서버 일시적 오류가 발생했습니다."
2. **API 인증 실패**: "제휴사 API 인증 실패"
3. **시스템 점검**: "제휴사 시스템 점검 중입니다."

### 랜덤 Timeout 발생
Partner2 API 호출 시 10% 확률로 5-10초 사이의 랜덤한 시간만큼 응답이 지연됩니다.

#### Timeout 특징
- **발생 확률**: 10%
- **지연 시간**: 5-10초 (랜덤)
- **동작 방식**: 예외 발생과 독립적으로 작동
- **목적**: 실제 운영 환경의 네트워크 지연이나 서버 부하 상황 시뮬레이션

#### 예외 응답 예시
**REST JSON (Partner1, Partner2):**
```json
{
  "timestamp": "2024-01-15T14:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "제휴사 서버 일시적 오류가 발생했습니다.",
  "path": "/api/partner1/orders"
}
```

**REST XML (Partner3):**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<errorResponse>
    <timestamp>2024-01-15T14:30:00</timestamp>
    <status>500</status>
    <error>Internal Server Error</error>
    <message>제휴사 서버 일시적 오류가 발생했습니다.</message>
    <path>/api/partner3/orders</path>
</errorResponse>
```

### 예외 처리 특징
- REST JSON API: HTTP 500 상태 코드로 JSON 에러 포맷 제공
- REST XML API: HTTP 500 상태 코드로 XML 에러 포맷 제공
- 일관된 에러 메시지와 타임스탬프 정보 포함

## 개발 환경

- **Java**: 11
- **Spring Boot**: 2.7.18
- **Gradle**: 8.x
- **서버 포트**: 9710

이 프로젝트는 SSG 제휴 중계 서비스 과제용으로 제작되었습니다.
