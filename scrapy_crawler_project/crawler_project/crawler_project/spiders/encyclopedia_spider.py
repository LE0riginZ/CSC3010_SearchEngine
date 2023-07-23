import os
import re
import scrapy
import signal
import logging
from scrapy.crawler import CrawlerProcess
from scrapy.signals import engine_stopped
from scrapy.spiders import SitemapSpider
from datetime import datetime
from bs4 import BeautifulSoup
import json

logging.getLogger('scrapy').setLevel(logging.ERROR)
logging.getLogger('py').setLevel(logging.ERROR)

crawled_data_dir = 'crawled_json'

class EncyclopediaSpider(SitemapSpider):
    name = 'encyclopedia'
    
    allowed_domains = ["www.encyclopedia.com/places", 
                       "www.encyclopedia.com/medicine", 
                       "www.encyclopedia.com/earth-and-environment",
                       "www.encyclopedia.com/history",
                       "www.encyclopedia.com/literature-and-arts",
                       "www.encyclopedia.com/people",
                       "www.encyclopedia.com/philosophy-and-religion",
                       "www.encyclopedia.com/plants-and-animals",
                       "www.encyclopedia.com/science-and-technology",
                       "www.encyclopedia.com/social-sciences-and-law",
                       "www.encyclopedia.com/sports-and-everyday-life",
                       "www.encyclopedia.com/references",
                       "www.encyclopedia.com/articles",
                       "www.encyclopedia.com/daily",
                       "www.encyclopedia.com"]
    
    sitemap_urls = [
        "https://www.encyclopedia.com/sitemap.xml",
    ]
    crawled_urls_file = 'crawled_urls.txt'
    paused = False    
    desired_words = {
        'computer', 'glasgow', 'united', 'kingdom', 'library', 'fog', 'empires',
        'doctor', 'hospital', 'bachelor', 'degree', 'internet', 'things',
        'information', 'info', 'retrieval', 'retrieve', 'universe', 'university'
    }

    def __init__(self, *args, **kwargs):
        super(EncyclopediaSpider, self).__init__(*args, **kwargs)
        self.crawled_urls = self.load_crawled_urls()

    def load_crawled_urls(self):
        crawled_urls = set()
        if os.path.exists(self.crawled_urls_file):
            with open(self.crawled_urls_file, 'r') as f:
                crawled_urls = set(line.strip() for line in f)
        return crawled_urls

    def save_crawled_urls(self):
        with open(self.crawled_urls_file, 'w') as f:
            f.write('\n'.join(self.crawled_urls))

    def remove_html_tags(self, html_content):
        # Use BeautifulSoup to parse the HTML and remove the tags
        soup = BeautifulSoup(html_content, 'html.parser')
        return soup.get_text()

    def clean_filename(self, title_content):
        # Remove invalid characters from the title_content
        cleaned_title = re.sub(r'[\\/:*?"<>|]', '', title_content)

        # Replace spaces with dashes
        cleaned_title = cleaned_title.replace(' ', '-')

        return cleaned_title

    def parse(self, response):
        # Check if the URL has already been crawled
        if response.url in self.crawled_urls:
            return

        # Extract the article content
        article_content = response.css('div.articleWrapper').get()
        title_content = response.css('h1.doctitle::text').get()
        if article_content:
            # Remove HTML tags from the article content
            article_text = self.remove_html_tags(article_content)

            # Check if any of the desired words are present in the article content
            if any(word in article_text.lower() for word in self.desired_words):
                # Create the 'crawled' folder if it doesn't exist
                if not os.path.exists(crawled_data_dir):
                    os.makedirs(crawled_data_dir)

                # Create a dictionary for crawled page data
                crawled_data_dict = {
                    'title': title_content.strip() if title_content is not None else response.url.split("/")[-2],
                    'content': article_text,
                    'url': response.url
                }

                # Convert the dictionary to a JSON string
                json_data = json.dumps(crawled_data_dict, ensure_ascii=False, indent=2)

                # Store the fetched content in a file
                timestamp = datetime.now().strftime("%Y-%m-%d-%H_%M_%S")
                if title_content is not None:
                    filename = self.clean_filename(title_content.strip()) + timestamp + '.json'
                else:
                    filename = self.clean_filename(response.url.split("/")[-2] + timestamp) + '.json'
                    
                # + "-" + response.url.split("/")[-2]

                file_path = os.path.join(crawled_data_dir, filename)
                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(json_data)
                    #f.write(response.body)

                # Update the list of crawled URLs
                self.crawled_urls.add(response.url)
                self.save_crawled_urls()


        # Extract links from fetched content and add them to the URL Frontier
        for link in response.css('a::attr(href)').getall():
            if not self.paused:
                # Check if the URL starts with "mailto:"
                if response.url.startswith('mailto:'):
                    return
                
                yield response.follow(link, callback=self.parse)

    @classmethod
    def from_crawler(cls, crawler, *args, **kwargs):
        spider = super(EncyclopediaSpider, cls).from_crawler(crawler, *args, **kwargs)
        crawler.signals.connect(spider.engine_stopped, signal=engine_stopped)
        return spider

    def engine_stopped(self):
        # Called when the crawler engine is stopped (e.g., when paused or finished)
        self.save_crawled_urls()

# Handle Ctrl+C signal to pause the spider
def signal_handler(signal, frame):
    EncyclopediaSpider.paused = True
    print('Crawling paused. Press Ctrl+C again to exit.')
    signal.signal(signal.SIGINT, signal.SIG_DFL)  # Reset Ctrl+C to default behavior

# Register the signal handler
signal.signal(signal.SIGINT, signal_handler)

# Initialize the crawler process for distributed operation and scalability
process = CrawlerProcess(settings={
    'CONCURRENT_REQUESTS': 5000,  # Adjust the number of concurrent requests for performance
    'CONCURRENT_REQUESTS_PER_DOMAIN': 1000,  # Limit concurrent requests per domain
    'AUTOTHROTTLE_ENABLED': True,  # Enable automatic throttling to optimize crawl rate
    'AUTOTHROTTLE_TARGET_CONCURRENCY': 1000,  # Set the target concurrency level for optimal performance
    'USER_AGENT': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36',  # Set a user agent to mimic a web browser
    'LOG_LEVEL': 'INFO',  # Set log level to display relevant information
    'FEED_FORMAT': 'json',  # Set the desired output format for scraped data
    'FEED_URI': 'crawled/data.json',  # Store the scraped data in a JSON file
    'CLOSESPIDER_ITEMCOUNT': 1000,  # Limit the number of items to be crawled
})

# Start the crawling process
process.crawl(EncyclopediaSpider)
process.start()
