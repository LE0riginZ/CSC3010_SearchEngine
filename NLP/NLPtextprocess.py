import os
import json
import nltk
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from nltk.stem import PorterStemmer
from nltk.stem import WordNetLemmatizer
from collections import Counter
import string

# Uncomment and run if first time
# nltk.download("punkt")
# nltk.download("stopwords")

def process_text(text):
    # Tokenization using word_tokenize
    words = word_tokenize(text.lower())

    # Remove stopwords and punctuations
    stop_words = set(stopwords.words("english"))
    words = [word for word in words if word.isalpha() and word not in stop_words]

    # Lemmatization
    lemmatizer = WordNetLemmatizer()
    words = [lemmatizer.lemmatize(word) for word in words]

    return words

def remove_punctuations_and_symbols(word):
    # Remove punctuations and symbols from each word
    return ''.join(c for c in word if c.isalpha())

def clean_title(title):
    # Remove invalid characters from the title
    valid_chars = "-_.() %s%s" % (string.ascii_letters, string.digits)
    return ''.join(c if c in valid_chars else '_' for c in title)

def main():

    # Data directory path
    data_directory = 'C:/Users/Cheeyee/Desktop/Y3T3/3010/proj/data_directory'  

    # Load data from data.json
    data_path = os.path.join(data_directory, 'data.json')
    with open('data.json', 'r') as file:
        data = json.load(file)

    # Create a folder to store text files
    if not os.path.exists('processed_texts2'):
        os.makedirs('processed_texts2')

    # Process each item and create separate text files
    for item in data:
        title = item.get('title', '')
        content = item.get('content', '')

        processed_words = process_text(content)

        # Apply custom function to remove punctuations and symbols while keeping compound words
        processed_words = [remove_punctuations_and_symbols(word) for word in processed_words]

        word_freq = Counter(processed_words)

        # Clean title
        cleaned_title = clean_title(title)

        # Create a text file with the cleaned title in the 'processed_texts' folder
        with open(f'processed_texts2/{cleaned_title}.txt', 'w', encoding='utf-8') as text_file:
            # Save word: frequency on each line
            for word, freq in word_freq.items():
                text_file.write(f'{word}: {freq}\n')

if __name__ == "__main__":
    main()
