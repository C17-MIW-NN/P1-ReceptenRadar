// Function to add a new step input dynamically
function addStep() {
    const stepsContainer = document.getElementById("steps-container");

    // Count current step inputs to know the index for the new one
    const index = stepsContainer.querySelectorAll(".step").length;

    // Create a wrapper div for the new step
    const newStepDiv = document.createElement("div");
    newStepDiv.classList.add("step");

    // Create label and input
    newStepDiv.innerHTML = `
                <label>Step ${index + 1}:</label>
                <input type="text" name="steps[${index}].description" />
            `;

    stepsContainer.appendChild(newStepDiv);
}