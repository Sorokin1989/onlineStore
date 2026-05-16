// Генерация slug из названия
function generateSlug(name) {
    const slugField = document.querySelector('[th\\:field="*{slug}"]');
    if (slugField && !slugField.value) {
        slugField.value = name
            .toLowerCase()
            .replace(/[^a-zа-яё0-9\s]/g, '')
            .replace(/\s+/g, '-')
            .replace(/[^a-z0-9-]/g, '');
    }
}

// Валидация формы
function validateForm() {
    let isValid = true;

    // Получаем поля
    const nameField = document.querySelector('[th\\:field="*{name}"]');
    const priceField = document.querySelector('[th\\:field="*{price}"]');
    const categoryField = document.querySelector('[th\\:field="*{categoryId}"]');

    // Очищаем предыдущие ошибки
    document.querySelectorAll('.is-invalid').forEach(el => {
        el.classList.remove('is-invalid');
    });

    // Валидация названия
    if (!nameField || !nameField.value.trim()) {
        if (nameField) nameField.classList.add('is-invalid');
        isValid = false;
    }

    // Валидация цены
    if (!priceField || !priceField.value || parseFloat(priceField.value) <= 0) {
        if (priceField) priceField.classList.add('is-invalid');
        isValid = false;
    }

    // Валидация категории
    if (!categoryField || !categoryField.value) {
        if (categoryField) categoryField.classList.add('is-invalid');
        isValid = false;
    }

    if (!isValid) {
        alert('Пожалуйста, заполните обязательные поля:\n- Название\n- Цена\n- Категория');
    }

    return isValid;
}

// Добавляем валидацию при потере фокуса
document.addEventListener('DOMContentLoaded', function() {
    const nameField = document.querySelector('[th\\:field="*{name}"]');
    const priceField = document.querySelector('[th\\:field="*{price}"]');
    const categoryField = document.querySelector('[th\\:field="*{categoryId}"]');

    if (nameField) {
        nameField.addEventListener('blur', function() {
            if (!this.value.trim()) {
                this.classList.add('is-invalid');
            } else {
                this.classList.remove('is-invalid');
            }
        });
    }

    if (priceField) {
        priceField.addEventListener('blur', function() {
            if (!this.value || parseFloat(this.value) <= 0) {
                this.classList.add('is-invalid');
            } else {
                this.classList.remove('is-invalid');
            }
        });
    }

    if (categoryField) {
        categoryField.addEventListener('change', function() {
            if (!this.value) {
                this.classList.add('is-invalid');
            } else {
                this.classList.remove('is-invalid');
            }
        });
    }
});

