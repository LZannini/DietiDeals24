PGDMP         :                |           dietideals24    15.4    15.3 >    =           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            >           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            ?           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            @           1262    17075    dietideals24    DATABASE        CREATE DATABASE dietideals24 WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Italian_Italy.1252';
    DROP DATABASE dietideals24;
                postgres    false            S           1247    17077    tipo_utente    TYPE     ^   CREATE TYPE public.tipo_utente AS ENUM (
    'COMPRATORE',
    'VENDITORE',
    'COMPLETO'
);
    DROP TYPE public.tipo_utente;
       public          postgres    false            �            1259    17083    asta    TABLE     �  CREATE TABLE public.asta (
    id integer NOT NULL,
    id_creatore integer NOT NULL,
    nome character varying(32) NOT NULL,
    descrizione character varying(256),
    categoria smallint NOT NULL,
    foto bytea,
    dtype character varying(31) NOT NULL,
    id_asta integer,
    prezzo real,
    scadenza timestamp(6) without time zone,
    decremento real,
    minimo real,
    timer timestamp(6) without time zone
);
    DROP TABLE public.asta;
       public         heap    postgres    false            �            1259    17088    asta_id_creatore_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_id_creatore_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.asta_id_creatore_seq;
       public          postgres    false    214            A           0    0    asta_id_creatore_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.asta_id_creatore_seq OWNED BY public.asta.id_creatore;
          public          postgres    false    215            �            1259    17089    asta_id_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.asta_id_seq;
       public          postgres    false    214            B           0    0    asta_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE public.asta_id_seq OWNED BY public.asta.id;
          public          postgres    false    216            �            1259    17090    asta_inversa    TABLE     �   CREATE TABLE public.asta_inversa (
    id_asta integer NOT NULL,
    prezzo double precision NOT NULL,
    scadenza date NOT NULL
);
     DROP TABLE public.asta_inversa;
       public         heap    postgres    false            �            1259    17093    asta_inversa_id_asta_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_inversa_id_asta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.asta_inversa_id_asta_seq;
       public          postgres    false    217            C           0    0    asta_inversa_id_asta_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.asta_inversa_id_asta_seq OWNED BY public.asta_inversa.id_asta;
          public          postgres    false    218            �            1259    17094    asta_ribasso    TABLE     �   CREATE TABLE public.asta_ribasso (
    id_asta integer NOT NULL,
    prezzo double precision NOT NULL,
    timer date NOT NULL,
    decremento double precision NOT NULL,
    minimo double precision NOT NULL
);
     DROP TABLE public.asta_ribasso;
       public         heap    postgres    false            �            1259    17097    asta_ribasso_id_asta_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_ribasso_id_asta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.asta_ribasso_id_asta_seq;
       public          postgres    false    219            D           0    0    asta_ribasso_id_asta_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.asta_ribasso_id_asta_seq OWNED BY public.asta_ribasso.id_asta;
          public          postgres    false    220            �            1259    17098    asta_silenziosa    TABLE     b   CREATE TABLE public.asta_silenziosa (
    id_asta integer NOT NULL,
    scadenza date NOT NULL
);
 #   DROP TABLE public.asta_silenziosa;
       public         heap    postgres    false            �            1259    17101    asta_silenziosa_id_asta_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_silenziosa_id_asta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 2   DROP SEQUENCE public.asta_silenziosa_id_asta_seq;
       public          postgres    false    221            E           0    0    asta_silenziosa_id_asta_seq    SEQUENCE OWNED BY     [   ALTER SEQUENCE public.asta_silenziosa_id_asta_seq OWNED BY public.asta_silenziosa.id_asta;
          public          postgres    false    222            �            1259    17102    notifica    TABLE     �   CREATE TABLE public.notifica (
    id integer NOT NULL,
    id_utente integer NOT NULL,
    testo character varying(128) NOT NULL,
    data timestamp(6) without time zone NOT NULL,
    letta boolean NOT NULL
);
    DROP TABLE public.notifica;
       public         heap    postgres    false            �            1259    17105    offerta    TABLE     �   CREATE TABLE public.offerta (
    id_utente integer NOT NULL,
    id_asta integer NOT NULL,
    valore double precision NOT NULL,
    data timestamp(6) without time zone NOT NULL,
    id integer NOT NULL
);
    DROP TABLE public.offerta;
       public         heap    postgres    false            �            1259    17108    offerta_id_asta_seq    SEQUENCE     �   CREATE SEQUENCE public.offerta_id_asta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.offerta_id_asta_seq;
       public          postgres    false    224            F           0    0    offerta_id_asta_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.offerta_id_asta_seq OWNED BY public.offerta.id_asta;
          public          postgres    false    225            �            1259    17109    offerta_id_utente_seq    SEQUENCE     �   CREATE SEQUENCE public.offerta_id_utente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.offerta_id_utente_seq;
       public          postgres    false    224            G           0    0    offerta_id_utente_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.offerta_id_utente_seq OWNED BY public.offerta.id_utente;
          public          postgres    false    226            �            1259    17110    utente    TABLE     K  CREATE TABLE public.utente (
    id integer NOT NULL,
    username character varying(32) NOT NULL,
    email character varying(64) NOT NULL,
    password character varying(32) NOT NULL,
    biografia character varying(256),
    sitoweb character varying(32),
    paese character varying(32),
    avatar bytea,
    tipo smallint
);
    DROP TABLE public.utente;
       public         heap    postgres    false            �            1259    17115    utente_id_seq    SEQUENCE     �   CREATE SEQUENCE public.utente_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.utente_id_seq;
       public          postgres    false    227            H           0    0    utente_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.utente_id_seq OWNED BY public.utente.id;
          public          postgres    false    228            �           2604    17116    asta id    DEFAULT     b   ALTER TABLE ONLY public.asta ALTER COLUMN id SET DEFAULT nextval('public.asta_id_seq'::regclass);
 6   ALTER TABLE public.asta ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    216    214            �           2604    17117    asta id_creatore    DEFAULT     t   ALTER TABLE ONLY public.asta ALTER COLUMN id_creatore SET DEFAULT nextval('public.asta_id_creatore_seq'::regclass);
 ?   ALTER TABLE public.asta ALTER COLUMN id_creatore DROP DEFAULT;
       public          postgres    false    215    214            �           2604    17118    asta_inversa id_asta    DEFAULT     |   ALTER TABLE ONLY public.asta_inversa ALTER COLUMN id_asta SET DEFAULT nextval('public.asta_inversa_id_asta_seq'::regclass);
 C   ALTER TABLE public.asta_inversa ALTER COLUMN id_asta DROP DEFAULT;
       public          postgres    false    218    217            �           2604    17119    asta_ribasso id_asta    DEFAULT     |   ALTER TABLE ONLY public.asta_ribasso ALTER COLUMN id_asta SET DEFAULT nextval('public.asta_ribasso_id_asta_seq'::regclass);
 C   ALTER TABLE public.asta_ribasso ALTER COLUMN id_asta DROP DEFAULT;
       public          postgres    false    220    219            �           2604    17120    asta_silenziosa id_asta    DEFAULT     �   ALTER TABLE ONLY public.asta_silenziosa ALTER COLUMN id_asta SET DEFAULT nextval('public.asta_silenziosa_id_asta_seq'::regclass);
 F   ALTER TABLE public.asta_silenziosa ALTER COLUMN id_asta DROP DEFAULT;
       public          postgres    false    222    221            �           2604    17121    offerta id_utente    DEFAULT     v   ALTER TABLE ONLY public.offerta ALTER COLUMN id_utente SET DEFAULT nextval('public.offerta_id_utente_seq'::regclass);
 @   ALTER TABLE public.offerta ALTER COLUMN id_utente DROP DEFAULT;
       public          postgres    false    226    224            �           2604    17122    offerta id_asta    DEFAULT     r   ALTER TABLE ONLY public.offerta ALTER COLUMN id_asta SET DEFAULT nextval('public.offerta_id_asta_seq'::regclass);
 >   ALTER TABLE public.offerta ALTER COLUMN id_asta DROP DEFAULT;
       public          postgres    false    225    224            �           2604    17123 	   utente id    DEFAULT     f   ALTER TABLE ONLY public.utente ALTER COLUMN id SET DEFAULT nextval('public.utente_id_seq'::regclass);
 8   ALTER TABLE public.utente ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    228    227            ,          0    17083    asta 
   TABLE DATA           �   COPY public.asta (id, id_creatore, nome, descrizione, categoria, foto, dtype, id_asta, prezzo, scadenza, decremento, minimo, timer) FROM stdin;
    public          postgres    false    214   �G       /          0    17090    asta_inversa 
   TABLE DATA           A   COPY public.asta_inversa (id_asta, prezzo, scadenza) FROM stdin;
    public          postgres    false    217   �G       1          0    17094    asta_ribasso 
   TABLE DATA           R   COPY public.asta_ribasso (id_asta, prezzo, timer, decremento, minimo) FROM stdin;
    public          postgres    false    219   �G       3          0    17098    asta_silenziosa 
   TABLE DATA           <   COPY public.asta_silenziosa (id_asta, scadenza) FROM stdin;
    public          postgres    false    221   �G       5          0    17102    notifica 
   TABLE DATA           E   COPY public.notifica (id, id_utente, testo, data, letta) FROM stdin;
    public          postgres    false    223   H       6          0    17105    offerta 
   TABLE DATA           G   COPY public.offerta (id_utente, id_asta, valore, data, id) FROM stdin;
    public          postgres    false    224   /H       9          0    17110    utente 
   TABLE DATA           h   COPY public.utente (id, username, email, password, biografia, sitoweb, paese, avatar, tipo) FROM stdin;
    public          postgres    false    227   LH       I           0    0    asta_id_creatore_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.asta_id_creatore_seq', 1, false);
          public          postgres    false    215            J           0    0    asta_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.asta_id_seq', 1, false);
          public          postgres    false    216            K           0    0    asta_inversa_id_asta_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.asta_inversa_id_asta_seq', 1, false);
          public          postgres    false    218            L           0    0    asta_ribasso_id_asta_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.asta_ribasso_id_asta_seq', 1, false);
          public          postgres    false    220            M           0    0    asta_silenziosa_id_asta_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.asta_silenziosa_id_asta_seq', 1, false);
          public          postgres    false    222            N           0    0    offerta_id_asta_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.offerta_id_asta_seq', 1, false);
          public          postgres    false    225            O           0    0    offerta_id_utente_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.offerta_id_utente_seq', 1, false);
          public          postgres    false    226            P           0    0    utente_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.utente_id_seq', 12, true);
          public          postgres    false    228            �           2606    17125    asta asta_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.asta
    ADD CONSTRAINT asta_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.asta DROP CONSTRAINT asta_pkey;
       public            postgres    false    214            �           2606    17127    notifica notifica_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.notifica
    ADD CONSTRAINT notifica_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.notifica DROP CONSTRAINT notifica_pkey;
       public            postgres    false    223            �           2606    17129    offerta offerta_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.offerta
    ADD CONSTRAINT offerta_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.offerta DROP CONSTRAINT offerta_pkey;
       public            postgres    false    224            �           2606    17131    utente utente_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_pkey;
       public            postgres    false    227            �           2606    17132    asta asta_id_creatore_fkey    FK CONSTRAINT     ~   ALTER TABLE ONLY public.asta
    ADD CONSTRAINT asta_id_creatore_fkey FOREIGN KEY (id_creatore) REFERENCES public.utente(id);
 D   ALTER TABLE ONLY public.asta DROP CONSTRAINT asta_id_creatore_fkey;
       public          postgres    false    227    214    3222            �           2606    17137 &   asta_inversa asta_inversa_id_asta_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.asta_inversa
    ADD CONSTRAINT asta_inversa_id_asta_fkey FOREIGN KEY (id_asta) REFERENCES public.asta(id);
 P   ALTER TABLE ONLY public.asta_inversa DROP CONSTRAINT asta_inversa_id_asta_fkey;
       public          postgres    false    3216    217    214            �           2606    17142 &   asta_ribasso asta_ribasso_id_asta_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.asta_ribasso
    ADD CONSTRAINT asta_ribasso_id_asta_fkey FOREIGN KEY (id_asta) REFERENCES public.asta(id);
 P   ALTER TABLE ONLY public.asta_ribasso DROP CONSTRAINT asta_ribasso_id_asta_fkey;
       public          postgres    false    214    219    3216            �           2606    17147 ,   asta_silenziosa asta_silenziosa_id_asta_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.asta_silenziosa
    ADD CONSTRAINT asta_silenziosa_id_asta_fkey FOREIGN KEY (id_asta) REFERENCES public.asta(id);
 V   ALTER TABLE ONLY public.asta_silenziosa DROP CONSTRAINT asta_silenziosa_id_asta_fkey;
       public          postgres    false    3216    214    221            �           2606    17152     notifica notifica_id_utente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.notifica
    ADD CONSTRAINT notifica_id_utente_fkey FOREIGN KEY (id_utente) REFERENCES public.utente(id);
 J   ALTER TABLE ONLY public.notifica DROP CONSTRAINT notifica_id_utente_fkey;
       public          postgres    false    3222    223    227            �           2606    17157    offerta offerta_id_asta_fkey    FK CONSTRAINT     z   ALTER TABLE ONLY public.offerta
    ADD CONSTRAINT offerta_id_asta_fkey FOREIGN KEY (id_asta) REFERENCES public.asta(id);
 F   ALTER TABLE ONLY public.offerta DROP CONSTRAINT offerta_id_asta_fkey;
       public          postgres    false    224    214    3216            �           2606    17162    offerta offerta_id_utente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.offerta
    ADD CONSTRAINT offerta_id_utente_fkey FOREIGN KEY (id_utente) REFERENCES public.utente(id);
 H   ALTER TABLE ONLY public.offerta DROP CONSTRAINT offerta_id_utente_fkey;
       public          postgres    false    224    227    3222            ,      x������ � �      /      x������ � �      1      x������ � �      3      x������ � �      5      x������ � �      6      x������ � �      9   �   x�}��j�0���Ä:Yz+�8�ӎ���"5��;)}���`������K�f�{6v�Grhl����9��p��)Z�XqF���F]��<Nl�0�@;с����TM��Esք6�]��h����
p��WI�b�8;��@m��}��B{�4W:�S�$cV�k����-��%�����q|Y�:I�`�B���R$����r���̉�#�T�s��y�F�  U�=     