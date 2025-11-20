document.addEventListener('DOMContentLoaded', () => {
    const container = document.getElementById('step-container');
    const addBtn = document.getElementById('add-step-btn');

    function renumber() {
        container.querySelectorAll('.step-row').forEach((row, i) => {
            row.querySelector('.step-label').textContent = `Stap ${i + 1}:`;
            row.querySelector('input').name = `directions[${i}].direction`;
        });
    }

    function addStep() {
        const div = document.createElement('div');
        div.classList.add('step-row');
        div.innerHTML = `
            <label class="step-label"></label>
            <input type="text" />
            <button type="button" class="remove-step-btn button-styling">Wissen</button>
        `;
        container.appendChild(div);


        div.querySelector('.remove-step-btn').addEventListener('click', () => {
            div.remove();
            renumber();
        });

        renumber();
    }

    addStep();

    addBtn.addEventListener('click', addStep);

    container.querySelectorAll('.remove-step-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            btn.closest('.step-row').remove();
            renumber();
        });
    });
});

    container.querySelectorAll('.remove-step-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            btn.closest('.step-row').remove();
            renumber();
        });


        addBtn.addEventListener('click', () => {
        const div = document.createElement('div');
        div.classList.add('step-row');
        div.innerHTML = `
            <label class="step-label"></label>
            <input type="text" />
            <button type="button" class="remove-step-btn button-styling">Wissen</button>
        `;
        container.appendChild(div);

        div.querySelector('.remove-step-btn').addEventListener('click', () => {
            div.remove();
            renumber();
        });

        renumber();
    });

    container.querySelectorAll('.remove-step-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            btn.closest('.step-row').remove();
            renumber();
        });
    });


});