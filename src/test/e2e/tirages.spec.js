import { test, expect } from '@playwright/test';

test('Les tirages sont affichés', async ({ page }) => {
  await page.goto('http://localhost:5500');

  // Section tirages
  await expect(page.getByText('Derniers tirages')).toBeVisible();

  // Cartes présentes
  const cards = await page.locator('text=Détail du tirage').count();
  expect(cards).toBeGreaterThan(0);

  // Vérifie présence numéros (cercles)
  const numbers = await page.locator('.ball, .number, span').count();
  expect(numbers).toBeGreaterThan(5);
});
