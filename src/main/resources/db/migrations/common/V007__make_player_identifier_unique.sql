ALTER TABLE player
ALTER COLUMN identifier SET NOT NULL;

ALTER TABLE player
ADD CONSTRAINT identifier_unique UNIQUE (identifier);