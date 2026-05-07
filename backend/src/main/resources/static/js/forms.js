document.querySelectorAll(".validate-form").forEach((form) => {
  form.addEventListener("submit", (event) => {
    const requiredFields = form.querySelectorAll("[required]");
    const hasEmptyField = Array.from(requiredFields).some((field) => !field.value.trim());

    if (hasEmptyField) {
      event.preventDefault();
      alert("Please fill in all required fields.");
    }
  });
});
