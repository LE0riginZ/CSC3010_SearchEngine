import os
import nltk

# Need to run once if package does not exist
# nltk.download('punkt')
# nltk.download('stopwords')

from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
from string import punctuation
import re

def preprocess_text(html_data):
    processed_texts = []

    for html_text in html_data:
        # Preprocess the text
        text = clean_text(html_text)

        # Tokenize the text
        tokens = word_tokenize(text)

        # Remove stop words
        stop_words = set(stopwords.words('english'))
        tokens = [token for token in tokens if token.lower() not in stop_words]

        # Append the processed text to the list
        processed_texts.append(tokens)

    return processed_texts

def clean_text(text):
    # Remove HTML tags
    cleanr = re.compile('<.*?>')
    cleantext = re.sub(cleanr, '', text)

    # Remove punctuation and convert to lowercase
    return cleantext.lower().translate(str.maketrans('', '', punctuation))


html_data = []

# Replace with your directory
html_files_directory = r"C:\Users\Cheeyee\Desktop\Y3T3\3010\proj\crawled"

# find the html files
html_files = [f for f in os.listdir(html_files_directory) if f.endswith('.html')]

# Read file
for html_file in html_files:
    with open(os.path.join(html_files_directory, html_file), 'r', encoding='latin-1') as file:
        html_text = file.read()
        html_data.append(html_text)

preprocessed_texts = preprocess_text(html_data)


# Save to txt file
output_file_path = 'preprocessed_tokens.txt'
with open(output_file_path, 'w', encoding='utf-8') as output_file:
    for tokens in preprocessed_texts:
        output_file.write(' '.join(tokens) + '\n')

print(f"Preprocessed tokens have been saved to {output_file_path}.")