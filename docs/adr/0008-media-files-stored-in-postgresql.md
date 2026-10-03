# Media files are stored in PostgreSQL

The images the Bureau uploads (Post banners, the carousel, the logo, the gallery) are kept in the database, in a `media_contents` table apart from their description in `medias`, and served by the application at `/api/medias/{id}/content` with a one-year cache, since a file never changes. Uploads are limited to JPEG, PNG, WebP and GIF images of at most 8 MB. For an association's photos this keeps a single thing to back up and nothing more to configure on the server.

## Considered Options

- **A Docker volume on the VPS**: rejected for now; a second thing to back up and to mount on every deployment.
- **Object storage (S3-compatible)**: rejected for now; an external service, credentials and costs, for a few hundred images.

## Consequences

- The database grows with the images; past a few gigabytes, moving the files to object storage means a migration of `media_contents`, while `medias` and the API stay as they are.
- Videos and SVG images are refused: the former do not belong in the database, the latter could carry a script.
