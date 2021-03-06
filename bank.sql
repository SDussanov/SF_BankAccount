PGDMP     .    :                z            bank    14.2    14.2                0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    16421    bank    DATABASE     h   CREATE DATABASE bank WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'English_United States.1252';
    DROP DATABASE bank;
                postgres    false            ?            1259    16428    accounts    TABLE     W   CREATE TABLE public.accounts (
    id integer NOT NULL,
    amount integer NOT NULL
);
    DROP TABLE public.accounts;
       public         heap    postgres    false            ?            1259    16427    accounts_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.accounts_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.accounts_id_seq;
       public          postgres    false    210                       0    0    accounts_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.accounts_id_seq OWNED BY public.accounts.id;
          public          postgres    false    209            ?            1259    16445    operations_id_seq    SEQUENCE     z   CREATE SEQUENCE public.operations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.operations_id_seq;
       public          postgres    false            ?            1259    16434 
   operations    TABLE     ?   CREATE TABLE public.operations (
    id integer DEFAULT nextval('public.operations_id_seq'::regclass) NOT NULL,
    id_account integer NOT NULL,
    type integer NOT NULL,
    amount integer NOT NULL,
    date date NOT NULL
);
    DROP TABLE public.operations;
       public         heap    postgres    false    212            ?            1259    16453    transfers_id    SEQUENCE     u   CREATE SEQUENCE public.transfers_id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.transfers_id;
       public          postgres    false            ?            1259    16461 	   transfers    TABLE     ?   CREATE TABLE public.transfers (
    id integer DEFAULT nextval('public.transfers_id'::regclass) NOT NULL,
    send_id integer,
    receive_id integer,
    amount integer,
    date date
);
    DROP TABLE public.transfers;
       public         heap    postgres    false    213            f           2604    16431    accounts id    DEFAULT     j   ALTER TABLE ONLY public.accounts ALTER COLUMN id SET DEFAULT nextval('public.accounts_id_seq'::regclass);
 :   ALTER TABLE public.accounts ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    210    209    210            ?          0    16428    accounts 
   TABLE DATA           .   COPY public.accounts (id, amount) FROM stdin;
    public          postgres    false    210   h       ?          0    16434 
   operations 
   TABLE DATA           H   COPY public.operations (id, id_account, type, amount, date) FROM stdin;
    public          postgres    false    211   ?       ?          0    16461 	   transfers 
   TABLE DATA           J   COPY public.transfers (id, send_id, receive_id, amount, date) FROM stdin;
    public          postgres    false    214   F                  0    0    accounts_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.accounts_id_seq', 3, true);
          public          postgres    false    209                       0    0    operations_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.operations_id_seq', 41, true);
          public          postgres    false    212                       0    0    transfers_id    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.transfers_id', 24, true);
          public          postgres    false    213            j           2606    16433    accounts accounts_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.accounts
    ADD CONSTRAINT accounts_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.accounts DROP CONSTRAINT accounts_pkey;
       public            postgres    false    210            l           2606    16438    operations operations_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.operations
    ADD CONSTRAINT operations_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.operations DROP CONSTRAINT operations_pkey;
       public            postgres    false    211            m           2606    16439    operations id_account    FK CONSTRAINT     z   ALTER TABLE ONLY public.operations
    ADD CONSTRAINT id_account FOREIGN KEY (id_account) REFERENCES public.accounts(id);
 ?   ALTER TABLE ONLY public.operations DROP CONSTRAINT id_account;
       public          postgres    false    211    3178    210            ?      x?3?45 .#N3 i?ih?b???? 5??      ?   ?   x?m??? ?s?_?X??ￎ??r0#nOH??B.?ǘ/e酭??z????֢79?-z}??????$?
????1I=2,mI[?Nҡ?w*?U?*??mk??5?lk?
?2?w??+I???du?k)???C]??CW?C??]?????#?o?      ?   W   x?mб?0?:?%<[????s??P???14?ޓ???mI?	?)?x???PO?u?&?F?????2A[k?k*??B[?s??D0D?     