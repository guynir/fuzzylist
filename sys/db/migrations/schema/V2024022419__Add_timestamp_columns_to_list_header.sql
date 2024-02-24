ALTER TABLE list_header
    ADD COLUMN created_at TIMESTAMP (6),
    ADD COLUMN updated_at TIMESTAMP (6);

UPDATE list_header SET created_at = NOW() WHERE created_at IS NULL;
UPDATE list_header SET updated_at = NOW() WHERE updated_at IS NULL;

ALTER TABLE list_header
    ALTER COLUMN created_at SET NOT NULL,
    ALTER COLUMN updated_at SET NOT NULL;

ALTER TABLE if EXISTS list_entry DROP CONSTRAINT if EXISTS UK7cc1v1l7k5955pcuu3xdnvxsf;
ALTER TABLE if EXISTS list_entry ADD CONSTRAINT UK7cc1v1l7k5955pcuu3xdnvxsf UNIQUE (parent_id, "index");
