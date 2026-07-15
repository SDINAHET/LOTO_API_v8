--
-- PostgreSQL database dump
--

\restrict 3bzywhTi3m0icGcEbHaoW21LhxykBJUrE98H6x6H0BVFD1VQGDR5xDeuGAWQYjP

-- Dumped from database version 14.23 (Ubuntu 14.23-0ubuntu0.22.04.1)
-- Dumped by pg_dump version 14.22 (Ubuntu 14.22-0ubuntu0.22.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
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
-- Name: ticket_gains; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.ticket_gains (
    id character varying NOT NULL,
    ticket_id character varying NOT NULL,
    matching_numbers integer NOT NULL,
    lucky_number_match boolean NOT NULL,
    gain_amount numeric(10,2) NOT NULL
);


--
-- Name: tickets; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tickets (
    id character varying NOT NULL,
    numbers character varying NOT NULL,
    lucky_number integer NOT NULL,
    draw_date date NOT NULL,
    draw_day character varying,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    user_id character varying NOT NULL
);


--
-- Name: user_ticket; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_ticket (
    user_id character varying NOT NULL,
    ticket_id character varying NOT NULL,
    role character varying DEFAULT 'creator'::character varying,
    CONSTRAINT user_ticket_role_check CHECK (((role)::text = ANY ((ARRAY['creator'::character varying, 'assignee'::character varying, 'watcher'::character varying])::text[])))
);


--
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
    id character varying NOT NULL,
    first_name character varying NOT NULL,
    last_name character varying NOT NULL,
    email character varying NOT NULL,
    password character varying NOT NULL,
    is_admin boolean DEFAULT false NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: ticket_gains ticket_gains_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ticket_gains
    ADD CONSTRAINT ticket_gains_pkey PRIMARY KEY (id);


--
-- Name: tickets tickets_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_pkey PRIMARY KEY (id);


--
-- Name: user_ticket user_ticket_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_ticket
    ADD CONSTRAINT user_ticket_pkey PRIMARY KEY (user_id, ticket_id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: ticket_gains ticket_gains_ticket_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ticket_gains
    ADD CONSTRAINT ticket_gains_ticket_id_fkey FOREIGN KEY (ticket_id) REFERENCES public.tickets(id) ON DELETE CASCADE;


--
-- Name: tickets tickets_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: user_ticket user_ticket_ticket_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_ticket
    ADD CONSTRAINT user_ticket_ticket_id_fkey FOREIGN KEY (ticket_id) REFERENCES public.tickets(id) ON DELETE CASCADE;


--
-- Name: user_ticket user_ticket_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_ticket
    ADD CONSTRAINT user_ticket_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

\unrestrict 3bzywhTi3m0icGcEbHaoW21LhxykBJUrE98H6x6H0BVFD1VQGDR5xDeuGAWQYjP

