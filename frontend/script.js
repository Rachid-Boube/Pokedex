/* Pokémon données */
const pokemons = [
    { id: 1, nom: "Bulbizarre", type: "Plante" },
    { id: 4, nom: "Salamèche", type: "Feu" },
    { id: 7, nom: "Carapuce", type: "Eau" },
    // Ajoutez d'autres Pokémon ici
];


/* liste de Pokémon */
function trierPar(critere) {
    // Trier la liste des Pokémon selon le critère choisi
    pokemons.sort((a, b) => a[critere].toString().localeCompare(b[critere].toString()));
    afficherListe(pokemons);
}

function afficherListe(pokemons) {
    const liste = document.getElementById('listePokemons');
    liste.innerHTML = '';
    pokemons.forEach(pokemon => {
        const item = document.createElement('li');
        item.textContent = `${pokemon.id} - ${pokemon.nom} (${pokemon.type})`;
        liste.appendChild(item);
    });
}

// Écoute le changement de sélection du critère de tri
function changementCritere() {
    const critere = document.getElementById('sort-options').value;
    trierPar(critere);
}

window.onload = () => {
    trierPar('id'); // Affiche la liste triée par défaut par ID
};



/* recherche un Pokémon */
function rechercher() {
    const critere = document.getElementById('critere').value;
    const valeur = document.getElementById('valeur').value.toLowerCase();
    const resultat = pokemons.filter(pokemon => 
        pokemon[critere].toString().toLowerCase().includes(valeur)
    );
    const resultDiv = document.getElementById('resultat');

    resultDiv.innerHTML = ''; 

    if (resultat.length > 0) {
        resultat.forEach(pokemon => {
            resultDiv.innerHTML += `${pokemon.id} - ${pokemon.nom} (${pokemon.type})<br>`;
        });
    } else {
        resultDiv.innerHTML = '<p>Aucun Pokémon trouvé.<p>';
    }
}