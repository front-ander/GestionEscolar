document.addEventListener('DOMContentLoaded', function () {
    const sidebarCollapse = document.getElementById('sidebarCollapse');
    const sidebar = document.querySelector('.sidebar');
    const content = document.querySelector('.content');

    if (sidebarCollapse && sidebar && content) {
        // Function to toggle sidebar based on screen size
        const toggleSidebarBasedOnSize = () => {
            if (window.innerWidth <= 768) {
                sidebar.classList.add('toggled'); // Hidden by default on small screens
                content.classList.add('sidebar-toggled');
            } else {
                sidebar.classList.remove('toggled'); // Shown by default on large screens
                content.classList.remove('sidebar-toggled');
            }
        };

        // Initial check
        toggleSidebarBasedOnSize();

        sidebarCollapse.addEventListener('click', function () {
            sidebar.classList.toggle('toggled');
            content.classList.toggle('sidebar-toggled');
        });

        // Adjust sidebar on window resize
        window.addEventListener('resize', toggleSidebarBasedOnSize);
    }

    // Dropdown toggle for sidebar submenus
    const dropdownToggle = document.querySelectorAll('.sidebar .dropdown-toggle');
    dropdownToggle.forEach(function (toggle) {
        toggle.addEventListener('click', function (e) {
            // Check if the target is already shown by Bootstrap's collapse
            const targetId = this.getAttribute('href'); // e.g., #studentsSubmenu
            const targetElement = document.querySelector(targetId);
            
            // Bootstrap's collapse plugin handles the actual toggling.
            // This event listener is mostly for compatibility or custom logic if needed.
            // For now, Bootstrap handles the collapse functionality via data-bs-toggle.
        });
    });
});
