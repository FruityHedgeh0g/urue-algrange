-- The Fonctionnalités the site knows, as it ships them
INSERT INTO features (name, description, is_active) VALUES
    ('dons-en-ligne', 'Afficher le module de don en ligne sur le site public.', false),
    ('inscription-evenements', 'Permettre l''inscription en ligne aux événements.', true),
    ('galerie-photos', 'Afficher la galerie photos publique.', true)
ON CONFLICT (name) DO NOTHING;

-- Changes to the site asked by the Bureau
CREATE TABLE feature_requests (
    request_id uuid NOT NULL PRIMARY KEY,
    title varchar(255) NOT NULL,
    description text NOT NULL,
    requested_by uuid NOT NULL REFERENCES users (user_id),
    created_at timestamp(6) NOT NULL,
    updated_at timestamp(6) NOT NULL,
    updated_by uuid
);
