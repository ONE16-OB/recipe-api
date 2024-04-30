INSERT INTO ingredients (created_at, updated_at, uuid, name)
VALUES
    ('2024-04-30 15:28:37.372795', '2024-04-30 15:28:37.372795', 'ddbabd83-1d21-497e-9be0-e0cd10433a82', 'Ingredient A'),
    ('2024-04-30 15:28:40.724579', '2024-04-30 15:30:30.495489', '03caf04c-6804-4233-a361-67bdc218b72d', 'Ingredient B'),
    ('2024-04-30 15:28:33.313129', '2024-04-30 15:28:33.313129', 'cd4e583a-13a1-4f2f-a868-9a30ef30d5a3', 'Ingredient C'),
    ('2024-04-30 15:28:40.724579', '2024-04-30 15:30:30.495489', '78a6a417-1bcf-4bb2-af93-1cb7861d13f1', 'Ingredient D'),
    ('2024-04-30 15:28:33.313129', '2024-04-30 15:28:33.313129', '6aee245a-1522-4920-aafd-8b6be1f6a9c1', 'Ingredient E'),
    ('2024-04-30 15:30:30.495489', '2024-04-30 15:30:30.495489', '54173520-8799-4a1c-a2bc-fc57a3e1de6e', 'Ingredient F'),
    ('2024-04-30 15:32:12.833561', '2024-04-30 15:32:12.833561', 'e1bb99f3-8b17-4737-b57f-03a5a914b6c3', 'Ingredient G'),
    ('2024-04-30 15:28:33.313129', '2024-04-30 15:28:33.313129', '52992926-6db7-4f02-81bb-f897d5f80da7', 'Ingredient H'),
    ('2024-04-30 15:30:30.495489', '2024-04-30 15:30:30.495489', '95f36b64-f9f7-4b35-8374-5ff8c927bf7c', 'Ingredient I'),
    ('2024-04-30 15:32:12.833561', '2024-04-30 15:32:12.833561', 'a8471f5e-59f4-487f-86cb-42f445d83475', 'Ingredient J'),
    ('2024-04-30 15:28:33.313129', '2024-04-30 15:28:33.313129', '1af4e1d8-d570-4bc7-9256-11e196a66fd5', 'Ingredient K'),
    ('2024-04-30 15:30:30.495489', '2024-04-30 15:30:30.495489', '2b6a81f2-5a9c-448f-9f20-09db1c08c16e', 'Ingredient L'),
    ('2024-04-30 15:32:12.833561', '2024-04-30 15:32:12.833561', 'c7f6f59b-c6cb-43a1-b8a6-2e63e1b37c24', 'Ingredient M');

insert into recipes (created_at, updated_at, uuid, cooking_instructions, description, name)
values
    ('2024-04-30 15:28:37.372795', '2024-04-30 15:28:37.372795', 'ddbabd83-1d21-497e-9be0-e0cd10433a82', 'Cooking instructions for recipe A', 'Description for recipe A', 'Recipe A'),
    ('2024-04-30 15:28:40.724579', '2024-04-30 15:30:30.495489', '03caf04c-6804-4233-a361-67bdc218b72d', 'Cooking instructions for recipe B', 'Description for recipe B', 'Recipe B'),
    ('2024-04-30 15:28:33.313129', '2024-04-30 15:28:33.313129', 'cd4e583a-13a1-4f2f-a868-9a30ef30d5a3', 'Cooking instructions for recipe C', 'Description for recipe C', 'Recipe C'),
    ('2024-04-30 15:30:30.495489', '2024-04-30 15:30:30.495489', '8c07a44f-2cc9-4a6d-9a0b-cc0bf4ec0578', 'Cooking instructions for recipe D', 'Description for recipe D', 'Recipe D'),
    ('2024-04-30 15:32:12.833561', '2024-04-30 15:32:12.833561', '51b4482a-7e68-4099-ba0f-680fa8e51ac4', 'Cooking instructions for recipe E', 'Description for recipe E', 'Recipe E');

INSERT INTO recipe_ingredient (created_at, updated_at, ingredient_id, recipe_id, quantity)
SELECT
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP,
            ingredient.id,
            recipe.id,
            'quantity'
FROM
    ingredients AS ingredient
        CROSS JOIN
    recipes AS recipe;
