@template.base.page-template(content = @`
    <div class="hero" data-theme="dark">
        <div class="hero-background">
            <div class="image"></div>
            <div class="gradient"></div>
        </div>

        <nav class="container-fluid">
            <ul>
                <li>
                    <a href="" class="contrast">
                        <strong>Todo App Plus</strong>
                    </a>
                </li>
            </ul>
            <ul>
                <li>
                    <a href="https://www.openvalue.eu/" class="contrast">OpenValue</a>
                </li>
                <li>
                    <a href="https://github.com/lukas-poos-openvalue/jjj-stack-meetup" class="contrast">Repo @
                        Github</a>
                </li>
            </ul>
        </nav>

        <header class="container">
            <hgroup>
                <h1>Todo App Plus</h1>
                <p>The perfect app for managing todos with multiple people!</p>
            </hgroup>
            <p>
                <a href="/api/login" role="button">Sign in</a>
            </p>
        </header>
    </div>

    <main class="container">
        <div class="grid">
            <section>
                <hgroup>
                    <h2>Can the modern Java dev survive without application frameworks like Spring-Boot?</h2>
                    <p>A sophisticated demo application without the sophisticated dependencies</p>
                </hgroup>

                <h3>Abstract</h3>
                <p>
                    Application frameworks such as Spring-Boot / Quarkus / Micronaut / etc. are common choices for many
                    backend-related projects in the Java world.
                </p>
                <p>
                    The reason is obvious: They provide environments with many well-known and battle-tried features and
                    tools. These frameworks enable dev teams to write quality software in a reasonable amount of time.
                    But with all application frameworks, you trade features and developer ergonomics in for
                    abstractions,
                    hidden complexity and many dependencies. Without getting into much detail whether this trade-off is
                    worth it or not, let the question be: Can you reasonably live without them?
                </p>
                <p>
                    In this talk, I'd like to showcase some smaller, alternative libraries and frameworks that shift the
                    control (and the responsibility) back in your hands. How tedious is it really to write all the
                    common
                    use-cases without application frameworks? And what do you gain in return in regards to simplicity
                    and
                    performance?
                </p>
                <p>
                    By the end of it, we'll know if Javalin, JOOQ, JTE, HTMX etc. can compete against application
                    frameworks
                    and if they are eligle options for your next project.
                </p>

                <h3>How does this demo work?</h3>
                <h4>Run the project yourself!</h4>
                <ol>
                    <li>Clone from Github</li>
                    <li>Do a quick build: <code>./mvnw clean install</code></li>
                    <li>Start all required systems: <code>docker compose up</code></li>
                    <li>Start the application server: <code>java -jar target/todo-app-plus.jar</code></li>
                    <li>Get to this page with <a href="http://localhost:8080/app">this link</a></li>
                </ol>
                <h4>Take a gander</h4>
                <ol>
                    <li>Sign in with users that should be available as members
                        (<code>admin / admin</code>, <code>user1 / user</code> and <code>user2 / user</code>)
                    </li>
                    <li>Create a board and add some members (had to be signed in before)</li>
                    <li>Have fun with the todo features!</li>
                </ol>
                <h4>Check out the code!</h4>
                <ol>
                    <li>The README.md provides an introduction and overview section</li>
                    <li>Each top-level-package is a 'module' and provides a package documentation!
                    </li>
                    <li>Check the comments in the code; special locations are marked as a <abbr
                                title="Point of Interest">PoI</abbr></li>
                    <li>Last but not least: check the summary section in the README.md!</li>
                </ol>
            </section>

            <aside>
                <h3>Libraries used in this demo</h3>

                <div class="libraries-grid">
                    <article>
                        <a href="https://javalin.io/"><img class="img-fix-light" src="/img/lib-logos/javalin.svg"
                                                           alt="Javalin"/></a>
                        <div>
                            <strong>Javalin: </strong>
                            <small>A simple web framework for Java and Kotlin</small>
                        </div>
                    </article>
                    <article>
                        <a href="https://www.jooq.org/"><img class="img-fix-light" src="/img/lib-logos/jooq.png"
                                                             alt="jOOQ"/></a>
                        <div>
                            <strong>jOOQ: </strong>
                            <small>
                                Generate Java code from your database and build type safe SQL queries through its
                                fluent API
                            </small>
                        </div>
                    </article>
                    <article>
                        <a href="https://jte.gg/"><img src="/img/lib-logos/jte.svg" alt="JTE"/></a>
                        <div>
                            <strong>JTE: </strong>
                            <small>
                                Secure and lightweight template engine for Java and Kotlin. Supports type safe
                                templates!
                            </small>
                        </div>
                    </article>
                    <article>
                        <a href="https://www.pac4j.org/"><img src="/img/lib-logos/pac4j.png" alt="pac4j"/></a>
                        <div>
                            <strong>pac4j: </strong>
                            <small>
                                The Java security framework to protect all your web applications and web services!
                            </small>
                        </div>
                    </article>
                    <article>
                        <a href="https://www.liquibase.org/"><img src="/img/lib-logos/liquibase.svg"
                                                                  alt="Liquibase"/></a>
                        <div>
                            <strong>Liquibase: </strong>
                            <small>
                                An open-source database-independent library for tracking, managing and applying database
                                schema changes.
                            </small>
                        </div>
                    </article>
                    <article>
                        <a href="https://github.com/brettwooldridge/HikariCP">
                            <img src="/img/lib-logos/hikari.png" alt="hikari"/>
                        </a>
                        <div>
                            <strong>HikariCP: </strong>
                            <small>
                                Fast, simple, reliable. HikariCP is a "zero-overhead" production ready JDBC connection
                                pool.
                            </small>
                        </div>
                    </article>
                    <article>
                        <a href="https://sqids.org/java">
                            <img src="/img/lib-logos/sqids.svg" alt="sqids"/>
                        </a>
                        <div>
                            <strong>Sqids: </strong>
                            <small>
                                An open-source library that lets you generate short unique identifiers from numbers.
                            </small>
                        </div>
                    </article>
                    <article>
                        <a href="https://junit.org/junit5/">
                            <img src="/img/lib-logos/junit5.png" alt="JUnit5"/>
                        </a>
                        <div>
                            <strong>JUnit5: </strong>
                            <small>
                                The 5th major version of the programmer-friendly testing framework for Java and the JVM.
                            </small>
                        </div>
                    </article>
                    <article>
                        <a href="https://testcontainers.com/">
                            <img src="/img/lib-logos/testcontainers.svg" alt="Testcontainers"/>
                        </a>
                        <div>
                            <strong>Testcontainers: </strong>
                            <small>
                                An open source framework for providing throwaway, lightweight instances of
                                anything that can run in a Docker container.
                            </small>
                        </div>
                    </article>

                    <article class="etc">
                        <strong>And many more...</strong>
                    </article>
                </div>
            </aside>
        </div>
    </main>

    <style>
        :root {
            --spacing-hero: 3rem;
            --font-weight: 400;
            --border-radius: 0;
        }

        .hero {
            position: relative;
        }

        .hero-background,
        .hero-background * {
            position: absolute;
            inset: 0;
            z-index: -1;
            height: inherit;
        }

        .hero-background .image {
            background-image: url("img/index-hero-background.jpg");
            background-repeat: no-repeat;
            background-position: center;
            background-size: cover;
        }

        .hero-background .gradient {
            background: linear-gradient(
                180deg,
                hsl(205, 15%, 41%, 60%) 0%,
                var(--background-color) 100%
            );
        }

        .hero nav,
        .hero header {
            position: relative;
        }

        .hero header {
            padding: var(--spacing-hero) 0;
        }

        .hero header hgroup > :last-child {
            color: var(--h3-color);
        }

        .hero header hgroup {
            margin-bottom: var(--spacing-hero);
        }

        @media (min-width: 992px) {
            main .grid {
                grid-column-gap: var(--spacing-hero);
                grid-template-columns: auto 42%;
            }
        }

        .libraries-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(10em, 1fr));
            gap: 1em;
        }

        .libraries-grid > article {
            display: grid;
            grid-template-rows: 7em auto;
            gap: 1em;
            padding: 0.5em 1em;
            margin: 0;
        }

        .libraries-grid a {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 7em;
        }
        .libraries-grid a img {
            height: 7em;
            aspect-ratio: 1;
        }
        @media only screen and (prefers-color-scheme: light) {
            .img-fix-light {
                filter: invert(100%);
            }
        }

        .libraries-grid .etc {
            grid-template-rows: 1fr;
            justify-content: center;
            align-items: center;
        }
    </style>
`)
