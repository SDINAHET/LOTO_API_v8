/*
=========================================================
 LOTTERY MACHINE - LOTO TRACKER RNCP6
 PREMIUM SIDEBAR VERSION
=========================================================
*/

let machineCanvas = null;
let mctx = null;

function initLotteryCanvas() {

  machineCanvas =
    document.getElementById(
      "lotteryMachine"
    );

  if (!machineCanvas) {

    console.warn(
      "Canvas lotteryMachine introuvable"
    );

    return false;
  }

  mctx =
    machineCanvas.getContext("2d");

  return true;
}

/*
=========================================================
BACKGROUND
=========================================================
*/

const lotteryBackground =
  new Image();

lotteryBackground.src =
  "/assets/img/backrgound.png";

/*
=========================================================
HELPERS
=========================================================
*/

function getCanvasWidth() {

  return machineCanvas.width /
    (window.devicePixelRatio || 1);
}

function getCanvasHeight() {

  return machineCanvas.height /
    (window.devicePixelRatio || 1);
}

function getCenterX() {

  return getCanvasWidth() / 2;
}

function getCenterY() {

  return getCanvasHeight() / 2;
}

function rand(min, max) {

  return Math.random() *
    (max - min) + min;
}

function wait(ms) {

  return new Promise(resolve => {

    setTimeout(resolve, ms);

  });
}

/*
=========================================================
GLOBALS
=========================================================
*/

const MACHINE_RADIUS = 64;

const MAIN_BALL_COLOR =
  "#60a5fa";

const CHANCE_BALL_COLOR =
  "#ff4d73";

let allBalls = [];

let spinning = false;

let animationFrame = null;

let currentLotteryDate =
  "Chargement...";

/*
=========================================================
RESIZE
=========================================================
*/

function resizeLotteryCanvas() {

  if (!machineCanvas || !mctx)
    return;

  const ratio =
    window.devicePixelRatio || 1;

  const width =
    machineCanvas.offsetWidth;

  const height = 420;

  machineCanvas.width =
    width * ratio;

  machineCanvas.height =
    height * ratio;

  machineCanvas.style.height =
    `${height}px`;

  mctx.setTransform(
    1, 0, 0, 1, 0, 0
  );

  mctx.scale(ratio, ratio);
}

/*
=========================================================
CREATE BALL
=========================================================
*/

function createBall(
  number,
  type = "main"
) {

  const angle =
    Math.random() * Math.PI * 2;

  return {

    number,
    type,

    orbitRadius:
      rand(12, 52),

    orbitAngle:
      angle,

    orbitSpeed:
      rand(.003, .006),

    floatOffset:
      rand(0, 9999),

    radius:
      type === "chance"
        ? rand(9, 10)
        : rand(10, 11),

    x: 0,
    y: 0,

    extracted: false
  };
}

/*
=========================================================
DRAW BALL
=========================================================
*/

function drawBall(
  x,
  y,
  radius,
  text,
  color,
  isChance = false
) {

  if (!mctx) return;

  mctx.save();

  mctx.shadowColor =
    color;

  mctx.shadowBlur =
    isChance ? 18 : 12;

  const gradient =
    mctx.createRadialGradient(
      x - radius * .3,
      y - radius * .3,
      2,
      x,
      y,
      radius
    );

  gradient.addColorStop(
    0,
    "#ffffff"
  );

  gradient.addColorStop(
    .25,
    isChance
      ? "#ffb3c4"
      : "#cce4ff"
  );

  gradient.addColorStop(
    1,
    color
  );

  mctx.beginPath();

  mctx.arc(
    x,
    y,
    radius,
    0,
    Math.PI * 2
  );

  mctx.fillStyle =
    gradient;

  mctx.fill();

  /*
  REFLECTION
  */

  mctx.beginPath();

  mctx.arc(
    x - radius * .25,
    y - radius * .25,
    radius * .18,
    0,
    Math.PI * 2
  );

  mctx.fillStyle =
    "rgba(255,255,255,.55)";

  mctx.fill();

  /*
  TEXT
  */

  mctx.shadowBlur = 0;

  mctx.fillStyle =
    "#ffffff";

  mctx.font =
    `900 ${radius * .72}px Inter`;

  mctx.textAlign =
    "center";

  mctx.textBaseline =
    "middle";

  mctx.fillText(
    text,
    x,
    y + .5
  );

  mctx.restore();
}

/*
=========================================================
DRAW BACKGROUND
=========================================================
*/

function drawBackground(
  width,
  height
) {

  if (
    lotteryBackground.complete &&
    lotteryBackground.width
  ) {

    mctx.drawImage(
      lotteryBackground,
      0,
      0,
      width,
      height
    );

  } else {

    mctx.fillStyle =
      "#020617";

    mctx.fillRect(
      0,
      0,
      width,
      height
    );
  }
}

/*
=========================================================
DRAW MACHINE
=========================================================
*/

function drawMachine() {

  if (!mctx || !machineCanvas)
    return;

  const width =
    getCanvasWidth();

  const height =
    getCanvasHeight();

  const centerX =
    getCenterX();

  const centerY = 170;

  const sphereRadius = 95;

  /*
  CLEAR
  */

  mctx.clearRect(
    0,
    0,
    width,
    height
  );

  /*
  BACKGROUND
  */

  drawBackground(
    width,
    height
  );

  /*
  DATE
  */

  mctx.save();

  mctx.font =
    "900 18px Inter";

  mctx.fillStyle =
    "#ffffff";

  mctx.textAlign =
    "center";

  mctx.shadowColor =
    "rgba(255,255,255,.45)";

  mctx.shadowBlur = 12;

  mctx.fillText(
    `Tirage du ${currentLotteryDate}`,
    centerX,
    34
  );

  mctx.restore();

  /*
  NEON BASE
  */

  const baseY =
    centerY + 118;

  const neonGradient =
    mctx.createLinearGradient(
      centerX - 100,
      baseY,
      centerX + 100,
      baseY
    );

  neonGradient.addColorStop(
    0,
    "#2563eb"
  );

  neonGradient.addColorStop(
    .5,
    "#7c3aed"
  );

  neonGradient.addColorStop(
    1,
    "#ff2da0"
  );

  mctx.beginPath();

  mctx.ellipse(
    centerX,
    baseY,
    96,
    18,
    0,
    0,
    Math.PI * 2
  );

  mctx.strokeStyle =
    neonGradient;

  mctx.lineWidth = 6;

  mctx.shadowBlur = 25;

  mctx.shadowColor =
    "#60a5fa";

  mctx.stroke();

  /*
  GLASS SPHERE
  */

  const glassGradient =
    mctx.createRadialGradient(
      centerX - 35,
      centerY - 40,
      10,
      centerX,
      centerY,
      sphereRadius
    );

  glassGradient.addColorStop(
    0,
    "rgba(255,255,255,.16)"
  );

  glassGradient.addColorStop(
    .5,
    "rgba(255,255,255,.04)"
  );

  glassGradient.addColorStop(
    1,
    "rgba(255,255,255,.01)"
  );

  mctx.beginPath();

  mctx.arc(
    centerX,
    centerY,
    sphereRadius,
    0,
    Math.PI * 2
  );

  mctx.fillStyle =
    glassGradient;

  mctx.fill();

  mctx.lineWidth = 2.5;

  mctx.strokeStyle =
    "rgba(255,255,255,.30)";

  mctx.stroke();

  /*
  GLOW
  */

  mctx.beginPath();

  mctx.arc(
    centerX,
    centerY,
    sphereRadius + 2,
    0,
    Math.PI * 2
  );

  mctx.strokeStyle =
    "rgba(96,165,250,.10)";

  mctx.lineWidth = 8;

  mctx.shadowBlur = 28;

  mctx.shadowColor =
    "#60a5fa";

  mctx.stroke();

  /*
  REFLECTION
  */

  mctx.beginPath();

  mctx.ellipse(
    centerX - 30,
    centerY - 42,
    38,
    12,
    -.4,
    0,
    Math.PI * 2
  );

  mctx.fillStyle =
    "rgba(255,255,255,.22)";

  mctx.fill();

  /*
  CLIP INSIDE SPHERE
  */

  mctx.save();

  mctx.beginPath();

  mctx.arc(
    centerX,
    centerY,
    sphereRadius - 4,
    0,
    Math.PI * 2
  );

  mctx.clip();

  /*
  BALLS
  */

  for (const ball of allBalls) {

    if (ball.extracted)
      continue;

    if (spinning) {

      ball.orbitAngle +=
        ball.orbitSpeed;
    }

    const floatY =
      Math.sin(
        performance.now() * .0012 +
        ball.floatOffset
      ) * 2.5;

    ball.x =
      centerX +
      Math.cos(ball.orbitAngle)
      * ball.orbitRadius;

    ball.y =
      centerY +
      Math.sin(ball.orbitAngle)
      * ball.orbitRadius +
      floatY;

    /*
    DEPTH
    */

    const depth =
      (ball.y - centerY + 60)
      / 120;

    const scale =
      .72 + depth * .28;

    /*
    SHADOW
    */

    mctx.beginPath();

    mctx.ellipse(
      ball.x,
      baseY - 6,
      ball.radius * scale,
      2,
      0,
      0,
      Math.PI * 2
    );

    mctx.fillStyle =
      "rgba(0,0,0,.10)";

    mctx.fill();

    /*
    BALL
    */

    drawBall(
      ball.x,
      ball.y,
      ball.radius * scale,
      ball.number,

      ball.type === "chance"
        ? CHANCE_BALL_COLOR
        : MAIN_BALL_COLOR,

      ball.type === "chance"
    );
  }

  mctx.restore();

  animationFrame =
    requestAnimationFrame(
      drawMachine
    );
}

/*
=========================================================
RESET
=========================================================
*/

function resetMachine() {

  allBalls = [];

  if (animationFrame) {

    cancelAnimationFrame(
      animationFrame
    );
  }
}

/*
=========================================================
RESULT BOX ONLY
=========================================================
*/

function updateLiveResult(
  numbers,
  chance = null
) {

  const resultBox =
    document.getElementById(
      "drawResultText"
    );

  if (!resultBox) return;

  resultBox.innerHTML = `

    <div class="lottery-title-live">
      Résultat du Loto
    </div>

    <div class="live-result-balls compact">

      ${numbers.map(n => `
        <div class="live-ball small">
          ${n}
        </div>
      `).join("")}

      ${
        chance !== null
        ? `
          <div class="live-ball chance small">
            ${chance}
          </div>
        `
        : ""
      }

    </div>
  `;
}

/*
=========================================================
MAIN ANIMATION
=========================================================
*/

async function launchLotteryMachine() {

  if (spinning)
    return;

  spinning = true;

  resetMachine();

  let latestDraw = null;

  try {

    const response =
      await fetch(
        `${window.API_BASE}/api/historique/last`,
        {
          credentials: "include"
        }
      );

    if (!response.ok) {

      throw new Error(
        "Erreur API"
      );
    }

    latestDraw =
      await response.json();

  } catch (error) {

    console.error(
      "Impossible de charger le tirage",
      error
    );

    latestDraw = {

      boule1: 7,
      boule2: 18,
      boule3: 27,
      boule4: 35,
      boule5: 48,

      numeroChance: 5,

      dateDeTirage:
        "06/05/2026"
    };
  }

  currentLotteryDate =
    latestDraw.dateDeTirage;

  /*
  CREATE BALLS
  */

  for (let i = 1; i <= 20; i++) {

    allBalls.push(
      createBall(i, "main")
    );
  }

  for (let i = 1; i <= 8; i++) {

    allBalls.push(
      createBall(i, "chance")
    );
  }

  drawMachine();

  await wait(4500);

  spinning = false;

  /*
  RESULTS ONLY BELOW
  */

  updateLiveResult(
    [
      latestDraw.boule1,
      latestDraw.boule2,
      latestDraw.boule3,
      latestDraw.boule4,
      latestDraw.boule5
    ],
    latestDraw.numeroChance
  );
}

/*
=========================================================
START
=========================================================
*/

document.addEventListener(
  "layout:ready",
  () => {

    if (!initLotteryCanvas())
      return;

    resizeLotteryCanvas();

    drawMachine();

    setTimeout(() => {

      launchLotteryMachine();

    }, 1000);
  }
);

window.addEventListener(
  "resize",
  resizeLotteryCanvas
);


}
