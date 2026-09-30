-- Run once on an existing database when deploying Secteur membership (#22, ADR 0004).
-- From Membre up, every person belongs to one Secteur; Algrange is the only one so far.
-- Bénévoles (the shared pool) and the Super admin keep no Secteur.
-- Check first that exactly one Secteur matches: the update fails if several do, and changes nothing if none does.
UPDATE users
SET sector_id = (SELECT sector_id FROM sectors WHERE name LIKE '%Algrange%')
WHERE role IN ('MEMBRE', 'CHEF_DE_GROUPE', 'BUREAU', 'ADMIN')
  AND sector_id IS NULL
  AND EXISTS (SELECT 1 FROM sectors WHERE name LIKE '%Algrange%');
