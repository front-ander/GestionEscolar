// Funcionalidades para el módulo de Pagos

const PagoUtils = {
    
    // Inicializar el módulo
    init: function() {
        this.setupEventListeners();
        this.setupFormValidation();
        this.setupAmountFormatting();
        this.setupDatePickers();
        this.setupAutoComplete();
    },
    
    // Configurar event listeners
    setupEventListeners: function() {
        // Formateo automático de monto
        const montoInput = document.querySelector('input[name="monto"]');
        if (montoInput) {
            montoInput.addEventListener('input', this.formatAmount.bind(this));
            montoInput.addEventListener('blur', this.finalizeAmountFormat.bind(this));
        }
        
        // Validación de monto mínimo
        if (montoInput) {
            montoInput.addEventListener('change', this.validateAmount.bind(this));
        }
        
        // Búsqueda en tiempo real
        const searchInput = document.querySelector('.table-search');
        if (searchInput) {
            searchInput.addEventListener('input', this.handleSearch.bind(this));
        }
        
        // Filtros de estado
        const estadoFilter = document.querySelector('.estado-filter');
        if (estadoFilter) {
            estadoFilter.addEventListener('change', this.handleEstadoFilter.bind(this));
        }
        
        // Filtros de mes/año
        const mesFilter = document.querySelector('.mes-filter');
        const anioFilter = document.querySelector('.anio-filter');
        if (mesFilter) {
            mesFilter.addEventListener('change', this.handleMesFilter.bind(this));
        }
        if (anioFilter) {
            anioFilter.addEventListener('change', this.handleAnioFilter.bind(this));
        }
    },
    
    // Formatear monto mientras se escribe
    formatAmount: function(event) {
        let value = event.target.value.replace(/[^\d.]/g, '');
        
        // Permitir solo un punto decimal
        const parts = value.split('.');
        if (parts.length > 2) {
            value = parts[0] + '.' + parts.slice(1).join('');
        }
        
        // Limitar a 2 decimales
        if (parts.length === 2 && parts[1].length > 2) {
            value = parts[0] + '.' + parts[1].substring(0, 2);
        }
        
        event.target.value = value;
    },
    
    // Finalizar formato de monto
    finalizeAmountFormat: function(event) {
        let value = parseFloat(event.target.value);
        if (!isNaN(value)) {
            event.target.value = value.toFixed(2);
        }
    },
    
    // Validar monto
    validateAmount: function(event) {
        const value = parseFloat(event.target.value);
        if (value <= 0) {
            this.showAlert('El monto debe ser mayor a 0', 'error');
            event.target.value = '';
            event.target.focus();
        }
    },
    
    // Configurar validación de formulario
    setupFormValidation: function() {
        const form = document.querySelector('form');
        if (form) {
            form.addEventListener('submit', this.validateForm.bind(this));
        }
    },
    
    // Validar formulario
    validateForm: function(event) {
        const estudianteId = document.querySelector('select[name="estudianteId"]').value;
        const monto = parseFloat(document.querySelector('input[name="monto"]').value);
        const mes = document.querySelector('select[name="mes"]').value;
        const anio = document.querySelector('select[name="anio"]').value;
        const metodoPago = document.querySelector('select[name="metodoPago"]').value;
        
        if (!estudianteId) {
            event.preventDefault();
            this.showAlert('Por favor selecciona un estudiante', 'error');
            return false;
        }
        
        if (!monto || monto <= 0) {
            event.preventDefault();
            this.showAlert('Por favor ingresa un monto válido', 'error');
            return false;
        }
        
        if (!mes) {
            event.preventDefault();
            this.showAlert('Por favor selecciona el mes', 'error');
            return false;
        }
        
        if (!anio) {
            event.preventDefault();
            this.showAlert('Por favor selecciona el año', 'error');
            return false;
        }
        
        if (!metodoPago) {
            event.preventDefault();
            this.showAlert('Por favor selecciona el método de pago', 'error');
            return false;
        }
    },
    
    // Configurar formateo de montos
    setupAmountFormatting: function() {
        // Formatear montos en la tabla
        const amountCells = document.querySelectorAll('.amount-cell');
        amountCells.forEach(cell => {
            const amount = parseFloat(cell.textContent);
            if (!isNaN(amount)) {
                cell.textContent = this.formatCurrency(amount);
            }
        });
    },
    
    // Formatear moneda
    formatCurrency: function(amount) {
        return new Intl.NumberFormat('es-PE', {
            style: 'currency',
            currency: 'PEN'
        }).format(amount);
    },
    
    // Configurar date pickers
    setupDatePickers: function() {
        const dateInputs = document.querySelectorAll('input[type="date"]');
        dateInputs.forEach(input => {
            // Establecer fecha máxima como hoy
            const today = new Date().toISOString().split('T')[0];
            input.setAttribute('max', today);
            
            // Si no hay valor, establecer fecha actual
            if (!input.value) {
                input.value = today;
            }
        });
    },
    
    // Configurar autocompletado
    setupAutoComplete: function() {
        const searchInputs = document.querySelectorAll('.estudiante-search');
        searchInputs.forEach(input => {
            input.addEventListener('input', this.handleAutoComplete.bind(this));
        });
    },
    
    // Manejar autocompletado
    handleAutoComplete: function(event) {
        const query = event.target.value;
        if (query.length >= 2) {
            // Aquí se podría hacer una llamada AJAX para obtener sugerencias
            this.showSuggestions(query, event.target);
        }
    },
    
    // Mostrar sugerencias
    showSuggestions: function(query, input) {
        // Implementar lógica de sugerencias
        console.log('Buscando estudiante:', query);
    },
    
    // Manejar búsqueda en tiempo real
    handleSearch: function(event) {
        const query = event.target.value.toLowerCase();
        const tableRows = document.querySelectorAll('tbody tr');
        
        tableRows.forEach(row => {
            const text = row.textContent.toLowerCase();
            if (text.includes(query)) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    },
    
    // Manejar filtro por estado
    handleEstadoFilter: function(event) {
        const estado = event.target.value;
        const tableRows = document.querySelectorAll('tbody tr');
        
        tableRows.forEach(row => {
            const estadoCell = row.querySelector('td:nth-child(7)'); // Columna de estado
            if (estado === '' || estadoCell.textContent.includes(estado)) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    },
    
    // Manejar filtro por mes
    handleMesFilter: function(event) {
        const mes = event.target.value;
        const tableRows = document.querySelectorAll('tbody tr');
        
        tableRows.forEach(row => {
            const mesCell = row.querySelector('td:nth-child(4)'); // Columna de mes/año
            if (mes === '' || mesCell.textContent.includes(mes)) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    },
    
    // Manejar filtro por año
    handleAnioFilter: function(event) {
        const anio = event.target.value;
        const tableRows = document.querySelectorAll('tbody tr');
        
        tableRows.forEach(row => {
            const anioCell = row.querySelector('td:nth-child(4)'); // Columna de mes/año
            if (anio === '' || anioCell.textContent.includes(anio)) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    },
    
    // Mostrar alerta
    showAlert: function(message, type = 'info') {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type === 'error' ? 'danger' : type} alert-dismissible fade show`;
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
    },
    
    // Exportar datos
    exportData: function(format = 'csv') {
        const table = document.querySelector('table');
        if (!table) return;
        
        const rows = Array.from(table.querySelectorAll('tbody tr'));
        const headers = Array.from(table.querySelectorAll('thead th')).map(th => th.textContent.trim());
        
        let csvContent = headers.join(',') + '\n';
        
        rows.forEach(row => {
            const cells = Array.from(row.querySelectorAll('td')).map(td => {
                let text = td.textContent.trim();
                // Escapar comillas en CSV
                if (text.includes(',')) {
                    text = `"${text}"`;
                }
                return text;
            });
            csvContent += cells.join(',') + '\n';
        });
        
        // Descargar archivo
        const blob = new Blob([csvContent], { type: 'text/csv' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `pagos_${new Date().toISOString().split('T')[0]}.csv`;
        a.click();
        window.URL.revokeObjectURL(url);
    },
    
    // Generar estadísticas
    generateStats: function() {
        const tableRows = document.querySelectorAll('tbody tr');
        const stats = {
            total: tableRows.length,
            pagados: 0,
            pendientes: 0,
            vencidos: 0,
            totalMonto: 0
        };
        
        tableRows.forEach(row => {
            const estado = row.querySelector('td:nth-child(7)').textContent.trim();
            const montoText = row.querySelector('td:nth-child(5)').textContent.trim();
            const monto = parseFloat(montoText.replace(/[^\d.]/g, ''));
            
            if (estado.includes('PAGADO')) {
                stats.pagados++;
                stats.totalMonto += monto;
            } else if (estado.includes('PENDIENTE')) {
                stats.pendientes++;
            } else if (estado.includes('VENCIDO')) {
                stats.vencidos++;
            }
        });
        
        return stats;
    },
    
    // Mostrar estadísticas
    showStats: function() {
        const stats = this.generateStats();
        const statsHtml = `
            <div class="modal fade" id="statsModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Estadísticas de Pagos</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-6">
                                    <div class="text-center">
                                        <h3 class="text-primary">${stats.total}</h3>
                                        <p>Total Pagos</p>
                                    </div>
                                </div>
                                <div class="col-6">
                                    <div class="text-center">
                                        <h3 class="text-success">${stats.pagados}</h3>
                                        <p>Pagados</p>
                                    </div>
                                </div>
                                <div class="col-6">
                                    <div class="text-center">
                                        <h3 class="text-warning">${stats.pendientes}</h3>
                                        <p>Pendientes</p>
                                    </div>
                                </div>
                                <div class="col-6">
                                    <div class="text-center">
                                        <h3 class="text-danger">${stats.vencidos}</h3>
                                        <p>Vencidos</p>
                                    </div>
                                </div>
                                <div class="col-12 mt-3">
                                    <div class="text-center">
                                        <h4 class="text-success">${this.formatCurrency(stats.totalMonto)}</h4>
                                        <p>Total Recaudado</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;
        
        document.body.insertAdjacentHTML('beforeend', statsHtml);
        const modal = new bootstrap.Modal(document.getElementById('statsModal'));
        modal.show();
        
        // Limpiar modal después de cerrar
        document.getElementById('statsModal').addEventListener('hidden.bs.modal', function() {
            this.remove();
        });
    },
    
    // Crear pagos pendientes para un estudiante
    createPendingPayments: function(estudianteId) {
        const monto = prompt('Ingresa el monto mensual:');
        const anio = prompt('Ingresa el año:', new Date().getFullYear());
        
        if (monto && anio) {
            const url = `/pagos/crear-pendientes/${estudianteId}?montoMensual=${monto}&anio=${anio}`;
            window.location.href = url;
        }
    },
    
    // Marcar pago como pagado
    markAsPaid: function(pagoId) {
        if (confirm('¿Estás seguro de marcar este pago como pagado?')) {
            window.location.href = `/pagos/marcar-pagado/${pagoId}`;
        }
    }
};

// Inicializar cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    PagoUtils.init();
});

// Exportar para uso global
window.PagoUtils = PagoUtils; 