// Funcionalidades para el módulo de Asistencias

const AsistenciaUtils = {
    
    // Inicializar el módulo
    init: function() {
        this.setupEventListeners();
        this.setupFormValidation();
        this.setupAutoComplete();
        this.setupDatePickers();
    },
    
    // Configurar event listeners
    setupEventListeners: function() {
        // Cambio de tipo de persona
        const tipoPersonaSelect = document.getElementById('tipoPersona');
        if (tipoPersonaSelect) {
            tipoPersonaSelect.addEventListener('change', this.handleTipoPersonaChange.bind(this));
        }
        
        // Validación de horarios
        const horaEntradaInput = document.querySelector('input[name="horaEntrada"]');
        const horaSalidaInput = document.querySelector('input[name="horaSalida"]');
        
        if (horaEntradaInput && horaSalidaInput) {
            horaEntradaInput.addEventListener('change', this.validateHorarios.bind(this));
            horaSalidaInput.addEventListener('change', this.validateHorarios.bind(this));
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
    },
    
    // Manejar cambio de tipo de persona
    handleTipoPersonaChange: function(event) {
        const tipo = event.target.value;
        const estudianteSelect = document.getElementById('estudianteSelect');
        const profesorSelect = document.getElementById('profesorSelect');
        
        if (tipo === 'estudiante') {
            estudianteSelect.style.display = 'block';
            profesorSelect.style.display = 'none';
            profesorSelect.querySelector('select').value = '';
        } else if (tipo === 'profesor') {
            estudianteSelect.style.display = 'none';
            profesorSelect.style.display = 'block';
            estudianteSelect.querySelector('select').value = '';
        } else {
            estudianteSelect.style.display = 'none';
            profesorSelect.style.display = 'none';
        }
    },
    
    // Validar horarios
    validateHorarios: function() {
        const horaEntrada = document.querySelector('input[name="horaEntrada"]').value;
        const horaSalida = document.querySelector('input[name="horaSalida"]').value;
        
        if (horaEntrada && horaSalida) {
            const entrada = new Date(`2000-01-01T${horaEntrada}`);
            const salida = new Date(`2000-01-01T${horaSalida}`);
            
            if (entrada >= salida) {
                this.showAlert('La hora de salida debe ser posterior a la hora de entrada', 'warning');
                document.querySelector('input[name="horaSalida"]').value = '';
            }
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
        const tipoPersona = document.getElementById('tipoPersona').value;
        const estudianteId = document.querySelector('select[name="estudianteId"]').value;
        const profesorId = document.querySelector('select[name="profesorId"]').value;
        
        if (tipoPersona === 'estudiante' && !estudianteId) {
            event.preventDefault();
            this.showAlert('Por favor selecciona un estudiante', 'error');
            return false;
        }
        
        if (tipoPersona === 'profesor' && !profesorId) {
            event.preventDefault();
            this.showAlert('Por favor selecciona un profesor', 'error');
            return false;
        }
        
        if (!tipoPersona) {
            event.preventDefault();
            this.showAlert('Por favor selecciona el tipo de persona', 'error');
            return false;
        }
    },
    
    // Configurar autocompletado
    setupAutoComplete: function() {
        // Implementar autocompletado para búsqueda de personas
        const searchInputs = document.querySelectorAll('.person-search');
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
        console.log('Buscando:', query);
    },
    
    // Configurar date pickers
    setupDatePickers: function() {
        const dateInputs = document.querySelectorAll('input[type="date"]');
        dateInputs.forEach(input => {
            // Establecer fecha máxima como hoy
            const today = new Date().toISOString().split('T')[0];
            input.setAttribute('max', today);
        });
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
        a.download = `asistencias_${new Date().toISOString().split('T')[0]}.csv`;
        a.click();
        window.URL.revokeObjectURL(url);
    },
    
    // Generar estadísticas
    generateStats: function() {
        const tableRows = document.querySelectorAll('tbody tr');
        const stats = {
            total: tableRows.length,
            presentes: 0,
            ausentes: 0,
            tardanzas: 0
        };
        
        tableRows.forEach(row => {
            const estado = row.querySelector('td:nth-child(7)').textContent.trim();
            if (estado.includes('PRESENTE')) stats.presentes++;
            else if (estado.includes('AUSENTE')) stats.ausentes++;
            else if (estado.includes('TARDANZA')) stats.tardanzas++;
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
                            <h5 class="modal-title">Estadísticas de Asistencias</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-6">
                                    <div class="text-center">
                                        <h3 class="text-primary">${stats.total}</h3>
                                        <p>Total</p>
                                    </div>
                                </div>
                                <div class="col-6">
                                    <div class="text-center">
                                        <h3 class="text-success">${stats.presentes}</h3>
                                        <p>Presentes</p>
                                    </div>
                                </div>
                                <div class="col-6">
                                    <div class="text-center">
                                        <h3 class="text-danger">${stats.ausentes}</h3>
                                        <p>Ausentes</p>
                                    </div>
                                </div>
                                <div class="col-6">
                                    <div class="text-center">
                                        <h3 class="text-warning">${stats.tardanzas}</h3>
                                        <p>Tardanzas</p>
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
    }
};

// Inicializar cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    AsistenciaUtils.init();
});

// Exportar para uso global
window.AsistenciaUtils = AsistenciaUtils; 