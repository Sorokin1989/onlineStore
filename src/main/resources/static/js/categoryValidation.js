// Генерация slug из названия
function generateSlug(name) {
    return name
        .toLowerCase()
        .replace(/[^a-zа-яё0-9\s]/g, '')
        .replace(/\s+/g, '-')
        .replace(/[^a-z0-9-]/g, '');
}

// Автогенерация slug
const nameInput = document.querySelector('[th\\:field="*{name}"]');
const slugInput = document.querySelector('[th\\:field="*{slug}"]');

if (nameInput && slugInput) {
    nameInput.addEventListener('blur', function() {
        if (!slugInput.value) {
            slugInput.value = generateSlug(this.value);
        }
    });
}

// Валидация формы
function validateForm() {
    const nameField = document.querySelector('[th\\:field="*{name}"]');

    if (!nameField.value.trim()) {
        alert('Введите название категории');
        nameField.classList.add('is-invalid');
        return false;
    }

    return true;
}