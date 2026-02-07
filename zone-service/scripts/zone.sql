-- Create database if not exists
\connect postgres
CREATE DATABASE zone_db;
\connect zone_db

-- DROP SCHEMA "zone";

CREATE SCHEMA "zone" AUTHORIZATION pg_database_owner;

-- DROP TYPE "zone"."energy_level";

CREATE TYPE "zone"."energy_level" AS ENUM (
	'HIGH',
	'MEDIUM',
	'LOW');

-- DROP TYPE "zone"."intervals";

CREATE TYPE "zone"."intervals" AS ENUM (
	'YEAR',
	'MONTH',
	'WEEK',
	'DAY');

-- "zone".area (Роль/Зона ответственности)
CREATE TABLE "zone".area
(
    id         uuid                  DEFAULT gen_random_uuid() NOT NULL,
    "name"     varchar               DEFAULT 'No name'::character varying NOT NULL,
    color      varchar               DEFAULT '#FFFFFF'::character varying NOT NULL,
    energy     "zone"."energy_level" DEFAULT 'MEDIUM'::zone.energy_level NOT NULL,
    user_id    uuid                  NOT NULL,
    is_blocked bool                  DEFAULT false NOT NULL,
    CONSTRAINT area_pk PRIMARY KEY (id)
);

-- "zone".activity (Активности в рамках роли)
CREATE TABLE "zone".activity
(
    id      uuid    DEFAULT gen_random_uuid() NOT NULL,
    "name"  varchar DEFAULT 'No name'::character varying NOT NULL,
    area_id uuid    NOT NULL,
    CONSTRAINT activity_pk PRIMARY KEY (id),
    CONSTRAINT activity_area_fk FOREIGN KEY (area_id) REFERENCES "zone".area (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- "zone".energy_plan (План энергии для Area)
CREATE TABLE "zone".energy_plan
(
    id          uuid                         DEFAULT gen_random_uuid() NOT NULL,
    area_id     uuid                         NOT NULL,
    duration    "zone"."intervals"           DEFAULT 'MONTH'::zone.intervals NOT NULL,
    rhythm      "zone"."intervals"           NOT NULL,
    frequency   int4                         DEFAULT 0 NOT NULL,
    date_start  timestamptz                  NULL,
    date_finish timestamptz                  NULL,
    CONSTRAINT energy_plan_pk PRIMARY KEY (id),
    CONSTRAINT energy_plan_area_fk FOREIGN KEY (area_id) REFERENCES "zone".area (id) ON DELETE CASCADE
);

-- "zone".plan_progress (Прогресс выполнения плана)
CREATE TABLE "zone".plan_progress
(
    id             uuid               DEFAULT gen_random_uuid() NOT NULL,
    energy_plan_id uuid               NOT NULL,
    took           int4               DEFAULT 0 NOT NULL,
    timespan       "zone"."intervals" DEFAULT 'WEEK'::zone.intervals NOT NULL,
    is_done        bool               DEFAULT false NOT NULL,
    CONSTRAINT plan_progress_pk PRIMARY KEY (id),
    CONSTRAINT plan_progress_energy_plan_fk FOREIGN KEY (energy_plan_id) REFERENCES "zone".energy_plan (id) ON DELETE CASCADE
);

-- "zone".activity_in_progress (Связь Activity с PlanProgress - many-to-many)
CREATE TABLE "zone".activity_in_progress
(
    id               uuid DEFAULT gen_random_uuid() NOT NULL,
    activity_id      uuid NOT NULL,
    plan_progress_id uuid NOT NULL,
    CONSTRAINT activity_in_progress_pk PRIMARY KEY (id),
    CONSTRAINT aip_activity_fk FOREIGN KEY (activity_id) REFERENCES "zone".activity (id) ON DELETE CASCADE,
    CONSTRAINT aip_plan_progress_fk FOREIGN KEY (plan_progress_id) REFERENCES "zone".plan_progress (id) ON DELETE CASCADE
);

-- "zone".timer (Таймер для трекинга активности)
CREATE TABLE "zone".timer
(
    id                      uuid DEFAULT gen_random_uuid() NOT NULL,
    activity_in_progress_id uuid NOT NULL,
    "start"                 timestamptz NOT NULL,
    CONSTRAINT timer_pk PRIMARY KEY (id),
    CONSTRAINT timer_aip_fk FOREIGN KEY (activity_in_progress_id) REFERENCES "zone".activity_in_progress (id) ON DELETE CASCADE
);