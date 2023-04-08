<div align="center">

  <h1>Appointment Scheduler</h1>
  
  <p>
    A desktop appointment scheduler made with Java, JavaFX, and PostreSQL.
  </p>
  
  
<!-- Badges -->

[![Contributors][contributors-shield]][contributors-url]
![Last Commit][lastcommit-shield]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]

<h4>
    <a href="https://github.com/nbalabis/appointment-scheduler/issues/">Report Bug</a>
  </h4>
</div>

<br />

<!-- Table of Contents -->

# :notebook_with_decorative_cover: Table of Contents

- [About the Project](#star2-about-the-project)
  - [Screenshots](#camera-screenshots)
  - [Tech Stack](#space_invader-tech-stack)
  - [Features](#dart-features)
- [Getting Started](#toolbox-getting-started)
  - [Prerequisites](#bangbang-prerequisites)
  - [Run Locally](#running-run-locally)
- [License](#warning-license)
- [Contact](#handshake-contact)
- [Acknowledgements](#gem-acknowledgements)

<!-- About the Project -->

## :star2: About the Project
<p>A GUI-based desktop scheduling solution for internal use in a company with a database of preexisting users, customers, and appointments.</p>

<!-- Screenshots -->

### :camera: Screenshots

<div align="center"> 
  <img src="Appointment Scheduler Screenshots/login-form.png" alt="login form" />
  <img src="Appointment Scheduler Screenshots/login-translate.png" alt="translated login form" />
   <img src="Appointment Scheduler Screenshots/upcoming-apts.png" alt="upcoming apt alert" />
  <img src="Appointment Scheduler Screenshots/all-customers.png" alt="all customers view" />
  <img src="Appointment Scheduler Screenshots/login-record.png" alt="login record" />
</div>

<!-- TechStack -->

### :space_invader: Tech Stack

[![Java][java.js]][java-url]
[![JavaFX][javafx.js]][javafx-url]
[![PostgreSQL][postgresql.js]][postgresql-url]

<!-- Features -->

### :dart: Features

- Authenticates users
- Stores a record of all login attempts
- Translates content based on region
- Create/Read/Update/Delete functionality for appointments and customers stored in SQL database
- Alerts user to upcoming appointments when logging in
- Validates all input values when adding/editing appointments or customer data
- Generates various appointment reports

<!-- Getting Started -->

## :toolbox: Getting Started

<!-- Prerequisites -->

### :bangbang: Prerequisites

- Latest Java version
- JDK: Java SE 11.0.1

<!-- Run Locally -->

### :running: Run Locally

Clone the project

```bash
  git clone https://github.com/nbalabis/appointment-scheduler.git
```

Go to the project directory

```bash
  cd appointment-scheduler
```

Compile code

```bash
  javac src/main/Main.java
```

Run the compiled code

```bash
  java src/main/Main
```

<!-- License -->

## :warning: License

Distributed under the MIT License. See LICENSE.txt for more information.

<!-- Contact -->

## :handshake: Contact

Nicholas Balabis - [LinkedIn](https://www.linkedin.com/in/nicholas-balabis-094571153/) - balabisnicholas@gmail.com

Project Link: [https://github.com/nbalabis/appointment-scheduler](https://github.com/nbalabis/appointment-scheduler)

<!-- Acknowledgments -->

## :gem: Acknowledgements

Built as a submission to complete WGU's Software II course.

<!-- MARKDOWN LINKS & IMAGES -->

[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/nicholas-balabis-094571153/
[contributors-shield]: https://img.shields.io/github/contributors/nbalabis/appointment-scheduler.svg?style=for-the-badge
[contributors-url]: https://github.com/nbalabis/appointment-scheduler/graphs/contributors
[lastcommit-shield]: https://img.shields.io/github/last-commit/nbalabis/appointment-scheduler.svg?style=for-the-badge
[license-shield]: https://img.shields.io/github/license/nbalabis/appointment-scheduler.svg?style=for-the-badge
[license-url]: https://github.com/nbalabis/appointment-scheduler/blob/main/LICENSE
[java.js]: https://img.shields.io/badge/Java-3a75b0?style=for-the-badge
[java-url]: https://www.java.com/en/
[javafx.js]: https://img.shields.io/badge/JavaFX-e76f00?style=for-the-badge
[javafx-url]: https://openjfx.io
[postgresql.js]: https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=FFFFFF
[postgresql-url]: https://www.postgresql.org
