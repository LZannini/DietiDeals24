PGDMP     7    4                |           dietideals24    14.4    14.4 D    6           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            7           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            8           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            9           1262    58633    dietideals24    DATABASE     h   CREATE DATABASE dietideals24 WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Italian_Italy.1252';
    DROP DATABASE dietideals24;
                postgres    false            F           1247    58635    tipo_utente    TYPE     ^   CREATE TYPE public.tipo_utente AS ENUM (
    'COMPRATORE',
    'VENDITORE',
    'COMPLETO'
);
    DROP TYPE public.tipo_utente;
       public          postgres    false            �            1255    58727    check_offerta_presente()    FUNCTION     U  CREATE FUNCTION public.check_offerta_presente() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM offerta
        WHERE id_utente = NEW.id_utente
        AND id_asta = NEW.id_asta
    ) THEN
        RAISE EXCEPTION 'L''utente ha già un''offerta in questa asta';
    END IF;
    RETURN NEW;
END;
$$;
 /   DROP FUNCTION public.check_offerta_presente();
       public          postgres    false            �            1259    58641    asta    TABLE     �   CREATE TABLE public.asta (
    id integer NOT NULL,
    id_creatore integer NOT NULL,
    nome character varying(32) NOT NULL,
    descrizione character varying(256),
    categoria smallint NOT NULL,
    foto bytea
);
    DROP TABLE public.asta;
       public         heap    postgres    false            �            1259    58646    asta_id_creatore_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_id_creatore_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.asta_id_creatore_seq;
       public          postgres    false    209            :           0    0    asta_id_creatore_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.asta_id_creatore_seq OWNED BY public.asta.id_creatore;
          public          postgres    false    210            �            1259    58647    asta_id_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.asta_id_seq;
       public          postgres    false    209            ;           0    0    asta_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE public.asta_id_seq OWNED BY public.asta.id;
          public          postgres    false    211            �            1259    58648    asta_inversa    TABLE     �   CREATE TABLE public.asta_inversa (
    id_asta integer NOT NULL,
    prezzo double precision NOT NULL,
    scadenza timestamp(6) without time zone NOT NULL
);
     DROP TABLE public.asta_inversa;
       public         heap    postgres    false            �            1259    58651    asta_inversa_id_asta_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_inversa_id_asta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.asta_inversa_id_asta_seq;
       public          postgres    false    212            <           0    0    asta_inversa_id_asta_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.asta_inversa_id_asta_seq OWNED BY public.asta_inversa.id_asta;
          public          postgres    false    213            �            1259    58652    asta_ribasso    TABLE     �   CREATE TABLE public.asta_ribasso (
    id_asta integer NOT NULL,
    prezzo double precision NOT NULL,
    timer timestamp(6) without time zone NOT NULL,
    decremento double precision NOT NULL,
    minimo double precision NOT NULL
);
     DROP TABLE public.asta_ribasso;
       public         heap    postgres    false            �            1259    58655    asta_ribasso_id_asta_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_ribasso_id_asta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.asta_ribasso_id_asta_seq;
       public          postgres    false    214            =           0    0    asta_ribasso_id_asta_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.asta_ribasso_id_asta_seq OWNED BY public.asta_ribasso.id_asta;
          public          postgres    false    215            �            1259    58656    asta_silenziosa    TABLE     |   CREATE TABLE public.asta_silenziosa (
    id_asta integer NOT NULL,
    scadenza timestamp(6) without time zone NOT NULL
);
 #   DROP TABLE public.asta_silenziosa;
       public         heap    postgres    false            �            1259    58659    asta_silenziosa_id_asta_seq    SEQUENCE     �   CREATE SEQUENCE public.asta_silenziosa_id_asta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 2   DROP SEQUENCE public.asta_silenziosa_id_asta_seq;
       public          postgres    false    216            >           0    0    asta_silenziosa_id_asta_seq    SEQUENCE OWNED BY     [   ALTER SEQUENCE public.asta_silenziosa_id_asta_seq OWNED BY public.asta_silenziosa.id_asta;
          public          postgres    false    217            �            1259    58660    notifica    TABLE     �   CREATE TABLE public.notifica (
    id integer NOT NULL,
    id_utente integer NOT NULL,
    testo character varying(128) NOT NULL,
    data timestamp(6) without time zone NOT NULL,
    letta boolean NOT NULL
);
    DROP TABLE public.notifica;
       public         heap    postgres    false            �            1259    58663    offerta    TABLE     �   CREATE TABLE public.offerta (
    id_utente integer NOT NULL,
    id_asta integer NOT NULL,
    valore double precision NOT NULL,
    data timestamp(6) without time zone NOT NULL,
    id integer NOT NULL
);
    DROP TABLE public.offerta;
       public         heap    postgres    false            �            1259    58666    offerta_id_asta_seq    SEQUENCE     �   CREATE SEQUENCE public.offerta_id_asta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.offerta_id_asta_seq;
       public          postgres    false    219            ?           0    0    offerta_id_asta_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.offerta_id_asta_seq OWNED BY public.offerta.id_asta;
          public          postgres    false    220            �            1259    58725    offerta_id_seq    SEQUENCE        CREATE SEQUENCE public.offerta_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;
 %   DROP SEQUENCE public.offerta_id_seq;
       public          postgres    false    219            @           0    0    offerta_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.offerta_id_seq OWNED BY public.offerta.id;
          public          postgres    false    224            �            1259    58667    offerta_id_utente_seq    SEQUENCE     �   CREATE SEQUENCE public.offerta_id_utente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.offerta_id_utente_seq;
       public          postgres    false    219            A           0    0    offerta_id_utente_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.offerta_id_utente_seq OWNED BY public.offerta.id_utente;
          public          postgres    false    221            �            1259    58668    utente    TABLE     T  CREATE TABLE public.utente (
    id integer NOT NULL,
    username character varying(32) NOT NULL,
    email character varying(64) NOT NULL,
    password character varying(32) NOT NULL,
    biografia character varying(256),
    sitoweb character varying(32),
    paese character varying(32),
    avatar bytea,
    tipo smallint NOT NULL
);
    DROP TABLE public.utente;
       public         heap    postgres    false            �            1259    58673    utente_id_seq    SEQUENCE     �   CREATE SEQUENCE public.utente_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.utente_id_seq;
       public          postgres    false    222            B           0    0    utente_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.utente_id_seq OWNED BY public.utente.id;
          public          postgres    false    223            �           2604    58674    asta id    DEFAULT     b   ALTER TABLE ONLY public.asta ALTER COLUMN id SET DEFAULT nextval('public.asta_id_seq'::regclass);
 6   ALTER TABLE public.asta ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    211    209            �           2604    58675    asta id_creatore    DEFAULT     t   ALTER TABLE ONLY public.asta ALTER COLUMN id_creatore SET DEFAULT nextval('public.asta_id_creatore_seq'::regclass);
 ?   ALTER TABLE public.asta ALTER COLUMN id_creatore DROP DEFAULT;
       public          postgres    false    210    209            �           2604    58676    asta_inversa id_asta    DEFAULT     |   ALTER TABLE ONLY public.asta_inversa ALTER COLUMN id_asta SET DEFAULT nextval('public.asta_inversa_id_asta_seq'::regclass);
 C   ALTER TABLE public.asta_inversa ALTER COLUMN id_asta DROP DEFAULT;
       public          postgres    false    213    212            �           2604    58677    asta_ribasso id_asta    DEFAULT     |   ALTER TABLE ONLY public.asta_ribasso ALTER COLUMN id_asta SET DEFAULT nextval('public.asta_ribasso_id_asta_seq'::regclass);
 C   ALTER TABLE public.asta_ribasso ALTER COLUMN id_asta DROP DEFAULT;
       public          postgres    false    215    214            �           2604    58678    asta_silenziosa id_asta    DEFAULT     �   ALTER TABLE ONLY public.asta_silenziosa ALTER COLUMN id_asta SET DEFAULT nextval('public.asta_silenziosa_id_asta_seq'::regclass);
 F   ALTER TABLE public.asta_silenziosa ALTER COLUMN id_asta DROP DEFAULT;
       public          postgres    false    217    216            �           2604    58679    offerta id_utente    DEFAULT     v   ALTER TABLE ONLY public.offerta ALTER COLUMN id_utente SET DEFAULT nextval('public.offerta_id_utente_seq'::regclass);
 @   ALTER TABLE public.offerta ALTER COLUMN id_utente DROP DEFAULT;
       public          postgres    false    221    219            �           2604    58680    offerta id_asta    DEFAULT     r   ALTER TABLE ONLY public.offerta ALTER COLUMN id_asta SET DEFAULT nextval('public.offerta_id_asta_seq'::regclass);
 >   ALTER TABLE public.offerta ALTER COLUMN id_asta DROP DEFAULT;
       public          postgres    false    220    219            �           2604    58726 
   offerta id    DEFAULT     h   ALTER TABLE ONLY public.offerta ALTER COLUMN id SET DEFAULT nextval('public.offerta_id_seq'::regclass);
 9   ALTER TABLE public.offerta ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    224    219            �           2604    58681 	   utente id    DEFAULT     f   ALTER TABLE ONLY public.utente ALTER COLUMN id SET DEFAULT nextval('public.utente_id_seq'::regclass);
 8   ALTER TABLE public.utente ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    223    222            $          0    58641    asta 
   TABLE DATA           S   COPY public.asta (id, id_creatore, nome, descrizione, categoria, foto) FROM stdin;
    public          postgres    false    209   `N       '          0    58648    asta_inversa 
   TABLE DATA           A   COPY public.asta_inversa (id_asta, prezzo, scadenza) FROM stdin;
    public          postgres    false    212   �N       )          0    58652    asta_ribasso 
   TABLE DATA           R   COPY public.asta_ribasso (id_asta, prezzo, timer, decremento, minimo) FROM stdin;
    public          postgres    false    214   &O       +          0    58656    asta_silenziosa 
   TABLE DATA           <   COPY public.asta_silenziosa (id_asta, scadenza) FROM stdin;
    public          postgres    false    216   CO       -          0    58660    notifica 
   TABLE DATA           E   COPY public.notifica (id, id_utente, testo, data, letta) FROM stdin;
    public          postgres    false    218   {O       .          0    58663    offerta 
   TABLE DATA           G   COPY public.offerta (id_utente, id_asta, valore, data, id) FROM stdin;
    public          postgres    false    219   �O       1          0    58668    utente 
   TABLE DATA           h   COPY public.utente (id, username, email, password, biografia, sitoweb, paese, avatar, tipo) FROM stdin;
    public          postgres    false    222   �O       C           0    0    asta_id_creatore_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.asta_id_creatore_seq', 1, false);
          public          postgres    false    210            D           0    0    asta_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.asta_id_seq', 46, true);
          public          postgres    false    211            E           0    0    asta_inversa_id_asta_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.asta_inversa_id_asta_seq', 1, false);
          public          postgres    false    213            F           0    0    asta_ribasso_id_asta_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.asta_ribasso_id_asta_seq', 1, false);
          public          postgres    false    215            G           0    0    asta_silenziosa_id_asta_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.asta_silenziosa_id_asta_seq', 1, false);
          public          postgres    false    217            H           0    0    offerta_id_asta_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.offerta_id_asta_seq', 1, false);
          public          postgres    false    220            I           0    0    offerta_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.offerta_id_seq', 4, true);
          public          postgres    false    224            J           0    0    offerta_id_utente_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.offerta_id_utente_seq', 1, false);
          public          postgres    false    221            K           0    0    utente_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.utente_id_seq', 13, true);
          public          postgres    false    223            �           2606    58683    asta asta_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.asta
    ADD CONSTRAINT asta_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.asta DROP CONSTRAINT asta_pkey;
       public            postgres    false    209            �           2606    58685    notifica notifica_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.notifica
    ADD CONSTRAINT notifica_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.notifica DROP CONSTRAINT notifica_pkey;
       public            postgres    false    218            �           2606    58687    offerta offerta_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.offerta
    ADD CONSTRAINT offerta_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.offerta DROP CONSTRAINT offerta_pkey;
       public            postgres    false    219            �           2606    58689    utente utente_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_pkey;
       public            postgres    false    222            �           2620    58728    offerta blocca_offerte_doppie    TRIGGER     �   CREATE TRIGGER blocca_offerte_doppie BEFORE INSERT ON public.offerta FOR EACH ROW EXECUTE FUNCTION public.check_offerta_presente();
 6   DROP TRIGGER blocca_offerte_doppie ON public.offerta;
       public          postgres    false    225    219            �           2606    58690    asta asta_id_creatore_fkey    FK CONSTRAINT     ~   ALTER TABLE ONLY public.asta
    ADD CONSTRAINT asta_id_creatore_fkey FOREIGN KEY (id_creatore) REFERENCES public.utente(id);
 D   ALTER TABLE ONLY public.asta DROP CONSTRAINT asta_id_creatore_fkey;
       public          postgres    false    209    222    3216            �           2606    58695 &   asta_inversa asta_inversa_id_asta_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.asta_inversa
    ADD CONSTRAINT asta_inversa_id_asta_fkey FOREIGN KEY (id_asta) REFERENCES public.asta(id);
 P   ALTER TABLE ONLY public.asta_inversa DROP CONSTRAINT asta_inversa_id_asta_fkey;
       public          postgres    false    209    212    3210            �           2606    58700 &   asta_ribasso asta_ribasso_id_asta_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.asta_ribasso
    ADD CONSTRAINT asta_ribasso_id_asta_fkey FOREIGN KEY (id_asta) REFERENCES public.asta(id);
 P   ALTER TABLE ONLY public.asta_ribasso DROP CONSTRAINT asta_ribasso_id_asta_fkey;
       public          postgres    false    214    3210    209            �           2606    58705 ,   asta_silenziosa asta_silenziosa_id_asta_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.asta_silenziosa
    ADD CONSTRAINT asta_silenziosa_id_asta_fkey FOREIGN KEY (id_asta) REFERENCES public.asta(id);
 V   ALTER TABLE ONLY public.asta_silenziosa DROP CONSTRAINT asta_silenziosa_id_asta_fkey;
       public          postgres    false    209    3210    216            �           2606    58710     notifica notifica_id_utente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.notifica
    ADD CONSTRAINT notifica_id_utente_fkey FOREIGN KEY (id_utente) REFERENCES public.utente(id);
 J   ALTER TABLE ONLY public.notifica DROP CONSTRAINT notifica_id_utente_fkey;
       public          postgres    false    222    218    3216            �           2606    58715    offerta offerta_id_asta_fkey    FK CONSTRAINT     z   ALTER TABLE ONLY public.offerta
    ADD CONSTRAINT offerta_id_asta_fkey FOREIGN KEY (id_asta) REFERENCES public.asta(id);
 F   ALTER TABLE ONLY public.offerta DROP CONSTRAINT offerta_id_asta_fkey;
       public          postgres    false    3210    209    219            �           2606    58720    offerta offerta_id_utente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.offerta
    ADD CONSTRAINT offerta_id_utente_fkey FOREIGN KEY (id_utente) REFERENCES public.utente(id);
 H   ALTER TABLE ONLY public.offerta DROP CONSTRAINT offerta_id_utente_fkey;
       public          postgres    false    3216    222    219            $   ~   x�-���0@��W܎1A[v'� !aq`9��pg,%��6���g-��9&�L}6<�j�Q���PA�����5�0^;hߴO3��(IW�kU޵�A��0��ҝ�ćn1'O,��CP?�_��1_��'S      '   (   x�31�450�4202�50�54S04�24�20������ \sj      )      x������ � �      +   (   x�31�4202�50�54S04�24�20�21�*���� �!�      -      x������ � �      .   5   x�3�41"N##]S]#SCS+c+��!�����vyc�=... �dT      1   �   x�}��j�0���S�	B��	�v�i�B�l���VH�����t0ׇ��~d}�/����Vvp[�\�D�UF<�q������Uc�/��CU��8��b	�T7��D���P���NSzʉZ�G�a6̦�>d�$A~�@[x�]>�|�qU����m�8��0�� �#6���b1��C�Õ���
��֞�f�L�/-'R��B��o[���=��m�ݦ��9H:�O�(;��9F��+����
     