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

## Run the application in Docker

- Configure `docker-compose.yml` file with your local frontend and backend directories
  - `services.backend.build.context`: path to `nyt-rss-web`
  - `services.frontend.build.context`: path to `nyt-rss-frontend`
  - `services.frontend.volumes`: first volume contain the path to `nyt-rss-frontend`
```bash
docker-compose up
```
- Access the NYT RSS application:
    - Backend API: http://localhost:8080/api/articles/technology
    - Frontend: http://localhost:5173
- Stop the application:
```bash
docker-compose down
```
---

## Run the tests

- Run unit tests only (*Test)
```bash
mvn test
```

- Run unit and integration tests (*Test and *IT)
```bash
mvn verify
```

- Generate a test coverage report
```bash
mvn surefire-report:report-only
```

- Open report in browser
```bash
open target/reports/surefire.html
```