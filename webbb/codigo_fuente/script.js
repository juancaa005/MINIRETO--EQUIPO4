document.addEventListener('DOMContentLoaded', function() {
    const banner = document.getElementById('cookie-banner');
    const acceptBtn = document.getElementById('accept-cookies');

    // Mostrar el banner
    banner.style.display = 'block';

    // Ocultar al hacer clic en aceptar
    acceptBtn.addEventListener('click', function() {
        banner.style.display = 'none';
    });
});