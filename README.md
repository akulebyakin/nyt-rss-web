# NYT‑RSS web application (Spring Boot)

SpringBoot service that

* pulls the [NYT Technology RSS feed](https://rss.nytimes.com/services/xml/rss/nyt/Technology.xml)
* converts each news into a JSON `Article`
* caches the list for 15 minutes
* exposes articles at [http://localhost:8080/api/articles/technology](http://localhost:8080/api/articles/technology) (use GET)

---

## Prerequisites

| Tool           | Version         |
|----------------|-----------------|
| **Java**       | 17 LTS or newer |
| **Maven**      | 3.9+            |

---

## Running locally

- Run Spring-Boot application
```bash
mvn clean install
mvn spring-boot:run
```
- Get articles at [http://localhost:8080/api/articles/technology](http://localhost:8080/api/articles/technology)

---

## Run the tests

```bash
mvn clean test
```
