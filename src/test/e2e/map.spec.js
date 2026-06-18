import { test, expect } from '@playwright/test';

test('La carte est affichée', async ({ page }) => {
  await page.goto('http://localhost:5500');

  await expect(page.locator('.leaflet-container')).toBeVisible();

  await expect(page.locator('#locateBtn')).toBeVisible();
});
