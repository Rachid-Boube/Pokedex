// 全局变量来存储所有的 Pokémon 数据
let pokemons = [];
let currentPage = 1;
const itemsPerPage = 10;  // 每页显示10个 Pokémon

// 获取所有 Pokémon 数据并更新 UI
async function fetchPokemonData() {
    const loadingDiv = document.getElementById('loading');
    loadingDiv.style.display = 'block';  // 显示加载状态

    try {
        const response = await fetch('http://localhost:3000/api/pokemon'); // 调用你的 API
        const data = await response.json();
        pokemons = data;
        trierPar('pokemonId');  // 默认按 ID 排序
    } catch (err) {
        console.error('Error fetching Pokémon data:', err);
        alert('Failed to load Pokémon data!');
    } finally {
        loadingDiv.style.display = 'none'; // 隐藏加载状态
    }
}

// 获取当前页的数据
function getCurrentPageData() {
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = currentPage * itemsPerPage;
    return pokemons.slice(startIndex, endIndex);
}

// 显示 Pokémon 列表
function afficherListe(pokemons) {
    const liste = document.getElementById('listePokemons');
    liste.innerHTML = '';  // 清空当前列表

    pokemons.forEach(pokemon => {
        const item = document.createElement('li');
        item.innerHTML = `
            <strong>${pokemon.name.english}</strong> (${pokemon.type.join(', ')})<br>
            <span>ID: ${pokemon.pokemonId}</span><br>
            <span>HP: ${pokemon.base.HP}</span><br>
            <span>Attack: ${pokemon.base.Attack}</span><br>
            <span>Speed: ${pokemon.base.Speed}</span><br>
            <span>Description: ${pokemon.description}</span><br><br>
        `;
        liste.appendChild(item);
    });
}

// 按照筛选条件进行排序或过滤 Pokémon
function trierPar(critere) {
    const sortedPokemons = [...pokemons].sort((a, b) => {
        if (critere.includes(".")) {
            const keys = critere.split('.');
            return a[keys[0]][keys[1]].localeCompare(b[keys[0]][keys[1]]);
        }

        if (critere === "type") {
            const typeA = a[critere].join(', ').toLowerCase();
            const typeB = b[critere].join(', ').toLowerCase();
            return typeA.localeCompare(typeB);
        }

        if (typeof a[critere] === 'string') {
            return a[critere].localeCompare(b[critere]);
        }
        return a[critere] - b[critere];
    });
    pokemons = [...sortedPokemons];  // 更新全局变量，保存排序后的数据

    // 更新显示和分页
    afficherListe(getCurrentPageData());  // 显示排序后的当前页
    updatePaginationUI();  // 更新分页 UI
}

// 更新分页 UI
function updatePaginationUI() {
    const totalPages = Math.ceil(pokemons.length / itemsPerPage);
    const paginationDiv = document.getElementById('pagination');
    
    paginationDiv.innerHTML = '';  // 清空当前的分页按钮
    
    for (let i = 1; i <= totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.textContent = i;
        pageButton.onclick = () => {
            currentPage = i;
            afficherListe(getCurrentPageData());
            updatePaginationUI();  // 更新分页按钮状态
        };
        paginationDiv.appendChild(pageButton);
    }
}

// 当选择排序方式变化时调用此函数
function changementCritere() {
    const sortOption = document.getElementById('sort-options').value;
    trierPar(sortOption);  // 调用 trierPar，自动更新列表和分页
}

// 按名称或类型进行搜索
async function rechercher() {
    const critere = document.getElementById('critere').value;  // 获取选择的条件
    const valeur = document.getElementById('valeur').value.toLowerCase();  // 获取用户输入的值，转换为小写字母

    let result = [];
    if (critere === 'type') {
        result = pokemons.filter(pokemon => 
            pokemon.type.some(t => t.toLowerCase().includes(valeur))  // 类型匹配
        );
    } else if (critere === 'pokemonId') {
        result = pokemons.filter(pokemon => pokemon.pokemonId.toString() === valeur);  // ID 匹配
    } else {
        result = pokemons.filter(pokemon => {
            const fieldValue = pokemon.name[critere]?.toLowerCase() || '';  // 语言名称匹配
            return fieldValue.includes(valeur);
        });
    }

    const resultDiv = document.getElementById('resultat');
    resultDiv.innerHTML = '';

    if (result.length > 0) {
        result.forEach(pokemon => {
            resultDiv.innerHTML += `
                <strong>${pokemon.name.english}</strong> (${pokemon.type.join(', ')})<br>
                <span>ID: ${pokemon.pokemonId}</span><br>
                <span>HP: ${pokemon.base.HP}</span><br>
                <span>Attack: ${pokemon.base.Attack}</span><br>
                <span>Speed: ${pokemon.base.Speed}</span><br>
                <span>Description: ${pokemon.description}</span><br><br>
            `;
        });
    } else {
        resultDiv.innerHTML = '<p>Aucun Pokémon trouvé.</p>';
    }
}


// 页面加载时获取 Pokémon 数据
window.onload = () => {
    fetchPokemonData(); // 获取 Pokémon 数据并更新页面
};
