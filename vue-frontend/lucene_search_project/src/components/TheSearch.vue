<template>
    <div>
        <div class="search-bar">
            <input type="text" v-model="searchQuery" placeholder="Enter your search query" class="search-input">
            <button @click="search" class="search-button">Search</button>
        </div>

        <div v-if="searchResults.documents.length > 0">
            <h2>Search Results:</h2>
            <ul>
                <li v-for="result in searchResults.documents" :key="result.title">
                    {{ result.title }} - {{ result.url }}
                </li>
            </ul>
        </div>

        <div v-else>
            <p>No results found.</p>
        </div>
    </div>
</template>
  
<script lang="ts">
import axios from "axios";

export default {
    name: 'SearchEngine',
    data() {
        return {
            searchQuery: '' as string,
            searchResults: []
        };
    },
    methods: {
        search() {
            // Replace this URL with your actual API endpoint
            const apiUrl = `http://localhost:8080/search/page?searchTerm=${this.searchQuery}&pageNum=1&pageSize=10`;

            // Make the GET request using Axios
            axios
                .get(apiUrl)
                .then(response => {
                    // Assuming your API returns an array of search results
                    this.searchResults = response.data;

                    console.log(response.data)

                })
                .catch(error => {
                    console.error("Error fetching search results:", error);
                });
        }
    }
};
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
</style>