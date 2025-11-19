/**
 * Registreert een modal met een open-knop en zorgt dat deze geopend en gesloten kan worden.
 * @param {string} modalId - ID van de modal (bijv. "addRecipeModal")
 * @param {string} openButtonId - ID van de knop die de modal opent (bijv. "openAddModalButton")
 */



function registerModal(modalId, openButtonId) {
    const modal = document.getElementById(modalId);
    const openBtn = document.getElementById(openButtonId);
    const closeBtn = modal.querySelector('.close');

    // Open modal bij klikken op de knop
    openBtn.addEventListener('click', () => {
        modal.style.display = 'block';
    });

    // Sluit modal bij klikken op de '×'
    closeBtn.addEventListener('click', () => {
        modal.style.display = 'none';
    });

    // Sluit modal bij klikken buiten de content
    window.addEventListener('click', (event) => {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
}

document.addEventListener('DOMContentLoaded', () => {
    registerModal('addRecipeModal', 'openAddModalButton');
    registerModal('editRecipeModal', 'openEditModalButton');
});
