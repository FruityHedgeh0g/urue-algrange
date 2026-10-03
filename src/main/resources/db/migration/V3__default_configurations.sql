-- The site-wide settings the site knows, so the Super admin has them to fill in
INSERT INTO configurations (name, config_value, created_at, updated_at) VALUES
    ('site.title', 'Une Rose Un Espoir', now(), now()),
    ('association.tagline', 'Ensemble contre le cancer', now(), now()),
    ('contact.email', '', now(), now()),
    ('navbar.logo', '', now(), now())
ON CONFLICT (name) DO NOTHING;
