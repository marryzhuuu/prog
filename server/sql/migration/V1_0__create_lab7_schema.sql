--
-- PostgreSQL database dump
--

-- Dumped from database version 14.2 (Debian 14.2-1.pgdg110+1)
-- Dumped by pg_dump version 17.4 (Ubuntu 17.4-1.pgdg20.04+2)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: dragons; Type: TABLE; Schema: public; Owner: s381731
--

CREATE TABLE public.dragons (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    x double precision NOT NULL,
    y real NOT NULL,
    creation_date date DEFAULT now() NOT NULL,
    age integer NOT NULL,
    description text NOT NULL,
    color text NOT NULL,
    temper text NOT NULL,
    cave_depth integer NOT NULL,
    cave_treasures bigint NOT NULL,
    creator_id integer NOT NULL,
    CONSTRAINT chk_age CHECK ((age > 0)),
    CONSTRAINT chk_cave_treasures CHECK ((cave_treasures > 0)),
    CONSTRAINT chk_y CHECK ((y > ('-948'::integer)::double precision)),
    CONSTRAINT dragons_color_check CHECK ((color = ANY (ARRAY['RED'::text, 'ORANGE'::text, 'WHITE'::text, 'BROWN'::text]))),
    CONSTRAINT dragons_temper_check CHECK ((temper = ANY (ARRAY['CUNNING'::text, 'EVIL'::text, 'GOOD'::text, 'CHAOTIC_EVIL'::text, 'FICKLE'::text])))
);


ALTER TABLE public.dragons OWNER TO s381731;

--
-- Name: dragons_id_seq; Type: SEQUENCE; Schema: public; Owner: s381731
--

CREATE SEQUENCE public.dragons_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.dragons_id_seq OWNER TO s381731;

--
-- Name: dragons_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: s381731
--

ALTER SEQUENCE public.dragons_id_seq OWNED BY public.dragons.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: s381731
--

CREATE TABLE public.users (
    id integer NOT NULL,
    name character varying(40) NOT NULL,
    password_digest character varying(128) NOT NULL,
    salt character varying(10) NOT NULL
);


ALTER TABLE public.users OWNER TO s381731;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: s381731
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO s381731;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: s381731
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: dragons id; Type: DEFAULT; Schema: public; Owner: s381731
--

ALTER TABLE ONLY public.dragons ALTER COLUMN id SET DEFAULT nextval('public.dragons_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: s381731
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: dragons; Type: TABLE DATA; Schema: public; Owner: s381731
--

COPY public.dragons (id, name, x, y, creation_date, age, description, color, temper, cave_depth, cave_treasures, creator_id) FROM stdin;
15	11	11	11	2025-06-12	11	11	RED	GOOD	11	11	4
16	2	2	2	2025-06-12	2	2	RED	GOOD	2	2	5
17	22	22	22	2025-06-13	22	22	ORANGE	FICKLE	22	22	5
18	3	3	3	2025-06-13	33	33	RED	EVIL	3	3	6
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: s381731
--

COPY public.users (id, name, password_digest, salt) FROM stdin;
4	user1	adf4fed7fe2efc1d9ae888cac869d6dfb4ca537b1a87bfb39a2719c091c7393b43a37e1a2922a2315884d96e7096975b636141441caadddfe4d3553c6ab02721	g1WEXwOUG1
6	user3	78d6fe8daf2dbdc3a46737c0cc1bc457191c29f4fbb87dfbb30a12d4fd031435dd451cd9213f4f79024a73136e7b2aae59cf575251c548c40f9be880e1b9f167	anu4PzFmzE
5	user2	8aa895f62ec70c44d60a47b8ae53b78702aac4bcb8bb99c566ce3afdf8dde2d953b2a71a26379516eda2f3df63f49bdab19899700d93c339fc09882e71afb12b	R9onk4L3VE
\.


--
-- Name: dragons_id_seq; Type: SEQUENCE SET; Schema: public; Owner: s381731
--

SELECT pg_catalog.setval('public.dragons_id_seq', 18, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: s381731
--

SELECT pg_catalog.setval('public.users_id_seq', 6, true);


--
-- Name: dragons dragons_pkey; Type: CONSTRAINT; Schema: public; Owner: s381731
--

ALTER TABLE ONLY public.dragons
    ADD CONSTRAINT dragons_pkey PRIMARY KEY (id);


--
-- Name: users users_name_key; Type: CONSTRAINT; Schema: public; Owner: s381731
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_name_key UNIQUE (name);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: s381731
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: dragons dragons_creator_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: s381731
--

ALTER TABLE ONLY public.dragons
    ADD CONSTRAINT dragons_creator_id_fkey FOREIGN KEY (creator_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: s381731
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

