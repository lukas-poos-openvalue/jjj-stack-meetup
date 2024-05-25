@import org.pac4j.core.util.Pac4jConstants

@param gg.jte.Content content

<!doctype html>
<html lang="en">
<head>
    <title>Todo App Plus</title>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="/css/pico.min.css"/>
    <link rel="stylesheet" href="/css/icons.min.css"/>
    <link rel="stylesheet" href="/css/global.css"/>

    <script src="/js/htmx.min.js"></script>
</head>
<body>

${content}

<script>
    function getCookieByName(name) {
        const cookies = document.cookie.split(';')
        const cookie = cookies.find(c => c.indexOf(name) !== -1);
        return cookie ? cookie.split('=')[1] : undefined;
    }

    document.body.addEventListener('htmx:configRequest', function(evt) {
        const csrfToken = getCookieByName('${Pac4jConstants.CSRF_TOKEN}');
        if (csrfToken) {
            evt.detail.headers['${Pac4jConstants.CSRF_TOKEN}'] = csrfToken;
        }
    });
</script>
</body>
</html>

