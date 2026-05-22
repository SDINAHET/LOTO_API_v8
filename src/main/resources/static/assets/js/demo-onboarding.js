/* =========================================
   LOAD COMPONENT
========================================= */

async function loadDemoComponent(){

  const container =
    document.getElementById("demoContainer");

  if(!container){
    return;
  }

  const response =
    await fetch("assets/components/demo.html");

  const html =
    await response.text();

  container.innerHTML = html;

  initDemo();
}

/* =========================================
   INIT DEMO
========================================= */

function initDemo(){

  const demoSteps = [

    {
      title: "Consultez les résultats du Loto",
      text: "Accédez rapidement aux derniers tirages."
    },

    {
      title: "Vérifiez vos tickets automatiquement",
      text: "Comparez vos numéros avec les résultats officiels."
    },

    {
      title: "Suivez vos statistiques",
      text: "Analysez vos historiques et fréquences de jeu."
    },

    {
      title: "Créez un compte gratuitement",
      text: "Enregistrez vos tickets et retrouvez-les dans votre espace personnel."
    }

  ];

  let currentStep = 0;

  const overlay =
    document.getElementById("demoOverlay");

  const title =
    document.getElementById("demoTitle");

  const text =
    document.getElementById("demoText");

  const nextBtn =
    document.getElementById("demoNextBtn");

  const skipBtn =
    document.getElementById("demoSkipBtn");

  const dots =
    document.querySelectorAll(".demo-dot");

  function updateStep(){

    title.textContent =
      demoSteps[currentStep].title;

    text.textContent =
      demoSteps[currentStep].text;

    dots.forEach(dot =>
      dot.classList.remove("active")
    );

    dots[currentStep]
      .classList.add("active");

    if(currentStep === demoSteps.length - 1){

      nextBtn.textContent =
        "Créer un compte";
    }
  }

  function showDemo(){

    const alreadySeen =
      sessionStorage.getItem("demo_seen");

    const token =
      localStorage.getItem("token");

    if(alreadySeen || token){
      return;
    }

    overlay.classList.remove("hidden");

    sessionStorage.setItem(
      "demo_seen",
      "1"
    );

    updateStep();
  }

  setTimeout(showDemo, 15000);

  nextBtn.addEventListener("click", () => {

    currentStep++;

    if(currentStep >= demoSteps.length){

      window.location.href =
        "/register.html";

      return;
    }

    updateStep();
  });

  skipBtn.addEventListener("click", () => {

    overlay.classList.add("hidden");
  });
}

/* =========================================
   START
========================================= */

document.addEventListener(
  "DOMContentLoaded",
  loadDemoComponent
);
