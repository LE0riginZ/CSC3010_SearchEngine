import os
import io
import json

# Function to read individual JSON files and collect the dictionaries
def collect_json_data(folder_path):
    json_data = []
    for filename in os.listdir(folder_path):
        if filename.endswith('.json'):
            file_path = os.path.join(folder_path, filename)
            try:
                with io.open(file_path, 'r', encoding='utf-8') as file:
                    content = file.read()
                    if content.strip():  # Check if the file is not empty
                        try:
                            print("joining")
                            data = json.loads(content)
                            json_data.append(data)
                        except json.JSONDecodeError as e:
                            print(f"Error parsing JSON in {file_path}: {e}")
                    else:
                        print(f"Warning: Empty file found: {file_path}")
            except (IOError, OSError) as e:
                print(f"Error reading {file_path}: {e}")
    return json_data

# Function to write the collected dictionaries into a single JSON file
def write_combined_json(output_filename, json_data):
    print("hereee")
    with open(output_filename, 'w', encoding='utf-8') as file:  # Specify the encoding as utf-8
        json.dump(json_data, file, indent=4, ensure_ascii=False)  # Set ensure_ascii to False to preserve non-ASCII characters


# Provide the folder path and output file name
folder_path = '/data_crawled_json'
output_filename = 'combined_data.json'

# Collect dictionaries from individual JSON files
collected_data = collect_json_data(folder_path)

# Write the combined JSON data into the output file
write_combined_json(output_filename, collected_data)

print(f"Combined JSON data saved to {output_filename}.")
