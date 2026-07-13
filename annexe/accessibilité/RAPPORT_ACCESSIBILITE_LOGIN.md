# Rapport d’accessibilité – Page de connexion

Audit automatisé réalisé avec **Pa11y / HTML_CodeSniffer**, selon le niveau **WCAG 2 AA**.

## Résumé

- **Total de signalements : 40**
- **Erreurs : 1**
- **Avertissements : 39**
- **Notifications : 0**

> Un avertissement Pa11y ne représente pas forcément une non-conformité certaine. Certains contrôles doivent être vérifiés manuellement.

## Corrections prioritaires

### 1. Iframe sans titre

Une iframe injectée dans la page ne possède pas d’attribut `title` non vide.

```html
<iframe title="Gestion du consentement" ...></iframe>
```

Pour une iframe purement technique :

```html
<iframe
  title="Contenu technique"
  aria-hidden="true"
  tabindex="-1"
  ...>
</iframe>
```

### 2. Structure des titres

Le titre principal de la page doit être un `<h1>`.

```html
<h1>Loto Tracker – Connexion</h1>
```

### 3. Contraste des couleurs

Plusieurs textes utilisent des couleurs contenant de la transparence. Le contraste doit être vérifié.

- **4,5:1** pour un texte normal ;
- **3:1** pour un texte de grande taille.

### 4. Éléments fixes

Le header, le footer et la fenêtre de cookies utilisent `position: fixed`.

Il faut vérifier :

- l’absence de défilement horizontal ;
- l’absence de contenu masqué ;
- l’affichage à une largeur de 320 pixels ;
- le zoom jusqu’à 200 %.

### 5. Noms accessibles

Le contenu de `aria-label` doit reprendre le texte visible du lien ou du bouton.

```html
<a href="index.html"
   aria-label="Tracker du Loto Français – retour à l’accueil">
  Tracker du Loto Français
</a>
```

### 6. Navigation principale

Les liens du header peuvent être placés dans une navigation structurée.

```html
<nav aria-label="Navigation principale">
  <ul>
    <li><a href="login.html">Connexion</a></li>
    <li><a href="register.html">Créer un compte</a></li>
  </ul>
</nav>
```

## Détail regroupé des signalements

### Error — 1 occurrence(s)

Iframe element requires a non-empty title attribute that identifies the frame.

Éléments concernés :

- `html > body > iframe`

### Warning — 29 occurrence(s)

This element's text or background contains transparency. Ensure the contrast ratio between the text and background are at least 4.5:1.

Éléments concernés :

- `#brandTitle`
- `#currentTime`
- `#registerBtn > span`
- `#loginForm > div:nth-child(1) > label`
- `#loginForm > div:nth-child(2) > label`
- `#loginForm > div:nth-child(3) > a`
- `html > body > div:nth-child(2) > div > div > p`
- `#registerLink`
- `#appFooter > footer > a:nth-child(1)`
- `#appFooter > footer > a:nth-child(2)`
- `#appFooter > footer > a:nth-child(3)`
- `#openCookiePrefs > span`
- … et 17 autre(s)

### Warning — 3 occurrence(s)

This element has "position: fixed". This may require scrolling in two dimensions, which is considered a failure of this Success Criterion.

Éléments concernés :

- `#appHeader > header`
- `#appFooter > footer`
- `#cookie-popup`

### Warning — 2 occurrence(s)

Accessible name for this element does not contain the visible label text. Check that for user interface components with labels that include text or images of text, the name contains the text that is presented visually.

Éléments concernés :

- `#appHeader > header > a`
- `#appFooter > footer > a:nth-child(6)`

### Warning — 2 occurrence(s)

This element's text or background contains transparency. Ensure the contrast ratio between the text and background are at least 3:1.

Éléments concernés :

- `html > body > div:nth-child(2) > div > h2`
- `#cookie-title`

### Warning — 1 occurrence(s)

If this element contains a navigation section, it is recommended that it be marked up as a list.

Éléments concernés :

- `#appHeader > header > div`

### Warning — 1 occurrence(s)

The heading structure is not logically nested. This h2 element appears to be the primary document heading, so should be an h1 element.

Éléments concernés :

- `html > body > div:nth-child(2) > div > h2`

### Warning — 1 occurrence(s)

The heading structure is not logically nested. This h2 element should be an h1 to be properly nested.

Éléments concernés :

- `html > body > div:nth-child(2) > div > h2`

## Conclusion

L’audit contient principalement des avertissements nécessitant une vérification manuelle.

La correction prioritaire concerne l’iframe sans titre.

Les autres améliorations portent sur la hiérarchie des titres, les contrastes, les noms accessibles et le comportement responsive des éléments fixes.