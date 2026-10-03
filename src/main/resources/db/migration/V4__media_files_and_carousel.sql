-- Media files live in the database, apart from their description (ADR 0008)
ALTER TABLE medias ADD COLUMN mime_type varchar(255);
ALTER TABLE medias ADD COLUMN alt varchar(255);
CREATE TABLE media_contents (
    media_id uuid NOT NULL PRIMARY KEY REFERENCES medias (media_id),
    content bytea NOT NULL
);

-- The home page's carousel
CREATE TABLE carousel_items (
    item_id uuid NOT NULL PRIMARY KEY,
    title varchar(255) NOT NULL,
    caption text,
    media_id uuid REFERENCES medias (media_id),
    link_to varchar(255),
    active boolean NOT NULL,
    position integer NOT NULL,
    created_at timestamp(6) NOT NULL,
    updated_at timestamp(6) NOT NULL,
    updated_by uuid
);
