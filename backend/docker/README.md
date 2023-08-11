# application-local.yml 예시

```bash
#application-local.yml
spring:
  jpa:
    hibernate:
      ddl-auto: create
    show-sql: false
    properties:
      hibernate:
        format_sql: true

  datasource:
    url: jdbc:mariadb://localhost:3306/bootex
    driver-class-name: org.mariadb.jdbc.Driver
    username: root
    password: root

  cache:
    type: redis
  data:
    redis:
      host: 127.0.0.1
      port: 6379

jwt:
  secretKey: sKeTChMeEEesKeTChMeEEesKeTChMeEEesKeTChMeEEesKeTChMeEEesKeTChMeEEe

server:
  port: 8000
  servlet:
    context-path: /dev/api
```

위 코드를 보고, resources 경로 안에 application-local.yml을 만드세요.

