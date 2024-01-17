@import de.lpo.todo.auth.model.AuthUser

@param AuthUser currentUser

@template.base.page-template(content = @`
    <nav class="container-fluid">
        <ul>
            <li>
                <a href="/app/boards" role="button" class="secondary outline">Back</a>
            </li>
        </ul>
        <ul>
            <li><strong>Admin Panel</strong></li>
        </ul>
        <ul>
            <li><a style="visibility: hidden">hidden</a></li>
        </ul>
    </nav>

    <main class="container">
        <hgroup>
            <h1>Admin panel</h1>
            <span>Thanks for being the coolest admin ever!</span>
        </hgroup>

        <h2>OIDC profile (stored excerpt)</h2>
        <table>
            <tr>
                <td>ID</td>
                <td>${currentUser.userId()}</td>
            </tr>
            <tr>
                <td>Name</td>
                <td>${currentUser.userName()}</td>
            </tr>
            <tr>
                <td>Email</td>
                <td>${currentUser.email()}</td>
            </tr>
            <tr>
                <td>Display Name</td>
                <td>${currentUser.displayName()}</td>
            </tr>
        </table>

        <h2>Totally working buttons</h2>
        <a href="" role="button">Manage data</a>
        <a href="" role="button">Clear database</a>
        <a href="" role="button">Manage users</a>
    </main>

    <style>
        nav {
            position: sticky;
            top: 0;
            background-color: var(--background-color);
            box-shadow: 0 1px 0 var(--accordion-border-color);
        }
    </style>
`))