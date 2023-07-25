from googlesearch import search
import os
import csv

def create_csv_file(filename):
  """Creates a CSV file if it doesn't exist."""

  if not os.path.exists(filename):
    with open(filename, "w", newline="") as csvfile:
      csvwriter = csv.writer(csvfile, delimiter=",")
      csvwriter.writerow(["title", "url", "position"])

keyword = "Retrieval"

list = search(keyword + " site:https://www.encyclopedia.com", num_results=10, advanced=True)

urlList = []

title = []

for i in list:
    # print(i)
    urlList.append(i.url)
    title.append(i.title)

# Check if the CSV file exists
    csv_file = "topTen searches_" + keyword + ".csv"
    create_csv_file(csv_file)

with open(csv_file, "a", newline="", encoding="utf-8") as file:
        writer = csv.writer(file)
        for i in range(0, len(urlList)):
                # print(title[i], "  ", urlList[i])
                # Write data rows
                row = []
                row.append(title[i])
                row.append(urlList[i])
                row.append(i+1)
                print(row)
                writer.writerow(row)

