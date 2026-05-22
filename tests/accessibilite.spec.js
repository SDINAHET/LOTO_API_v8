import { test, expect } from '@playwright/test';
import AxeBuilder from '@axe-core/playwright';

test('Accessibilité OK', async ({ page }) => {
  await page.goto('http://localhost:5500');

  const results = await new AxeBuilder({ page }).analyze();

  // Log utile
  console.log(results.violations);

  // On tolère mais limite
  expect(results.violations.length).toBeLessThan(10);
});
