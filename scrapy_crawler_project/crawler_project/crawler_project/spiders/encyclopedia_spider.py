import os
import requests
from bs4 import BeautifulSoup
import json
from datetime import datetime, timedelta
import signal
import sys
import re

# Set of desired words to filter links
desired_words = {
    'computer', 'glasgow', 'united', 'kingdom', 'library', 'fog', 'empires',
    'doctor', 'hospital', 'bachelor', 'degree', 'internet', 'things',
    'information', 'info', 'retrieval', 'retrieve', 'universe', 'university'
}

crawled_urls = []

crawled_urls_file = 'crawled_urls.txt'

def sanitize_filename(filename):
    # Remove invalid characters and replace spaces with underscores
    return re.sub(r'[\\/*?:"<>|]', '', filename).replace(' ', '_')

# Function to fetch and parse the sitemap.xml
def parse_sitemap(sitemap_url):
    response = requests.get(sitemap_url)
    soup = BeautifulSoup(response.content, 'xml')
    return [loc.text for loc in soup.find_all('loc')]

# Function to load existing scraped data from the JSON file
def load_scraped_data():
    crawled_urls = set()
    if os.path.exists(crawled_urls_file):
        with open(crawled_urls_file, 'r') as f:
            crawled_urls = set(line.strip() for line in f)
    return crawled_urls

# Function to save the scraped data to the JSON file
def save_scraped_data():
    with open(crawled_urls_file, 'w') as f:
            f.write('\n'.join(crawled_urls))

# Function to handle the Ctrl+C signal for pausing
def signal_handler(signal, frame):
    print("Crawling paused. Press Ctrl+C again to exit.")
    sys.exit(0)

# Function to crawl and extract data from individual links
def crawl_link(link):
    data_dict = None

    try:
        response = requests.get(link)
        soup = BeautifulSoup(response.content, 'html.parser')
        # Extract the article content
        article_content = soup.select_one('div.articleWrapper').get_text()
        title_element = soup.select_one('h1.doctitle')
        title_content = title_element.get_text() if title_element else ""

        # Check if any of the desired words are present in the title or article content
        if any(word in title_content.lower() for word in desired_words):
        #or any(word in article_content.lower() for word in desired_words):
            # Create a dictionary to store the data
            data_dict = {
                'title': title_content.strip(),
                'content': article_content.strip(),
                'url': link
            }

        return data_dict
    except Exception as e:
        print(f"Error occurred while crawling {link}: {e}")
        return None

if __name__ == "__main__":
    # Parse the sitemap.xml and get the links to crawl
    sitemap_url = 'https://www.encyclopedia.com/sitemap.xml'
    main_links = parse_sitemap(sitemap_url)

    # Check for Ctrl+C signal and set the signal handler
    signal.signal(signal.SIGINT, signal_handler)

    # Load existing scraped data
    scraped_data = load_scraped_data()
    
    main_links_to_crawl = [link for link in main_links]

    # Crawl and extract data from individual links
    for main_link in main_links_to_crawl:
        response = requests.get(main_link)
        soup = BeautifulSoup(response.content, 'xml')
        xml_links = [loc.text for loc in soup.find_all('loc')]

        for link in xml_links:
            if link not in scraped_data:
                data_dict = crawl_link(link)
                
                timestamp = datetime.now().strftime('%Y%m%d%H%M%S').replace(':', '_')
                # Check if data_dict is not None and the 'title' field is not empty before saving
                if data_dict and data_dict.get('title'):
                    # Generate the filename using the title or response.url
                    filename = f"crawled_{timestamp}.json"

                    # Create the 'data_crawled_json' folder if it doesn't exist
                    if not os.path.exists('data_crawled_json'):
                        os.makedirs('data_crawled_json')

                    file_path = os.path.join('data_crawled_json', sanitize_filename(filename))

                    with open(file_path, 'w', encoding='utf-8') as f:
                        json.dump(data_dict, f, ensure_ascii=False, indent=2)

                    # Update the scraped_data dictionary with the new data
                    save_scraped_data()
                    
    print("Crawling completed.")