TRUNCATE TABLE task_user_mapping;
DELETE FROM person;
DELETE FROM task;

INSERT INTO person (id, name, surname, email, version) VALUES (1, 'Peter', 'Griffin', 'peter.griffin@familyguy.com', 1);
INSERT INTO person (id, name, surname, email, version) VALUES (2, 'Lois', 'Griffin', 'lois.griffin@familyguy.com', 1);
INSERT INTO person (id, name, surname, email, version) VALUES (3, 'Sterling', 'Archer', 'sterling.archer@archer.com', 1);
INSERT INTO person (id, name, surname, email, version) VALUES (4, 'Lana', 'Kane', 'lana.kane@archer.com', 1);
INSERT INTO person (id, name, surname, email, version) VALUES (5, 'Michael', 'Scofield', 'michael.scofield@prisonbreak.com', 1);
INSERT INTO person (id, name, surname, email, version) VALUES (6, 'Lincoln', 'Burrows', 'lincoln.burrows@prisonbreak.com', 1);

-- Insert data into the task table
INSERT INTO task (id, title, description, status, deadline, version)
VALUES
    (1, 'Complete Project Plan', 'Prepare the project plan for the new system', 1, '2023-11-30', 1),
    (2, 'Code Review', 'Review the code for the authentication module', 2, '2024-12-20', 1),
    (3, 'Database Optimization', 'Optimize the database queries for better performance', 3, '2025-12-30', 1);

-- Insert data into the task_user_mapping table to create relationships
INSERT INTO task_user_mapping (task_id, user_id)
VALUES
    (1, 1), -- Peter Griffin is assigned to "Complete Project Plan"
    (1, 2), -- Lois Griffin is also assigned to "Complete Project Plan"
    (2, 3), -- Sterling Archer is assigned to "Code Review"
    (2, 4), -- Lana Kane is also assigned to "Code Review"
    (3, 5), -- Michael Scofield is assigned to "Database Optimization"
    (3, 6); -- Lincoln Burrows is also assigned to "Database Optimization"