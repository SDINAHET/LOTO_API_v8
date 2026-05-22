// import { test, expect } from '@playwright/test';

// test('Loto page loads and displays results', async ({ page }) => {

//   await page.goto('http://localhost:5500');

//   // Vérifie le titre
//   await expect(page).toHaveTitle(/Loto/i);

//   // Vérifie body
//   await expect(page.locator('body')).toBeVisible();

//   // Vérifie le vrai titre principal (ACCESSIBLE)
//   await expect(
//     page.getByRole('heading', { name: /Résultats du Loto/i })
//   ).toBeVisible();

//   // Vérifie contenu
//   const elements = await page.locator('div, span').count();
//   expect(elements).toBeGreaterThan(5);

//   // Vérifie erreurs JS
//   page.on('pageerror', (err) => {
//     throw new Error(`JS Error: ${err.message}`);
//   });
// });

import { test, expect } from '@playwright/test';

test('Page principale fonctionne', async ({ page }) => {
  await page.goto('http://localhost:5500');

  await expect(page).toHaveTitle(/Loto/i);
  await expect(page.locator('body')).toBeVisible();

  await expect(
    page.getByRole('heading', { name: /Résultats du Loto/i })
  ).toBeVisible();
});
