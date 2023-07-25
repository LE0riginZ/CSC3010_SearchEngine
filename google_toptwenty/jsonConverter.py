import os
import pandas as pd
import json

def csv_to_json(csv_file, json_file):
    # Read CSV data into a DataFrame
    df = pd.read_csv(csv_file)

    # Convert DataFrame to a list of dictionaries
    data = []
    for index, row in df.iterrows():
        item = {
            'title': row['title'],
            'url': row['url'],
            'position': row['position']
        }
        data.append(item)

    # Write data to JSON file
    with open(json_file, 'w') as f:
        json.dump(data, f, indent=4)

def convert_csv_to_json_in_directory(input_directory, output_directory):
    # Create output directory if it doesn't exist
    if not os.path.exists(output_directory):
        os.makedirs(output_directory)

    # Get list of CSV files in the input directory
    csv_files = [f for f in os.listdir(input_directory) if f.endswith('.csv')]

    # Process each CSV file
    for csv_file in csv_files:
        input_path = os.path.join(input_directory, csv_file)
        output_file = csv_file.replace('.csv', '.json')
        output_path = os.path.join(output_directory, output_file)
        csv_to_json(input_path, output_path)
        print(f"Converted {csv_file} to {output_file}")

if __name__ == '__main__':
    input_directory = '.'  # Replace with the path to the input directory containing CSV files
    output_directory = './jsonFiles'  # Replace with the path to the output directory for JSON files
    convert_csv_to_json_in_directory(input_directory, output_directory)
