CREATE TABLE sinic."groups" (
    id int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	uuid varchar NOT NULL,
	name varchar NOT NULL
);

CREATE TABLE sinic."cycles" (
	id int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	uuid varchar NOT NULL,
	"year" integer NOT NULL,
	amount_periods integer NOT NULL,
	observations varchar NULL,
	created_at timestamp NOT NULL,
	status bool NOT NULL DEFAULT false
);

CREATE TABLE sinic."periods" (
	id int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	uuid varchar NOT NULL,
	cycle_id varchar NOT NULL,
	date_start timestamp NOT NULL,
	date_finish timestamp NOT NULL,
	created_at timestamp NOT NULL
);

CREATE TABLE sinic."period_groups" (
	id int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	uuid varchar NOT NULL,
	period_id varchar NOT NULL,
	group_id varchar NOT NULL,
	date_start timestamp NOT NULL,
	date_finish timestamp NOT NULL
);
