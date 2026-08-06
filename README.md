# HWR Zeiterfassung – Backend

REST-Backend eines Arbeitszeiterfassungssystems. Spring Boot 2.6 mit Spring Data JPA und
MySQL, bildet im MVC-Entwurf des Gesamtsystems den Controller und den Resource Management
Layer ab. Die beiden Clients ([Desktop](#zugehörige-repositories) und
[Web](#zugehörige-repositories)) greifen ausschließlich über die hier definierten
REST-Schnittstellen auf die Datenbank zu.

> **Studienprojekt, archiviert.** Der Stand entspricht der Abgabe vom 04.06.2022
> (Version 0.1.0) und wird nicht weiter gepflegt. Abhängigkeiten sind entsprechend alt.

## Kontext

Entstanden im Rahmen des Wahlpflichtfachs **WI-401-13 „Programmierung/Architektur von
Client-Server-Anwendungen“** bei Sandro Hartenstein an der Hochschule für Wirtschaft und
Recht Berlin (Fachbereich 2 – Duales Studium Wirtschaft/Technik, Wirtschaftsinformatik,
Jahrgang 2020, 4. Semester).

Die Prüfungsleistung bestand aus zwei Projektreviews (Persistenzschicht bzw.
Präsentationsschicht) und einem Projektbericht. Gefordert war eine mehrstufige
Client-Server-Anwendung mit Presentation Layer, Application Logic Layer und Resource
Management Layer.

**Fachliches Szenario:** Digitalisierung der Arbeitszeiterfassung im Amt für Statistik
Berlin-Brandenburg. Anforderungen wurden per User Stories aufgenommen und über MVP-Stufen
priorisiert; umgesetzt wurde MVP 1 (Login, Zeiterfassung, Korrektur und Auswertung von
Arbeitszeiten inkl. Kostenstellen-/Projektbuchung). Abwesenheitsverwaltung und
Personaldatenverwaltung waren als MVP 2/3 geplant und sind nicht implementiert.

**Team:** Josephine Kobelt, Nico Regenberg, Nico Steinmüller, Simon Valiente, Tom Weißflog.
Das Backend verantworteten Nico Steinmüller und Simon Valiente.

## Technologie

| Bereich       | Einsatz                                      |
| ------------- | -------------------------------------------- |
| Sprache       | Java 17                                      |
| Framework     | Spring Boot 2.6, Spring Web (REST), Spring Data JPA |
| Persistenz    | MySQL 8, Hibernate als ORM (`ddl-auto: update`) |
| Build         | Maven                                        |
| Test          | JUnit 5, Spring Boot Test                    |
| CI/CD         | Jenkins, Docker Hub, OWASP Dependency-Check  |

## Projektstruktur

```
src/main/java/com/hwr/hwrzeiterfassung
├── controller/            fachliche REST-Controller (Buchen, Korrigieren, Auswerten, Profil)
├── database
│   ├── tables/            JPA-Entitäten (Human, Login, Role, Project, Day, Time)
│   ├── repositorys/       Spring-Data-Repositories
│   └── controller/        Controller nahe an den Entitäten (Login, Human, Project, …)
└── response/models/       DTOs für ein- und ausgehende REST-Kommunikation
```

### Datenmodell

- **Human** – Beschäftigte:r mit Soll-Arbeitszeit, Vorgesetzte:r, Standardprojekt, Rolle
  und den zugeordneten Projekten
- **Login** – E-Mail als Schlüssel und SHA-256-gehashtes Passwort, 1:1 zu `Human`
- **Role** – Dienstkraft, Führungskraft, Personalservice, Superuser
- **Project** – Kostenstelle/Projekt, auf das Zeiten gebucht werden
- **Day** – Tagesbilanz je Person: Datum, Sollzeit, Pausenzeit, Zeitdifferenz
- **Time** – einzelner Zeitabschnitt mit Start, Ende, Pausenkennzeichen, Notiz und Projekt

## Setup

Voraussetzungen: JDK 17, Maven, erreichbare MySQL-8-Instanz.

1. **Datenbank anlegen.** Schema `hwr-zeiterfassung` erstellen. Die Tabellen legt Hibernate
   beim Start selbst an (`ddl-auto: update`). Testdaten lagen der Abgabe als
   `DumpHwrZeiterfassung.sql` bei.
2. **Verbindung konfigurieren** in [application.yml](src/main/resources/application.yml):
   `spring.datasource.url`, `username` und `password` setzen. Im Repository steht dort der
   Platzhalter `(datasource_password)`, den die Jenkins-Pipeline zur Build-Zeit aus einem
   Credential ersetzt.
3. **Starten:**

   ```bash
   mvn spring-boot:run          # oder: mvn package && java -jar target/*.jar
   ```

Der Server lauscht auf Port `8080`. Funktionstest:

```bash
curl http://localhost:8080/role/getAllRoles
# [{"name":"Dienstkraft"},{"name":"Führungskraft"},{"name":"Personalservice"},{"name":"Superuser"}]
```

### Tests und Reports

```bash
mvn test                     # JUnit-Tests (Controller- und Repository-Ebene)
mvn site                     # Surefire-Report
mvn dependency-check:check   # OWASP Dependency-Check
```

Die Tests nutzen eine eigene Konfiguration unter
[application-test.yml](src/test/java/resources/application-test.yml).

### Docker

```bash
mvn package
docker build -t hwr-zeiterfassung-backend .
docker run -p 8080:8080 hwr-zeiterfassung-backend
```

## REST-Schnittstellen

Authentifiziert wird bei jedem Aufruf über die Request-Parameter `email` und `password`
(SHA-256-Hash des Passworts) – es gibt keine Session und kein Token. Datumsangaben im
Format `yyyy-MM-dd`.

### `/login`

| Methode | Pfad          | Beschreibung                                    |
| ------- | ------------- | ----------------------------------------------- |
| POST    | `/basicLogin` | Prüft die Zugangsdaten (`202` bzw. `406`)       |

### `/book` – Zeiterfassung

| Methode | Pfad    | Parameter                                              |
| ------- | ------- | ------------------------------------------------------ |
| POST    | `/time` | `isStart`, `pause`, `note`, `projectId` – bucht Beginn/Ende einer Arbeits- oder Pausenzeit |

### `/time`, `/human`, `/role` – Stammdaten und Status

| Methode | Pfad                    | Beschreibung                                     |
| ------- | ----------------------- | ------------------------------------------------ |
| GET     | `/time/lastStatus`      | letzter Buchungsstatus, damit der Client die richtigen Buttons aktiviert |
| GET     | `/human/name`           | Vor- und Nachname                                |
| GET     | `/human/getAllProjects` | Projekte, auf die die Person buchen darf         |
| GET     | `/human/getDefaultProject` | hinterlegtes Standardprojekt                  |
| GET     | `/role/getAllRoles`     | alle Rollen                                      |

### `/correct` – Korrekturbuchungen

| Methode | Pfad                        | Beschreibung                                   |
| ------- | --------------------------- | ---------------------------------------------- |
| GET     | `/getDayInformationAndTimes` | Tagesbilanz und Zeiteinträge zu `date`        |
| POST    | `/times`                    | überschreibt die Zeiteinträge eines Tages (Body: `DateAndListOfTimes`) |

### `/overview` – Auswertungen

Alle Endpunkte erwarten `start` und `end` als Intervallgrenzen.

| Methode | Pfad                          | Beschreibung                              |
| ------- | ----------------------------- | ----------------------------------------- |
| GET     | `/DayFullOverviewInInterval`  | Tagesübersichten inkl. Einzelzeiten       |
| GET     | `/DayCompactOverviewInInterval` | verdichtete Tagesübersichten            |
| GET     | `/AverageWorkingHoursInInterval` | durchschnittliche Arbeitszeit          |
| GET     | `/AveragePauseInInterval`     | durchschnittliche Pausenzeit              |

### `/update` – Profildaten

| Methode | Pfad                       | Parameter                  |
| ------- | -------------------------- | -------------------------- |
| POST    | `/password`                | `newPassword`              |
| POST    | `/lastName`                | `newLastName`              |
| POST    | `/targetDailyWorkingTime`  | `targetDailyWorkingTime`   |
| POST    | `/defaultProjectId`        | `projectId`                |

## CI/CD

Die [Jenkinsfile](Jenkinsfile) baut, testet und paketiert das Projekt und ersetzt dabei die
Datenbank-Platzhalter aus dem Jenkins-Credential-Store. Auf `develop` und `release` wird
zusätzlich ein Docker-Image gebaut und nach `stier09/hwr-zeiterfassung-backend` gepusht, auf
`release` außerdem als `latest`. Der OWASP Dependency-Check läuft nur auf `release`; die
SonarQube-Stages sind im Skript auskommentiert.

## Zugehörige Repositories

- [hwr-zeiterfassung-desktopfrontend](https://github.com/NicoSteinmueller/hwr-zeiterfassung-desktopfrontend) – Fat Client (JavaFX)
- [hwr-zeiterfassung-webfrontend](https://github.com/NicoSteinmueller/hwr-zeiterfassung-webfrontend) – Webclient (React)

## Bekannte Einschränkungen

Es handelt sich um einen Studienprototypen für MVP 1, nicht um produktionsreifen Code:

- Zugangsdaten werden bei jedem Request als Query-Parameter mitgeschickt, ohne Session,
  Token oder Spring Security
- Passwörter liegen als ungesalzener SHA-256-Hash in der Datenbank
- keine Autorisierung anhand der Rollen – jede angemeldete Person darf alle Endpunkte nutzen
- `ddl-auto: update` statt versionierter Migrationen
