// Admin Dashboard JavaScript
document.addEventListener('DOMContentLoaded', function() {
    initializeDashboard();
});

function initializeDashboard() {
    // Inicializar animaciones
    initializeAnimations();
    
    // Inicializar tooltips
    initializeTooltips();
    
    // Inicializar efectos de hover
    initializeHoverEffects();
    
    // Inicializar contadores animados
    initializeCounters();
    
    // Inicializar efectos de parallax
    initializeParallax();
    
    // Inicializar responsive sidebar
    initializeResponsiveSidebar();
}

// Animaciones de entrada para las tarjetas
function initializeAnimations() {
    const cards = document.querySelectorAll('.modern-card, .stats-card');
    const observer = new IntersectionObserver((entries) => {
        entries.forEach((entry, index) => {
            if (entry.isIntersecting) {
                setTimeout(() => {
                    entry.target.style.opacity = '1';
                    entry.target.style.transform = 'translateY(0)';
                }, index * 100);
            }
        });
    }, {
        threshold: 0.1
    });

    cards.forEach(card => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(30px)';
        card.style.transition = 'all 0.6s cubic-bezier(0.4, 0, 0.2, 1)';
        observer.observe(card);
    });
}

// Inicializar tooltips de Bootstrap
function initializeTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

// Efectos de hover mejorados
function initializeHoverEffects() {
    const cards = document.querySelectorAll('.modern-card, .stats-card');
    
    cards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-8px) scale(1.02)';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0) scale(1)';
        });
    });

    // Efecto de ondas en botones
    const buttons = document.querySelectorAll('.btn-modern, .action-btn');
    buttons.forEach(button => {
        button.addEventListener('click', function(e) {
            createRippleEffect(e, this);
        });
    });
}

// Crear efecto de ondas en botones
function createRippleEffect(event, element) {
    const ripple = document.createElement('span');
    const rect = element.getBoundingClientRect();
    const size = Math.max(rect.width, rect.height);
    const x = event.clientX - rect.left - size / 2;
    const y = event.clientY - rect.top - size / 2;
    
    ripple.style.width = ripple.style.height = size + 'px';
    ripple.style.left = x + 'px';
    ripple.style.top = y + 'px';
    ripple.classList.add('ripple');
    
    // Agregar estilos CSS para el efecto
    if (!document.querySelector('#ripple-styles')) {
        const style = document.createElement('style');
        style.id = 'ripple-styles';
        style.textContent = `
            .ripple {
                position: absolute;
                border-radius: 50%;
                background: rgba(255, 255, 255, 0.6);
                transform: scale(0);
                animation: ripple-animation 0.6s linear;
                pointer-events: none;
            }
            @keyframes ripple-animation {
                to {
                    transform: scale(4);
                    opacity: 0;
                }
            }
        `;
        document.head.appendChild(style);
    }
    
    element.style.position = 'relative';
    element.style.overflow = 'hidden';
    element.appendChild(ripple);
    
    setTimeout(() => {
        ripple.remove();
    }, 600);
}

// Animación de contadores
function initializeCounters() {
    const counters = document.querySelectorAll('.stats-number');
    
    const animateCounter = (counter) => {
        const target = parseInt(counter.textContent);
        const duration = 2000; // 2 segundos
        const step = target / (duration / 16); // 60 FPS
        let current = 0;
        
        const timer = setInterval(() => {
            current += step;
            if (current >= target) {
                counter.textContent = target;
                clearInterval(timer);
            } else {
                counter.textContent = Math.floor(current);
            }
        }, 16);
    };

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                animateCounter(entry.target);
                observer.unobserve(entry.target);
            }
        });
    });

    counters.forEach(counter => {
        observer.observe(counter);
    });
}

// Efecto parallax sutil
function initializeParallax() {
    const sidebarHeader = document.querySelector('.sidebar-header');
    if (sidebarHeader) {
        window.addEventListener('scroll', () => {
            const scrolled = window.pageYOffset;
            const rate = scrolled * -0.3;
            sidebarHeader.style.transform = `translateY(${rate}px)`;
        });
    }
}

// Sidebar responsivo
function initializeResponsiveSidebar() {
    const sidebar = document.querySelector('.sidebar');
    const content = document.querySelector('.content');
    const toggleBtn = document.querySelector('#sidebar-toggle');
    
    // Crear botón de toggle si no existe
    if (!toggleBtn && window.innerWidth <= 768) {
        createSidebarToggle();
    }
    
    // Manejar resize de ventana
    window.addEventListener('resize', () => {
        if (window.innerWidth <= 768) {
            if (!document.querySelector('#sidebar-toggle')) {
                createSidebarToggle();
            }
            sidebar.classList.add('mobile-hidden');
            content.style.marginLeft = '0';
        } else {
            const toggle = document.querySelector('#sidebar-toggle');
            if (toggle) toggle.remove();
            sidebar.classList.remove('mobile-hidden');
            content.style.marginLeft = '280px';
        }
    });
    
    // Aplicar estado inicial en móvil
    if (window.innerWidth <= 768) {
        sidebar.classList.add('mobile-hidden');
        content.style.marginLeft = '0';
    }
}

// Crear botón de toggle para móvil
function createSidebarToggle() {
    const toggleBtn = document.createElement('button');
    toggleBtn.id = 'sidebar-toggle';
    toggleBtn.className = 'btn btn-primary position-fixed';
    toggleBtn.style.cssText = `
        top: 20px;
        left: 20px;
        z-index: 1001;
        border-radius: 50%;
        width: 50px;
        height: 50px;
        display: flex;
        align-items: center;
        justify-content: center;
    `;
    toggleBtn.innerHTML = '<i class="fas fa-bars"></i>';
    
    toggleBtn.addEventListener('click', toggleSidebar);
    document.body.appendChild(toggleBtn);
}

// Toggle sidebar en móvil
function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    const overlay = document.querySelector('.sidebar-overlay');
    
    if (sidebar.classList.contains('mobile-hidden')) {
        sidebar.classList.remove('mobile-hidden');
        createOverlay();
    } else {
        sidebar.classList.add('mobile-hidden');
        if (overlay) overlay.remove();
    }
}

// Crear overlay para móvil
function createOverlay() {
    const overlay = document.createElement('div');
    overlay.className = 'sidebar-overlay';
    overlay.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.5);
        z-index: 999;
        backdrop-filter: blur(2px);
    `;
    
    overlay.addEventListener('click', toggleSidebar);
    document.body.appendChild(overlay);
}

// Función para actualizar datos en tiempo real (opcional)
function updateDashboardData() {
    // Esta función puede ser llamada periódicamente para actualizar datos
    fetch('/api/dashboard/stats')
        .then(response => response.json())
        .then(data => {
            document.querySelector('[data-stat="personas"]').textContent = data.totalPersonas;
            document.querySelector('[data-stat="estudiantes"]').textContent = data.totalEstudiantes;
            document.querySelector('[data-stat="profesores"]').textContent = data.totalProfesores;
            document.querySelector('[data-stat="cursos"]').textContent = data.totalCursos;
        })
        .catch(error => console.log('Error actualizando datos:', error));
}

// Función para mostrar notificaciones
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `alert alert-${type} position-fixed`;
    notification.style.cssText = `
        top: 20px;
        right: 20px;
        z-index: 1050;
        min-width: 300px;
        animation: slideInRight 0.3s ease;
    `;
    notification.innerHTML = `
        <div class="d-flex align-items-center">
            <i class="fas fa-info-circle me-2"></i>
            <span>${message}</span>
            <button type="button" class="btn-close ms-auto" onclick="this.parentElement.parentElement.remove()"></button>
        </div>
    `;
    
    // Agregar animación CSS si no existe
    if (!document.querySelector('#notification-styles')) {
        const style = document.createElement('style');
        style.id = 'notification-styles';
        style.textContent = `
            @keyframes slideInRight {
                from {
                    transform: translateX(100%);
                    opacity: 0;
                }
                to {
                    transform: translateX(0);
                    opacity: 1;
                }
            }
        `;
        document.head.appendChild(style);
    }
    
    document.body.appendChild(notification);
    
    // Auto-remover después de 5 segundos
    setTimeout(() => {
        if (notification.parentElement) {
            notification.remove();
        }
    }, 5000);
}

// Exportar funciones para uso global
window.dashboardUtils = {
    updateDashboardData,
    showNotification,
    toggleSidebar
};
