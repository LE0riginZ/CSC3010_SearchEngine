import json
import nltk
from nltk import word_tokenize
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer

# Uncomment and run if first time
# nltk.download('wordnet')
# nltk.download("punkt")
# nltk.download("stopwords")

def lemmatize_content(text):
    # Tokenization using word_tokenize
    words = word_tokenize(text.lower())

    # Remove stopwords and punctuations
    stop_words = set(stopwords.words("english"))
    words = [word for word in words if word.isalpha() and word not in stop_words]
    # Apply custom function to remove punctuations and symbols while keeping compound words
    processed_words = [remove_punctuations_and_symbols(word) for word in words]

    # Initialize WordNetLemmatizer
    lemmatizer = WordNetLemmatizer()

    # Tokenize the content into words
    words = nltk.word_tokenize(text)


    # Lemmatize each word in the content
    lemmatized_words = [lemmatizer.lemmatize(word) for word in processed_words]

    # Join the lemmatized words back into a string
    lemmatized_content = " ".join(lemmatized_words)

    return lemmatized_content

def remove_punctuations_and_symbols(word):
    # Remove punctuations and symbols from each word
    return ''.join(c for c in word if c.isalpha())

def lemmatize_json_file(input_file, output_file):
    with open(input_file, 'r', encoding='utf-8') as f:
        data = json.load(f)

    for item in data:
        if 'content' in item:
            item['content'] = lemmatize_content(item['content'])

    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=2, ensure_ascii=False)


def main():
    input_file = "combined_data_hack_cheat.json"  # Replace with the path to your input .json file
    output_file = "lemmatized_combined_data_hack_cheat.json"  # Replace with the desired output .json file

    lemmatize_json_file(input_file, output_file)


if __name__ == "__main__":
    main()
