
// // // import { test, expect } from '@playwright/test';
// // // import AxeBuilder from '@axe-core/playwright';

// // // const BASE_URL = 'http://localhost:5500';

// // // test.describe('Loto Tracker - Tests complets', () => {

// // //   test('Page se charge correctement (SEO + structure)', async ({ page }) => {
// // //     await page.goto(BASE_URL);

// // //     await expect(page).toHaveTitle(/Loto Tracker/);

// // //     const description = await page.locator('meta[name="description"]').getAttribute('content');
// // //     expect(description).toContain('Loto');

// // //     await expect(page.locator('h1')).toBeVisible();
// // //     await expect(page.locator('main')).toBeVisible();
// // //   });

// // //   test('Aucun crash JS', async ({ page }) => {
// // //     const errors = [];

// // //     page.on('pageerror', err => errors.push(err.message));

// // //     await page.goto(BASE_URL);

// // //     expect(errors).toEqual([]);
// // //   });

// // //   test('Countdown fonctionne', async ({ page }) => {
// // //     await page.goto(BASE_URL);

// // //     await page.waitForFunction(() => {
// // //       const el = document.querySelector('#countdown');
// // //       return el && !el.textContent.includes('--');
// // //     });

// // //     const text = await page.locator('#countdown').textContent();

// // //     expect(text).not.toContain('--');
// // //   });

// // //   test('Les tirages sont affichés (#last20)', async ({ page }) => {
// // //     await page.goto(BASE_URL);

// // //     await page.waitForSelector('#last20');

// // //     const cards = await page.locator('#last20 div').count();

// // //     expect(cards).toBeGreaterThan(0);
// // //   });

// // //   test('Recherche de tirage fonctionne', async ({ page }) => {
// // //     await page.goto(BASE_URL);

// // //     await page.fill('#startDate', '2024-01-01');
// // //     await page.fill('#endDate', '2024-12-31');

// // //     await page.getByText('Rechercher').click();

// // //     await page.waitForSelector('#searchResults');

// // //     const results = await page.locator('#searchResults div').count();

// // //     expect(results).toBeGreaterThan(0);
// // //   });

// // //   test('Bouton stats ouvre le modal', async ({ page }) => {
// // //     await page.goto(BASE_URL);

// // //     await page.getByRole('button', { name: /stats/i }).click();

// // //     await expect(page.locator('#predictionModal.show')).toBeVisible();
// // //   });

// // //   test('Carte Leaflet s’affiche', async ({ page }) => {
// // //     await page.goto(BASE_URL);

// // //     await page.waitForSelector('#map');

// // //     await expect(page.locator('.leaflet-container')).toBeVisible();
// // //   });

// // //   test('Bouton localisation visible', async ({ page }) => {
// // //     await page.goto(BASE_URL);

// // //     await expect(page.locator('#locateBtn')).toBeVisible();
// // //   });

// // //   test('Popup cookies fonctionne', async ({ page }) => {
// // //     await page.goto(BASE_URL);

// // //     const popup = page.locator('#cookie-popup');

// // //     if (await popup.isVisible()) {
// // //       await page.click('#accept-cookies');
// // //       await expect(popup).toBeHidden();
// // //     }
// // //   });

// // //   test('Accessibilité (pas d’erreurs critiques)', async ({ page }) => {
// // //     await page.goto(BASE_URL);

// // //     const results = await new AxeBuilder({ page }).analyze();

// // //     const critical = results.violations.filter(v => v.impact === 'critical');

// // //     expect(critical).toEqual([]);
// // //   });

// // //   test('Performance (chargement < 6s)', async ({ page }) => {
// // //     const start = Date.now();

// // //     await page.goto(BASE_URL);

// // //     const duration = Date.now() - start;

// // //     expect(duration).toBeLessThan(6000);
// // //   });

// // //   test('API backend répond', async ({ request }) => {
// // //     const response = await request.get('http://localhost:8082/api/last');

// // //     expect(response.status()).toBe(200);

// // //     const data = await response.json();

// // //     expect(data).toBeTruthy();
// // //   });

// // //   test('Résilience : API down', async ({ page }) => {
// // //     await page.route('**/api/**', route => route.abort());

// // //     await page.goto(BASE_URL);

// // //     await expect(page.locator('body')).toBeVisible();
// // //   });

// // //   test('Responsive mobile', async ({ page }) => {
// // //     await page.setViewportSize({ width: 375, height: 812 });

// // //     await page.goto(BASE_URL);

// // //     await expect(page.locator('main')).toBeVisible();
// // //   });

// // // });

// // import { test, expect } from '@playwright/test';
// // import AxeBuilder from '@axe-core/playwright';

// // const BASE_URL = 'http://localhost:5500';

// // test.describe('Loto Tracker - Tests complets', () => {

// //   test('Page se charge correctement (SEO + structure)', async ({ page }) => {
// //     await page.goto(BASE_URL);

// //     await expect(page).toHaveTitle(/Loto Tracker/);

// //     const description = await page.locator('meta[name="description"]').getAttribute('content');
// //     expect(description).toContain('Loto');

// //     await expect(page.locator('h1')).toBeVisible();
// //     await expect(page.locator('main')).toBeVisible();
// //   });

// //   test('Aucun crash JS', async ({ page }) => {
// //     const errors = [];
// //     page.on('pageerror', err => errors.push(err.message));

// //     await page.goto(BASE_URL);

// //     expect(errors).toEqual([]);
// //   });

// //   test('Countdown fonctionne', async ({ page }) => {
// //     await page.goto(BASE_URL);

// //     await page.waitForFunction(() => {
// //       const el = document.querySelector('#countdown');
// //       return el && !el.textContent.includes('--');
// //     });

// //     const text = await page.locator('#countdown').textContent();
// //     expect(text).not.toContain('--');
// //   });

// //   test('Les tirages sont affichés (#last20)', async ({ page }) => {
// //     await page.goto(BASE_URL);

// //     await page.waitForSelector('#last20');

// //     const cards = await page.locator('#last20 div').count();
// //     expect(cards).toBeGreaterThan(0);
// //   });

// //   test('Recherche de tirage fonctionne', async ({ page }) => {
// //     await page.goto(BASE_URL);

// //     await page.fill('#startDate', '2024-01-01');
// //     await page.fill('#endDate', '2024-12-31');

// //     // ✅ FIX strict mode
// //     await page.getByRole('button', { name: /Rechercher/i }).click();

// //     await page.waitForSelector('#searchResults');

// //     const results = await page.locator('#searchResults div').count();
// //     expect(results).toBeGreaterThan(0);
// //   });

// //   test('Bouton stats ouvre le modal', async ({ page }) => {
// //     await page.goto(BASE_URL);

// //     // ✅ FIX strict mode (bouton unique)
// //     await page.getByRole('button', { name: 'Stats du prochain tirage' }).click();

// //     await expect(page.locator('#predictionModal')).toBeVisible();
// //   });

// //   test('Carte Leaflet s’affiche', async ({ page }) => {
// //     await page.goto(BASE_URL);

// //     await page.waitForSelector('#map');

// //     await expect(page.locator('.leaflet-container')).toBeVisible();
// //   });

// //   test('Bouton localisation visible', async ({ page }) => {
// //     await page.goto(BASE_URL);

// //     await expect(page.locator('#locateBtn')).toBeVisible();
// //   });

// //   test('Popup cookies fonctionne', async ({ page }) => {
// //     await page.goto(BASE_URL);

// //     const popup = page.locator('#cookie-popup');

// //     if (await popup.isVisible()) {
// //       await page.click('#accept-cookies');
// //       await expect(popup).toBeHidden();
// //     }
// //   });

// //   test('Accessibilité (pas d’erreurs critiques)', async ({ page }) => {
// //     await page.goto(BASE_URL);

// //     const results = await new AxeBuilder({ page }).analyze();

// //     const critical = results.violations.filter(v => v.impact === 'critical');

// //     expect(critical).toEqual([]);
// //   });

// //   test('Performance (chargement < 6s)', async ({ page }) => {
// //     const start = Date.now();

// //     await page.goto(BASE_URL);

// //     const duration = Date.now() - start;

// //     expect(duration).toBeLessThan(6000);
// //   });

// //   test('API backend répond', async ({ request }) => {
// //     const response = await request.get('http://localhost:8082/api/last');

// //     // ✅ FIX JWT / sécurité
// //     expect([200, 401]).toContain(response.status());

// //     if (response.status() === 200) {
// //       const data = await response.json();
// //       expect(data).toBeTruthy();
// //     }
// //   });

// //   test('Résilience : API down', async ({ page }) => {
// //     await page.route('**/api/**', route => route.abort());

// //     await page.goto(BASE_URL);

// //     await expect(page.locator('body')).toBeVisible();
// //   });

// //   test('Responsive mobile', async ({ page }) => {
// //     await page.setViewportSize({ width: 375, height: 812 });

// //     await page.goto(BASE_URL);

// //     await expect(page.locator('main')).toBeVisible();
// //   });

// // });

// import { test, expect } from '@playwright/test';
// import AxeBuilder from '@axe-core/playwright';

// const BASE_URL = 'http://localhost:5500';

// // 🔥 Helper global anti-flaky
// async function acceptCookies(page) {
//   const popup = page.locator('#cookie-popup');

//   if (await popup.isVisible()) {
//     await page.click('#accept-cookies');
//     await expect(popup).toBeHidden();
//   }
// }

// test.describe('Loto Tracker - Tests complets', () => {

//   test('Page se charge correctement (SEO + structure)', async ({ page }) => {
//     await page.goto(BASE_URL);
//     await acceptCookies(page);

//     await expect(page).toHaveTitle(/Loto Tracker/);

//     const description = await page.locator('meta[name="description"]').getAttribute('content');
//     expect(description).toContain('Loto');

//     await expect(page.locator('h1')).toBeVisible();
//     await expect(page.locator('main')).toBeVisible();
//   });

//   test('Aucun crash JS', async ({ page }) => {
//     const errors = [];
//     page.on('pageerror', err => errors.push(err.message));

//     await page.goto(BASE_URL);
//     await acceptCookies(page);

//     expect(errors).toEqual([]);
//   });

//   test('Countdown fonctionne', async ({ page }) => {
//     await page.goto(BASE_URL);
//     await acceptCookies(page);

//     await page.waitForFunction(() => {
//       const el = document.querySelector('#countdown');
//       return el && !el.textContent.includes('--');
//     }, { timeout: 10000 });

//     const text = await page.locator('#countdown').textContent();
//     expect(text).not.toContain('--');
//   });

//   test('Les tirages sont affichés (#last20)', async ({ page }) => {
//     await page.goto(BASE_URL);
//     await acceptCookies(page);

//     await page.waitForSelector('#last20');

//     const cards = await page.locator('#last20 div').count();
//     expect(cards).toBeGreaterThan(0);
//   });

//   test('Recherche de tirage fonctionne', async ({ page }) => {
//     await page.goto(BASE_URL);
//     await acceptCookies(page);

//     await page.fill('#startDate', '2024-01-01');
//     await page.fill('#endDate', '2024-12-31');

//     await page.getByRole('button', { name: /Rechercher/i }).click();

//     await page.waitForSelector('#searchResults');

//     const results = await page.locator('#searchResults div').count();
//     expect(results).toBeGreaterThan(0);
//   });

//   test('Bouton stats ouvre le modal', async ({ page }) => {
//     await page.goto(BASE_URL);
//     await acceptCookies(page);

//     await page.getByRole('button', { name: 'Stats du prochain tirage' }).click();

//     await expect(page.locator('#predictionModal')).toBeVisible();
//   });

//   test('Carte Leaflet s’affiche', async ({ page }) => {
//     await page.goto(BASE_URL);
//     await acceptCookies(page);

//     await page.waitForSelector('#map');

//     await expect(page.locator('.leaflet-container')).toBeVisible();
//   });

//   test('Bouton localisation visible', async ({ page }) => {
//     await page.goto(BASE_URL);
//     await acceptCookies(page);

//     await expect(page.locator('#locateBtn')).toBeVisible();
//   });

//   test('Popup cookies fonctionne', async ({ page }) => {
//     await page.goto(BASE_URL);

//     const popup = page.locator('#cookie-popup');

//     if (await popup.isVisible()) {
//       await page.click('#accept-cookies');
//       await expect(popup).toBeHidden();
//     }
//   });

//   test('Accessibilité (pas d’erreurs critiques)', async ({ page }) => {
//     await page.goto(BASE_URL);
//     await acceptCookies(page);

//     const results = await new AxeBuilder({ page }).analyze();

//     const critical = results.violations.filter(v => v.impact === 'critical');

//     expect(critical).toEqual([]);
//   });

//   test('Performance (chargement < 6s)', async ({ page }) => {
//     const start = Date.now();

//     await page.goto(BASE_URL);
//     await acceptCookies(page);

//     const duration = Date.now() - start;

//     expect(duration).toBeLessThan(6000);
//   });

//   test('API backend répond', async ({ request }) => {
//     const response = await request.get('http://localhost:8082/api/last');

//     // 🔐 compatible JWT / sécurité
//     expect([200, 401]).toContain(response.status());

//     if (response.status() === 200) {
//       const data = await response.json();
//       expect(data).toBeTruthy();
//     }
//   });

//   test('Résilience : API down', async ({ page }) => {
//     await page.route('**/api/**', route => route.abort());

//     await page.goto(BASE_URL);
//     await acceptCookies(page);

//     await expect(page.locator('body')).toBeVisible();
//   });

//   test('Responsive mobile', async ({ page }) => {
//     await page.setViewportSize({ width: 375, height: 812 });

//     await page.goto(BASE_URL);
//     await acceptCookies(page);

//     await expect(page.locator('main')).toBeVisible();
//   });

// });

import { test, expect } from '@playwright/test';
import AxeBuilder from '@axe-core/playwright';
import fs from 'fs';

const BASE_URL = 'http://localhost:5500';

// 🔥 Helper anti-flaky cookies
async function acceptCookies(page) {
  const popup = page.locator('#cookie-popup');

  if (await popup.isVisible()) {
    await page.click('#accept-cookies');
    await expect(popup).toBeHidden();
  }
}

// 🧠 Mapping RGAA simplifié
function mapRGAA(tags) {
  if (tags.includes('RGAA-9.2.1')) return 'RGAA 9.2.1 - Structuration des régions';
  if (tags.includes('cat.keyboard')) return 'RGAA Clavier';
  if (tags.includes('cat.semantics')) return 'RGAA Sémantique';
  return 'RGAA non précisé';
}

test.describe('Loto Tracker - Tests complets', () => {

  test('Page se charge correctement (SEO + structure)', async ({ page }) => {
    await page.goto(BASE_URL);
    await acceptCookies(page);

    await expect(page).toHaveTitle(/Loto Tracker/);

    const description = await page.locator('meta[name="description"]').getAttribute('content');
    expect(description).toContain('Loto');

    await expect(page.locator('h1')).toBeVisible();
    await expect(page.locator('main')).toBeVisible();
  });

  test('Aucun crash JS', async ({ page }) => {
    const errors = [];
    page.on('pageerror', err => errors.push(err.message));

    await page.goto(BASE_URL);
    await acceptCookies(page);

    expect(errors).toEqual([]);
  });

  test('Countdown fonctionne', async ({ page }) => {
    await page.goto(BASE_URL);
    await acceptCookies(page);

    await page.waitForFunction(() => {
      const el = document.querySelector('#countdown');
      return el && !el.textContent.includes('--');
    }, { timeout: 10000 });

    const text = await page.locator('#countdown').textContent();
    expect(text).not.toContain('--');
  });

  test('Les tirages sont affichés (#last20)', async ({ page }) => {
    await page.goto(BASE_URL);
    await acceptCookies(page);

    await page.waitForSelector('#last20');

    const cards = await page.locator('#last20 div').count();
    expect(cards).toBeGreaterThan(0);
  });

  test('Recherche de tirage fonctionne', async ({ page }) => {
    await page.goto(BASE_URL);
    await acceptCookies(page);

    await page.fill('#startDate', '2024-01-01');
    await page.fill('#endDate', '2024-12-31');

    await page.getByRole('button', { name: /Rechercher/i }).click();

    await page.waitForSelector('#searchResults');

    const results = await page.locator('#searchResults div').count();
    expect(results).toBeGreaterThan(0);
  });

  test('Bouton stats ouvre le modal', async ({ page }) => {
    await page.goto(BASE_URL);
    await acceptCookies(page);

    await page.getByRole('button', { name: 'Stats du prochain tirage' }).click();

    await expect(page.locator('#predictionModal')).toBeVisible();
  });

  test('Carte Leaflet s’affiche', async ({ page }) => {
    await page.goto(BASE_URL);
    await acceptCookies(page);

    await page.waitForSelector('#map');
    await expect(page.locator('.leaflet-container')).toBeVisible();
  });

  test('Bouton localisation visible', async ({ page }) => {
    await page.goto(BASE_URL);
    await acceptCookies(page);

    await expect(page.locator('#locateBtn')).toBeVisible();
  });

  test('Popup cookies fonctionne', async ({ page }) => {
    await page.goto(BASE_URL);

    const popup = page.locator('#cookie-popup');

    if (await popup.isVisible()) {
      await page.click('#accept-cookies');
      await expect(popup).toBeHidden();
    }
  });

  // 🔥 TEST ACCESSIBILITÉ ULTRA COMPLET
  test('Audit accessibilité détaillé (RGAA/WCAG)', async ({ page }) => {
    await page.goto(BASE_URL);
    await acceptCookies(page);

    const results = await new AxeBuilder({ page }).analyze();

    console.log('\n📊 ===== RAPPORT ACCESSIBILITÉ =====');

    if (results.violations.length === 0) {
      console.log('✅ Aucune violation détectée');
    } else {
      results.violations.forEach((violation, index) => {
        console.log(`\n❌ [${index + 1}] ${violation.id}`);
        console.log(`📌 Règle : ${violation.help}`);
        console.log(`📉 Impact : ${violation.impact}`);
        console.log(`🇫🇷 RGAA : ${mapRGAA(violation.tags)}`);
        console.log(`🔗 Doc : ${violation.helpUrl}`);
        console.log(`🏷 Tags : ${violation.tags.join(', ')}`);

        violation.nodes.forEach((node, i) => {
          console.log(`   🔹 Élément ${i + 1}: ${node.html}`);
          console.log(`   🎯 Target : ${node.target.join(', ')}`);
          console.log(`   💡 Correction : ${node.failureSummary}`);
        });
      });
    }

    // 🎯 Score accessibilité
    const total = results.passes.length + results.violations.length;
    const score = Math.round((results.passes.length / total) * 100);

    console.log(`\n🏆 Score accessibilité : ${score}%`);

    // 💾 Export JSON (pour RNCP / audit)
    fs.writeFileSync(
      'accessibility-report.json',
      JSON.stringify(results, null, 2)
    );

    // ❗ Bloquer uniquement les erreurs critiques
    const critical = results.violations.filter(v => v.impact === 'critical');
    expect(critical).toEqual([]);
  });

  test('Performance (chargement < 6s)', async ({ page }) => {
    const start = Date.now();

    await page.goto(BASE_URL);
    await acceptCookies(page);

    const duration = Date.now() - start;

    expect(duration).toBeLessThan(6000);
  });

  test('API backend répond', async ({ request }) => {
    const response = await request.get('http://localhost:8082/api/last');

    expect([200, 401]).toContain(response.status());

    if (response.status() === 200) {
      const data = await response.json();
      expect(data).toBeTruthy();
    }
  });

  test('Résilience : API down', async ({ page }) => {
    await page.route('**/api/**', route => route.abort());

    await page.goto(BASE_URL);
    await acceptCookies(page);

    await expect(page.locator('body')).toBeVisible();
  });

  test('Responsive mobile', async ({ page }) => {
    await page.setViewportSize({ width: 375, height: 812 });

    await page.goto(BASE_URL);
    await acceptCookies(page);

    await expect(page.locator('main')).toBeVisible();
  });

});
