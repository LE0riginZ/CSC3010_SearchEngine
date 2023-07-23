<template>
    <div>
        <div class="search-bar">
            <input type="text" v-model="searchQuery" placeholder="Enter your search query" class="search-input"  @keyup.enter="search">
            <button @click="search" class="search-button">Search</button>
        </div>

        <div v-if="searchResults != null">
            <!-- Pagination controls -->
            <div>
                <button @click="prevPage()" :disabled="currentPage === 1">Previous</button>
                <span>{{ currentPage }}</span>
                <button @click="nextPage()" :disabled="currentPage === totalPagesNum">Next</button>
            </div>

            <!-- Dropdown for number of results per page -->
            <label>Show results per page:</label>
            <select v-model="resultsPerPage" @change="changePageSize">
                <option v-for="option in resultsPerPageOptions" :value="option" :key="option">
                    {{ option }}
                </option>
            </select>
            <!-- Table with search results -->
            <h2>Search Results:</h2>
            <table>
                <thead>
                    <tr>
                        <th>Title</th>
                        <th>Description</th>
                        <th>URL</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="result in searchResults.documents" :key="result.title" @click="handleRowClick(result.url)"
                        class="clickable-row">
                        <td>{{ result.title }}</td>
                        <td>{{ truncateString(result.content, 280) }}</td>
                        <td>{{ result.url }}</td>
                    </tr>
                </tbody>
            </table>


        </div>
        <div v-else>
            <p>Search away :D</p>
        </div>
    </div>
</template>
  
<script lang="ts" setup>
import axios from "axios";
import { ref, computed, onMounted } from "vue";

const totalPagesNum = ref(1);
const apiUrl = ref("");

const searchQuery = ref("");
const searchResults = ref(null);
const currentPage = ref(1);
const resultsPerPage = ref(10);
const resultsPerPageOptions = [5, 10, 25, 50, 100];
const activeRowUrl = ref(null);

const paginatedResults = computed(() => {
  if (!searchResults.value || searchResults.value.documents.length === 0) {
    return [];
  }

  const startIndex = (currentPage.value - 1) * resultsPerPage.value;
  const endIndex = startIndex + resultsPerPage.value;

  return searchResults.value.documents.slice(startIndex, endIndex);
});

const totalPages = computed(() => {
  if (!searchResults.value) {
    totalPagesNum.value = 1;
  } else {
    totalPagesNum.value = Math.ceil(searchResults.value.documents.length / resultsPerPage.value);
  }
  return totalPagesNum.value;
});

function changePageSize() {
  console.log(resultsPerPage.value)
  search()
}

function search() {
  if (searchQuery.value.trim() === "") {
    apiUrl.value = `http://localhost:8080/search/page?searchTerm=${searchQuery.value}`;
    searchQuery.value = ""
    searchResults.value = null
  } else {
    apiUrl.value = `http://localhost:8080/search/page?searchTerm=${searchQuery.value}&pageNum=${currentPage.value}&pageSize=${resultsPerPage.value}`;
  }

  axios
    .get(apiUrl.value)
    .then(response => {
      searchResults.value = response.data;
      totalPagesNum.value = searchResults.value.totalNumOfPages;

      console.log(totalPagesNum.value)
      console.log(searchResults.value)
    })
    .catch(error => {
      console.error("Error fetching search results:", error);
    });
}

function nextPage() {
    console.log(currentPage.value)
    console.log(totalPagesNum.value)

  if (currentPage.value < totalPagesNum.value) {
    currentPage.value++;    
    console.log(currentPage.value)
    search();
  }
}

function prevPage() {
    console.log(currentPage.value)
    console.log(totalPagesNum.value)

  if (currentPage.value > 1) {
    currentPage.value--;
    search();
  }
}

function truncateString(str: string, maxLength: number) {
  if (str.length > maxLength) {
    return str.slice(0, maxLength) + "...";
  } else {
    return str;
  }
}

function handleRowClick(url: string) {
  window.open(url, "_blank");
  activeRowUrl.value = url;
}

function isRowActive(url: string) {
  return activeRowUrl.value === url;
}

// Call the search function on component mount
onMounted(() => {
  search();
});
</script>


<style scoped>
.search-bar {
    display: flex;
    align-items: center;
}

.search-input {
    padding: 8px;
    font-size: 16px;
    border: 1px solid #ccc;
    border-radius: 4px;
    margin-right: 8px;
}

.search-button {
    padding: 8px 12px;
    background-color: #007bff;
    color: #fff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

.search-button:hover {
    background-color: #0069d9;
}

.search-button:active {
    background-color: #0056b3;
}

/* Styling for clickable rows */
.clickable-row {
    cursor: pointer;
    /* transition: background-color 0.2s ease; */
}

.clickable-row:hover {
    background-color: #bbf0e4;
}

.clickable-row:hover>td {
    font-weight: bold;
}

.clickable-row.active {
    background-color: #90cabd;
}
</style>