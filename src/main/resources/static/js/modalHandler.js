function setupModal(modal, openBtn) {
    const closeBtn = modal.querySelector('.close');

    const openModal = () => modal.style.display = 'block';
    const closeModal = () => modal.style.display = 'none';

    openBtn.addEventListener('click', openModal);
    if (closeBtn) closeBtn.addEventListener('click', closeModal);

    window.addEventListener('click', (event) => {
        if (event.target === modal) closeModal();
    });

    document.addEventListener('keydown', (event) => {
        if (event.key === 'Escape' && modal.style.display === 'block') {
            closeModal();
        }
    });
}

function waitForModal(modalId, openButtonId) {
    const tryRegister = () => {
        const modal = document.getElementById(modalId);
        const openBtn = document.getElementById(openButtonId);
        if (modal && openBtn) {
            setupModal(modal, openBtn);
            return true;
        }
        return false;
    };

    if (tryRegister()) return;

    const observer = new MutationObserver(() => {
        if (tryRegister()) observer.disconnect();
    });

    observer.observe(document.body, { childList: true, subtree: true });
}

// Voeg hier modals toe
const modalConfigs = [
    { modalId: 'addRecipeModal', openButtonId: 'openAddModalButton' },
    { modalId: 'editRecipeModal', openButtonId: 'openEditModalButton' },
];

document.addEventListener('DOMContentLoaded', () => {
    modalConfigs.forEach(({ modalId, openButtonId }) => {
        waitForModal(modalId, openButtonId);
    });
});