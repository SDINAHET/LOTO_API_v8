import json
from pathlib import Path
from collections import Counter, defaultdict


# Dossier dans lequel se trouve pa11y.py
BASE_DIR = Path(__file__).resolve().parent

# Fichier Pa11y source
SOURCE_JSON = BASE_DIR / "pa11y-loto-tracker_login.json"

# Fichiers générés
PRETTY_JSON = BASE_DIR / "pa11y-loto-tracker_login_pretty.json"
REPORT_MD = BASE_DIR / "RAPPORT_ACCESSIBILITE_LOGIN.md"


def main():
    if not SOURCE_JSON.exists():
        raise FileNotFoundError(
            f"Fichier introuvable : {SOURCE_JSON}"
        )

    data = json.loads(
        SOURCE_JSON.read_text(encoding="utf-8")
    )

    # Création du JSON indenté
    PRETTY_JSON.write_text(
        json.dumps(
            data,
            ensure_ascii=False,
            indent=2
        ),
        encoding="utf-8"
    )

    counts = Counter(
        item.get("type", "unknown")
        for item in data
    )

    by_message = defaultdict(list)

    for item in data:
        message = item.get(
            "message",
            "Message absent"
        )
        by_message[message].append(item)

    priority = {
        "error": 0,
        "warning": 1,
        "notice": 2,
        "unknown": 3
    }

    lines = [
        "# Rapport d’accessibilité – Page de connexion",
        "",
        "Audit automatisé réalisé avec **Pa11y / HTML_CodeSniffer**, "
        "selon le niveau **WCAG 2 AA**.",
        "",
        "## Résumé",
        "",
        f"- **Total de signalements : {len(data)}**",
        f"- **Erreurs : {counts.get('error', 0)}**",
        f"- **Avertissements : {counts.get('warning', 0)}**",
        f"- **Notifications : {counts.get('notice', 0)}**",
        "",
        "> Un avertissement Pa11y ne représente pas forcément "
        "une non-conformité certaine. Certains contrôles doivent "
        "être vérifiés manuellement.",
        "",
        "## Corrections prioritaires",
        "",
        "### 1. Iframe sans titre",
        "",
        "Une iframe injectée dans la page ne possède pas "
        "d’attribut `title` non vide.",
        "",
        "```html",
        '<iframe title="Gestion du consentement" ...></iframe>',
        "```",
        "",
        "Pour une iframe purement technique :",
        "",
        "```html",
        '<iframe',
        '  title="Contenu technique"',
        '  aria-hidden="true"',
        '  tabindex="-1"',
        '  ...>',
        "</iframe>",
        "```",
        "",
        "### 2. Structure des titres",
        "",
        "Le titre principal de la page doit être un `<h1>`.",
        "",
        "```html",
        "<h1>Loto Tracker – Connexion</h1>",
        "```",
        "",
        "### 3. Contraste des couleurs",
        "",
        "Plusieurs textes utilisent des couleurs contenant "
        "de la transparence. Le contraste doit être vérifié.",
        "",
        "- **4,5:1** pour un texte normal ;",
        "- **3:1** pour un texte de grande taille.",
        "",
        "### 4. Éléments fixes",
        "",
        "Le header, le footer et la fenêtre de cookies utilisent "
        "`position: fixed`.",
        "",
        "Il faut vérifier :",
        "",
        "- l’absence de défilement horizontal ;",
        "- l’absence de contenu masqué ;",
        "- l’affichage à une largeur de 320 pixels ;",
        "- le zoom jusqu’à 200 %.",
        "",
        "### 5. Noms accessibles",
        "",
        "Le contenu de `aria-label` doit reprendre le texte "
        "visible du lien ou du bouton.",
        "",
        "```html",
        '<a href="index.html"',
        '   aria-label="Tracker du Loto Français – retour à l’accueil">',
        "  Tracker du Loto Français",
        "</a>",
        "```",
        "",
        "### 6. Navigation principale",
        "",
        "Les liens du header peuvent être placés dans une "
        "navigation structurée.",
        "",
        "```html",
        '<nav aria-label="Navigation principale">',
        "  <ul>",
        '    <li><a href="login.html">Connexion</a></li>',
        '    <li><a href="register.html">Créer un compte</a></li>',
        "  </ul>",
        "</nav>",
        "```",
        "",
        "## Détail regroupé des signalements",
        ""
    ]

    sorted_messages = sorted(
        by_message.items(),
        key=lambda item: (
            priority.get(
                item[1][0].get("type", "unknown"),
                3
            ),
            -len(item[1])
        )
    )

    for message, items in sorted_messages:
        item_type = items[0].get(
            "type",
            "unknown"
        ).capitalize()

        lines.extend([
            f"### {item_type} — {len(items)} occurrence(s)",
            "",
            message,
            ""
        ])

        selectors = []

        for item in items:
            selector = item.get("selector")

            if selector and selector not in selectors:
                selectors.append(selector)

        if selectors:
            lines.append("Éléments concernés :")
            lines.append("")

            for selector in selectors[:12]:
                lines.append(f"- `{selector}`")

            if len(selectors) > 12:
                remaining = len(selectors) - 12
                lines.append(
                    f"- … et {remaining} autre(s)"
                )

            lines.append("")

    lines.extend([
        "## Conclusion",
        "",
        "L’audit contient principalement des avertissements "
        "nécessitant une vérification manuelle.",
        "",
        "La correction prioritaire concerne l’iframe sans titre.",
        "",
        "Les autres améliorations portent sur la hiérarchie des "
        "titres, les contrastes, les noms accessibles et le "
        "comportement responsive des éléments fixes."
    ])

    REPORT_MD.write_text(
        "\n".join(lines),
        encoding="utf-8"
    )

    print("Rapport généré avec succès.")
    print(f"JSON lisible : {PRETTY_JSON}")
    print(f"Rapport Markdown : {REPORT_MD}")


if __name__ == "__main__":
    main()
