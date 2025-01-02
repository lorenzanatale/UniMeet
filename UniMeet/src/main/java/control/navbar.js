/**
 * 
 */

function setupNavbar(userType = null, userEmail = null) {
    const navContent = document.querySelector('.navbar-content');
    const menuDropdown = document.querySelector('.menu-dropdown');
    const authButtons = document.querySelector('.auth-buttons');

    if (userType && userEmail) {
        // Crea il menu utente
        const userMenu = document.createElement('div');
        userMenu.className = 'user-menu';
        userMenu.innerHTML = `
            <button class="user-button">
                ${userType === 'professor' ? 'Menu docente' : 'Profilo studente'}
                <svg width="16" height="16" fill="none" stroke="currentColor">
                    <path d="M19 9l-7 7-7-7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </button>
            <div class="dropdown">
                ${userType === 'professor' ? `
                    <a href="#">GESTISCI RICEVIMENTI</a>
                    <a href="#">RIEPILOGO RICEVIMENTI</a>
                    <a href="#">RICEVIMENTI IN PROGRAMMA</a>
                ` : `
                    <a href="#">I MIEI APPUNTAMENTI</a>
                    <a href="#">PRENOTA RICEVIMENTO</a>
                `}
                <hr>
                <a href="#" class="logout">LOGOUT</a>
            </div>
        `;

        // Aggiungi alla navbar e al menu mobile
        navContent.insertBefore(userMenu, authButtons);
        const userMenuClone = userMenu.cloneNode(true);
        menuDropdown.appendChild(userMenuClone);

        // Rimuovi i pulsanti di autenticazione
        authButtons.style.display = 'none';

        // Gestione dropdown desktop
        const userButton = userMenu.querySelector('.user-button');
        const dropdown = userMenu.querySelector('.dropdown');

        userButton.addEventListener('click', (e) => {
            e.stopPropagation();
            dropdown.classList.toggle('active');
        });

        // Gestione dropdown mobile
        const mobileUserButton = userMenuClone.querySelector('.user-button');
        const mobileDropdown = userMenuClone.querySelector('.dropdown');

        mobileUserButton.addEventListener('click', (e) => {
            e.stopPropagation();
            mobileDropdown.classList.toggle('active');
        });

        // Chiudi dropdown quando si clicca fuori
        document.addEventListener('click', () => {
            dropdown.classList.remove('active');
            mobileDropdown.classList.remove('active');
        });
    }
}

// Gestione menu mobile
const mobileMenuButton = document.querySelector('.mobile-menu-button');
const menuDropdown = document.querySelector('.menu-dropdown');

mobileMenuButton.addEventListener('click', () => {
    menuDropdown.classList.toggle('active');
});

