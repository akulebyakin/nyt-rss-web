# NYT‑RSS web application (Spring Boot)

SpringBoot service that

* pulls the NYT Technology RSS feed
* converts each news into a JSON `Article`
* caches the list for 15 minutes
* exposes articles at `/api/articles/technology`

---

## Prerequisites

| Tool           | Version         |
|----------------|-----------------|
| **Java**       | 17 LTS or newer |
| **Maven**      | 3.9+            |

---

## Running locally

```bash
mvn spring-boot:run
```

---

## Run the tests

```bash
mvn test
```
