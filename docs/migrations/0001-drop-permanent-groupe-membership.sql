-- Run once on an existing database when deploying the removal of the permanent
-- person-Groupe membership (#19): Hibernate `update` never drops columns.
-- Who rides with a Groupe now lives on each Event's sign-ups (event_registrations).
-- Dropping the column also drops its foreign key to groups (H2 and PostgreSQL).
ALTER TABLE users DROP COLUMN IF EXISTS group_id;
