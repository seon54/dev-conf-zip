ALTER TABLE conference ALTER COLUMN conference_status TYPE varchar(15);
ALTER TABLE conference ALTER COLUMN conference_status SET DEFAULT 'BEFORE_WATCHING';