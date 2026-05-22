import { defineConfig } from '@playwright/test';

export default defineConfig({
  testDir: './src/test/e2e',

  timeout: 30000,

  use: {
    baseURL: 'http://127.0.0.1:5500', // 🔥 FIX WSL
    headless: true,
    trace: 'on-first-retry',
    screenshot: 'only-on-failure',
    video: 'retain-on-failure'
  },

  reporter: [
    ['list'],
    ['html', { open: 'never' }] // 🔥 évite bug auto-open WSL
  ]
});
