PGDMP     2                    |           dietideals24    14.4    14.4 K    B           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            C           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            D           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            E           1262    49769    dietideals24    DATABASE     h   CREATE DATABASE dietideals24 WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Italian_Italy.1252';
    DROP DATABASE dietideals24;
                postgres    false            �            1259    49781    asta    TABLE        CREATE TABLE public.asta (
    id integer NOT NULL,
    id_creatore integer NOT NULL,
    nome character varying(32) NOT NULL,
    descrizione character varying(256),
    categoria smallint NOT NULL,
    foto bytea,
    dtype character varying(31) NOT NULL,
    tipo smallint,
    id_asta integer,
    prezzo real,
    scadenza timestamp(6) without time zone,
    decremento real,
    minimo real,
    timer timestamp(6) without time zone,
    CONSTRAINT asta_tipo_check CHECK (((tipo >= 0) AND (tipo <= 2)))
);
    DROP TABLE public.asta;
       public         heap    postgres    false            �            1259    49780    asta_id_creatore_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_id_creatore_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.asta_id_creatore_seq;
       public          postgres    false    213            F           0    0    asta_id_creatore_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.asta_id_creatore_seq OWNED BY public.asta.id_creatore;
          public          postgres    false    212            �            1259    49779    asta_id_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.asta_id_seq;
       public          postgres    false    213            G           0    0    asta_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE public.asta_id_seq OWNED BY public.asta.id;
          public          postgres    false    211            �            1259    49853    asta_inversa    TABLE     �   CREATE TABLE public.asta_inversa (
    id_asta integer NOT NULL,
    prezzo double precision NOT NULL,
    scadenza date NOT NULL
);
     DROP TABLE public.asta_inversa;
       public         heap    postgres    false            �            1259    49852    asta_inversa_id_asta_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_inversa_id_asta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.asta_inversa_id_asta_seq;
       public          postgres    false    226            H           0    0    asta_inversa_id_asta_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.asta_inversa_id_asta_seq OWNED BY public.asta_inversa.id_asta;
          public          postgres    false    225            �            1259    49833    asta_ribasso    TABLE     �   CREATE TABLE public.asta_ribasso (
    id_asta integer NOT NULL,
    prezzo double precision NOT NULL,
    timer date NOT NULL,
    decremento double precision NOT NULL,
    minimo double precision NOT NULL
);
     DROP TABLE public.asta_ribasso;
       public         heap    postgres    false            �            1259    49832    asta_ribasso_id_asta_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_ribasso_id_asta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.asta_ribasso_id_asta_seq;
       public          postgres    false    222            I           0    0    asta_ribasso_id_asta_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.asta_ribasso_id_asta_seq OWNED BY public.asta_ribasso.id_asta;
          public          postgres    false    221            �            1259    49843    asta_silenziosa    TABLE     b   CREATE TABLE public.asta_silenziosa (
    id_asta integer NOT NULL,
    scadenza date NOT NULL
);
 #   DROP TABLE public.asta_silenziosa;
       public         heap    postgres    false            �            1259    49842    asta_silenziosa_id_asta_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_silenziosa_id_asta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 2   DROP SEQUENCE public.asta_silenziosa_id_asta_seq;
       public          postgres    false    224            J           0    0    asta_silenziosa_id_asta_seq    SEQUENCE OWNED BY     [   ALTER SEQUENCE public.asta_silenziosa_id_asta_seq OWNED BY public.asta_silenziosa.id_asta;
          public          postgres    false    223            �            1259    49813 
   compratore    TABLE     C   CREATE TABLE public.compratore (
    id_utente integer NOT NULL
);
    DROP TABLE public.compratore;
       public         heap    postgres    false            �            1259    49812    compratore_id_utente_seq    SEQUENCE     �   CREATE SEQUENCE public.compratore_id_utente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.compratore_id_utente_seq;
       public          postgres    false    218            K           0    0    compratore_id_utente_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.compratore_id_utente_seq OWNED BY public.compratore.id_utente;
          public          postgres    false    217            �            1259    49966    notifica    TABLE     �   CREATE TABLE public.notifica (
    id integer NOT NULL,
    id_utente integer NOT NULL,
    testo character varying(128) NOT NULL,
    data timestamp(6) without time zone NOT NULL,
    letta boolean NOT NULL
);
    DROP TABLE public.notifica;
       public         heap    postgres    false            �            1259    49797    offerta    TABLE     �   CREATE TABLE public.offerta (
    id_utente integer NOT NULL,
    id_asta integer NOT NULL,
    valore double precision NOT NULL,
    data timestamp(6) without time zone NOT NULL,
    id integer NOT NULL
);
    DROP TABLE public.offerta;
       public         heap    postgres    false            �            1259    49796    offerta_id_asta_seq    SEQUENCE     �   CREATE SEQUENCE public.offerta_id_asta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.offerta_id_asta_seq;
       public          postgres    false    216            L           0    0    offerta_id_asta_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.offerta_id_asta_seq OWNED BY public.offerta.id_asta;
          public          postgres    false    215            �            1259    49795    offerta_id_utente_seq    SEQUENCE     �   CREATE SEQUENCE public.offerta_id_utente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.offerta_id_utente_seq;
       public          postgres    false    216            M           0    0    offerta_id_utente_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.offerta_id_utente_seq OWNED BY public.offerta.id_utente;
          public          postgres    false    214            �            1259    49771    utente    TABLE     �  CREATE TABLE public.utente (
    id integer NOT NULL,
    username character varying(32) NOT NULL,
    email character varying(64) NOT NULL,
    password character varying(32) NOT NULL,
    biografia character varying(256),
    sitoweb character varying(32),
    paese character varying(32),
    avatar bytea,
    tipo smallint,
    CONSTRAINT utente_tipo_check CHECK (((tipo >= 0) AND (tipo <= 1)))
);
    DROP TABLE public.utente;
       public         heap    postgres    false            �            1259    49770    utente_id_seq    SEQUENCE     �   CREATE SEQUENCE public.utente_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.utente_id_seq;
       public          postgres    false    210            N           0    0    utente_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.utente_id_seq OWNED BY public.utente.id;
          public          postgres    false    209            �            1259    49823 	   venditore    TABLE     B   CREATE TABLE public.venditore (
    id_utente integer NOT NULL
);
    DROP TABLE public.venditore;
       public         heap    postgres    false            �            1259    49822    venditore_id_utente_seq    SEQUENCE     �   CREATE SEQUENCE public.venditore_id_utente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.venditore_id_utente_seq;
       public          postgres    false    220            O           0    0    venditore_id_utente_seq    SEQUENCE OWNED BY     S   ALTER SEQUENCE public.venditore_id_utente_seq OWNED BY public.venditore.id_utente;
          public          postgres    false    219            �           2604    49784    asta id    DEFAULT     b   ALTER TABLE ONLY public.asta ALTER COLUMN id SET DEFAULT nextval('public.asta_id_seq'::regclass);
 6   ALTER TABLE public.asta ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    213    211    213            �           2604    49785    asta id_creatore    DEFAULT     t   ALTER TABLE ONLY public.asta ALTER COLUMN id_creatore SET DEFAULT nextval('public.asta_id_creatore_seq'::regclass);
 ?   ALTER TABLE public.asta ALTER COLUMN id_creatore DROP DEFAULT;
       public          postgres    false    212    213    213            �           2604    49856    asta_inversa id_asta    DEFAULT     |   ALTER TABLE ONLY public.asta_inversa ALTER COLUMN id_asta SET DEFAULT nextval('public.asta_inversa_id_asta_seq'::regclass);
 C   ALTER TABLE public.asta_inversa ALTER COLUMN id_asta DROP DEFAULT;
       public          postgres    false    226    225    226            �           2604    49836    asta_ribasso id_asta    DEFAULT     |   ALTER TABLE ONLY public.asta_ribasso ALTER COLUMN id_asta SET DEFAULT nextval('public.asta_ribasso_id_asta_seq'::regclass);
 C   ALTER TABLE public.asta_ribasso ALTER COLUMN id_asta DROP DEFAULT;
       public          postgres    false    222    221    222            �           2604    49846    asta_silenziosa id_asta    DEFAULT     �   ALTER TABLE ONLY public.asta_silenziosa ALTER COLUMN id_asta SET DEFAULT nextval('public.asta_silenziosa_id_asta_seq'::regclass);
 F   ALTER TABLE public.asta_silenziosa ALTER COLUMN id_asta DROP DEFAULT;
       public          postgres    false    224    223    224            �           2604    49816    compratore id_utente    DEFAULT     |   ALTER TABLE ONLY public.compratore ALTER COLUMN id_utente SET DEFAULT nextval('public.compratore_id_utente_seq'::regclass);
 C   ALTER TABLE public.compratore ALTER COLUMN id_utente DROP DEFAULT;
       public          postgres    false    217    218    218            �           2604    49800    offerta id_utente    DEFAULT     v   ALTER TABLE ONLY public.offerta ALTER COLUMN id_utente SET DEFAULT nextval('public.offerta_id_utente_seq'::regclass);
 @   ALTER TABLE public.offerta ALTER COLUMN id_utente DROP DEFAULT;
       public          postgres    false    214    216    216            �           2604    49801    offerta id_asta    DEFAULT     r   ALTER TABLE ONLY public.offerta ALTER COLUMN id_asta SET DEFAULT nextval('public.offerta_id_asta_seq'::regclass);
 >   ALTER TABLE public.offerta ALTER COLUMN id_asta DROP DEFAULT;
       public          postgres    false    215    216    216            �           2604    49774 	   utente id    DEFAULT     f   ALTER TABLE ONLY public.utente ALTER COLUMN id SET DEFAULT nextval('public.utente_id_seq'::regclass);
 8   ALTER TABLE public.utente ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    209    210    210            �           2604    49826    venditore id_utente    DEFAULT     z   ALTER TABLE ONLY public.venditore ALTER COLUMN id_utente SET DEFAULT nextval('public.venditore_id_utente_seq'::regclass);
 B   ALTER TABLE public.venditore ALTER COLUMN id_utente DROP DEFAULT;
       public          postgres    false    220    219    220            1          0    49781    asta 
   TABLE DATA           �   COPY public.asta (id, id_creatore, nome, descrizione, categoria, foto, dtype, tipo, id_asta, prezzo, scadenza, decremento, minimo, timer) FROM stdin;
    public          postgres    false    213   �V       >          0    49853    asta_inversa 
   TABLE DATA           A   COPY public.asta_inversa (id_asta, prezzo, scadenza) FROM stdin;
    public          postgres    false    226   W       :          0    49833    asta_ribasso 
   TABLE DATA           R   COPY public.asta_ribasso (id_asta, prezzo, timer, decremento, minimo) FROM stdin;
    public          postgres    false    222   2W       <          0    49843    asta_silenziosa 
   TABLE DATA           <   COPY public.asta_silenziosa (id_asta, scadenza) FROM stdin;
    public          postgres    false    224   OW       6          0    49813 
   compratore 
   TABLE DATA           /   COPY public.compratore (id_utente) FROM stdin;
    public          postgres    false    218   lW       ?          0    49966    notifica 
   TABLE DATA           E   COPY public.notifica (id, id_utente, testo, data, letta) FROM stdin;
    public          postgres    false    227   �W       4          0    49797    offerta 
   TABLE DATA           G   COPY public.offerta (id_utente, id_asta, valore, data, id) FROM stdin;
    public          postgres    false    216   �W       .          0    49771    utente 
   TABLE DATA           h   COPY public.utente (id, username, email, password, biografia, sitoweb, paese, avatar, tipo) FROM stdin;
    public          postgres    false    210   �W       8          0    49823 	   venditore 
   TABLE DATA           .   COPY public.venditore (id_utente) FROM stdin;
    public          postgres    false    220   X       P           0    0    asta_id_creatore_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.asta_id_creatore_seq', 1, false);
          public          postgres    false    212            Q           0    0    asta_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.asta_id_seq', 1, false);
          public          postgres    false    211            R           0    0    asta_inversa_id_asta_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.asta_inversa_id_asta_seq', 1, false);
          public          postgres    false    225            S           0    0    asta_ribasso_id_asta_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.asta_ribasso_id_asta_seq', 1, false);
          public          postgres    false    221            T           0    0    asta_silenziosa_id_asta_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.asta_silenziosa_id_asta_seq', 1, false);
          public          postgres    false    223            U           0    0    compratore_id_utente_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.compratore_id_utente_seq', 1, false);
          public          postgres    false    217            V           0    0    offerta_id_asta_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.offerta_id_asta_seq', 1, false);
          public          postgres    false    215            W           0    0    offerta_id_utente_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.offerta_id_utente_seq', 1, false);
          public          postgres    false    214            X           0    0    utente_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.utente_id_seq', 2, true);
          public          postgres    false    209            Y           0    0    venditore_id_utente_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.venditore_id_utente_seq', 1, false);
          public          postgres    false    219            �           2606    49789    asta asta_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.asta
    ADD CONSTRAINT asta_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.asta DROP CONSTRAINT asta_pkey;
       public            postgres    false    213            �           2606    49970    notifica notifica_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.notifica
    ADD CONSTRAINT notifica_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.notifica DROP CONSTRAINT notifica_pkey;
       public            postgres    false    227            �           2606    58148    offerta offerta_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.offerta
    ADD CONSTRAINT offerta_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.offerta DROP CONSTRAINT offerta_pkey;
       public            postgres    false    216            �           2606    49778    utente utente_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_pkey;
       public            postgres    false    210            �           2606    49790    asta asta_id_creatore_fkey    FK CONSTRAINT     ~   ALTER TABLE ONLY public.asta
    ADD CONSTRAINT asta_id_creatore_fkey FOREIGN KEY (id_creatore) REFERENCES public.utente(id);
 D   ALTER TABLE ONLY public.asta DROP CONSTRAINT asta_id_creatore_fkey;
       public          postgres    false    3218    210    213            �           2606    49857 &   asta_inversa asta_inversa_id_asta_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.asta_inversa
    ADD CONSTRAINT asta_inversa_id_asta_fkey FOREIGN KEY (id_asta) REFERENCES public.asta(id);
 P   ALTER TABLE ONLY public.asta_inversa DROP CONSTRAINT asta_inversa_id_asta_fkey;
       public          postgres    false    226    213    3220            �           2606    49837 &   asta_ribasso asta_ribasso_id_asta_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.asta_ribasso
    ADD CONSTRAINT asta_ribasso_id_asta_fkey FOREIGN KEY (id_asta) REFERENCES public.asta(id);
 P   ALTER TABLE ONLY public.asta_ribasso DROP CONSTRAINT asta_ribasso_id_asta_fkey;
       public          postgres    false    213    222    3220            �           2606    49847 ,   asta_silenziosa asta_silenziosa_id_asta_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.asta_silenziosa
    ADD CONSTRAINT asta_silenziosa_id_asta_fkey FOREIGN KEY (id_asta) REFERENCES public.asta(id);
 V   ALTER TABLE ONLY public.asta_silenziosa DROP CONSTRAINT asta_silenziosa_id_asta_fkey;
       public          postgres    false    224    213    3220            �           2606    49817 $   compratore compratore_id_utente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.compratore
    ADD CONSTRAINT compratore_id_utente_fkey FOREIGN KEY (id_utente) REFERENCES public.utente(id);
 N   ALTER TABLE ONLY public.compratore DROP CONSTRAINT compratore_id_utente_fkey;
       public          postgres    false    3218    210    218            �           2606    49971     notifica notifica_id_utente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.notifica
    ADD CONSTRAINT notifica_id_utente_fkey FOREIGN KEY (id_utente) REFERENCES public.utente(id);
 J   ALTER TABLE ONLY public.notifica DROP CONSTRAINT notifica_id_utente_fkey;
       public          postgres    false    227    3218    210            �           2606    49807    offerta offerta_id_asta_fkey    FK CONSTRAINT     z   ALTER TABLE ONLY public.offerta
    ADD CONSTRAINT offerta_id_asta_fkey FOREIGN KEY (id_asta) REFERENCES public.asta(id);
 F   ALTER TABLE ONLY public.offerta DROP CONSTRAINT offerta_id_asta_fkey;
       public          postgres    false    3220    216    213            �           2606    49802    offerta offerta_id_utente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.offerta
    ADD CONSTRAINT offerta_id_utente_fkey FOREIGN KEY (id_utente) REFERENCES public.utente(id);
 H   ALTER TABLE ONLY public.offerta DROP CONSTRAINT offerta_id_utente_fkey;
       public          postgres    false    216    210    3218            �           2606    49827 "   venditore venditore_id_utente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.venditore
    ADD CONSTRAINT venditore_id_utente_fkey FOREIGN KEY (id_utente) REFERENCES public.utente(id);
 L   ALTER TABLE ONLY public.venditore DROP CONSTRAINT venditore_id_utente_fkey;
       public          postgres    false    210    3218    220            1      x������ � �      >      x������ � �      :      x������ � �      <      x������ � �      6      x������ � �      ?      x������ � �      4      x������ � �      .   4   x�3�,(����,������Rs3s���s9K���А3�q��qqq d�      8      x������ � �     