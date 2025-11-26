document.addEventListener("DOMContentLoaded", function() {
    console.log(" Loading ingredient evenlisteners")
    const ingredientContainer = document.getElementById("ingredient-container");
    const addIngredientBtn = document.getElementById("add-ingredient-btn");

    // Voeg nieuwe ingrediënt-rij toe
    addIngredientBtn.addEventListener("click", function() {
        console.log("Add ingredient clicked");
        const index = ingredientContainer.querySelectorAll(".ingredient-row").length;

        const newRow = document.createElement("div");
        newRow.classList.add("ingredient-row");

        // Label
        const label = document.createElement("label");
        label.classList.add("ingredient-label");
        label.textContent = `Ingrediënt ${index + 1}:`;
        newRow.appendChild(label);

        // Select dropdown
        const select = document.createElement("select");
        select.name = `recipeIngredients[${index}].ingredient.ingredientId`;

        // Vul opties vanuit Thymeleaf-JSON
        allIngredients.forEach(ingredient => {
            const option = document.createElement("option");
            option.value = ingredient.ingredientId;
            option.textContent = ingredient.ingredientName;
            select.appendChild(option);
        });
        newRow.appendChild(select);

        // Quantity
        const quantityInput = document.createElement("input");
        quantityInput.type = "number";
        quantityInput.step = "0.1";
        quantityInput.name = `recipeIngredients[${index}].quantity`;
        quantityInput.placeholder = "Hoeveelheid";
        newRow.appendChild(quantityInput);

        // Unit
        const unitInput = document.createElement("input");
        unitInput.type = "text";
        unitInput.name = `recipeIngredients[${index}].unit`;
        unitInput.placeholder = "Eenheid (bijv. gram, ml)";
        newRow.appendChild(unitInput);

        // Remove button
        const removeBtn = document.createElement("button");
        removeBtn.type = "button";
        removeBtn.classList.add("remove-ingredient-btn");
        removeBtn.textContent = "Wissen";
        removeBtn.addEventListener("click", function() {
            newRow.remove();
        });
        newRow.appendChild(removeBtn);

        ingredientContainer.appendChild(newRow);
    });

    // Activeer bestaande verwijderknoppen
    document.querySelectorAll(".remove-ingredient-btn").forEach(btn => {
        btn.addEventListener("click", function() {
            btn.closest(".ingredient-row").remove();
        });
    });
});

