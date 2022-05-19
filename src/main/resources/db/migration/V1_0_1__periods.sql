CREATE TABLE sinic."groups" (
    id int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	uuid varchar NOT NULL,
	name varchar NOT NULL
);

CREATE TABLE sinic.periods (
	id varchar NOT NULL,
	uuid varchar NOT NULL,
	"year" integer NOT NULL,
	date_start timestamp NOT NULL,
	date_finish timestamp NOT NULL,
	created_at timestamp NOT NULL
);

CREATE TABLE sinic.period_groups (
	id varchar NOT NULL,
	uuid varchar NOT NULL,
	period_id varchar NOT NULL,
	group_id varchar NOT NULL,
	date_start timestamp NOT NULL,
	date_finish timestamp NOT NULL
);
