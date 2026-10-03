-- A Post's content and an Event's description are articles, not one-line labels
ALTER TABLE posts ALTER COLUMN content TYPE text;
ALTER TABLE events ALTER COLUMN description TYPE text;
