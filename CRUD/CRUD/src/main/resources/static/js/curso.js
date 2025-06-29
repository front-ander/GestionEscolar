// JavaScript para el módulo de Cursos

document.addEventListener('DOMContentLoaded', function() {
    console.log('Módulo de Cursos cargado');
    
    // Inicializar tooltips
    initializeTooltips();
    
    // Inicializar animaciones
    initializeAnimations();
    
    // Configurar eventos de confirmación
    setupConfirmations();
});

function initializeTooltips() {
    // Los tooltips se inicializan en el HTML principal
    console.log('Tooltips inicializados');
}

function initializeAnimations() {
    // Animación de entrada para las tarjetas de estadísticas
    const statsCards = document.querySelectorAll('.stats-card');
    statsCards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            card.style.transition = 'all 0.6s ease';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, index * 100);
    });
    
    // Animación para la tabla
    const tableContainer = document.querySelector('.table-container');
    if (tableContainer) {
        tableContainer.style.opacity = '0';
        setTimeout(() => {
            tableContainer.style.transition = 'opacity 0.8s ease';
            tableContainer.style.opacity = '1';
        }, 300);
    }
}

function setupConfirmations() {
    // Configurar confirmaciones para eliminaciones
    const deleteButtons = document.querySelectorAll('a[href*="/delete/"]');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (!confirm('¿Está seguro de eliminar este curso? Esta acción no se puede deshacer.')) {
                e.preventDefault();
            }
        });
    });
}

// Función para mostrar notificaciones
function showNotification(message, type = 'info') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    const container = document.querySelector('.container');
    container.insertBefore(alertDiv, container.firstChild);
    
    // Auto-dismiss después de 5 segundos
    setTimeout(() => {
        if (alertDiv.parentNode) {
            alertDiv.remove();
        }
    }, 5000);
}

// Función para validar formularios de cursos
function validateCursoForm() {
    const nombre = document.getElementById('nombre');
    if (nombre && nombre.value.trim().length < 3) {
        showNotification('El nombre del curso debe tener al menos 3 caracteres', 'danger');
        return false;
    }
    return true;
}

// Función para actualizar estadísticas en tiempo real
function updateStats() {
    const totalCursos = document.querySelectorAll('tbody tr').length;
    const statsNumbers = document.querySelectorAll('.stats-number');
    
    statsNumbers.forEach(stat => {
        stat.textContent = totalCursos;
    });
}

// Función para buscar cursos
function searchCursos(query) {
    const rows = document.querySelectorAll('tbody tr');
    const searchTerm = query.toLowerCase();
    
    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        if (text.includes(searchTerm)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

// Exportar funciones para uso global
window.cursoModule = {
    showNotification,
    validateCursoForm,
    updateStats,
    searchCursos
}; 