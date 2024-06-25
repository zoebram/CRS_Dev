import pandas as pd
import re
from neo4j import GraphDatabase
import os

# Define the file path
file_path = 'C:\\Users\\User\\Desktop\\Bram Unimas\\FYP\\74299_FYP_SOURCECODE\\Data\\chocolate.csv' #adjust according to your file directory.
output_file_path = 'rdf_triples.txt'

# Load CSV data
data = pd.read_csv(file_path)

# Define a function to create RDF triples
def create_rdf_triples(dataframe):
    rdf_triples = []

    for index, row in dataframe.iterrows():
        # Extract values
        product_index = row['index']
        product_name = row['specific_bean_origin_or_bar_name']
        company_name = row['company']
        company_location = row['company_location']
        country_of_origin = row['country_of_bean_origin']
        cocoa_percent = row['cocoa_percent']
        rating = row['rating']

        number_of_ingredients = row['counts_of_ingredients']
        
        ingredient_bean = row['beans']
        ingredient_cocoa_butter = row['cocoa_butter']
        ingredient_vanilla = row['vanilla']
        ingredient_lecithin = row['lecithin']
        ingredient_salt = row['salt']
        ingredient_sugar = row['sugar']
        ingredient_sweetener = row['sweetener_without_sugar']



        first_taste = row.get('first_taste')
        second_taste = row.get('second_taste')
        third_taste = row.get('third_taste')
        fourth_taste = row.get('fourth_taste')

        short_product_name = re.sub('[^a-zA-Z]', '', product_name)
        # Create a unique identifier for each product entry
        unique_id = f"{product_index}_{short_product_name}_{company_name}_{index}"

        # Create RDF triples with unique identifier
        rdf_triples.append(f'<{unique_id}> <productName> "{product_name}" .')
        rdf_triples.append(f'<{unique_id}> <producedBy> <{company_name}> .')
        rdf_triples.append(f'<{company_name}> <location> <{company_location}> .')
        rdf_triples.append(f'<{unique_id}> <originatesFrom> <{country_of_origin}> .')
        rdf_triples.append(f'<{unique_id}> <cocoaPercent> "{cocoa_percent}" .')
        rdf_triples.append(f'<{unique_id}> <rating> "{rating}" .')
        rdf_triples.append(f'<{unique_id}> <numberOfIngredients> "{number_of_ingredients}" .')

        # Handle optional ingredient
        if pd.notna(ingredient_bean) and ingredient_bean:
            rdf_triples.append(f'<{unique_id}> <contains> "{ingredient_bean}" .')
        if pd.notna(ingredient_cocoa_butter) and ingredient_cocoa_butter:
            rdf_triples.append(f'<{unique_id}> <contains> "{ingredient_cocoa_butter}" .')
        if pd.notna(ingredient_vanilla) and ingredient_vanilla:
            rdf_triples.append(f'<{unique_id}> <contains> "{ingredient_vanilla}" .')
        if pd.notna(ingredient_lecithin) and ingredient_lecithin:
            rdf_triples.append(f'<{unique_id}> <contains> "{ingredient_lecithin}" .')
        if pd.notna(ingredient_salt) and ingredient_salt:
            rdf_triples.append(f'<{unique_id}> <contains> "{ingredient_salt}" .')
        if pd.notna(ingredient_sugar) and ingredient_sugar:
            rdf_triples.append(f'<{unique_id}> <contains> "{ingredient_sugar}" .')
        if pd.notna(ingredient_sweetener) and ingredient_sweetener:
            rdf_triples.append(f'<{unique_id}> <contains> "{ingredient_sweetener}" .')

        # Handle optional tastes
        if pd.notna(first_taste) and first_taste:
            rdf_triples.append(f'<{unique_id}> <hasFlavor> <{first_taste}> .')
        if pd.notna(second_taste) and second_taste:
            rdf_triples.append(f'<{unique_id}> <hasFlavor> <{second_taste}> .')
        if pd.notna(third_taste) and third_taste:
            rdf_triples.append(f'<{unique_id}> <hasFlavor> <{third_taste}> .')
        if pd.notna(fourth_taste) and fourth_taste:
            rdf_triples.append(f'<{unique_id}> <hasFlavor> <{fourth_taste}> .')

    return rdf_triples

# Save RDF triples to a file
rdf_triples = create_rdf_triples(data)
with open(output_file_path, 'w') as file:
    for triple in rdf_triples:
        file.write(triple + '\n')


# Function to load RDF triples into Neo4j
def load_data_to_neo4j(uri, user, password, file_path, database):
    driver = GraphDatabase.driver(uri, auth=(user, password))

    def create_session_and_load():
        with driver.session(database=database) as session:
            with open(file_path, 'r') as file:
                for line in file:
                    match = re.match(r'<(.+?)> <(.+?)> (.+) \.', line.strip())
                    if match:
                        subject, predicate, obj = match.groups()
                        obj = re.sub(r'[<>"]', '', obj)  # Clean object

                        if predicate == 'productName':
                            session.run(
                            "MERGE (a:Product {id: $subject}) "
                            "SET a.name = $obj",
                            subject=subject, obj=obj)
                        elif predicate == 'producedBy':
                            session.run(
                            "MERGE (a:Product {id: $subject}) "
                            "MERGE (b:Company {name: $obj}) "
                            "MERGE (a)-[r:PRODUCED_BY]->(b) ",
                            subject=subject, obj=obj)
                        elif predicate == 'location':
                            session.run(
                                "MERGE (a:Company {name: $subject}) "
                                "MERGE (b:Location {name: $object}) "
                                "MERGE (a)-[:LOCATED_IN]->(b)",
                                subject=subject, object=obj)
                        elif predicate == 'originatesFrom':
                            session.run(
                                "MERGE (a:Product {id: $subject}) "
                                "MERGE (b:Origin {name: $object}) "
                                "MERGE (a)-[:ORIGINATES_FROM]->(b)",
                                subject=subject, object=obj)
                        elif predicate == 'cocoaPercent':
                            session.run(
                                "MERGE (a:Product {id: $subject}) "
                                "SET a.cocoaPercent = $object",
                                subject=subject, object=obj)
                        elif predicate == 'rating':
                            session.run(
                                "MERGE (a:Product {id: $subject}) "
                                "SET a.rating = $object",
                                subject=subject, object=obj)
                        elif predicate == 'numberOfIngredients':
                            session.run(
                                "MERGE (a:Product {id: $subject}) "
                                "SET a.numberOfIngredients = $object",
                                subject=subject, object=obj)
                        elif predicate == 'hasFlavor':
                            session.run(
                                "MERGE (a:Product {id: $subject}) "
                                "MERGE (b:Flavor {name: $object}) "
                                "MERGE (a)-[:HAS_FLAVOR]->(b)",
                                subject=subject, object=obj)
                        elif predicate == 'contains':
                            session.run(
                                "MERGE (a:Product {id: $subject}) "
                                "MERGE (b:Ingredient {name: $object}) "
                                "MERGE (a)-[:CONTAINS]->(b)",
                                subject=subject, object=obj)

    create_session_and_load()
    driver.close()

# Neo4j connection details
uri = "bolt://localhost:7687" #adjust according to your database uri.
user = "neo4j" #adjust according to database username.
password = "admin123" #adjust according to your database password.
database = "neo4j" #adjust according to your database name.

# Load the RDF triples into Neo4j
load_data_to_neo4j(uri, user, password, output_file_path, database)
print("Data uploaded successfully")
